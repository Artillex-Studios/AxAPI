package com.artillexstudios.axapi.nms.v1_20_R3.utils;

import com.artillexstudios.axapi.scheduler.Scheduler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AdvancementToast implements com.artillexstudios.axapi.utils.AdvancementToast {
    private final ResourceLocation resourceLocation;
    private boolean announceChat;
    private net.minecraft.network.chat.Component content;
    private net.minecraft.network.chat.Component description;
    private net.minecraft.world.item.ItemStack itemStack;
    private ToastType type;
    private ClientboundUpdateAdvancementsPacket showPacket;
    private ClientboundUpdateAdvancementsPacket forgetPacket;

    public AdvancementToast(Key namespace, ItemStack itemStack, boolean announceChat, Component content, Component description, ToastType type) {
        resourceLocation = new ResourceLocation(namespace.namespace(), namespace.value());
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);
        this.announceChat = announceChat;
        this.content = net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(content));
        this.description = net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(description));
        this.type = type;

        updatePacket();
    }

    @Override
    public void setContent(Component component) {
        this.content = net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(component));

        updatePacket();
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);

        updatePacket();
    }

    @Override
    public void setType(ToastType type) {
        this.type = type;

        updatePacket();
    }

    @Override
    public void setAnnounceChat(boolean announceChat) {
        this.announceChat = announceChat;

        updatePacket();
    }

    @Override
    public void setDescription(Component description) {
        this.description = net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serializer().toJsonTree(description));

        updatePacket();
    }

    @Override
    public void send(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(showPacket);

        Scheduler.get().runLater(task -> {
            if (serverPlayer.connection.isDisconnected()) {
                return;
            }

            serverPlayer.connection.send(forgetPacket);
        }, 80);
    }

    private void updatePacket() {
        DisplayInfo displayInfo = new DisplayInfo(itemStack == null ? net.minecraft.world.item.ItemStack.EMPTY : itemStack, content, description, Optional.empty(), AdvancementType.valueOf(type.name().toUpperCase(Locale.ENGLISH)), true, announceChat, true);
        Advancement advancement = new Advancement(Optional.empty(), Optional.of(displayInfo), AdvancementRewards.EMPTY, Map.of(), AdvancementRequirements.EMPTY, false);
        AdvancementHolder holder = new AdvancementHolder(resourceLocation, advancement);

        showPacket = new ClientboundUpdateAdvancementsPacket(false, List.of(holder), Set.of(), Map.of());
        forgetPacket = new ClientboundUpdateAdvancementsPacket(false, List.of(), Set.of(resourceLocation), Map.of());
    }
}
