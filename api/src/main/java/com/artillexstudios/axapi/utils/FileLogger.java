package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.AxPlugin;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class FileLogger {
    private static final Plugin INSTANCE = AxPlugin.getPlugin(AxPlugin.class);
    private static final Logger log = LoggerFactory.getLogger(FileLogger.class);
    private final Path logsPath;
    private final DateTimeFormatter dateFormat;
    private final DateTimeFormatter timeFormat;

    public FileLogger(String folder, DateTimeFormatter dateFormat, DateTimeFormatter timeFormat) {
        this.logsPath = INSTANCE.getDataFolder().toPath().resolve(folder + "/");
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }

    public FileLogger(String folder) {
        this(folder, DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("[HH:mm:ss]"));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void log(String log) {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());

        String date = time.format(this.dateFormat);
        Path logPath = this.logsPath.resolve(date + ".log");
        File logFile = logPath.toFile();

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdir();
        }

        try {
            logFile.createNewFile();
        } catch (IOException exception) {
            FileLogger.log.error("An unexpected error occurred while creating new file!", exception);
        }

        String formattedTime = this.timeFormat.format(time);
        try (FileWriter fileWriter = new FileWriter(logFile, true); PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print("[" + formattedTime + "] ");
            printWriter.println(log);
        } catch (IOException exception) {
            FileLogger.log.error("An unexpected error occurred while writing to log file!", exception);
        }
    }
}
