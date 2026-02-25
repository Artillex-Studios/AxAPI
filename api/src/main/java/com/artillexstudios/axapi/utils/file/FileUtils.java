package com.artillexstudios.axapi.utils.file;

import com.artillexstudios.axapi.dependency.DependencyContainer;

import java.io.File;

public interface FileUtils {

    static FileUtils getInstance() {
        return DependencyContainer.getInstance(FileUtils.class);
    }

    File getFolder();
}
