package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.AxPlugin;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtils {
    private static final Plugin INSTANCE = AxPlugin.getPlugin(AxPlugin.class);
    private static final Path LOGS_PATH = INSTANCE.getDataFolder().toPath().resolve("logs/");
    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("[HH:mm:ss]");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void log(String log) {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());

        String date = time.format(DATE_FORMAT);
        Path logPath = LOGS_PATH.resolve(date + ".log");
        File logFile = logPath.toFile();

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdir();
        }

        try {
            logFile.createNewFile();
        } catch (IOException exception) {
            LogUtils.log.error("An unexpected error occurred while creating new file!", exception);
        }

        String formattedTime = TIME_FORMAT.format(time);
        try (FileWriter fileWriter = new FileWriter(logFile, true); PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print("[" + formattedTime + "] ");
            printWriter.println(log);
        } catch (IOException exception) {
            LogUtils.log.error("An unexpected error occurred while writing to log file!", exception);
        }
    }
}
