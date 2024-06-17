/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import java.io.BufferedReader;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines commonly used file system methods.
 *
 * @author Bytevi
 */
public class FileSystem {
    
    public static void createBlankFile(@NotNull Path path) throws IOException {
        path.getFile().createNewFile();
    }
    
    public static void createDirectory(@NotNull Path path) {
        path.getFile().mkdirs();
    }

    public static @NotNull List<File> getAllInDirectory(@NotNull Path path) {
        var files = new ArrayList<File>();

        var folder = path.getFile();

        if (!folder.isDirectory()) {
            return files;
        }

        var listOfFiles = folder.listFiles();

        for (int e = 0; e < listOfFiles.length; e++) {
            var file = listOfFiles[e];

            files.add(file);
        }

        return files;
    }

    public static @NotNull List<File> getAllFilesInDirectory(@NotNull Path path) {
        return getAllFilesInDirectory(path, new ArrayList<>());
    }

    public static @NotNull List<File> getAllFilesInDirectory(@NotNull Path path, @NotNull String extension) {
        var extensions = new ArrayList<String>();
        extensions.add(extension);
        return getAllFilesInDirectory(path, extensions);
    }
    
    public static @NotNull List<File> getAllFilesInDirectory(@NotNull Path path, @NotNull List<String> extensions) {
        var files = new ArrayList<File>();

        var folder = path.getFile();

        if (!folder.isDirectory()) {
            return files;
        }

        var listOfFiles = folder.listFiles();

        for (int e = 0; e < listOfFiles.length; e++) {
            var file = listOfFiles[e];

            if (file.isDirectory()) {
                continue;
            }

            for (var extension : extensions) {
                if (file.getName().endsWith(extension)) {
                    files.add(listOfFiles[e]);
                }
            }
        }

        return files;
    }
    
    public static @NotNull String readFromFile(@NotNull File file) throws Exception {
        var data = "";

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            var line = reader.readLine();

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
    
    public static void writeToFile(@NotNull File file, @NotNull String data) throws Exception {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(file);
            var lines = data.split("\n");

            for (var line : lines) {
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
            
            throw e;
        }
    }
}
