package com.artillexstudios.axapi.nms.v1_20_R4;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.v1_20_R4.entity.EntityTracker;
import com.artillexstudios.axapi.nms.v1_20_R4.packet.PacketListener;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.Title;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.util.datafix.fixes.References;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Locale;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {
    private final String AXAPI_HANDLER;
    private Field channelField;
    private Field connectionField;

    public NMSHandler(JavaPlugin plugin) {
        AXAPI_HANDLER = "axapi_handler_" + plugin.getName().toLowerCase(Locale.ENGLISH);

        try {
            connectionField = Class.forName("net.minecraft.server.network.ServerCommonPacketListenerImpl").getDeclaredField("e");
            connectionField.setAccessible(true);
            channelField = Class.forName("net.minecraft.network.NetworkManager").getDeclaredField("n");
            channelField.setAccessible(true);
        } catch (Exception exception) {
            log.error("An exception occurred while initializing NMSHandler!", exception);
        }
    }

    @Override
    public Serializer<Object, Component> componentSerializer() {
        return new Serializer<>() {
            @Override
            public Component serialize(Object object) {
                if (!(object instanceof net.minecraft.network.chat.Component component)) {
                    throw new IllegalStateException("Can only serialize component!");
                }

                String gsonText = net.minecraft.network.chat.Component.Serializer.toJson(component, MinecraftServer.getServer().registryAccess());
                return GsonComponentSerializer.gson().deserialize(gsonText);
            }

            @Override
            public Object deserialize(Component value) {
                return net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(value), MinecraftServer.getServer().registryAccess());
            }
        };
    }

    @Override
    public void injectPlayer(Player player) {
        var serverPlayer = ((CraftPlayer) player).getHandle();

        var channel = getChannel(getConnection(serverPlayer.connection));

        if (!channel.pipeline().names().contains(PACKET_HANDLER)) {
            return;
        }

        if (channel.pipeline().names().contains(AXAPI_HANDLER)) {
            return;
        }

        channel.eventLoop().submit(() -> {
            channel.pipeline().addBefore(PACKET_HANDLER, AXAPI_HANDLER, new PacketListener(player));
        });
    }

    @Override
    public void uninjectPlayer(Player player) {
        var serverPlayer = ((CraftPlayer) player).getHandle();

        var channel = getChannel(getConnection(serverPlayer.connection));

        channel.eventLoop().submit(() -> {
            if (channel.pipeline().get(AXAPI_HANDLER) != null) {
                channel.pipeline().remove(AXAPI_HANDLER);
            }
        });
    }

    @Override
    public int getProtocolVersionId(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        return serverPlayer.connection.connection.protocolVersion;
    }

    @Override
    public PacketEntityTracker newTracker() {
        return new EntityTracker();
    }

    @Override
    public BlockSetter newSetter(World world) {
        return new BlockSetterImpl(world);
    }

    @Override
    public ActionBar newActionBar(Component content) {
        return new com.artillexstudios.axapi.nms.v1_20_R4.utils.ActionBar(content);
    }

    @Override
    public Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        return new com.artillexstudios.axapi.nms.v1_20_R4.utils.Title(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags) {
        return new com.artillexstudios.axapi.nms.v1_20_R4.utils.BossBar(title, progress, color, style, flags);
    }

    @Override
    public CompoundTag newTag() {
        return new com.artillexstudios.axapi.nms.v1_20_R4.items.nbt.CompoundTag(new net.minecraft.nbt.CompoundTag());
    }

    @Override
    public WrappedItemStack wrapItem(ItemStack itemStack) {
        return new com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack(itemStack);
    }

    @Override
    public WrappedItemStack wrapItem(String snbt) {
        try {
            net.minecraft.nbt.CompoundTag tag = TagParser.parseTag(snbt);
            int dataVersion = tag.getInt("DataVersion");
            net.minecraft.nbt.CompoundTag converted = (net.minecraft.nbt.CompoundTag) MinecraftServer.getServer().fixerUpper.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, tag), dataVersion, CraftMagicNumbers.INSTANCE.getDataVersion()).getValue();
            net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.parse(MinecraftServer.getServer().registryAccess(), converted).orElseThrow();
            return new com.artillexstudios.axapi.nms.v1_20_R4.items.WrappedItemStack(item);
        } catch (CommandSyntaxException exception) {
            log.error("An error occurred while parsing item from SNBT!", exception);
            return null;
        }
    }

    @Override
    public WrappedItemStack wrapItem(byte[] bytes) {
        return wrapItem(ItemStackSerializer.INSTANCE.deserializeFromBytes(bytes));
    }

    @Override
    public void openSignInput(SignInput signInput) {

    }

    @Override
    public ParallelBlockSetter newParallelSetter(World world) {
        return new ParallelBlockSetterImpl(world);
    }

    private Channel getChannel(Connection connection) {
        try {
            return (Channel) channelField.get(connection);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Connection getConnection(ServerCommonPacketListenerImpl serverGamePacketListener) {
        try {
            return (Connection) connectionField.get(serverGamePacketListener);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
