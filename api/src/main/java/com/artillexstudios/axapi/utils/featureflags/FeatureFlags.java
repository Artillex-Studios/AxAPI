package com.artillexstudios.axapi.utils.featureflags;

import com.artillexstudios.axapi.AxPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class FeatureFlags {
    private static final JavaPlugin plugin = AxPlugin.getProvidingPlugin(AxPlugin.class);
    public static final BooleanFlag PACKET_ENTITY_TRACKER_ENABLED = new BooleanFlag(plugin.getName() + "enableEntityTracker", false);
    public static final BooleanFlag DEBUG = new BooleanFlag(plugin.getName() + "debug", false);
    public static final BooleanFlag USE_LEGACY_HEX_FORMATTER = new BooleanFlag(plugin.getName() + "useLegacyHexFormat", false);
    public static final IntegerFlag PACKET_ENTITY_TRACKER_THREADS = new IntegerFlag(plugin.getName() + "entityTrackerThreads", 3);
    public static final LongFlag HOLOGRAM_UPDATE_TICKS = new LongFlag(plugin.getName() + "hologramUpdateTicks", 0L);
    public static final ListFlag<Pattern> PLACEHOLDER_PATTERNS = new ListFlag<>(plugin.getName() + "placeholderPatterns", PatternFlag.TRANSFORMER, Arrays.asList(Pattern.compile("%.+%"), Pattern.compile("<.+>")));
    public static final BooleanFlag PLACEHOLDER_API_HOOK = new BooleanFlag(plugin.getName() + "placeholderApiHook", false);
    public static final StringFlag PLACEHOLDER_API_IDENTIFIER = new StringFlag(plugin.getName() + "placeholderApiIdentifier", "");
}
