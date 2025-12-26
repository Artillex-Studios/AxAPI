package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents;

import com.artillexstudios.axapi.collections.RegistrationFailedException;
import com.artillexstudios.axapi.collections.Registry;
import com.artillexstudios.axapi.items.components.DataComponent;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.AdventureComponentDataComponent;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.CustomDataDataComponent;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.DataComponentHandler;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.IdentifierDataComponent;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.IdentityDataComponent;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.LoreDataComponent;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import org.jetbrains.annotations.NotNull;

public final class DataComponentTypes {
    private static final Registry<String, DataComponent<?>> components = new Registry<>();

    static {
        register("custom_data", DataComponents.CUSTOM_DATA, new CustomDataDataComponent());
        register("max_stack_size", DataComponents.MAX_STACK_SIZE, new IdentityDataComponent<>());
        register("max_damage", DataComponents.MAX_DAMAGE, new IdentityDataComponent<>());
        register("damage", DataComponents.DAMAGE, new IdentityDataComponent<>());
        register("custom_name", DataComponents.CUSTOM_NAME, new AdventureComponentDataComponent());
        register("item_name", DataComponents.ITEM_NAME, new AdventureComponentDataComponent());
        register("item_model", DataComponents.ITEM_MODEL, new IdentifierDataComponent());
        register("tooltip_style", DataComponents.TOOLTIP_STYLE, new IdentifierDataComponent());
        register("lore", DataComponents.LORE, new LoreDataComponent());
    }

    public static <T extends DataComponent<?>> void register(String id, T mapper) {
        try {
            components.register(id, mapper);
        } catch (RegistrationFailedException exception) {
            LogUtils.error("An exception occurred while registering component {}!", id, exception);
        }
    }

    public static <T, Z> void register(String id, DataComponentType<@NotNull Z> type, DataComponentHandler<T, Z> mapper) {
        register(id, com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl.DataComponent.create(type, mapper));
    }

    public static <T extends DataComponent<?>> T component(String id) {
        try {
            return (T) components.get(id);
        } catch (RegistrationFailedException exception) {
            LogUtils.error("Failed to find component {}! This is an issue with the code, and it should be reported to the developer of the plugin!",
                    id, exception);
            return null;
        }
    }
}
