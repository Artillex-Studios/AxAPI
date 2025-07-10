package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.gui.configuration.ConfigurationBackedGui;
import com.artillexstudios.axapi.gui.inventory.builder.PaginatedGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.DynamicGui;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.AsyncGuiItemProvider;
import com.artillexstudios.axapi.items.WrappedItemStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuiTest {

    public static void test() {
        DynamicGui gui = GuiBuilder.createDynamic()
                .title(ctx -> Component.text("oreo"))
                .rows(6)
                .build(null);

        gui.setItem(0, new AsyncGuiItemProvider(new GuiItem(ctx -> WrappedItemStack.wrap(new ItemStack(Material.STONE)), (ctx, event) -> {

        })));

        new ConfigurationBackedGui<>(null, PaginatedGuiBuilder.createPaginated()).create(gui.player);
    }
}
