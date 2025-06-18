package com.artillexstudios.axapi.nms.v1_21_R5.wrapper;

import com.artillexstudios.axapi.collections.RegistrationFailedException;
import com.artillexstudios.axapi.collections.Registry;
import com.artillexstudios.axapi.nms.v1_21_R5.ItemStackSerializer;
import com.artillexstudios.axapi.nms.v1_21_R5.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.wrapper.Wrapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperMapper;
import com.artillexstudios.axapi.nms.wrapper.exception.UnknownWrappedException;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WrapperMapperRegistry {
    private static final Registry<String, WrapperMapper<?>> mappers = new Registry<>();
    public static final WrapperMapper<ServerPlayerWrapper> SERVER_PLAYER = register("server_player", object -> {
        if (object instanceof ServerPlayerWrapper serverPlayerWrapper) {
            return serverPlayerWrapper;
        } else if (object instanceof Player player) {
            return new ServerPlayerWrapper(player);
        } else if (object instanceof ServerPlayer serverPlayer) {
            return new ServerPlayerWrapper(serverPlayer);
        }

        throw new UnknownWrappedException("Could not wrap " + object + ", due to there being no mapper for it's class: " + object.getClass() + "!");
    });
    public static final WrapperMapper<WorldWrapper> WORLD = register("world", object -> {
        if (object instanceof WorldWrapper worldWrapper) {
            return worldWrapper;
        } else if (object instanceof World world) {
            return new WorldWrapper(world);
        } else if (object instanceof ServerLevel serverLevel) {
            return new WorldWrapper(serverLevel);
        }

        throw new UnknownWrappedException("Could not wrap " + object + ", due to there being no mapper for it's class: " + object.getClass() + "!");
    });
    public static final WrapperMapper<ServerWrapper> SERVER = register("server", object -> ServerWrapper.INSTANCE);
    public static final WrapperMapper<WrappedItemStack> ITEM_STACK = register("item_stack", object -> {
        return switch (object) {
            case WrappedItemStack wrapped -> wrapped;
            case ItemStack itemStack -> new WrappedItemStack(itemStack);
            case String snbt -> new WrappedItemStack(ItemStackSerializer.INSTANCE.deserializeFromSnbt(snbt));
            case byte[] bytes -> new WrappedItemStack(ItemStackSerializer.INSTANCE.deserializeFromBytes(bytes));
            default ->
                    throw new UnknownWrappedException("Could not wrap " + object + ", due to there being no mapper for it's class: " + object.getClass() + "!");
        };
    });

    public static <Z extends Wrapper<?>, T extends WrapperMapper<Z>> T register(String id, WrapperMapper<Z> mapper) {
        try {
            mappers.register(id, mapper);
        } catch (RegistrationFailedException exception) {
            LogUtils.error("An exception occurred while registering mapper {}!", id, exception);
        }

        return (T) mapper;
    }

    public static <T extends WrapperMapper<?>> T mapper(String id) {
        try {
            return (T) mappers.get(id);
        } catch (RegistrationFailedException exception) {
            LogUtils.error("Failed to find mapper {}! This is an issue with the code, and it should be reported to the developer of the plugin!",
                    id, exception);
            return null;
        }
    }
}
