package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.reflection.FastMethodInvoker;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

public enum Version {
    v1_21_8_PAPER(774, "v1_21_R7_paper", Collections.singletonList("1.21.11"), PaperUtils::isPaper),
    v1_21_8(774, "v1_21_R7", Collections.singletonList("1.21.11"), () -> !PaperUtils.isPaper()),
    v1_21_7(773, "v1_21_R6", Arrays.asList("1.21.9", "1.21.10")),
    v1_21_6(772, "v1_21_R5", Arrays.asList("1.21.7", "1.21.8")),
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
        final FastMethodInvoker methodInvoker = ExceptionUtils.orElse(
                () -> FastMethodInvoker.createSilently("net.minecraft.SharedConstants", "c"),
                () -> FastMethodInvoker.createSilently("net.minecraft.SharedConstants", "getProtocolVersion"),
                exception -> {
            LogUtils.error("Failed to fetch the version information!");
            return null;
        });

        if (methodInvoker == null) {
            Version.serverVersion = UNKNOWN;
        } else {
            final int protocolVersion = methodInvoker.invoke(null);

            for (Version value : values()) {
                if (!value.supplier.getAsBoolean()) {
                    continue;
                }

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
    }


    private final List<String> versions;
    private final int protocolId;
    private final String nmsVersion;
    private final BooleanSupplier supplier; // If the version is allowed in this environment

    Version(int protocolId, String nmsVersion, List<String> versions, BooleanSupplier supplier) {
        this.protocolId = protocolId;
        this.versions = versions;
        this.nmsVersion = nmsVersion;
        this.supplier = supplier;
    }

    Version(int protocolId, String nmsVersion, List<String> versions) {
        this(protocolId, nmsVersion, versions, () -> true);
    }

    public static Version getPlayerVersion(Player player) {
        return versionMap.get(NMSHandlers.getNmsHandler().getProtocolVersionId(player));
    }

    public static Version getServerVersion() {
        return serverVersion;
    }

    public static int getProtocolVersion() {
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

    public List<String> getVersions() {
        return this.versions;
    }

    public int getProtocolId() {
        return this.protocolId;
    }

    public String getNMSVersion() {
        return this.nmsVersion;
    }
}
