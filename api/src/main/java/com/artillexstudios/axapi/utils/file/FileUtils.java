package com.artillexstudios.axapi.utils.file;

import com.artillexstudios.axapi.dependency.DependencyContainer;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    static void copyDirectory(Path from, Path to) {
        try {
            if (!Files.isDirectory(from)) {
                if (!Files.exists(to.getParent())) {
                    Files.createDirectories(to.getParent());
                }
                Files.copy(from, to, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
                return;
            }

            try (Stream<Path> paths = Files.list(from)) {
                paths.forEach(path -> {
                    Path newTo = to.resolve(from.relativize(path));
                    if (Files.isDirectory(path)) {
                        try {
                            if (!Files.exists(newTo)) {
                                Files.createDirectory(newTo);
                            }
                        } catch (IOException exception) {
                            LogUtils.error("Failed to copy directory from path {} to path {}!", path, newTo);
                        }
                    }

                    copyDirectory(path, newTo);
                });
            }
        } catch (IOException exception) {
            LogUtils.error("Failed to copy directory from path {} to path {}!", from, to);
        }
    }
}
