package com.gcml.common.repository.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

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

    public static void writeToFile(File file, String content) {
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

    public static String readFromFile(File file) {
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

    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean exists(File file) {
        return file.exists();
    }

    public static boolean clearDirectory(File directory) {
        if (directory == null
                || !directory.exists()
                || !directory.isDirectory()) {
            return false;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return false;
        }
        boolean deleted = false;
        for (File file : files) {
            deleted = file.delete();
        }
        return deleted;
    }

    public static long size(File file) {
        if (file == null) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }

        long size = 0;
        File[] files = file.listFiles();
        if (files == null) {
            return size;
        }
        for (File temp : files) {
            size += temp.length();
            if (temp.isDirectory()) {
                size += size(file);
            }
        }
        return size;
    }

    public static File getCacheDirectory(Context context, String type) {
        File appCacheDir = getExternalCacheDirectory(context, type);
        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDirectory(context, type);
        }

        if (appCacheDir == null) {
            Timber.e("getCacheDirectory: unknown exception !");
        } else {
            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                Timber.e("getCacheDirectory: make directory fail !");
            }
        }
        return appCacheDir;
    }

    public static File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            } else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/cache/" + type);
            }

            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                Timber.e("getExternalDirectory: make directory failed !");
            }
        } else {
            Timber.e("getExternalDirectory: SdCard not exist or SdCard mount fail !");
        }
        return appCacheDir;
    }

    public static File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(context.getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Timber.e("getInternalDirectory make directory fail !");
        }
        return appCacheDir;
    }
}
