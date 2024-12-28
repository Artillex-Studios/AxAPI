package com.artillexstudios.axapi.config.service.implementation;

import com.artillexstudios.axapi.config.service.Writer;
import com.artillexstudios.axapi.utils.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class FileWriter implements Writer {

    @Override
    public boolean write(Path path, String dumped) {
        try {
            File file = path.toFile();
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            File temp = new File(parent, file.getName() /*+ ".tmp"*/);
            temp.delete();
            temp.createNewFile();
            try {
                if (dumped != null) {
                    System.out.println(dumped);
                    try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(temp))) {
                        writer.write(dumped);
                        writer.flush();
                    }
                }

//                try {
//                    Files.move(temp.toPath(), path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
//                } catch (AtomicMoveNotSupportedException exception) {
//                    Files.move(temp.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
//                }
            } finally {
//                temp.delete();
            }
        } catch (IOException exception) {
            LogUtils.error("An unexpected error occurred while saving file!", exception);
            return false;
        }

        return true;
    }
}
