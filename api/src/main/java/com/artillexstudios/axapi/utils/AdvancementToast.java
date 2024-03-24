package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface AdvancementToast {

    default void send(Player player, Component content) {
        NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), new ItemStack(Material.AIR), false, content, Component.empty(), ToastType.TASK).send(player);
    }

    default void send(Player player, Component content, ItemStack display) {
        NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), display, false, content, Component.empty(), ToastType.TASK).send(player);
    }

    default void send(Player player, Component content, ItemStack display, ToastType type) {
        NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), display, false, content, Component.empty(), type).send(player);
    }

    default void send(Player player, Component content, ItemStack display, ToastType type, Component description) {
        NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), display, false, content, description, type).send(player);
    }

    default AdvancementToast create() {
        return NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), new ItemStack(Material.AIR), false, Component.empty(), Component.empty(), ToastType.TASK);
    }

    default AdvancementToast create(Component content, ItemStack display, ToastType type) {
        return NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), display, false, content, Component.empty(), type);
    }

    default AdvancementToast create(Component content, ItemStack display, ToastType type, Component description) {
        return NMSHandlers.getNmsHandler().newToast(Key.key("axapi", RandomStringGenerator.lowercase().generate(8)), display, false, content, description, type);
    }

    void setContent(Component component);

    void setItemStack(ItemStack itemStack);

    void setType(ToastType type);

    void setAnnounceChat(boolean announceChat);

    void setDescription(Component description);

    void send(Player player);

    enum ToastType {
        GOAL,
        TASK,
        CHALLENGE
    }
}
