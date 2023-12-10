package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.bukkit.Bukkit;
import java.util.Arrays;
import org.bukkit.entity.Player;

import java.util.List;

public enum Version {
    v1_20_3(765, "v1_20_R3", Arrays.asList("1.20.3", "1.20.4")),
    v1_20_2(764, "v1_20_R2", Arrays.asList("1.20.2")),
    v1_20_1(763, "v1_20_R1", Arrays.asList("1.20.1", "1.20")),
    v1_19_3(762, "v1_19_R3", Arrays.asList("1.19.4")),
    v1_19_2(761, "v1_19_R2", Arrays.asList("1.19.3")),
    v1_19_1(760, "v1_19_R1", Arrays.asList("1.19.1", "1.19.2")),
    v1_19(759, "v1_19_R1", Arrays.asList("1.19")),
    v1_18_2(758, "v1_18_R2", Arrays.asList("1.18.2")),
    v1_18(757, "v1_18_R1", Arrays.asList("1.18.1", "1.18")),
    v1_17_1(756, "v1_17_R2", Arrays.asList("1.17.1")),
    v1_17(755, "v1_17_R1", Arrays.asList("1.17")),
    v1_16_5(754, "v1_16_R3", Arrays.asList("1.16.5")),
    UNKNOWN(-1, "UNKNOWN", Arrays.asList("UNKNOWN"));

    private static final Int2ObjectArrayMap<Version> versionMap = new Int2ObjectArrayMap<>();
    private static Version serverVersion;

    static {
        final String serverVersion = Bukkit.getServer().getBukkitVersion().split("-")[0];

        for (Version value : values()) {
            versionMap.put(value.protocolId, value);

            if (value.versions.contains(serverVersion)) {
                Version.serverVersion = value;
            }
        }

        if (Version.serverVersion == null) {
            Version.serverVersion = UNKNOWN;
        }
    }

    public final List<String> versions;
    public final int protocolId;
    public final String nmsVersion;

    public boolean isNewerThan(Version version) {
        return protocolId > version.protocolId;
    }

    public boolean isNewerThanOrEqualTo(Version version) {
        return protocolId >= version.protocolId;
    }

    public boolean isOlderThan(Version version) {
        return protocolId < version.protocolId;
    }

    Version(int protocolId, String nmsVersion, List<String> versions) {
        this.protocolId = protocolId;
        this.versions = versions;
        this.nmsVersion = nmsVersion;
    }

    public static Version getPlayerVersion(Player player) {
        return versionMap.get(NMSHandlers.getNmsHandler().getProtocolVersionId(player));
    }

    public static Version getServerVersion() {
        return serverVersion;
    }
}
