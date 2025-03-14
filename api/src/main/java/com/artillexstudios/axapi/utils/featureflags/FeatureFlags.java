package com.artillexstudios.axapi.utils.featureflags;

import com.artillexstudios.axapi.AxPlugin;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class FeatureFlags {
    public final BooleanFlag PACKET_ENTITY_TRACKER_ENABLED;
    public final BooleanFlag DEBUG;
    public final BooleanFlag USE_LEGACY_HEX_FORMATTER;
    public final IntegerFlag PACKET_ENTITY_TRACKER_THREADS;
    public final LongFlag HOLOGRAM_UPDATE_TICKS;
    public final ListFlag<Pattern> PLACEHOLDER_PATTERNS;
    public final BooleanFlag PLACEHOLDER_API_HOOK;
    public final StringFlag PLACEHOLDER_API_IDENTIFIER;
    public final IntegerFlag COMPONENT_CACHE_SIZE;
    private final AxPlugin plugin;

    public FeatureFlags(AxPlugin plugin) {
        this.plugin = plugin;
        this.PACKET_ENTITY_TRACKER_ENABLED = new BooleanFlag(plugin.getName() + "enableEntityTracker", false);
        this.DEBUG = new BooleanFlag(plugin.getName() + "debug", false);
        this.USE_LEGACY_HEX_FORMATTER = new BooleanFlag(plugin.getName() + "useLegacyHexFormat", false);
        this.PACKET_ENTITY_TRACKER_THREADS = new IntegerFlag(plugin.getName() + "entityTrackerThreads", 3);
        this.HOLOGRAM_UPDATE_TICKS = new LongFlag(plugin.getName() + "hologramUpdateTicks", 0L);
        this.PLACEHOLDER_PATTERNS = new ListFlag<>(plugin.getName() + "placeholderPatterns", PatternFlag.TRANSFORMER, Arrays.asList(Pattern.compile("%.+%"), Pattern.compile("<.+>")));
        this.PLACEHOLDER_API_HOOK = new BooleanFlag(plugin.getName() + "placeholderApiHook", false);
        this.PLACEHOLDER_API_IDENTIFIER = new StringFlag(plugin.getName() + "placeholderApiIdentifier", "");
        this.COMPONENT_CACHE_SIZE = new IntegerFlag(plugin.getName() + "componentCacheSize", 200);
    }
}
