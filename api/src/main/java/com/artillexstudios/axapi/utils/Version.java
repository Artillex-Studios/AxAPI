package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.reflection.FastMethodInvoker;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Version {
    v1_21_6(772, "v1_21_R5", Collections.singletonList("1.21.7")),
    v1_21_5(771, "v1_21_R5", Collections.singletonList("1.21.6")),
    v1_21_4(770, "v1_21_R4", Collections.singletonList("1.21.5")),
    v1_21_3(769, "v1_21_R3", Collections.singletonList("1.21.4")),
    v1_21_2(768, "v1_21_R2", Arrays.asList("1.21.2", "1.21.3")),
    v1_21(767, "v1_21_R1", Arrays.asList("1.21", "1.21.1")),
    v1_20_4(766, "v1_20_R4", Arrays.asList("1.20.5", "1.20.6")),
    v1_20_3(765, "v1_20_R3", Arrays.asList("1.20.3", "1.20.4")),
    v1_20_2(764, "v1_20_R2", Collections.singletonList("1.20.2")),
    v1_20_1(763, "v1_20_R1", Arrays.asList("1.20", "1.20.1")),
    v1_19_3(762, "v1_19_R3", Collections.singletonList("1.19.4")),
    v1_19_2(761, "v1_19_R2", Collections.singletonList("1.19.3")),
    v1_19_1(760, "v1_19_R1", Arrays.asList("1.19.1", "1.19.2")),
    v1_19(759, "v1_19_R1", Collections.singletonList("1.19")),
    v1_18_2(758, "v1_18_R2", Collections.singletonList("1.18.2")),
    v1_18(757, "v1_18_R1", Arrays.asList("1.18", "1.18.1")),
    v1_17_1(756, "v1_17_R2", Collections.singletonList("1.17.1")),
    v1_17(755, "v1_17_R1", Collections.singletonList("1.17")),
    v1_16_5(754, "v1_16_R3", Collections.singletonList("1.16.5")),
    FUTURE_RELEASE(Integer.MAX_VALUE, "FUTURE_RELEASE", Collections.singletonList("FUTURE_RELEASE")),
    UNKNOWN(-1, "UNKNOWN", Collections.singletonList("UNKNOWN"));

    private static final Int2ObjectArrayMap<Version> versionMap = new Int2ObjectArrayMap<>();
    private static Version serverVersion;
    private static int protocolVersion;

    static {
        final FastMethodInvoker methodInvoker = FastMethodInvoker.create("net.minecraft.SharedConstants", "c");
        final int protocolVersion = methodInvoker.invoke(null);

        for (Version value : values()) {
            versionMap.put(value.protocolId, value);

            if (value.protocolId == protocolVersion) {
                Version.serverVersion = value;
                break;
            }
        }

        if (Version.serverVersion == null) {
            Version.serverVersion = UNKNOWN;
        }

        Version.protocolVersion = protocolVersion;
    }


    private final List<String> versions;
    private final int protocolId;
    private final String nmsVersion;

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

    public static int protocolVersion() {
        return protocolVersion;
    }

    public boolean isNewerThan(Version version) {
        return this.protocolId > version.protocolId;
    }

    public boolean isNewerThanOrEqualTo(Version version) {
        return this.protocolId >= version.protocolId;
    }

    public boolean isOlderThan(Version version) {
        return this.protocolId < version.protocolId;
    }

    public boolean isOlderThanOrEqualTo(Version version) {
        return this.protocolId <= version.protocolId;
    }

    public List<String> versions() {
        return this.versions;
    }

    public int protocolId() {
        return this.protocolId;
    }

    public String nmsVersion() {
        return this.nmsVersion;
    }
}
