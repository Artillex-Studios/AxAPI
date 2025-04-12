package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.utils.Version;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

public final class ClientboundPacketTypes {
    private static final Int2ObjectMap<PacketType> PACKET_TYPES = new Int2ObjectOpenHashMap<>();
    private static final Object2IntArrayMap<PacketType> REVERSE_PACKET_TYPES = new Object2IntArrayMap<>();
    public static final PacketType BUNDLE_DELIMITER = new PacketType("BUNDLE", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType ADD_ENTITY = new PacketType("ADD_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ADD_EXPERIENCE_ORB = new PacketType("ADD_EXPERIENCE_ORB", Version.v1_18, Version.v1_20_3);
    public static final PacketType ADD_MOB = new PacketType("ADD_MOB", Version.v1_18, Version.v1_18_2);
    public static final PacketType ADD_PAINTING = new PacketType("ADD_PAINTIN", Version.v1_18, Version.v1_18_2);
    public static final PacketType ADD_PLAYER = new PacketType("ADD_PLAYER", Version.v1_18, Version.v1_20_2);
    public static final PacketType ADD_VIBRATION_SIGNAL = new PacketType("ADD_VIBRATION_SIGNAL", Version.v1_18, Version.v1_18_2);
    public static final PacketType ANIMATE = new PacketType("ANIMATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType STATISTICS = new PacketType("STATISTICS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_BREAK_ACK = new PacketType("BLOCK_BREAK_ACK", Version.v1_18, Version.v1_18_2);
    public static final PacketType BLOCK_CHANGE_ACK = new PacketType("BLOCK_CHANGE_ACK", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_BREAK_ANIMATION = new PacketType("BLOCK_BREAK_ANIMATION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_ENTITY_DATA = new PacketType("BLOCK_ENTITY_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_EVENT = new PacketType("BLOCK_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_UPDATE = new PacketType("BLOCK_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BOSS_EVENT = new PacketType("BOSS_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DIFFICULTY = new PacketType("DIFFICULTY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHUNK_BATCH_END = new PacketType("CHUNK_BATCH_END", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CHUNK_BATCH_START = new PacketType("CHUNK_BATCH_START", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CHUNK_BIOMES = new PacketType("CHUNK_BIOMES", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType CHAT = new PacketType("CHAT", Version.v1_18, Version.v1_18_2);
    public static final PacketType CHAT_PREVIEW = new PacketType("CHAT_PREVIEW", Version.v1_19, Version.v1_19_1);
    public static final PacketType CLEAR_TITLES = new PacketType("CLEAR_TITLES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMMAND_SUGGESTIONS = new PacketType("COMMAND_SUGGESTIONS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMMANDS = new PacketType("COMMANDS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CLOSE = new PacketType("CONTAINER_CLOSE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CONTENT = new PacketType("CONTAINER_CONTENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_DATA = new PacketType("CONTAINER_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_SET_SLOT = new PacketType("CONTAINER_SET_SLOT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COOKIE_REQUEST = new PacketType("COOKIE_REQUEST", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType COOLDOWN = new PacketType("COOLDOWN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_COMPLETIONS = new PacketType("CHAT_COMPLETIONS", Version.v1_19_1, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_PAYLOAD = new PacketType("CUSTOM_PAYLOAD", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DAMAGE_EVENT = new PacketType("DAMAGE_EVENT", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType DEBUG_SAMPLE = new PacketType("DEBUG_SAMPLE", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_SOUND = new PacketType("CUSTOM_SOUND", Version.v1_18, Version.v1_19_1);
    public static final PacketType DELETE_CHAT = new PacketType("DELETE_CHAT", Version.v1_19_1, Version.FUTURE_RELEASE);
    public static final PacketType DISCONNECT = new PacketType("DISCONNECT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DISGUISED_CHAT = new PacketType("DISGUISED_CHAT", Version.v1_19_2, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_EVENT = new PacketType("ENTITY_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_POSITION_SYNC = new PacketType("ENTITY_POSITION_SYNC", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType EXPLODE = new PacketType("EXPLODE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType FORGET_LEVEL_CHUNK = new PacketType("FORGET_LEVEL_CHUNK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType GAME_EVENT = new PacketType("GAME_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType HORSE_SCREEN = new PacketType("HORSE_SCREEN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType HURT_ANIMATION = new PacketType("HURT_ANIMATION", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType INIT_WORLD_BORDER = new PacketType("INIT_WORLD_BORDER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType KEEPALIVE = new PacketType("KEEPALIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LEVEL_CHUNK_WITH_LIGHT = new PacketType("LEVEL_CHUNK_WITH_LIGHT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LEVEL_EVENT = new PacketType("LEVEL_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PARTICLES = new PacketType("PARTICLES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LIGHT_UPDATE = new PacketType("LIGHT_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LOGIN = new PacketType("LOGIN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MAP_ITEM_DATA = new PacketType("MAP_ITEM_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MERCHANT_OFFERS = new PacketType("MERCHANT_OFFERS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_ENTITY_POS = new PacketType("MOVE_ENTITY_POS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_ENTITY_POSROT = new PacketType("MOVE_ENTITY_POSROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_MINECART = new PacketType("MOVE_MINECART", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_ENTITY_ROT = new PacketType("MOVE_ENTITY_ROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_VEHICLE = new PacketType("MOVE_VEHICLE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType OPEN_BOOK = new PacketType("OPEN_BOOK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType OPEN_SCREEN = new PacketType("OPEN_SCREEN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType OPEN_SIGN = new PacketType("OPEN_SIGN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PING = new PacketType("PING", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DEBUG_PONG = new PacketType("DEBUG_PONG", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE = new PacketType("RECIPE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ABILITIES = new PacketType("ABILITIES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_HEADER = new PacketType("CHAT_HEADER", Version.v1_19_1, Version.v1_19_1);
    public static final PacketType CHAT_MESSAGE = new PacketType("CHAT_MESSAGE", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType COMBAT_END = new PacketType("COMBAT_END", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMBAT_ENTER = new PacketType("COMBAT_ENTER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMBAT_KILL = new PacketType("COMBAT_KILL", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_INFO = new PacketType("PLAYER_INFO", Version.v1_18, Version.v1_19_1);
    public static final PacketType PLAYER_INFO_REMOVE = new PacketType("PLAYER_INFO_REMOVE", Version.v1_19_2, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_INFO_UPDATE = new PacketType("PLAYER_INFO_UPDATE", Version.v1_19_2, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_LOOK_AT = new PacketType("PLAYER_LOOK_AT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_POSITION = new PacketType("PLAYER_POSITION", Version.v1_18, Version.v1_21);
    public static final PacketType PLAYER_ROTATION = new PacketType("PLAYER_ROTATION", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_ADD = new PacketType("RECIPE_BOOK_ADD", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_REMOVE = new PacketType("RECIPE_BOOK_REMOVE", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_SETTINGS = new PacketType("RECIPE_BOOK_SETTINGS", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType UNLOCK_RECIPES = new PacketType("UNLOCK_RECIPES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType REMOVE_ENTITIES = new PacketType("REMOVE_ENTITIES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType REMOVE_MOB_EFFECT = new PacketType("REMOVE_MOB_EFFECT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RESET_SCORE = new PacketType("RESET_SCORE", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType REMOVE_RESOURCE_PACK = new PacketType("REMOVE_RESOURCE_PACK", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType RESOURCEPACK = new PacketType("RESOURCEPACK", Version.v1_18, Version.v1_20_2);
    public static final PacketType RESOURCEPACK_PUSH = new PacketType("RESOURCEPACK_PUSH", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType RESPAWN = new PacketType("RESPAWN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ROTATE_HEAD = new PacketType("ROTATE_HEAD", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SECTION_BLOCKS_UPDATE = new PacketType("SECTION_BLOCKS_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SELECT_ADVANCEMENTS_TAB = new PacketType("SELECT_ADVANCEMENTS_TAB", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SERVER_DATA = new PacketType("SERVER_DATA", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType ACTION_BAR = new PacketType("ACTION_BAR", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_CENTER = new PacketType("BORDER_CENTER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_LERP = new PacketType("BORDER_LERP", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_SIZE = new PacketType("BORDER_SIZE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_WARNING_DELAY = new PacketType("BORDER_WARNING_DELAY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_WARNING_DISTANCE = new PacketType("BORDER_WARNING_DISTANCE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CAMERA = new PacketType("CAMERA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CARRIED_ITEM = new PacketType("CARRIED_ITEM", Version.v1_18, Version.v1_21);
    public static final PacketType SET_CHUNK_CACHE_CENTER = new PacketType("SET_CHUNK_CACHE_CENTER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CHUNK_CACHE_RADIUS = new PacketType("SET_CHUNK_CACHE_RADIUS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CURSOR_ITEM = new PacketType("SET_CURSOR_ITEM", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType SET_DEFAULT_SPAWN_POSITION = new PacketType("SET_DEFAULT_SPAWN_POSITION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_DISPLAY_CHAT_PREVIEW = new PacketType("SET_DISPLAY_CHAT_PREVIEW", Version.v1_19, Version.v1_19_1);
    public static final PacketType SET_DISPLAY_OBJECTIVE = new PacketType("SET_DISPLAY_OBJECTIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_ENTITY_DATA = new PacketType("SET_ENTITY_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_ENTITY_LINK = new PacketType("SET_ENTITY_LINK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_ENTITY_MOTION = new PacketType("SET_ENTITY_MOTION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_EQUIPMENT = new PacketType("SET_EQUIPMENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_EXPERIENCE = new PacketType("SET_EXPERIENCE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_HEALTH = new PacketType("SET_HEALTH", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_HELD_SLOT = new PacketType("SET_HELD_SLOT", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType SET_OBJECTIVE = new PacketType("SET_OBJECTIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_PASSENGERS = new PacketType("SET_PASSENGERS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_PLAYER_INVENTORY = new PacketType("SET_PLAYER_INVENTORY", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType SET_TEAM = new PacketType("SET_TEAM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_SCORE = new PacketType("SET_SCORE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_SIMULATION_DISTANCE = new PacketType("SET_SIMULATION_DISTANCE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_SUBTITLE = new PacketType("SET_SUBTITLE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TIME = new PacketType("SET_TIME", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TITLE_TEXT = new PacketType("SET_TITLE_TEXT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TITLE_ANIMATION = new PacketType("SET_TITLE_ANIMATION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_SOUND = new PacketType("ENTITY_SOUND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SOUND = new PacketType("SOUND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONFIGURATION_START = new PacketType("CONFIGURATION_START", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType STOP_SOUND = new PacketType("STOP_SOUND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType STORE_COOKIE = new PacketType("STORE_COOKIE", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType SYSTEM_CHAT_MESSAGE = new PacketType("SYSTEM_CHAT_MESSAGE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TAB_LIST = new PacketType("TAB_LIST", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TAG_QUERY = new PacketType("TAG_QUERY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TAKE_ITEM_ENTITY = new PacketType("TAKE_ITEM_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TELEPORT = new PacketType("TELEPORT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TEST_INSTANCE_BLOCK_STATUS = new PacketType("TEST_INSTANCE_BLOCK_STATUS", Version.v1_21_4, Version.FUTURE_RELEASE);
    public static final PacketType TICKING_STATE = new PacketType("TICKING_STATE", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType TICKING_STEP = new PacketType("TICKING_STEP", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType TRANSFER = new PacketType("TRANSFER", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_ADVANCEMENTS = new PacketType("UPDATE_ADVANCEMENTS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_ATTRIBUTES = new PacketType("UPDATE_ATTRIBUTES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_ENABLED_FEATURES = new PacketType("UPDATE_ENABLED_FEATURES", Version.v1_19_2, Version.v1_20_1);
    public static final PacketType UPDATE_MOB_EFFECTS = new PacketType("UPDATE_MOB_EFFECTS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_RECIPES = new PacketType("UPDATE_RECIPES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_TAGS = new PacketType("UPDATE_TAGS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PROJECTILE_POWER = new PacketType("PROJECTILE_POWER", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_REPORT_DETAILS = new PacketType("CUSTOM_REPORT_DETAILS", Version.v1_21, Version.FUTURE_RELEASE);
    public static final PacketType SERVER_LINKS = new PacketType("SERVER_LINKS", Version.v1_21, Version.FUTURE_RELEASE);

    public static void init() {
        register(BUNDLE_DELIMITER); // 1.19.4
        register(ADD_ENTITY);
        register(ADD_EXPERIENCE_ORB);
        register(ADD_MOB);
        register(ADD_PAINTING);
        register(ADD_PLAYER);
        register(ADD_VIBRATION_SIGNAL);
        register(ANIMATE);
        register(STATISTICS);
        register(BLOCK_BREAK_ACK);
        register(BLOCK_CHANGE_ACK); // 1.19
        register(BLOCK_BREAK_ANIMATION);
        register(BLOCK_ENTITY_DATA);
        register(BLOCK_EVENT);
        register(BLOCK_UPDATE);
        register(BOSS_EVENT);
        register(DIFFICULTY);
        register(CHUNK_BATCH_END); // 1.20.2
        register(CHUNK_BATCH_START); // 1.20.2
        register(CHAT);
        register(CHAT_PREVIEW); // 1.19
        register(CHUNK_BIOMES); // 1.19.4
        register(CLEAR_TITLES);
        register(COMMAND_SUGGESTIONS);
        register(COMMANDS);
        register(CONTAINER_CLOSE);
        register(CONTAINER_CONTENT);
        register(CONTAINER_DATA);
        register(CONTAINER_SET_SLOT);
        register(COOKIE_REQUEST);
        register(COOLDOWN);
        register(CHAT_COMPLETIONS); // 1.19
        register(CUSTOM_PAYLOAD);
        register(DAMAGE_EVENT); // 1.19.4
        register(DEBUG_SAMPLE); // 1.20.4
        register(CUSTOM_SOUND);
        register(DELETE_CHAT); // 1.19
        register(DISCONNECT);
        register(DISGUISED_CHAT); // 1.19.3
        register(ENTITY_EVENT);
        register(ENTITY_POSITION_SYNC);
        register(EXPLODE);
        register(FORGET_LEVEL_CHUNK);
        register(GAME_EVENT);
        register(HORSE_SCREEN);
        register(HURT_ANIMATION); // 1.19.4
        register(INIT_WORLD_BORDER);
        register(KEEPALIVE);
        register(LEVEL_CHUNK_WITH_LIGHT);
        register(LEVEL_EVENT);
        register(PARTICLES);
        register(LIGHT_UPDATE);
        register(LOGIN);
        register(MAP_ITEM_DATA);
        register(MERCHANT_OFFERS);
        register(MOVE_ENTITY_POS);
        register(MOVE_ENTITY_POSROT);
        register(MOVE_MINECART);
        register(MOVE_ENTITY_ROT);
        register(MOVE_VEHICLE);
        register(OPEN_BOOK);
        register(OPEN_SCREEN);
        register(OPEN_SIGN);
        register(PING);
        register(DEBUG_PONG); // 1.20.2
        register(UNLOCK_RECIPES);
        register(ABILITIES);
        register(CHAT_HEADER); // 1.19
        register(CHAT_MESSAGE); // 1.19
        register(COMBAT_END);
        register(COMBAT_ENTER);
        register(COMBAT_KILL);
        register(PLAYER_INFO);
        register(PLAYER_INFO_REMOVE); // 1.19.3
        register(PLAYER_INFO_UPDATE); // 1.19.3
        register(PLAYER_LOOK_AT);
        register(PLAYER_POSITION);
        register(PLAYER_ROTATION);
        register(RECIPE_BOOK_ADD);
        register(RECIPE_BOOK_REMOVE);
        register(RECIPE_BOOK_SETTINGS);
        register(RECIPE);
        register(REMOVE_ENTITIES);
        register(REMOVE_MOB_EFFECT);
        register(RESET_SCORE); // 1.20.3
        register(REMOVE_RESOURCE_PACK); // 1.20.3
        register(RESOURCEPACK_PUSH); // 1.20.3
        register(RESOURCEPACK);
        register(RESPAWN);
        register(ROTATE_HEAD);
        register(SECTION_BLOCKS_UPDATE);
        register(SELECT_ADVANCEMENTS_TAB);
        register(SERVER_DATA); // 1.19
        register(ACTION_BAR);
        register(BORDER_CENTER);
        register(BORDER_LERP);
        register(BORDER_SIZE);
        register(BORDER_WARNING_DELAY);
        register(BORDER_WARNING_DISTANCE);
        register(CAMERA);
        register(CARRIED_ITEM);
        register(SET_CHUNK_CACHE_CENTER);
        register(SET_CHUNK_CACHE_RADIUS);
        register(SET_CURSOR_ITEM);
        register(SET_DEFAULT_SPAWN_POSITION);
        register(SET_DISPLAY_CHAT_PREVIEW); // 1.19
        register(SET_DISPLAY_OBJECTIVE);
        register(SET_ENTITY_DATA);
        register(SET_ENTITY_LINK);
        register(SET_ENTITY_MOTION);
        register(SET_EQUIPMENT);
        register(SET_EXPERIENCE);
        register(SET_HEALTH);
        register(SET_HELD_SLOT);
        register(SET_OBJECTIVE);
        register(SET_PASSENGERS);
        register(SET_PLAYER_INVENTORY);
        register(SET_TEAM);
        register(SET_SCORE);
        register(SET_SIMULATION_DISTANCE);
        register(SET_SUBTITLE);
        register(SET_TIME);
        register(SET_TITLE_TEXT);
        register(SET_TITLE_ANIMATION);
        register(ENTITY_SOUND);
        register(SOUND);
        register(CONFIGURATION_START); // 1.20.2
        register(STOP_SOUND);
        register(STORE_COOKIE);
        register(SYSTEM_CHAT_MESSAGE); // 1.19
        register(TAB_LIST);
        register(TAG_QUERY);
        register(TAKE_ITEM_ENTITY);
        register(TELEPORT);
        register(TEST_INSTANCE_BLOCK_STATUS); // 1.21.5
        register(TICKING_STATE); // 1.20.3
        register(TICKING_STEP); // 1.20.3
        register(TRANSFER); // 1.20.3
        register(UPDATE_ADVANCEMENTS);
        register(UPDATE_ATTRIBUTES);
        register(UPDATE_ENABLED_FEATURES);
        register(UPDATE_MOB_EFFECTS);
        register(UPDATE_RECIPES);
        register(UPDATE_TAGS);
        register(PROJECTILE_POWER);
        register(CUSTOM_REPORT_DETAILS);
        register(SERVER_LINKS);
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
