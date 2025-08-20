package com.artillexstudios.axapi.utils.featureflags;

import com.artillexstudios.axapi.AxPlugin;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Defines settings which can be used to control behaviour of AxAPI.
 */
public final class FeatureFlags {
    /**
     * If we should enable the packetentity tracker system. If this is not enabled, you won't be able to
     * spawn packetentities.
     * <p>
     * Can be controlled with the system property: %pluginName%enableEntityTracker
     */
    public static final BooleanFlag PACKET_ENTITY_TRACKER_ENABLED = new BooleanFlag(false);
    /**
     * If we should send debug messages.
     * <p>
     * Can be controlled with the system property: %pluginName%debug
     */
    public static final BooleanFlag DEBUG = new BooleanFlag(false);
    /**
     * If we should send debug messages about incoming packets.
     * <p>
     * Can be controlled with the system property: %pluginName%debugIncomingPackets
     */
    public static final BooleanFlag DEBUG_INCOMING_PACKETS = new BooleanFlag(false);
    /**
     * If we should send debug messages about outgoing packets.
     * <p>
     * Can be controlled with the system property: %pluginName%debugOutgoingPackets
     */
    public static final BooleanFlag DEBUG_OUTGOING_PACKETS = new BooleanFlag(false);
    /**
     * If we should use the legacy hex formatter.
     * <p>
     * Can be controlled with the system property: %pluginName%useLegacyHexFormat
     */
    public static final BooleanFlag USE_LEGACY_HEX_FORMATTER = new BooleanFlag(false);
    /**
     * How many threads we should use for the packetentity tracker.
     * <p>
     * Can be controlled with the system property: %pluginName%entityTrackerThreads
     */
    public static final IntegerFlag PACKET_ENTITY_TRACKER_THREADS = new IntegerFlag(3);
    /**
     * How often we should update the holograms.
     * <p>
     * Can be controlled with the system property: %pluginName%hologramUpdateTicks
     */
    public static final LongFlag HOLOGRAM_UPDATE_TICKS = new LongFlag(0L);
    /**
     * Valid placeholder patterns that holograms, or other things check to see if they have placeholders in them.
     * <p>
     * Can be controlled with the system property: %pluginName%placeholderPatterns
     */
    public static final ListFlag<Pattern> PLACEHOLDER_PATTERNS = new ListFlag<>(PatternFlag.TRANSFORMER, Arrays.asList(Pattern.compile("%.+%"), Pattern.compile("<.+>")));
    /**
     * If we should try to hook into PlaceholderAPI.
     * <p>
     * Can be controlled with the system property: %pluginName%placeholderApiHook
     */
    public static final BooleanFlag PLACEHOLDER_API_HOOK = new BooleanFlag(false);
    /**
     * The identifier used for hooking the plugin's placeholders into PlaceholderAPI.
     * <p>
     * Can be controlled with the system property: %pluginName%placeholderApiIdentifier
     */
    public static final StringFlag PLACEHOLDER_API_IDENTIFIER = new StringFlag("");
    /**
     * The size of the component caches.
     * <p>
     * Can be controlled with the system property: %pluginName%componentCacheSize
     */
    public static final IntegerFlag COMPONENT_CACHE_SIZE = new IntegerFlag(1000);
    /**
     * If the guis should wait for all items to be created before opening the inventory.
     * <p>
     * Can be controlled with the system property: %pluginName%guiWaitForAll
     */
    public static final BooleanFlag GUI_WAIT_FOR_ALL = new BooleanFlag(false);
    /**
     * How much the players need to wait before we handle their clicks.
     * <p>
     * Can be controlled with the system property: %pluginName%inventoryClickCooldown
     */
    public static final IntegerFlag INVENTORY_CLICK_COOLDOWN = new IntegerFlag(25);
    /**
     * When an item override is added to a ConfigurationBackedGui, and the item doesn't exist
     * this method will print an error message about the missing option. Otherwise, the item will be ignored.
     * <p>
     * Can be controlled with the system property: %pluginName%strictInventoryOverrideHandling
     */
    public static final BooleanFlag STRICT_ITEM_OVERRIDE_HANDLING = new BooleanFlag(false);
    /**
     * Whether to create a scheduled task for updating inventories from InventoryRenderer.
     * <p>
     * Can be controlled with the system property: %pluginName%useInventoryUpdater
     */
    public static final BooleanFlag USE_INVENTORY_UPDATER = new BooleanFlag(false);
    /**
     * Whether exceptions handled with ExceptionUtils should be printed to console.
     * <p>
     * Can be controlled with the system property: %pluginName%logExceptionUtilsExceptions
     */
    public static final BooleanFlag LOG_EXCEPTION_UTILS_EXCEPTIONS = new BooleanFlag(false);
    /**
     * Whether exceptions handled with ExceptionUtils should be printed to console.
     * <p>
     * Can be controlled with the system property: %pluginName%parsePlaceholderAPiInItemBuilder
     */
    public static final BooleanFlag PARSE_PLACEHOLDER_API_IN_ITEM_BUILDER = new BooleanFlag(false);
    /**
     * The size of the color cache.
     * <p>
     * Can be controlled with the system property: %pluginName%colorCacheSize
     */
    public static final IntegerFlag COLOR_CACHE_SIZE = new IntegerFlag(1000);
    /**
     * If AxAPI should listen to ride packets to add riders for packet entities.
     * <p>
     * Can be controlled with the system property: %pluginName%listenToRidePacket
     */
    public static final BooleanFlag LISTEN_TO_RIDE_PACKET = new BooleanFlag(false);

    public static void refresh(AxPlugin plugin) {
        PACKET_ENTITY_TRACKER_ENABLED.refresh(plugin.getName() + "enableEntityTracker");
        DEBUG.refresh(plugin.getName() + "debug");
        DEBUG_INCOMING_PACKETS.refresh(plugin.getName() + "debugIncomingPackets");
        DEBUG_OUTGOING_PACKETS.refresh(plugin.getName() + "debugOutgoingPackets");
        USE_LEGACY_HEX_FORMATTER.refresh(plugin.getName() + "useLegacyHexFormat");
        PACKET_ENTITY_TRACKER_THREADS.refresh(plugin.getName() + "entityTrackerThreads");
        HOLOGRAM_UPDATE_TICKS.refresh(plugin.getName() + "hologramUpdateTicks");
        PLACEHOLDER_PATTERNS.refresh(plugin.getName() + "placeholderPatterns");
        PLACEHOLDER_API_HOOK.refresh(plugin.getName() + "placeholderApiHook");
        PLACEHOLDER_API_IDENTIFIER.refresh(plugin.getName() + "placeholderApiIdentifier");
        COMPONENT_CACHE_SIZE.refresh(plugin.getName() + "componentCacheSize");
        GUI_WAIT_FOR_ALL.refresh(plugin.getName() + "guiWaitForAll");
        INVENTORY_CLICK_COOLDOWN.refresh(plugin.getName() + "inventoryClickCooldown");
        STRICT_ITEM_OVERRIDE_HANDLING.refresh(plugin.getName() + "strictInventoryOverrideHandling");
        USE_INVENTORY_UPDATER.refresh(plugin.getName() + "useInventoryUpdater");
        LOG_EXCEPTION_UTILS_EXCEPTIONS.refresh(plugin.getName() + "logExceptionUtilsExceptions");
        PARSE_PLACEHOLDER_API_IN_ITEM_BUILDER.refresh(plugin.getName() + "parsePlaceholderAPiInItemBuilder");
        COLOR_CACHE_SIZE.refresh(plugin.getName() + "colorCacheSize");
        LISTEN_TO_RIDE_PACKET.refresh(plugin.getName() + "listenToRidePacket");
    }
}
