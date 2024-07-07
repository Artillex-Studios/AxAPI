package com.artillexstudios.axapi.nms.v1_21_R1;

import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponentImpl;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.v1_21_R1.packet.PacketListener;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.BossBar;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.Title;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {
    private final String AXAPI_HANDLER;
    private Field channelField;
    private Field connectionField;
    private AtomicInteger entityCounter;

    public NMSHandler(JavaPlugin plugin) {
        AXAPI_HANDLER = "axapi_handler_" + plugin.getName().toLowerCase(Locale.ENGLISH);

        try {
            connectionField = Class.forName("net.minecraft.server.network.ServerCommonPacketListenerImpl").getDeclaredField("e");
            connectionField.setAccessible(true);
            channelField = Class.forName("net.minecraft.network.NetworkManager").getDeclaredField("n");
            channelField.setAccessible(true);
            Field entityIdField = Entity.class.getDeclaredField("c");
            entityIdField.setAccessible(true);
            entityCounter = (AtomicInteger) entityIdField.get(null);
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
    public PacketEntity createEntity(EntityType entityType, Location location) {
        return new com.artillexstudios.axapi.nms.v1_21_R1.entity.PacketEntity(entityType, location);
    }

    @Override
    public BlockSetter newSetter(World world) {
        return new BlockSetterImpl(world);
    }

    @Override
    public ActionBar newActionBar(Component content) {
        return new com.artillexstudios.axapi.nms.v1_21_R1.utils.ActionBar(content);
    }

    @Override
    public Title newTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        return new com.artillexstudios.axapi.nms.v1_21_R1.utils.Title(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public BossBar newBossBar(Component title, float progress, BossBar.Color color, BossBar.Style style, BossBar.Flag... flags) {
        return new com.artillexstudios.axapi.nms.v1_21_R1.utils.BossBar(title, progress, color, style, flags);
    }

    @Override
    public CompoundTag newTag() {
        return new com.artillexstudios.axapi.nms.v1_21_R1.items.nbt.CompoundTag(new net.minecraft.nbt.CompoundTag());
    }

    @Override
    public WrappedItemStack wrapItem(ItemStack itemStack) {
        return new com.artillexstudios.axapi.nms.v1_21_R1.items.WrappedItemStack(itemStack);
    }

    @Override
    public WrappedItemStack wrapItem(String snbt) {
        try {
            net.minecraft.nbt.CompoundTag tag = TagParser.parseTag(snbt);
            int dataVersion = tag.getInt("DataVersion");
            net.minecraft.nbt.CompoundTag converted = (net.minecraft.nbt.CompoundTag) MinecraftServer.getServer().fixerUpper.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, tag), dataVersion, CraftMagicNumbers.INSTANCE.getDataVersion()).getValue();
            net.minecraft.world.item.ItemStack item = net.minecraft.world.item.ItemStack.parse(MinecraftServer.getServer().registryAccess(), converted).orElseThrow();
            return new com.artillexstudios.axapi.nms.v1_21_R1.items.WrappedItemStack(item);
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
        ServerPlayer player = ((CraftPlayer) signInput.getPlayer()).getHandle();
        BlockPos pos = CraftLocation.toBlockPosition(signInput.getLocation());
        player.connection.send(new ClientboundBlockUpdatePacket(pos, ((CraftBlockData) Material.OAK_SIGN.createBlockData()).getState()));

        net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        tag.putString("id", "minecraft:oak_sign");

        if (!tag.contains("front_text")) {
            tag.put("front_text", new net.minecraft.nbt.CompoundTag());
        }

        net.minecraft.nbt.CompoundTag sideTag = tag.getCompound("front_text");
        if (!tag.contains("messages")) {
            sideTag.put("messages", new ListTag());
        }

        ListTag messagesNbt = sideTag.getList("messages", Tag.TAG_STRING);

        for (int i = 0; i < 4; i++) {
            String gson = ComponentSerializer.INSTANCE.toGson(i > signInput.getLines().length ? Component.empty() : signInput.getLines()[i]);

            messagesNbt.add(i, net.minecraft.nbt.StringTag.valueOf(gson));
        }

        ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket = new ClientboundBlockEntityDataPacket(pos, BlockEntityType.SIGN, tag);
        ClientboundOpenSignEditorPacket openSignEditorPacket = new ClientboundOpenSignEditorPacket(pos, true);
        player.connection.send(clientboundBlockEntityDataPacket);
        player.connection.send(openSignEditorPacket);
    }

    @Override
    public void setTitle(Inventory inventory, Component title) {
        net.minecraft.network.chat.Component nmsTitle = ComponentSerializer.INSTANCE.toVanilla(title);
        for (HumanEntity viewer : inventory.getViewers()) {
            CraftPlayer craftPlayer = (CraftPlayer) viewer;
            ServerPlayer serverPlayer = craftPlayer.getHandle();
            int containerId = serverPlayer.containerMenu.containerId;
            MenuType<?> windowType = CraftContainer.getNotchInventoryType(inventory);
            serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, nmsTitle));
            craftPlayer.updateInventory();
        }
    }

    @Override
    public DataComponentImpl dataComponents() {
        return new com.artillexstudios.axapi.nms.v1_21_R1.items.data.DataComponentImpl();
    }

    @Override
    public OfflinePlayer getCachedOfflinePlayer(String name) {
        OfflinePlayer result = Bukkit.getPlayerExact(name);
        if (result == null) {
            GameProfile profile = MinecraftServer.getServer().getProfileCache().getProfileIfCached(name);
            if (profile != null) {
                result = ((CraftServer) Bukkit.getServer()).getOfflinePlayer(profile);
            }
        }

        return result;
    }

    @Override
    public void sendPacket(Player player, Object packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        serverPlayer.connection.send((Packet<?>) packet);
    }

    @Override
    public ParallelBlockSetter newParallelSetter(World world) {
        return new ParallelBlockSetterImpl(world);
    }

    @Override
    public int nextEntityId() {
        return entityCounter.incrementAndGet();
    }

    @Override
    public Pair<String, String> textures(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        GameProfile profile = serverPlayer.getGameProfile();
        Optional<Property> property = profile.getProperties().get("textures").stream().findFirst();
        return property.map(value -> Pair.of(value.value(), value.signature())).orElse(null);
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
