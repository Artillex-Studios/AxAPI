package com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents;

import com.artillexstudios.axapi.collections.RegistrationFailedException;
import com.artillexstudios.axapi.collections.Registry;
import com.artillexstudios.axapi.items.components.DataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.AdventureComponentDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.CustomDataDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.CustomModelDataDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.DataComponentHandler;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.DyedColorDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.EnchantmentsDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.IdentityDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.LoreDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.PotionContentsDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.ProfileDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.RarityDataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.UnbreakableDataComponent;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DataComponentTypes {
    private static final Registry<String, DataComponent<?>> components = new Registry<>();
    private static final List<String> unsupportedComponents = new ArrayList<>();

    static {
        register("custom_data", DataComponents.CUSTOM_DATA, new CustomDataDataComponent());
        register("max_stack_size", DataComponents.MAX_STACK_SIZE, new IdentityDataComponent<>());
        register("max_damage", DataComponents.MAX_DAMAGE, new IdentityDataComponent<>());
        register("damage", DataComponents.DAMAGE, new IdentityDataComponent<>());
        register("custom_name", DataComponents.CUSTOM_NAME, new AdventureComponentDataComponent());
        register("item_name", DataComponents.ITEM_NAME, new AdventureComponentDataComponent());
        registerUnsupported("item_model");
        registerUnsupported("tooltip_style");
        register("repair_cost", DataComponents.REPAIR_COST, new IdentityDataComponent<>());
        register("lore", DataComponents.LORE, new LoreDataComponent());
        register("rarity", DataComponents.RARITY, new RarityDataComponent());
        register("enchantments", DataComponents.ENCHANTMENTS, new EnchantmentsDataComponent());
        register("stored_enchantments", DataComponents.STORED_ENCHANTMENTS, new EnchantmentsDataComponent());
        register("custom_model_data", DataComponents.CUSTOM_MODEL_DATA, new CustomModelDataDataComponent());
        register("enchantment_glint_override", DataComponents.ENCHANTMENT_GLINT_OVERRIDE, new IdentityDataComponent<>());
        register("profile", DataComponents.PROFILE, new ProfileDataComponent());
//        register("material", null); // TODO: implement datacomponent for this
        register("dyed_color", DataComponents.DYED_COLOR, new DyedColorDataComponent());
        register("potion_contents", DataComponents.POTION_CONTENTS, new PotionContentsDataComponent());
        register("unbreakable", DataComponents.UNBREAKABLE, new UnbreakableDataComponent());
        registerUnsupported("minimum_attack_charge");
        registerUnsupported("tooltip_display");
//        register("trim", DataComponents.TRIM, new LoreDataComponent());
//        register("base_color", DataComponents.BASE_COLOR, new DyedColorDataComponent());
    }

    public static <T extends DataComponent<?>> void register(String id, T mapper) {
        try {
            components.register(id, mapper);
        } catch (RegistrationFailedException exception) {
            LogUtils.error("An exception occurred while registering component {}!", id, exception);
        }
    }

    public static <T, Z> void register(String id, DataComponentType<@NotNull Z> type, DataComponentHandler<T, Z> mapper) {
        register(id, com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl.DataComponent.create(id, type, mapper));
    }

    public static void registerUnsupported(String id) {
        unsupportedComponents.add(id);
    }

    public static DataComponent<?> component(String id) {
        try {
            return components.get(id);
        } catch (RegistrationFailedException exception) {
            if (unsupportedComponents.contains(id)) {
                return null;
            }

            LogUtils.error("Failed to find component {}! This is an issue with the code, and it should be reported to the developer of the plugin!",
                    id, exception);
            return null;
        }
    }
}
