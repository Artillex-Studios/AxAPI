package com.artillexstudios.axapi.nms.v1_21_R4;

import com.artillexstudios.axapi.gui.AnvilInput;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.nms.v1_21_R4.entity.PacketEntity;
import com.artillexstudios.axapi.nms.v1_21_R4.items.data.DataComponentImpl;
import com.artillexstudios.axapi.nms.v1_21_R4.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.v1_21_R4.loot.LootTable;
import com.artillexstudios.axapi.nms.v1_21_R4.packet.PacketTransformer;
import com.artillexstudios.axapi.nms.v1_21_R4.wrapper.WrapperMapperRegistry;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperMapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperRegistry;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.DynamicOps;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {

    @Override
    public Serializer<Object, Component> componentSerializer() {
        return new Serializer<>() {
            @Override
            public Component serialize(Object object) {
                if (!(object instanceof net.minecraft.network.chat.Component component)) {
                    throw new IllegalArgumentException("Can only serialize component!");
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
    public int getProtocolVersionId(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        return serverPlayer.connection.connection.protocolVersion;
    }

    @Override
    public PacketEntity createEntity(EntityType entityType, Location location) {
        return new PacketEntity(entityType, location);
    }

    @Override
    public CompoundTag newTag() {
        return new CompoundTag(new net.minecraft.nbt.CompoundTag());
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
        SignText text = new SignText();
        for (int i = 0; i < 4; i++) {
            text = text.setMessage(i, ComponentSerializer.instance().toVanilla(i > signInput.getLines().length ? Component.empty() : signInput.getLines()[i]));
        }

        DynamicOps<Tag> dynamicOps = MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE);
        tag.store("front_text", SignText.DIRECT_CODEC, dynamicOps, text);
        ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket = new ClientboundBlockEntityDataPacket(pos, BlockEntityType.SIGN, tag);
        ClientboundOpenSignEditorPacket openSignEditorPacket = new ClientboundOpenSignEditorPacket(pos, true);
        player.connection.send(clientboundBlockEntityDataPacket);
        player.connection.send(openSignEditorPacket);
    }

    @Override
    public void setTitle(Inventory inventory, Component title) {
        net.minecraft.network.chat.Component nmsTitle = ComponentSerializer.instance().toVanilla(title);
        for (HumanEntity viewer : inventory.getViewers()) {
            CraftPlayer craftPlayer = (CraftPlayer) viewer;
            ServerPlayer serverPlayer = craftPlayer.getHandle();
            int containerId = serverPlayer.containerMenu.containerId;
            MenuType<?> windowType = CraftContainer.getNotchInventoryType(inventory);
            serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, nmsTitle));
            serverPlayer.containerMenu.sendAllDataToRemote();
        }
    }

    @Override
    public DataComponentImpl dataComponents() {
        return new DataComponentImpl();
    }

    @Override
    public ServerPlayerWrapper dummyPlayer() {
        return WrapperRegistry.SERVER_PLAYER.map(new ServerPlayer(MinecraftServer.getServer(), WrapperMapperRegistry.WORLD.map(Bukkit.getWorlds().getFirst()).asMinecraft(), new GameProfile(UUID.randomUUID(), "dummy"), ClientInformation.createDefault()));
    }

    @Override
    public LootTable lootTable(Key key) {
        return new LootTable(key);
    }

    @Override
    public void openAnvilInput(AnvilInput anvilInput) {
        Player player = anvilInput.player();
        player.closeInventory();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        Location location = anvilInput.location();
        AnvilMenu anvilMenu = new AnvilMenu(serverPlayer.nextContainerCounter(), serverPlayer.getInventory(), ContainerLevelAccess.create(((CraftWorld) location.getWorld()).getHandle(), new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        anvilMenu.checkReachable = false;
        anvilMenu.setTitle(ComponentSerializer.instance().toVanilla(anvilInput.title()));

        Inventory inventory = ((AbstractContainerMenu) anvilMenu).getBukkitView().getTopInventory();
        inventory.setItem(0, anvilInput.itemStack().toBukkit());

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(anvilMenu.containerId, anvilMenu.getType(), anvilMenu.getTitle()));
        serverPlayer.containerMenu = anvilMenu;
        serverPlayer.initMenu(anvilMenu);
    }

    @Override
    public <T extends WrapperMapper<?>> T mapper(String id) {
        return WrapperMapperRegistry.mapper(id);
    }

    @Override
    public FriendlyByteBuf newBuf() {
        return PacketTransformer.newByteBuf();
    }
}
