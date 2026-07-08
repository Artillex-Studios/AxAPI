package com.artillexstudios.axapi.utils.file;

import com.artillexstudios.axapi.dependency.DependencyContainer;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileUtils {

    static FileUtils getInstance() {
        return DependencyContainer.getInstance(FileUtils.class);
    }

    File getFolder();

    static void deleteNested(Path directory) {
        if (!Files.exists(directory)) {
            return;
        }

        try {
            if (Files.isDirectory(directory)) {
                try (Stream<Path> files = Files.list(directory)) {
                    files.forEach(FileUtils::deleteNested);
                }
            }

            Files.delete(directory);
        } catch (IOException exception) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.error("Failed to delete directory {}", directory);
            }
        }
    }
}
