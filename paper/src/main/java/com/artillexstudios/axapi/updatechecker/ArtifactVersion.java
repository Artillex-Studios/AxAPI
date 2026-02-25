package com.artillexstudios.axapi.updatechecker;

public record ArtifactVersion(String string, long version) {

    public ArtifactVersion(String version) {
        this(version, toLong(version));
    }

    private static long toLong(String version) {
        String[] split = version.split("\\.");
        long v = 0;
        int j = 0;
        for (int i = split.length - 1; i >= 0; i--) {
            int len = split[i].length();
            v += Integer.parseInt(split[i]) * (long) Math.pow(10, j);
            j += len;
        }

        return v;
    }
}
