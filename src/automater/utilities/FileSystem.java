/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import java.io.BufferedReader;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * Defines commonly used file system methods.
 *
 * @author Bytevi
 */
public class FileSystem {

    private static String absolutePath;

    public static @NotNull String getDirectorySeparator() {
        if (DeviceOS.isWindows()) {
            return "\\";
        }

        return "/";
    }

    public synchronized static @NotNull String getLocalFilePath() {
        if (absolutePath == null) {
            absolutePath = new File("").getAbsolutePath();
        }

        return absolutePath;
    }
    
    public static boolean validatePath(@NotNull String path) {
        return true;
    }

    public static @NotNull String buildPath(@NotNull String base, @NotNull String path) {
        if (base.isEmpty()) {
            return path;
        }

        return base + getDirectorySeparator() + path;
    }

    public static @NotNull String buildPath(@NotNull String path) {
        String separator = getDirectorySeparator();
        int lastIndexOfSlash = path.lastIndexOf(separator);

        if (lastIndexOfSlash != -1 && lastIndexOfSlash + 1 < path.length()) {
            path = path.substring(0, lastIndexOfSlash + 1);
        }

        return path;
    }

    public static @NotNull String getLastComponentFromPath(@NotNull String path) {
        String separator = getDirectorySeparator();
        int lastIndexOfSlash = path.lastIndexOf(separator);

        if (lastIndexOfSlash == -1) {
            return path;
        }

        if (lastIndexOfSlash + 1 < path.length()) {
            path = path.substring(lastIndexOfSlash + 1);
            return path;
        }

        return "";
    }

    public static @NotNull String addFileExtension(@NotNull String path, @NotNull String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        
        if (!path.endsWith(extension)) {
            path = path + extension;
        }

        return path;
    }

    public static @NotNull List<File> getAllInDirectory(@NotNull String path) {
        ArrayList<File> files = new ArrayList<>();

        File folder = new File(path);

        if (!folder.isDirectory()) {
            return files;
        }

        File[] listOfFiles = folder.listFiles();

        for (int e = 0; e < listOfFiles.length; e++) {
            File file = listOfFiles[e];

            files.add(file);
        }

        return files;
    }

    public static @NotNull List<File> getAllFilesInDirectory(@NotNull String path) {
        return getAllFilesInDirectory(path, new ArrayList<>());
    }

    public static @NotNull List<File> getAllFilesInDirectory(@NotNull String path, @NotNull String extension) {
        ArrayList<String> extensions = new ArrayList<>();
        extensions.add(extension);
        return getAllFilesInDirectory(path, extensions);
    }
    
    public static @NotNull File getFile(@NotNull String base, @NotNull String name) {
        return getFile(FileSystem.buildPath(base, name));
    }
    
    public static @NotNull File getFile(@NotNull String path) {
        File file = new File(path);

        if (file.isDirectory()) {
            throw new UnsupportedOperationException("Not a file");
        }
        
        return file;
    }

    public static @NotNull List<File> getAllFilesInDirectory(@NotNull String path, @NotNull List<String> extensions) {
        ArrayList<File> files = new ArrayList<>();

        File folder = new File(path);

        if (!folder.isDirectory()) {
            return files;
        }

        File[] listOfFiles = folder.listFiles();

        for (int e = 0; e < listOfFiles.length; e++) {
            File file = listOfFiles[e];

            if (file.isDirectory()) {
                continue;
            }

            for (String extension : extensions) {
                if (file.getName().endsWith(extension)) {
                    files.add(listOfFiles[e]);
                }
            }
        }

        return files;
    }
    
    public static @NotNull String readFromFile(@NotNull File file) throws Exception {
        String data = "";

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            while (line != null) {
                data = data.concat(line);
                line = reader.readLine();
            }

            reader.readLine();
            reader.close();
        } catch (Exception e) {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e2) {

            }
            
            throw e;
        }

        return data;
    }
    
    public static @Nullable Exception writeToFile(@NotNull File file, @NotNull String data) throws Exception {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(file);
            String[] lines = data.split("\n");

            for (String line : lines) {
                writer.println(line);
            }

            writer.println();
            writer.close();
        } catch (Exception e) {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e2) {

            }
            
            return e;
        }
        
        return null;
    }
}
