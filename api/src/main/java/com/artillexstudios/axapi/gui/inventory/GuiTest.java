package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.gui.inventory.implementation.DynamicGui;
import com.artillexstudios.axapi.items.WrappedItemStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuiTest {

    public static void test() {
        DynamicGui gui = GuiBuilder.createDynamic()
                .title(ctx -> Component.text("oreo"))
                .rows(6)
                .build();

        gui.setItem(0, new GuiItem(ctx -> WrappedItemStack.wrap(new ItemStack(Material.STONE)), event -> {

        }));
    }
}
