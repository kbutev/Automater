/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logs various messages to the standard output.
 *
 * @author Bytevi
 */
public class Logger {

    public static final boolean VERBOSE = true;
    
    public static final boolean PRINT_TO_LOG_FILE = true;
    public static final String LOG_FILE_NAME = "logs.txt";
    public static final String LOG_BACKUP_FILE_NAME = "logs-previous.txt";
    public static final boolean DISPLAY_TIMESTAMPS = true;

    public static final String MESSAGE_ACTION_PREFIX = "#ACTION#";
    public static final String MESSAGE_EVENT_PREFIX = "#EVENT#";
    public static final String WARNING_PREFIX = "#WARNING#";
    public static final String ERROR_PREFIX = "#ERROR#";
    public static final String UTILITY_ERROR_PREFIX = "#UTILITY_ERROR#";
    public static final String SYSTEM_ERROR_PREFIX = "#SYSTEM_ERROR#";

    public static final String OVERRIDE_ME_MESSAGE = "Calling non-overriden base method ";

    private static final PrintStream _outStream = System.out;

    public static void printLine(@Nullable String data) {
        if (data == null) {
            return;
        }
        
        _outStream.println(data);

        if (PRINT_TO_LOG_FILE) {
            LoggerFile.writeToFile(data);
        }
    }

    public static <T> void message(@NotNull T origin, @Nullable String string) {
        printLine(generateText(origin, "", string));
    }

    public static <T> void message(String prefix, @Nullable String string) {
        printLine(generateText(prefix, string));
    }
    
    public static <T> void messageVerbose(@NotNull T origin, @Nullable String string) {
        if (VERBOSE) {
            printLine(generateText(origin, "", string));
        }
    }

    public static <T> void warning(String prefix, @Nullable String string) {
        prefix = WARNING_PREFIX + " " + prefix;

        printLine(generateText(prefix, string));
    }

    public static <T> void warning(T origin, @Nullable String string) {
        printLine(generateText(origin, WARNING_PREFIX, string));
    }

    public static <T> void error(T origin, @Nullable String string) {
        printLine(generateText(origin, ERROR_PREFIX, string));
    }

    public static <T> void error(@Nullable String prefix, @Nullable String string) {
        prefix = ERROR_PREFIX + " " + prefix;

        printLine(generateText(prefix, string));
    }

    private static <T> @NotNull String generateText(T origin, @Nullable String prefix, @Nullable String text) {
        String reportingClass = "Automater";
        
        if (origin.getClass() != null) {
            var name = origin.getClass().getSimpleName();
            reportingClass = name != null ? origin.getClass().getName() : reportingClass;
        } else {
            reportingClass = prefix;
        }
        
        return generateText(reportingClass, text);
    }

    private static <T> @NotNull String generateText(@Nullable String prefix, @Nullable String text) {
        String textPrefix = generateTimestamp(prefix);

        if (textPrefix.isEmpty()) {
            return text != null ? text : "";
        }

        return textPrefix + ": " + text;
    }

    private static @NotNull String generateTimestamp(@Nullable String prefix) {
        if (!DISPLAY_TIMESTAMPS || prefix == null) {
            return prefix != null ? prefix : "";
        }

        return prefix.length() > 0 ? getTimestamp() + " " + prefix : getTimestamp();
    }

    private static @NotNull String getTimestamp() {
        String pattern = "HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date());
    }
}

class LoggerFile {

    private static final @NotNull Object _lock = new Object();
    private static @Nullable File _logFile;
    private static @Nullable FileWriter _logWriter;

    public static @NotNull Path getFilePath() {
        return Path.getLocalDirectory().withSubpath(Logger.LOG_FILE_NAME);
    }

    public static @Nullable Path getBackupFilePath() {
        if (Logger.LOG_BACKUP_FILE_NAME.isEmpty()) {
            return null;
        }

        return Path.getLocalDirectory().withSubpath(Logger.LOG_BACKUP_FILE_NAME);
    }

    public static @NotNull File getFile() {
        return setupFileIfNecessary();
    }

    public static void writeToFile(@NotNull String data) {
        var writer = setupFileWriterIfNecessary();

        if (writer == null) {
            return;
        }

        try {
            String[] lines = data.split("\n");

            for (var line : lines) {
                writer.append(line + "\n");
            }

            writer.flush();
        } catch (Exception e) {

        }
    }

    public static @NotNull File setupFileIfNecessary() {
        synchronized (_lock) {
            boolean initialized = _logFile != null;

            if (!initialized) {
                // Backup current log file if a backup name is defined
                // Otherwise just delete the current log file
                if (getBackupFilePath() != null) {
                    backupFile();
                } else {
                    deleteFile();
                }

                _logFile = getFilePath().getFile();
            }

            return _logFile;
        }
    }

    public static void backupFile() {
        var backupPath = getBackupFilePath();
        
        if (backupPath == null) {
            return;
        }

        // Delete backup file (if it exists)
        // Backup current log file
        // Delete log file (if it exists)
        var backup = backupPath.getFile();
        var logFile = getFilePath().getFile();

        try {
            backup.delete();
        } catch (Exception e) {

        }

        if (logFile.exists()) {
            logFile.renameTo(backup);

            try {
                logFile.delete();
            } catch (Exception e) {

            }
        }
    }

    public static void deleteFile() {
        var logFile = getFilePath().getFile();

        try {
            logFile.delete();
        } catch (Exception e) {

        }
    }

    public static @Nullable FileWriter setupFileWriterIfNecessary() {
        var logFile = getFile();

        synchronized (_lock) {
            if (_logWriter == null) {
                try {
                    _logWriter = new FileWriter(logFile, true);
                } catch (Exception e) {
                    System.out.println("Logger could not start a log writer for the log file: " + e.toString());
                }
            }

            return _logWriter;
        }
    }
}
