package com.artillexstudios.axapi.nms.v1_21_R3.packet;

import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class PacketEncoder extends MessageToMessageEncoder<ByteBuf> {
    private static final MethodHandle DECODE_HANDLE;
    private static final MethodHandle ENCODE_HANDLE;
    private final String axapi;
    private static final Object PAPER_COMPRESSION_ENABLED = compressionEnabled();
    private static final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private boolean handledCompression = PAPER_COMPRESSION_ENABLED != null;
    private final Player player;

    static {
        MethodHandle decodeHandle = null;
        MethodHandle encodeHandle = null;
        try {
            Method decodeMethod = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
            decodeMethod.setAccessible(true);
            decodeHandle = MethodHandles.lookup().unreflect(decodeMethod);

            Method encodeMethod = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
            encodeMethod.setAccessible(true);
            encodeHandle = MethodHandles.lookup().unreflect(encodeMethod);
        } catch (Throwable throwable) {
            LogUtils.error("An exception occurred!", throwable);
        }
        DECODE_HANDLE = decodeHandle;
        ENCODE_HANDLE = encodeHandle;
    }

    public PacketEncoder(String encoderName, Player player) {
        this.axapi = encoderName;
        this.player = player;
    }

    private static Object compressionEnabled() {
        try {
            Class<?> clazz = Class.forName("io.papermc.paper.network.ConnectionEvent");
            return clazz.getDeclaredField("COMPRESSION_THRESHOLD_SET").get(null);
        } catch (ReflectiveOperationException exception) {
            return null;
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable()) {
            return;
        }

        boolean needsCompression = !this.handledCompression && this.handleCompressionOrder(channelHandlerContext, byteBuf);

        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        int packetId = VarInt.read(byteBuf);
        PacketType packetType = ClientboundPacketTypes.forPacketId(packetId);
        PacketEvent event = new PacketEvent(this.player, PacketSide.CLIENT_BOUND, packetType, () -> new FriendlyByteBufWrapper(decorator.apply(byteBuf)), () -> {
            ByteBuf out = channelHandlerContext.alloc().buffer();
            VarInt.write(out, packetId);
            return new FriendlyByteBufWrapper(decorator.apply(out));
        });

        PacketEvents.INSTANCE.callEvent(event);
        if (event.cancelled()) {
            return;
        }

        FriendlyByteBufWrapper out = ((FriendlyByteBufWrapper) event.directOut());
        if (out == null) {
            byteBuf.readerIndex(readerIndex);
            byteBuf.writerIndex(writerIndex);
            list.add(byteBuf.retain());
            return;
        }

        if (needsCompression) {
            recompress(channelHandlerContext, out.buf());
        }

        list.add(out.buf().retain());
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        int compressorIndex = ctx.pipeline().names().indexOf("compress");
        if (compressorIndex == -1) {
            return false;
        }

        this.handledCompression = true;
        if (compressorIndex > ctx.pipeline().names().indexOf(this.axapi + "_encoder")) {
            ByteBuf decompressed = (ByteBuf) callDecode((ByteToMessageDecoder) ctx.pipeline().get("decompress"), ctx, buf).get(0);
            try {
                buf.clear().writeBytes(decompressed);
            } finally {
                decompressed.release();
            }

            ctx.pipeline().addAfter("compress", this.axapi + "_encoder", ctx.pipeline().remove(this.axapi + "_encoder"));
            ctx.pipeline().addAfter("decompress", this.axapi + "_decoder", ctx.pipeline().remove(this.axapi + "_decoder"));
            return true;
        }

        return false;
    }

    private void recompress(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        try {
            callEncode((MessageToByteEncoder<ByteBuf>) channelHandlerContext.pipeline().get("compress"), channelHandlerContext, buf, buffer);
            buf.clear().writeBytes(buffer);
        } finally {
            buffer.release();
        }
    }

    public static List<Object> callDecode(ByteToMessageDecoder decoder, ChannelHandlerContext ctx, Object input) throws Exception {
        List<Object> output = new ArrayList<>();
        try {
            DECODE_HANDLE.invoke(decoder, ctx, input, output);
        } catch (Error | Exception e) {
            throw e;
        } catch (Throwable t) {
            throw new InvocationTargetException(t);
        }

        return output;
    }

    public static void callEncode(MessageToByteEncoder encoder, ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            ENCODE_HANDLE.invoke(encoder, ctx, msg, out);
        } catch (Error | Exception e) {
            throw e;
        } catch (Throwable t) {
            throw new InvocationTargetException(t);
        }
    }
}
