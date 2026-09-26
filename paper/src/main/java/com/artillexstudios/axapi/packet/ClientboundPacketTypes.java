package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.utils.Maps;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

import java.util.Map;

public final class ClientboundPacketTypes {
    private static final Int2ObjectMap<PacketType> PACKET_TYPES = new Int2ObjectOpenHashMap<>();
    private static final Object2IntArrayMap<PacketType> REVERSE_PACKET_TYPES = new Object2IntArrayMap<>();
    public static final PacketType BUNDLE_DELIMITER = PacketType.createClientbound("BUNDLE", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType ADD_ENTITY = PacketType.createClientbound("ADD_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ADD_EXPERIENCE_ORB = PacketType.createClientbound("ADD_EXPERIENCE_ORB", Version.v1_18, Version.v1_21_3);
    public static final PacketType ADD_MOB = PacketType.createClientbound("ADD_MOB", Version.v1_18, Version.v1_18_2);
    public static final PacketType ADD_PAINTING = PacketType.createClientbound("ADD_PAINTIN", Version.v1_18, Version.v1_18_2);
    public static final PacketType ADD_PLAYER = PacketType.createClientbound("ADD_PLAYER", Version.v1_18, Version.v1_20_1);
    public static final PacketType ADD_VIBRATION_SIGNAL = PacketType.createClientbound("ADD_VIBRATION_SIGNAL", Version.v1_18, Version.v1_18_2);
    public static final PacketType ANIMATE = PacketType.createClientbound("ANIMATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType STATISTICS = PacketType.createClientbound("STATISTICS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_BREAK_ACK = PacketType.createClientbound("BLOCK_BREAK_ACK", Version.v1_18, Version.v1_18_2);
    public static final PacketType BLOCK_CHANGE_ACK = PacketType.createClientbound("BLOCK_CHANGE_ACK", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_BREAK_ANIMATION = PacketType.createClientbound("BLOCK_BREAK_ANIMATION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_ENTITY_DATA = PacketType.createClientbound("BLOCK_ENTITY_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_EVENT = PacketType.createClientbound("BLOCK_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BLOCK_UPDATE = PacketType.createClientbound("BLOCK_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BOSS_EVENT = PacketType.createClientbound("BOSS_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DIFFICULTY = PacketType.createClientbound("DIFFICULTY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHUNK_BATCH_END = PacketType.createClientbound("CHUNK_BATCH_END", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CHUNK_BATCH_START = PacketType.createClientbound("CHUNK_BATCH_START", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType CHUNK_BIOMES = PacketType.createClientbound("CHUNK_BIOMES", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType CHAT = PacketType.createClientbound("CHAT", Version.v1_18, Version.v1_18_2);
    public static final PacketType CHAT_PREVIEW = PacketType.createClientbound("CHAT_PREVIEW", Version.v1_19, Version.v1_19_1);
    public static final PacketType CLEAR_TITLES = PacketType.createClientbound("CLEAR_TITLES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMMAND_SUGGESTIONS = PacketType.createClientbound("COMMAND_SUGGESTIONS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMMANDS = PacketType.createClientbound("COMMANDS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CLOSE = PacketType.createClientbound("CONTAINER_CLOSE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_CONTENT = PacketType.createClientbound("CONTAINER_CONTENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_DATA = PacketType.createClientbound("CONTAINER_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONTAINER_SET_SLOT = PacketType.createClientbound("CONTAINER_SET_SLOT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COOKIE_REQUEST = PacketType.createClientbound("COOKIE_REQUEST", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType COOLDOWN = PacketType.createClientbound("COOLDOWN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_COMPLETIONS = PacketType.createClientbound("CHAT_COMPLETIONS", Version.v1_19_1, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_PAYLOAD = PacketType.createClientbound("CUSTOM_PAYLOAD", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DAMAGE_EVENT = PacketType.createClientbound("DAMAGE_EVENT", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType DEBUG_BLOCK_VALUE = PacketType.createClientbound("DEBUG_BLOCK_VALUE", Version.v1_21_7, Version.FUTURE_RELEASE); // 1.21.9
    public static final PacketType DEBUG_CHUNK_VALUE = PacketType.createClientbound("DEBUG_CHUNK_VALUE", Version.v1_21_7, Version.FUTURE_RELEASE); // 1.21.9
    public static final PacketType DEBUG_ENTITY_VALUE = PacketType.createClientbound("DEBUG_ENTITY_VALUE", Version.v1_21_7, Version.FUTURE_RELEASE); // 1.21.9
    public static final PacketType DEBUG_EVENT = PacketType.createClientbound("DEBUG_EVENT", Version.v1_21_7, Version.FUTURE_RELEASE); // 1.21.9
    public static final PacketType DEBUG_SAMPLE = PacketType.createClientbound("DEBUG_SAMPLE", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_SOUND = PacketType.createClientbound("CUSTOM_SOUND", Version.v1_18, Version.v1_19_1);
    public static final PacketType DELETE_CHAT = PacketType.createClientbound("DELETE_CHAT", Version.v1_19_1, Version.FUTURE_RELEASE);
    public static final PacketType DISCONNECT = PacketType.createClientbound("DISCONNECT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DISGUISED_CHAT = PacketType.createClientbound("DISGUISED_CHAT", Version.v1_19_2, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_EVENT = PacketType.createClientbound("ENTITY_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_POSITION_SYNC = PacketType.createClientbound("ENTITY_POSITION_SYNC", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType EXPLODE = PacketType.createClientbound("EXPLODE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ADD_TRANSIENT_BLOCK = PacketType.createClientbound("ADD_TRANSIENT_BLOCK", Version.v26_3, Version.FUTURE_RELEASE);
    public static final PacketType FORGET_LEVEL_CHUNK = PacketType.createClientbound("FORGET_LEVEL_CHUNK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType GAME_EVENT = PacketType.createClientbound("GAME_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    private static final PacketType GAME_TEST_HIGHLIGHT_POS = PacketType.createClientbound("GAME_TEST_HIGHLIGHT_POS", Version.v1_21_7, Version.FUTURE_RELEASE); // 1.21.9
    public static final PacketType HORSE_SCREEN = PacketType.createClientbound("HORSE_SCREEN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType HURT_ANIMATION = PacketType.createClientbound("HURT_ANIMATION", Version.v1_19_3, Version.FUTURE_RELEASE);
    public static final PacketType INIT_WORLD_BORDER = PacketType.createClientbound("INIT_WORLD_BORDER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType KEEPALIVE = PacketType.createClientbound("KEEPALIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LEVEL_CHUNK_WITH_LIGHT = PacketType.createClientbound("LEVEL_CHUNK_WITH_LIGHT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LEVEL_EVENT = PacketType.createClientbound("LEVEL_EVENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PARTICLES = PacketType.createClientbound("PARTICLES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LIGHT_UPDATE = PacketType.createClientbound("LIGHT_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LOGIN = PacketType.createClientbound("LOGIN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType LOW_DISK_SPACE_WARNING = PacketType.createClientbound("LOW_DISK_SPACE_WARNING", Version.v26_1, Version.FUTURE_RELEASE);
    public static final PacketType MAP_ITEM_DATA = PacketType.createClientbound("MAP_ITEM_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MERCHANT_OFFERS = PacketType.createClientbound("MERCHANT_OFFERS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_ENTITY_POS = PacketType.createClientbound("MOVE_ENTITY_POS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_ENTITY_POSROT = PacketType.createClientbound("MOVE_ENTITY_POSROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_MINECART = PacketType.createClientbound("MOVE_MINECART", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_ENTITY_ROT = PacketType.createClientbound("MOVE_ENTITY_ROT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType MOVE_VEHICLE = PacketType.createClientbound("MOVE_VEHICLE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType OPEN_BOOK = PacketType.createClientbound("OPEN_BOOK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType OPEN_SCREEN = PacketType.createClientbound("OPEN_SCREEN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType OPEN_SIGN = PacketType.createClientbound("OPEN_SIGN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PING = PacketType.createClientbound("PING", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType DEBUG_PONG = PacketType.createClientbound("DEBUG_PONG", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE = PacketType.createClientbound("RECIPE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ABILITIES = PacketType.createClientbound("ABILITIES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType GAME_RULE_VALUES = PacketType.createClientbound("GAME_RULE_VALUES", Version.v26_1, Version.FUTURE_RELEASE);
    public static final PacketType CHAT_HEADER = PacketType.createClientbound("CHAT_HEADER", Version.v1_19_1, Version.v1_19_1);
    public static final PacketType CHAT_MESSAGE = PacketType.createClientbound("CHAT_MESSAGE", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType COMBAT_END = PacketType.createClientbound("COMBAT_END", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMBAT_ENTER = PacketType.createClientbound("COMBAT_ENTER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType COMBAT_KILL = PacketType.createClientbound("COMBAT_KILL", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_INFO = PacketType.createClientbound("PLAYER_INFO", Version.v1_18, Version.v1_19_1);
    public static final PacketType PLAYER_INFO_REMOVE = PacketType.createClientbound("PLAYER_INFO_REMOVE", Version.v1_19_2, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_INFO_UPDATE = PacketType.createClientbound("PLAYER_INFO_UPDATE", Version.v1_19_2, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_LOOK_AT = PacketType.createClientbound("PLAYER_LOOK_AT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PLAYER_POSITION = PacketType.createClientbound("PLAYER_POSITION", Version.v1_18, Version.v1_21);
    public static final PacketType PLAYER_ROTATION = PacketType.createClientbound("PLAYER_ROTATION", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_ADD = PacketType.createClientbound("RECIPE_BOOK_ADD", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_REMOVE = PacketType.createClientbound("RECIPE_BOOK_REMOVE", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType RECIPE_BOOK_SETTINGS = PacketType.createClientbound("RECIPE_BOOK_SETTINGS", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType UNLOCK_RECIPES = PacketType.createClientbound("UNLOCK_RECIPES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType REMOVE_ENTITIES = PacketType.createClientbound("REMOVE_ENTITIES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType REMOVE_MOB_EFFECT = PacketType.createClientbound("REMOVE_MOB_EFFECT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType RESET_SCORE = PacketType.createClientbound("RESET_SCORE", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType REMOVE_RESOURCE_PACK = PacketType.createClientbound("REMOVE_RESOURCE_PACK", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType RESOURCEPACK = PacketType.createClientbound("RESOURCEPACK", Version.v1_18, Version.v1_20_2);
    public static final PacketType RESOURCEPACK_PUSH = PacketType.createClientbound("RESOURCEPACK_PUSH", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType POST_EFFECTS = PacketType.createClientbound("POST_EFFECTS", Version.v26_3, Version.FUTURE_RELEASE);
    public static final PacketType RESPAWN = PacketType.createClientbound("RESPAWN", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ROTATE_HEAD = PacketType.createClientbound("ROTATE_HEAD", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SECTION_BLOCKS_UPDATE = PacketType.createClientbound("SECTION_BLOCKS_UPDATE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SELECT_ADVANCEMENTS_TAB = PacketType.createClientbound("SELECT_ADVANCEMENTS_TAB", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SERVER_DATA = PacketType.createClientbound("SERVER_DATA", Version.v1_19, Version.FUTURE_RELEASE);
    public static final PacketType ACTION_BAR = PacketType.createClientbound("ACTION_BAR", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_CENTER = PacketType.createClientbound("BORDER_CENTER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_LERP = PacketType.createClientbound("BORDER_LERP", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_SIZE = PacketType.createClientbound("BORDER_SIZE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_WARNING_DELAY = PacketType.createClientbound("BORDER_WARNING_DELAY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType BORDER_WARNING_DISTANCE = PacketType.createClientbound("BORDER_WARNING_DISTANCE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CAMERA = PacketType.createClientbound("CAMERA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CARRIED_ITEM = PacketType.createClientbound("CARRIED_ITEM", Version.v1_18, Version.v1_21);
    public static final PacketType SET_CHUNK_CACHE_CENTER = PacketType.createClientbound("SET_CHUNK_CACHE_CENTER", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CHUNK_CACHE_RADIUS = PacketType.createClientbound("SET_CHUNK_CACHE_RADIUS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_CURSOR_ITEM = PacketType.createClientbound("SET_CURSOR_ITEM", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType SET_DEFAULT_SPAWN_POSITION = PacketType.createClientbound("SET_DEFAULT_SPAWN_POSITION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_DISPLAY_CHAT_PREVIEW = PacketType.createClientbound("SET_DISPLAY_CHAT_PREVIEW", Version.v1_19, Version.v1_19_1);
    public static final PacketType SET_DISPLAY_OBJECTIVE = PacketType.createClientbound("SET_DISPLAY_OBJECTIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_ENTITY_DATA = PacketType.createClientbound("SET_ENTITY_DATA", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_ENTITY_LINK = PacketType.createClientbound("SET_ENTITY_LINK", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_ENTITY_MOTION = PacketType.createClientbound("SET_ENTITY_MOTION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_EQUIPMENT = PacketType.createClientbound("SET_EQUIPMENT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_EXPERIENCE = PacketType.createClientbound("SET_EXPERIENCE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_HEALTH = PacketType.createClientbound("SET_HEALTH", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_HELD_SLOT = PacketType.createClientbound("SET_HELD_SLOT", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType SET_OBJECTIVE = PacketType.createClientbound("SET_OBJECTIVE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_PASSENGERS = PacketType.createClientbound("SET_PASSENGERS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_PLAYER_INVENTORY = PacketType.createClientbound("SET_PLAYER_INVENTORY", Version.v1_21_2, Version.FUTURE_RELEASE);
    public static final PacketType SET_TEAM = PacketType.createClientbound("SET_TEAM", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_SCORE = PacketType.createClientbound("SET_SCORE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_SIMULATION_DISTANCE = PacketType.createClientbound("SET_SIMULATION_DISTANCE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_SUBTITLE = PacketType.createClientbound("SET_SUBTITLE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TIME = PacketType.createClientbound("SET_TIME", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TITLE_TEXT = PacketType.createClientbound("SET_TITLE_TEXT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SET_TITLE_ANIMATION = PacketType.createClientbound("SET_TITLE_ANIMATION", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType ENTITY_SOUND = PacketType.createClientbound("ENTITY_SOUND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType SOUND = PacketType.createClientbound("SOUND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType CONFIGURATION_START = PacketType.createClientbound("CONFIGURATION_START", Version.v1_20_2, Version.FUTURE_RELEASE);
    public static final PacketType STOP_SOUND = PacketType.createClientbound("STOP_SOUND", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType STORE_COOKIE = PacketType.createClientbound("STORE_COOKIE", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType SWING_ANIMATION = PacketType.createClientbound("SWING_ANIMATION", Version.v26_3, Version.FUTURE_RELEASE);
    public static final PacketType SYSTEM_CHAT_MESSAGE = PacketType.createClientbound("SYSTEM_CHAT_MESSAGE", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TAB_LIST = PacketType.createClientbound("TAB_LIST", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TAG_QUERY = PacketType.createClientbound("TAG_QUERY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TAKE_ITEM_ENTITY = PacketType.createClientbound("TAKE_ITEM_ENTITY", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TELEPORT = PacketType.createClientbound("TELEPORT", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType TEST_INSTANCE_BLOCK_STATUS = PacketType.createClientbound("TEST_INSTANCE_BLOCK_STATUS", Version.v1_21_4, Version.FUTURE_RELEASE);
    public static final PacketType TICKING_STATE = PacketType.createClientbound("TICKING_STATE", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType TICKING_STEP = PacketType.createClientbound("TICKING_STEP", Version.v1_20_3, Version.FUTURE_RELEASE);
    public static final PacketType TRANSFER = PacketType.createClientbound("TRANSFER", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_ADVANCEMENTS = PacketType.createClientbound("UPDATE_ADVANCEMENTS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_ATTRIBUTES = PacketType.createClientbound("UPDATE_ATTRIBUTES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_ENABLED_FEATURES = PacketType.createClientbound("UPDATE_ENABLED_FEATURES", Version.v1_19_2, Version.v1_20_1);
    public static final PacketType UPDATE_MOB_EFFECTS = PacketType.createClientbound("UPDATE_MOB_EFFECTS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_RECIPES = PacketType.createClientbound("UPDATE_RECIPES", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType UPDATE_TAGS = PacketType.createClientbound("UPDATE_TAGS", Version.v1_18, Version.FUTURE_RELEASE);
    public static final PacketType PROJECTILE_POWER = PacketType.createClientbound("PROJECTILE_POWER", Version.v1_20_4, Version.FUTURE_RELEASE);
    public static final PacketType CUSTOM_REPORT_DETAILS = PacketType.createClientbound("CUSTOM_REPORT_DETAILS", Version.v1_21, Version.FUTURE_RELEASE);
    public static final PacketType SERVER_LINKS = PacketType.createClientbound("SERVER_LINKS", Version.v1_21, Version.FUTURE_RELEASE);
    public static final PacketType WAYPOINT = PacketType.createClientbound("WAYPOINT", Version.v1_21_5, Version.FUTURE_RELEASE);
    public static final PacketType CLEAR_DIALOG = PacketType.createClientbound("CLEAR_DIALOG", Version.v1_21_5, Version.FUTURE_RELEASE);
    public static final PacketType SHOW_DIALOG = PacketType.createClientbound("SHOW_DIALOG", Version.v1_21_5, Version.FUTURE_RELEASE);

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
        register(DEBUG_BLOCK_VALUE); // 1.21.9
        register(DEBUG_CHUNK_VALUE); // 1.21.9
        register(DEBUG_ENTITY_VALUE); // 1.21.9
        register(DEBUG_EVENT); // 1.21.9
        register(DEBUG_SAMPLE); // 1.20.4
        register(CUSTOM_SOUND);
        register(DELETE_CHAT); // 1.19
        register(DISCONNECT);
        register(DISGUISED_CHAT); // 1.19.3
        register(ENTITY_EVENT);
        register(ENTITY_POSITION_SYNC);
        register(EXPLODE);
        register(ADD_TRANSIENT_BLOCK); // 26.3
        register(FORGET_LEVEL_CHUNK);
        register(GAME_EVENT);
        register(GAME_RULE_VALUES); // 26.1
        register(GAME_TEST_HIGHLIGHT_POS); // 1.21.9
        register(HORSE_SCREEN);
        register(HURT_ANIMATION); // 1.19.4
        register(INIT_WORLD_BORDER);
        register(KEEPALIVE);
        register(LEVEL_CHUNK_WITH_LIGHT);
        register(LEVEL_EVENT);
        register(PARTICLES);
        register(LIGHT_UPDATE);
        register(LOGIN);
        register(LOW_DISK_SPACE_WARNING); // 26.1
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
        register(POST_EFFECTS); // 26.3
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
        register(SWING_ANIMATION); // 26.3
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
        register(WAYPOINT);
        register(CLEAR_DIALOG);
        register(SHOW_DIALOG);
    }

    public static void register(PacketType packetType) {
        if (Version.getServerVersion().isNewerThanOrEqualTo(packetType.from()) && Version.getServerVersion().isOlderThanOrEqualTo(packetType.to())) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Registering clientbound packet: {}", packetType);
            }

            int size = PACKET_TYPES.size();
            PACKET_TYPES.put(size, packetType);
            REVERSE_PACKET_TYPES.put(packetType, size);
        } else if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Did not register clientbound packet: {}", packetType);
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
