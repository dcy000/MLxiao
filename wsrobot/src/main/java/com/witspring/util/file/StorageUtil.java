package com.witspring.util.file;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.witspring.unitbody.ChooseMemberActivity;
import com.witspring.util.CommUtil;
import com.witspring.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *  文件存储
 */
public class StorageUtil {
	private static final String tag = StorageUtil.class.getSimpleName();
	
	public static String FULL_ROOT_DIR = getRootDir(ChooseMemberActivity.app.getApplicationContext());

    /**
     * 获取可用存储空间的根目录，一般先获取外置存储空间，如果没有，再去获取内存存储空间， 如果都没有，则获取本地应用的可用目录
     */
    public static String getRootDir(Context context) {
        // 先找外置存储路径
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        // 再找内置SDCard
        if (CommUtil.notEmpty(mVolds)) {
            for (VoldFstab vold : mVolds) {
                File mount = new File(vold.mMountPoint);
                if (mount.exists() && mount.canRead() && mount.canWrite()) {
                    // && mount.canExecute()
                    return mount.getAbsolutePath();
                }
            }
        }
        // 再找本地应用内存路径
        if (context != null) {
            return context.getFilesDir().getAbsolutePath();
        } else {
        }
        return null;
    }

    /**
     * 获取文件类型，文件类型请查看FileCst定义
     */
    public static int getFileType(String fileName) {
        if (StringUtil.isBlank(fileName))
            return FileCst.TYPE_ERROR;
        fileName = fileName.toLowerCase();

        if (fileName.endsWith(FileCst.SUFFIX_PNG)
                || fileName.endsWith(FileCst.SUFFIX_JPG)
                || fileName.endsWith(FileCst.SUFFIX_JPE)
                || fileName.endsWith(FileCst.SUFFIX_JPEG)
                || fileName.endsWith(FileCst.SUFFIX_BMP)
                || fileName.endsWith(FileCst.SUFFIX_GIF)) { // 图片类型
            return FileCst.TYPE_IMAGE;
        } else if (fileName.endsWith(FileCst.SUFFIX_MP4)
                || fileName.endsWith(FileCst.SUFFIX_3GPP)
                || fileName.endsWith(FileCst.SUFFIX_M4A)
                || fileName.endsWith(FileCst.SUFFIX_AMR)){ // 音频类型
            return FileCst.TYPE_AUDIO;
        } else if (fileName.endsWith(FileCst.SUFFIX_VID)) { // 视频类型
            return FileCst.TYPE_VIDEO;
        } else if (fileName.endsWith(FileCst.SUFFIX_APK)
                || fileName.endsWith(FileCst.SUFFIX_DEX)) { // 安装包类型
            return FileCst.TYPE_APK;
        } else if (fileName.endsWith(FileCst.SUFFIX_TXT)) { // 文本类型
            return FileCst.TYPE_TXT;
        } else if (fileName.endsWith(FileCst.SUFFIX_LOG)) { // 日志类型
            return FileCst.TYPE_LOG;
        } else if (fileName.endsWith(FileCst.SUFFIX_RAR)
                || fileName.endsWith(FileCst.SUFFIX_ZIP)) { // 压缩包类型
            return FileCst.TYPE_ZIP;
        } else if (fileName.endsWith(FileCst.SUFFIX_DB)) { // 数据存储类型
            return FileCst.TYPE_DATA;
        } else { // 未知
            return FileCst.TYPE_UNKNOWN;
        }
    }

    /**
     * 获取文件目录名, 相对目录请查看FileCst定义
     */
    public static String getFileDir(Context context, int fileType) {
        String rootDir = getRootDir(context);
        if (StringUtil.isBlank(rootDir))
            return null;
        String directory;
        switch (fileType) {
            case FileCst.TYPE_IMAGE:
                directory = FileCst.DIR_IMAGE;
                break;
            case FileCst.TYPE_AUDIO:
                directory = FileCst.DIR_AUDIO;
                break;
            case FileCst.TYPE_VIDEO:
                directory = FileCst.DIR_VIDEO;
                break;
            case FileCst.TYPE_APK:
                directory = FileCst.DIR_APK;
                break;
            case FileCst.TYPE_TXT:
                directory = FileCst.DIR_TXT;
                break;
            case FileCst.TYPE_LOG:
                directory = FileCst.DIR_LOG;
                break;
            case FileCst.TYPE_ZIP:
                directory = FileCst.DIR_ZIP;
                break;
            case FileCst.TYPE_DATA:
                directory = FileCst.DIR_DATA;
                break;
            case FileCst.TYPE_UNKNOWN:
                directory = FileCst.DIR_UNKNOWN;
                break;
            case FileCst.TYPE_ERROR:
            default:
                directory = null;
                break;
        }
        return directory == null ? null : rootDir + File.separator + directory;
    }

    /**
     * 根据文件名获取文件目录名, 相对目录请查看FileCst定义
     * @see #getFileDir(Context, int)
     */
    public static String getFileDir(Context context, String fileName) {
        int fileType = getFileType(fileName);
        return getFileDir(context, fileType);
    }

    /**
     * 根据文件类型创建文件的目录
     */
    public static String createFileDir(Context context, int fileType) {
        String fileDir = getFileDir(context, fileType);
        return FileUtil.makeFolders(fileDir) ? fileDir : null;
    }

    /**
     * 根据文件名创建文件的目录
     * @see #createFileDir(Context, int)
     */
    public static String createFileDir(Context context, String fileName) {
        int fileType = getFileType(fileName);
        return createFileDir(context, fileType);
    }

    /**
     * 获取文件的绝对路径
     */
    public static String getFilePath(Context context, String fileName) {
        String fileDir = getFileDir(context, fileName);
        // 创建文件目录
        File f = new File(fileDir);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        return StringUtil.isBlank(fileDir) ? null : fileDir + File.separator + fileName;
    }

    /**
     * 创建文件
     */
    public static String createFilePath(Context context, String fileName) {
        String fileDir = createFileDir(context, fileName);
        if (StringUtil.isBlank(fileDir)) {
            return null;
        } else {
            String filePath = fileDir + File.separator + fileName;
            return FileUtil.createFile(filePath) ? filePath : null;
        }
    }

    /**
     * 获取指定目录剩余存储空间，返回单位为字节
     */
    public static long getLeftSpace(String directory) {
        if (StringUtil.isBlank(directory))
            return 0;
        long space = 0;
        try {
            StatFs sf = new StatFs(directory);
            space = (long) sf.getBlockSize() * sf.getAvailableBlocks();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return space;
    }

    /**
     * 获取指定目录所有存储空间, 返回单位为字节
     */
    public static long getTotalSpace(String directory) {
        if (StringUtil.isBlank(directory))
            return 0;
        long space = 0;
        try {
            StatFs sf = new StatFs(directory);
            space = (long) sf.getBlockSize() * sf.getBlockCount();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return space;
    }

    private static final String DEV_MOUNT = "dev_mount";
    private static ArrayList<VoldFstab> mVolds;
    static {
        mVolds = new ArrayList<VoldFstab>();
        BufferedReader reader = null;
        try {
            // vold.fstab文件
            File file = new File(Environment.getRootDirectory()
                    .getAbsoluteFile()
                    + File.separator
                    + "etc"
                    + File.separator + "vold.fstab");
            reader = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(DEV_MOUNT)) {
                    String[] infos = line.split(" ");
                    VoldFstab vold = new VoldFstab();
                    vold.mLabel = infos[1]; // 设置标签
                    vold.mMountPoint = infos[2].split(":")[0];// 设置挂载点
                    vold.mPart = infos[3];// 设置子分区个数
                    vold.mSysfs = infos[4].split(":");// 设置设备在sysfs文件系统下的路径
                    mVolds.add(vold);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class VoldFstab {
        // 标签
        public String mLabel;
        // 挂载点
        public String mMountPoint;
        // 子分区个数
        public String mPart;
        // 设备在sysfs文件系统下的路径
        public String[] mSysfs;
    }

}
