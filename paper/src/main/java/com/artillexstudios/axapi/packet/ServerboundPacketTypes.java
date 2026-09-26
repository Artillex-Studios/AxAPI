package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.utils.Maps;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

import java.util.Map;

public final class ServerboundPacketTypes {
    private static final Int2ObjectMap<PacketType> PACKET_TYPES = new Int2ObjectOpenHashMap<>();
    private static final Object2IntArrayMap<PacketType> REVERSE_PACKET_TYPES = new Object2IntArrayMap<>();
    public static final PacketType ACCEPT_TELEPORT = PacketType.createServerbound("ACCEPT_TELEPORT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ATTACK = PacketType.createServerbound("ATTACK", Version.v26_1, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_ENTITY_TAG_QUERY = PacketType.createServerbound("BLOCK_ENTITY_TAG_QUERY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BUNDLE_ITEM_SELECT = PacketType.createServerbound("BUNDLE_ITEM_SELECT", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType CHANGE_DIFFICULTY = PacketType.createServerbound("CHANGE_DIFFICULTY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHANGE_GAMEMODE = PacketType.createServerbound("CHANGE_GAMEMODE", Version.v1_21_5, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_ACK = PacketType.createServerbound("CHAT_ACK", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_COMMAND = PacketType.createServerbound("CHAT_COMMAND", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_COMMAND_SIGNED = PacketType.createServerbound("CHAT_COMMAND_SIGNED", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CHAT = PacketType.createServerbound("CHAT", Version.v1_18, Version.FUTURE_RELEASE);
    // Chat session update 1.19.3+
    public static final PacketType CHUNK_BATCH_RECEIVE = PacketType.createServerbound("CHUNK_BATCH_RECEIVE", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_PREVIEW = PacketType.createServerbound("CHAT_PREVIEW", Version.v1_19, Version.v1_19);
    public static final PacketType COMMAND = PacketType.createServerbound("COMMAND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CLIENT_TICK_END = PacketType.createServerbound("CLIENT_TICK_END", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType CLIENT_INFORMATION = PacketType.createServerbound("CLIENT_INFORMATION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMMAND_SUGGESTIONS = PacketType.createServerbound("COMMAND_SUGGESTIONS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONFIGURATION_ACKNOWLEDGED = PacketType.createServerbound("CONFIGURATION_ACKNOWLEDGED", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_BUTTON_CLICK = PacketType.createServerbound("CONTAINER_BUTTON_CLICK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CLICK = PacketType.createServerbound("CONTAINER_CLICK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CLOSE = PacketType.createServerbound("CONTAINER_CLOSE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_SLOT_STATE_CHANGED = PacketType.createServerbound("CONTAINER_SLOT_STATE_CHANGED", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType COOKIE_RESPONSE = PacketType.createServerbound("COOKIE_RESPONSE", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_PAYLOAD = PacketType.createServerbound("CUSTOM_PAYLOAD", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DEBUG_SAMPLE_SUBSCRIPTION = PacketType.createServerbound("DEBUG_SAMPLE_SUBSCRIPTION", Version.v1_20_4, Version.v1_21_6); // Changed the name in 1.21.9 to DEBUG_SUBSCRIPTION_REQUEST
    public static final PacketType DEBUG_SUBSCRIPTION_REQUEST = PacketType.createServerbound("DEBUG_SUBSCRIPTION_REQUEST", Version.v1_21_7, Version.FUTURE_RELEASE);
    public static final PacketType EDIT_BOOK = PacketType.createServerbound("EDIT_BOOK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_TAG_QUERY = PacketType.createServerbound("ENTITY_TAG_QUERY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType INTERACT = PacketType.createServerbound("INTERACT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType JIGSAW_GENERATE = PacketType.createServerbound("JIGSAW_GENERATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType KEEPALIVE = PacketType.createServerbound("KEEPALIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LOCK_DIFFICULTY = PacketType.createServerbound("LOCK_DIFFICULTY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_POS = PacketType.createServerbound("MOVE_PLAYER_POS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_POS_ROT = PacketType.createServerbound("MOVE_PLAYER_POS_ROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_ROT = PacketType.createServerbound("MOVE_PLAYER_ROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_STATUS = PacketType.createServerbound("MOVE_PLAYER_STATUS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_VEHICLE = PacketType.createServerbound("MOVE_VEHICLE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PADDLE_BOAT = PacketType.createServerbound("PADDLE_BOAT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PICK_ITEM = PacketType.createServerbound("PICK_ITEM", Version.v1_18, Version.v1_21_2);
    public static final PacketType PICK_ITEM_FROM_BLOCK = PacketType.createServerbound("PICK_ITEM_FROM_BLOCK", Version.v1_21_3, Version.FUTURE_RELEASE);
    public static final PacketType PICK_ITEM_FROM_ENTITY = PacketType.createServerbound("PICK_ITEM_FROM_ENTITY", Version.v1_21_3, Version.FUTURE_RELEASE);
    public static final PacketType PING_REQUEST = PacketType.createServerbound("PING_REQUEST", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType PLACE_RECIPE = PacketType.createServerbound("PLACE_RECIPE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_ABILITIES = PacketType.createServerbound("PLAYER_ABILITIES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_ACTION = PacketType.createServerbound("PLAYER_ACTION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_COMMAND = PacketType.createServerbound("PLAYER_COMMAND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_INPUT = PacketType.createServerbound("PLAYER_INPUT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_LOADED = PacketType.createServerbound("PLAYER_LOADED", Version.v1_21_3, Version.FUTURE_RELEASE);
    public static final PacketType PONG = PacketType.createServerbound("PONG", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PUNCH = PacketType.createServerbound("PUNCH", Version.v26_3, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_SESSION_UPDATE = PacketType.createServerbound("CHAT_SESSION_UPDATE", Version.v1_18, Version.FUTURE_RELEASE); // Order changed to after serverboundchat in 1.19.3
    public static final PacketType RECIPE_BOOK_CHANGE_SETTINGS = PacketType.createServerbound("RECIPE_BOOK_CHANGE_SETTINGS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_SEEN_RECIPE = PacketType.createServerbound("RECIPE_BOOK_SEEN_RECIPE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RENAME_ITEM = PacketType.createServerbound("RENAME_ITEM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RESOURCE_PACK = PacketType.createServerbound("RESOURCE_PACK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SEEN_ADVANCEMENTS = PacketType.createServerbound("SEEN_ADVANCEMENTS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SELECT_TRADE = PacketType.createServerbound("SELECT_TRADE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_BEACON = PacketType.createServerbound("SET_BEACON", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CARRIED_ITEM = PacketType.createServerbound("SET_CARRIED_ITEM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_COMMAND_BLOCK = PacketType.createServerbound("SET_COMMAND_BLOCK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_COMMAND_MINECART = PacketType.createServerbound("SET_COMMAND_MINECART", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CREATIVE_MODE_SLOT = PacketType.createServerbound("SET_CREATIVE_MODE_SLOT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_GAME_RULE = PacketType.createServerbound("SET_GAME_RULE", Version.v26_1, Version.FUTURE_RELEASE);
    public static final PacketType SET_JIGSAW_BLOCK = PacketType.createServerbound("SET_JIGSAW_BLOCK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_STRUCTURE_BLOCK = PacketType.createServerbound("SET_STRUCTURE_BLOCK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TEST_BLOCK = PacketType.createServerbound("SET_TEST_BLOCK", Version.v1_21_4, Version.FUTURE_RELEASE);
    public static final PacketType SIGN_UPDATE = PacketType.createServerbound("SIGN_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SPECTATE_ENTITY = PacketType.createServerbound("SPECTATE_ENTITY", Version.v26_1, Version.FUTURE_RELEASE);
    public static final PacketType SWING = PacketType.createServerbound("SWING", Version.v1_18, Version.v26_2);
    public static final PacketType TELEPORT_TO_ENTITY = PacketType.createServerbound("TELEPORT_TO_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TEST_INSTANCE_BLOCK_ACTION = PacketType.createServerbound("TEST_INSTANCE_BLOCK_ACTION", Version.v1_21_4, Version.FUTURE_RELEASE);
    public static final PacketType USE_ITEM_ON = PacketType.createServerbound("USE_ITEM_ON", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType USE_ITEM = PacketType.createServerbound("USE_ITEM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_CLICK_ACTION = PacketType.createServerbound("CUSTOM_CLICK_ACTION", Version.v1_21_5, Version.FUTURE_RELEASE);


    public static void init() {
        register(ACCEPT_TELEPORT);
        register(ATTACK); // 26.1
        register(BLOCK_ENTITY_TAG_QUERY);
        register(BUNDLE_ITEM_SELECT);
        register(CHANGE_DIFFICULTY);
        register(CHANGE_GAMEMODE);
        register(CHAT_ACK);
        register(CHAT_COMMAND);
        register(CHAT_COMMAND_SIGNED);
        register(CHAT);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19_3)) {
            register(CHAT_SESSION_UPDATE);
        }
        register(CHUNK_BATCH_RECEIVE);
        register(CHAT_PREVIEW);
        register(COMMAND);
        register(CLIENT_TICK_END);
        register(CLIENT_INFORMATION);
        register(COMMAND_SUGGESTIONS);
        register(CONFIGURATION_ACKNOWLEDGED);
        register(CONTAINER_BUTTON_CLICK);
        register(CONTAINER_CLICK);
        register(CONTAINER_CLOSE);
        register(CONTAINER_SLOT_STATE_CHANGED);
        register(COOKIE_RESPONSE);
        register(CUSTOM_PAYLOAD);
        register(DEBUG_SAMPLE_SUBSCRIPTION);
        register(DEBUG_SUBSCRIPTION_REQUEST);
        register(EDIT_BOOK);
        register(ENTITY_TAG_QUERY);
        register(INTERACT);
        register(JIGSAW_GENERATE);
        register(KEEPALIVE);
        register(LOCK_DIFFICULTY);
        register(MOVE_PLAYER_POS);
        register(MOVE_PLAYER_POS_ROT);
        register(MOVE_PLAYER_ROT);
        register(MOVE_PLAYER_STATUS);
        register(MOVE_VEHICLE);
        register(PADDLE_BOAT);
        register(PICK_ITEM);
        register(PICK_ITEM_FROM_BLOCK);
        register(PICK_ITEM_FROM_ENTITY);
        register(PING_REQUEST);
        register(PLACE_RECIPE);
        register(PLAYER_ABILITIES);
        register(PLAYER_ACTION);
        register(PLAYER_COMMAND);
        register(PLAYER_INPUT);
        register(PLAYER_LOADED);
        register(PONG);
        register(PUNCH);
        if (Version.getServerVersion().isOlderThan(Version.v1_19_3)) {
            register(CHAT_SESSION_UPDATE);
        }
        register(RECIPE_BOOK_CHANGE_SETTINGS);
        register(RECIPE_BOOK_SEEN_RECIPE);
        register(RENAME_ITEM);
        register(RESOURCE_PACK);
        register(SEEN_ADVANCEMENTS);
        register(SELECT_TRADE);
        register(SET_BEACON);
        register(SET_CARRIED_ITEM);
        register(SET_COMMAND_BLOCK);
        register(SET_COMMAND_MINECART);
        register(SET_CREATIVE_MODE_SLOT);
        register(SET_GAME_RULE); // 26.1
        register(SET_JIGSAW_BLOCK);
        register(SET_STRUCTURE_BLOCK);
        register(SET_TEST_BLOCK);
        register(SIGN_UPDATE);
        register(SPECTATE_ENTITY); // 26.1
        register(SWING);
        register(TELEPORT_TO_ENTITY);
        register(TEST_INSTANCE_BLOCK_ACTION);
        register(USE_ITEM_ON);
        register(USE_ITEM);
        register(CUSTOM_CLICK_ACTION);
    }

    /**
     * Register a PacketType into the packet id system. Both first and last versions are inclusive!
     * For example, if the server is on 1.18.2, and the packet is from 1.18 to 1.18.2 then this will be registered on 1.18.2
     *
     * @param packetType The PacketType to register
     */
    public static void register(PacketType packetType) {
        if (Version.getServerVersion().isNewerThanOrEqualTo(packetType.from()) && Version.getServerVersion().isOlderThanOrEqualTo(packetType.to())) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Registering serverbound packet: {}", packetType);
            }

            int size = PACKET_TYPES.size();
            PACKET_TYPES.put(size, packetType);
            REVERSE_PACKET_TYPES.put(packetType, size);
        } else if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Did not register serverbound packet: {}", packetType);
        }
    }

    public static PacketType forPacketId(int id) {
        return PACKET_TYPES.get(id);
    }

    public static int forPacketType(PacketType type) {
        return REVERSE_PACKET_TYPES.getOrDefault(type, -1);
    }

    public static String dump() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, PacketType> entry : Maps.order(PACKET_TYPES).entrySet()) {
            builder.append(entry.getValue().name())
                    .append('\n');
        }

        return builder.toString();
    }
}
