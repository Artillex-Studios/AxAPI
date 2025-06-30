package com.artillexstudios.axapi.utils.featureflags;

import com.artillexstudios.axapi.AxPlugin;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class FeatureFlags {
    public static final BooleanFlag PACKET_ENTITY_TRACKER_ENABLED = new BooleanFlag(false);
    public static final BooleanFlag DEBUG = new BooleanFlag(false);
    public static final BooleanFlag DEBUG_INCOMING_PACKETS = new BooleanFlag(false);
    public static final BooleanFlag DEBUG_OUTGOING_PACKETS = new BooleanFlag(false);
    public static final BooleanFlag USE_LEGACY_HEX_FORMATTER = new BooleanFlag(false);
    public static final IntegerFlag PACKET_ENTITY_TRACKER_THREADS = new IntegerFlag(3);
    public static final LongFlag HOLOGRAM_UPDATE_TICKS = new LongFlag(0L);
    public static final ListFlag<Pattern> PLACEHOLDER_PATTERNS = new ListFlag<>(PatternFlag.TRANSFORMER, Arrays.asList(Pattern.compile("%.+%"), Pattern.compile("<.+>")));
    public static final BooleanFlag PLACEHOLDER_API_HOOK = new BooleanFlag(false);
    public static final StringFlag PLACEHOLDER_API_IDENTIFIER = new StringFlag("");
    public static final IntegerFlag COMPONENT_CACHE_SIZE = new IntegerFlag(200);
    public static final BooleanFlag GUI_WAIT_FOR_ALL = new BooleanFlag(false);

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
    }
}
