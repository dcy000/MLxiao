
package com.witspring.util.file;

import android.content.Context;
import android.util.Log;

import com.witspring.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 文件工具类 public method <li>createFile(String) 创建文件</li>
 * <li>makeDirs(String) 根据文件路径，创建目录</li>
 * <li>makeFolders(String) 根据文件夹路径，创建目录</li>
 */
public class FileUtil {

    public final static String tag = "FileUtil";

    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
    	if(StringUtil.isBlank(filePath)){
    		Log.e(tag,"filePath==null");
    		return false;
    	}
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            if(fileWriter==null	){
            	Log.e(tag,"fileWriter==null");
            	return false;
            }
	            fileWriter.write(content);
	            fileWriter.close();
            return true;
        } catch (IOException e) {
        	Log.e(tag, "IOException occurred. ", e);
        	return false;
            
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                	return false;
                }
            }
        }
    }

    public static boolean writeFile(String filePath, InputStream stream) {
        OutputStream o = null;
        try {
            o = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static String getFileName(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    public static String getFileExt(String filePath) {
        String fileName = getFileName(filePath);
        String[] arr = fileName.split("\\.");
        if (arr != null && arr.length >= 2) {
            return "." + arr[arr.length - 1];
        }
        return null;
    }


    public static boolean isFileExist(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * 获取文件夹下的文件list  
     */
    public static File[] getFilePathListByFolderPath(String folderPath){
    	File folderDir = new File(StorageUtil.FULL_ROOT_DIR+"/"+folderPath);
    	File[] fileArray;
    	if(folderDir == null){
    		return null;
    	}
    	if(folderDir.isDirectory()){
    		fileArray = folderDir.listFiles();
    		return fileArray;
    	}
    	return null;
    }

    public static boolean isFolderExist(String directoryPath) {
        if (StringUtil.isBlank(directoryPath)) {
            return false;
        }
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    public static boolean deleteFile(String path) {
        if (StringUtil.isBlank(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public static long getFileSize(String path) {
        if (StringUtil.isBlank(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 文件拷贝
     * @param source 源文件
     * @param dest 目标文件
     */
    public static boolean copyFile(String source, String dest) {
        try {
            if (FileUtil.isFileExist(source) && StringUtil.isNotBlank(dest)) {
                FileInputStream fileIn = new FileInputStream(source);
                FileOutputStream fileOut = new FileOutputStream(dest);
                byte[] buffer = new byte[8192];
                int length = 0;
                while ((length = fileIn.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, length);
                }
                fileIn.close();
                fileOut.close();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 拷贝assets文件夹下的文件
     * @param context 上下文
     * @param source assets文件夹下源文件
     * @param dest 目标文件
     */
    public static boolean copyAssetsFile(Context context, String source, String dest) {
        try {
            if (context != null && StringUtil.isNotTrimBlank(source) && StringUtil.isNotBlank(dest)) {
                BufferedInputStream bufferIn = new BufferedInputStream(context.getResources().getAssets().open(source));
                FileOutputStream fileOut = new FileOutputStream(dest);
                byte[] buffer = new byte[8192];
                int length = 0;
                while ((length = bufferIn.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, length);
                }
                bufferIn.close();
                fileOut.close();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static String getFolderName(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 创建文件
     */
    public static boolean createFile(String filePath) {
        if (StringUtil.isBlank(filePath))
            return false;
        File file = new File(filePath);
        if (file.exists())
            return true;
        boolean result = false;
        try {
            if (makeDirs(filePath))
                result = file.createNewFile();
        } catch (IOException ex) {
        }
        return result;
    }

    /**
     * 根据文件路径，创建目录
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtil.isBlank(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    /**
     * 根据文件夹路径，创建目录
     */
    public static boolean makeFolders(String fileDir) {
        if (StringUtil.isBlank(fileDir)) {
            return false;
        }
        File folder = new File(fileDir);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

}
