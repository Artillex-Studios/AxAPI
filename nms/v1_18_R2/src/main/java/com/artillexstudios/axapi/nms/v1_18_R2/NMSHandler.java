package com.artillexstudios.axapi.nms.v1_18_R2;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.v1_18_R2.entity.EntityTracker;
import com.artillexstudios.axapi.nms.v1_18_R2.packet.PacketListener;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.FastFieldAccessor;
import com.artillexstudios.axapi.utils.Title;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {
    private static final UUID NIL_UUID = new UUID(0, 0);
    private static final Logger log = LoggerFactory.getLogger(NMSHandler.class);
    private final ItemStackSerializer serializer = new ItemStackSerializer();
    private static final String PACKET_HANDLER = "packet_handler";
    private final String AXAPI_HANDLER;
    private Method skullMetaMethod;
    private Field channelField;

    public NMSHandler(JavaPlugin plugin) {
        AXAPI_HANDLER = "axapi_handler_" + plugin.getName().toLowerCase(Locale.ENGLISH);

        try {
            channelField = Class.forName("net.minecraft.network.NetworkManager").getDeclaredField("m");
            channelField.setAccessible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public byte[] serializeItemStack(ItemStack itemStack) {
        return serializer.serializeAsBytes(itemStack);
    }

    @Override
    public ItemStack deserializeItemStack(byte[] bytes) {
        return serializer.deserializeFromBytes(bytes);
    }

    @Override
    public void injectPlayer(Player player) {
        var serverPlayer = ((CraftPlayer) player).getHandle();

        var channel = getChannel(serverPlayer.connection.connection);

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

        var channel = getChannel(serverPlayer.connection.connection);

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
    public void setItemStackTexture(ItemMeta meta, String texture) {
        if (meta instanceof SkullMeta skullMeta) {
            if (skullMetaMethod == null) {
                try {
                    skullMetaMethod = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                    skullMetaMethod.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            GameProfile profile = new GameProfile(NIL_UUID, "skull");
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                skullMetaMethod.invoke(skullMeta, profile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
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
    public String toSNBT(ItemStack itemStack) {
        return new SnbtPrinterTagVisitor().visit(CraftItemStack.asNMSCopy(itemStack).save(new CompoundTag()));
    }

    @Override
    public ItemStack fromSNBT(String snbt) {
        try {
            CompoundTag tag = TagParser.parseTag(snbt);
            net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.of(tag);
            return CraftItemStack.asBukkitCopy(item);
        } catch (CommandSyntaxException exception) {
            log.error("An error occurred while parsing item from SNBT!", exception);
            return null;
        }
    }

    @Override
    public ActionBar newActionBar(Component content) {
        return new com.artillexstudios.axapi.nms.v1_18_R2.utils.ActionBar(content);
    }

    @Override
    public Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        return new com.artillexstudios.axapi.nms.v1_18_R2.utils.Title(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags) {
        return new com.artillexstudios.axapi.nms.v1_18_R2.utils.BossBar(title, progress, color, style, flags);
    }

    @Override
    public com.artillexstudios.axapi.items.nbt.CompoundTag newTag() {
        return new com.artillexstudios.axapi.nms.v1_18_R2.items.nbt.CompoundTag(new CompoundTag());
    }

    @Override
    public WrappedItemStack wrapItem(ItemStack itemStack) {
        return new com.artillexstudios.axapi.nms.v1_18_R2.items.WrappedItemStack(itemStack);
    }

    @Override
    public void openSignInput(SignInput signInput) {
        ServerPlayer player = ((CraftPlayer) signInput.getPlayer()).getHandle();
        BlockPos pos = new BlockPos(signInput.getLocation().getBlockX(), signInput.getLocation().getBlockY(), signInput.getLocation().getBlockZ());
        player.connection.send(new ClientboundBlockUpdatePacket(pos, ((CraftBlockData) Material.OAK_SIGN.createBlockData()).getState()));

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        buf.writeVarInt(Registry.BLOCK_ENTITY_TYPE.getId(BlockEntityType.SIGN));
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        tag.putString("id", "minecraft:oak_sign");


        for (int i = 0; i < 4; i++) {
            String gson = toGson(i > signInput.getLines().length ? Component.empty() : signInput.getLines()[i]);

            tag.putString("Text" + i + 1, gson);
        }

        buf.writeNbt(tag);

        ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket = new ClientboundBlockEntityDataPacket(buf);
        ClientboundOpenSignEditorPacket openSignEditorPacket = new ClientboundOpenSignEditorPacket(pos);
        player.connection.send(clientboundBlockEntityDataPacket);
        player.connection.send(openSignEditorPacket);
    }

    public String toGson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    @Override
    public String getTextureValue(ItemMeta meta) {
        if (!(meta instanceof SkullMeta skullMeta)) return null;

        try {
            FastFieldAccessor accessor = FastFieldAccessor.forClassField(Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftMetaSkull"), "profile");
            GameProfile profile = accessor.get(skullMeta);

            for (Property textures : profile.getProperties().get("textures")) {
                if (textures.getName().equals("textures")) {
                    return textures.getValue();
                }
            }
            return null;
        } catch (ClassNotFoundException exception) {
            log.error("An error occurred while getting skull texture!", exception);
            return null;
        }
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
}
