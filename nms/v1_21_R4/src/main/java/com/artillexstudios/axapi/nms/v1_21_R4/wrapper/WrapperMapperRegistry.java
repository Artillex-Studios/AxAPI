package com.artillexstudios.axapi.nms.v1_21_R4.wrapper;

import com.artillexstudios.axapi.collections.RegistrationFailedException;
import com.artillexstudios.axapi.collections.Registry;
import com.artillexstudios.axapi.nms.wrapper.Wrapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperMapper;
import com.artillexstudios.axapi.nms.wrapper.exception.UnknownWrappedException;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

public class WrapperMapperRegistry {
    private static final Registry<String, WrapperMapper<?>> mappers = new Registry<>();
    public static final WrapperMapper<?> SERVER_PLAYER = register("server_player", object -> {
        if (object instanceof ServerPlayerWrapper serverPlayerWrapper) {
            return serverPlayerWrapper;
        } else if (object instanceof Player player) {
            return new ServerPlayerWrapper(player);
        } else if (object instanceof ServerPlayer serverPlayer) {
            return new ServerPlayerWrapper(serverPlayer);
        }

        throw new UnknownWrappedException("Could not wrap " + object + ", due to there being no mapper for it's class: " + object.getClass() + "!");
    });

    public static WrapperMapper<?> register(String id, WrapperMapper<?> mapper) {
        try {
            mappers.register(id, mapper);
        } catch (RegistrationFailedException exception) {
            LogUtils.error("An exception occurred while registering mapper {}!", id, exception);
        }

        return mapper;
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
