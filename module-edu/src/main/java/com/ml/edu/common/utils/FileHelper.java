package com.ml.edu.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by afirez on 18-1-31.
 */
@Singleton
public class FileHelper {

    private static volatile FileHelper sInstance;

    public static FileHelper getInstance() {
        if (sInstance == null) {
            synchronized (FileHelper.class) {
                if (sInstance == null) {
                    sInstance = new FileHelper();
                }
            }
        }
        return sInstance;
    }

    @Inject
    public FileHelper() {

    }

    public void writeToFile(File file, String content) {
        if (!file.exists()) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String readFromFile(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        if (file.exists()) {
            String line;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return contentBuilder.toString();
    }

    public boolean exists(File file) {
        return file.exists();
    }

    public boolean clearDirectory(File directory) {
        if (directory == null
                || !directory.exists()
                || !directory.isDirectory()) {
            return false;
        }
        boolean deleted = false;
        for (File file : directory.listFiles()) {
            deleted = file.delete();
        }
        return deleted;
    }

    public long size(File file) {
        if (file == null) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }

        long size = 0;
        File[] files = file.listFiles();
        for (File temp : files) {
            size += temp.length();
            if (temp.isDirectory()) {
                size += size(file);
            }
        }
        return size;
    }
}
