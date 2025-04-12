package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.utils.Version;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

public final class ServerboundPacketTypes {
    private static final Int2ObjectMap<PacketType> PACKET_TYPES = new Int2ObjectOpenHashMap<>();
    private static final Object2IntArrayMap<PacketType> REVERSE_PACKET_TYPES = new Object2IntArrayMap<>();
    public static final PacketType ACCEPT_TELEPORT = new PacketType("ACCEPT_TELEPORT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_ENTITY_TAG_QUERY = new PacketType("BLOCK_ENTITY_TAG_QUERY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BUNDLE_ITEM_SELECT = new PacketType("BUNDLE_ITEM_SELECT", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType CHANGE_DIFFICULTY = new PacketType("CHANGE_DIFFICULTY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_ACK = new PacketType("CHAT_ACK", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_COMMAND = new PacketType("CHAT_COMMAND", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_COMMAND_SIGNED = new PacketType("CHAT_COMMAND_SIGNED", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType CHAT = new PacketType("CHAT", Version.v1_18, Version.FUTURE_RELEASE);
    // Chat session update 1.19.3+
    public static final PacketType CHUNK_BATCH_RECEIVE = new PacketType("CHUNK_BATCH_RECEIVE", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_PREVIEW = new PacketType("CHAT_PREVIEW", Version.v1_19, Version.v1_19);
    public static final PacketType COMMAND = new PacketType("COMMAND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CLIENT_TICK_END = new PacketType("CLIENT_TICK_END", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType CLIENT_INFORMATION = new PacketType("CLIENT_INFORMATION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMMAND_SUGGESTIONS = new PacketType("COMMAND_SUGGESTIONS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONFIGURATION_ACKNOWLEDGED = new PacketType("CONFIGURATION_ACKNOWLEDGED", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_BUTTON_CLICK = new PacketType("CONTAINER_BUTTON_CLICK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CLICK = new PacketType("CONTAINER_CLICK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CLOSE = new PacketType("CONTAINER_CLOSE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_SLOT_STATE_CHANGED = new PacketType("CONTAINER_SLOT_STATE_CHANGED", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType COOKIE_RESPONSE = new PacketType("COOKIE_RESPONSE", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_PAYLOAD = new PacketType("CUSTOM_PAYLOAD", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DEBUG_SAMPLE_SUBSCRIPTION = new PacketType("DEBUG_SAMPLE_SUBSCRIPTION", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType EDIT_BOOK = new PacketType("EDIT_BOOK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_TAG_QUERY = new PacketType("ENTITY_TAG_QUERY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType INTERACT = new PacketType("INTERACT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType JIGSAW_GENERATE = new PacketType("JIGSAW_GENERATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType KEEPALIVE = new PacketType("KEEPALIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LOCK_DIFFICULTY = new PacketType("LOCK_DIFFICULTY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_POS = new PacketType("MOVE_PLAYER_POS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_POS_ROT = new PacketType("MOVE_PLAYER_POS_ROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_ROT = new PacketType("MOVE_PLAYER_ROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_PLAYER_STATUS = new PacketType("MOVE_PLAYER_STATUS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_VEHICLE = new PacketType("MOVE_VEHICLE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PADDLE_BOAT = new PacketType("PADDLE_BOAT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PICK_ITEM = new PacketType("PICK_ITEM", Version.v1_18, Version.v1_21_2);
    public static final PacketType PICK_ITEM_FROM_BLOCK = new PacketType("PICK_ITEM_FROM_BLOCK", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType PICK_ITEM_FROM_ENTITY = new PacketType("PICK_ITEM_FROM_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PING_REQUEST = new PacketType("PING_REQUEST", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType PLACE_RECIPE = new PacketType("PLACE_RECIPE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_ABILITIES = new PacketType("PLAYER_ABILITIES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_ACTION = new PacketType("PLAYER_ACTION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_COMMAND = new PacketType("PLAYER_COMMAND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_INPUT = new PacketType("PLAYER_INPUT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_LOADED = new PacketType("PLAYER_LOADED", Version.v1_21_3, Version.FUTURE_RELEASE);
    public static final PacketType PONG = new PacketType("PONG", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_SESSION_UPDATE = new PacketType("CHAT_SESSION_UPDATE", Version.v1_18, Version.FUTURE_RELEASE); // Order changed to after serverboundchat in 1.19.3
    public static final PacketType RECIPE_BOOK_CHANGE_SETTINGS = new PacketType("RECIPE_BOOK_CHANGE_SETTINGS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_SEEN_RECIPE = new PacketType("RECIPE_BOOK_SEEN_RECIPE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RENAME_ITEM = new PacketType("RENAME_ITEM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RESOURCE_PACK = new PacketType("RESOURCE_PACK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SEEN_ADVANCEMENTS = new PacketType("SEEN_ADVANCEMENTS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SELECT_TRADE = new PacketType("SELECT_TRADE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_BEACON = new PacketType("SET_BEACON", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CARRIED_ITEM = new PacketType("SET_CARRIED_ITEM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_COMMAND_BLOCK = new PacketType("SET_COMMAND_BLOCK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_COMMAND_MINECART = new PacketType("SET_COMMAND_MINECART", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CREATIVE_MODE_SLOT = new PacketType("SET_CREATIVE_MODE_SLOT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_JIGSAW_BLOCK = new PacketType("SET_JIGSAW_BLOCK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_STRUCTURE_BLOCK = new PacketType("SET_STRUCTURE_BLOCK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TEST_BLOCK = new PacketType("SET_TEST_BLOCK", Version.v1_21_4, Version.FUTURE_RELEASE);
    public static final PacketType SIGN_UPDATE = new PacketType("SIGN_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SWING = new PacketType("SWING", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TELEPORT_TO_ENTITY = new PacketType("TELEPORT_TO_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TEST_INSTANCE_BLOCK_ACTION = new PacketType("TEST_INSTANCE_BLOCK_ACTION", Version.v1_21_4, Version.FUTURE_RELEASE);
    public static final PacketType USE_ITEM_ON = new PacketType("USE_ITEM_ON", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType USE_ITEM = new PacketType("USE_ITEM", Version.v1_18, Version.FUTURE_RELEASE);


    public static void init() {
        register(ACCEPT_TELEPORT);
        register(BLOCK_ENTITY_TAG_QUERY);
        register(BUNDLE_ITEM_SELECT);
        register(CHANGE_DIFFICULTY);
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
        register(SET_JIGSAW_BLOCK);
        register(SET_STRUCTURE_BLOCK);
        register(SET_TEST_BLOCK);
        register(SIGN_UPDATE);
        register(SWING);
        register(TELEPORT_TO_ENTITY);
        register(TEST_INSTANCE_BLOCK_ACTION);
        register(USE_ITEM_ON);
        register(USE_ITEM);
    }

    public static void register(PacketType packetType) {
        if (Version.getServerVersion().isNewerThanOrEqualTo(packetType.from()) && Version.getServerVersion().isOlderThanOrEqualTo(packetType.to())) {
            int size = PACKET_TYPES.size();
            PACKET_TYPES.put(size, packetType);
            REVERSE_PACKET_TYPES.put(packetType, size);
        }
    }

    public static PacketType forPacketId(int id) {
        return PACKET_TYPES.get(id);
    }

    public static int forPacketType(PacketType type) {
        return REVERSE_PACKET_TYPES.getOrDefault(type, -1);
    }
}
