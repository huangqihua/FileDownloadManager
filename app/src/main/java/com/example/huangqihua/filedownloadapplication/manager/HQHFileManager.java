package com.example.huangqihua.filedownloadapplication.manager;

import android.content.Context;
import android.os.Environment;
import com.example.huangqihua.filedownloadapplication.util.HQHFramework;
import com.example.huangqihua.filedownloadapplication.util.MiniMd5;

import java.io.File;
import java.io.IOException;

/**
 * Created by huangqihua on 16/9/27.
 */
public class HQHFileManager {

    public static String existFileForUrl(Context context, String url){
        String fileName = fileForUrl(context, url);
        if (fileName != null){
            File file = new File(fileName);
            if (file.exists()){
                return fileName;
            }
        }
        return null;
    }

    public static String fileForUrl(Context context, String url) {
        String fileName = MiniMd5.md5String(url);
        int index = url.lastIndexOf(".");
        if (index != -1) {
            String ext = url.substring(index + 1);
            fileName = fileName + "." + ext;
        }
        fileName = getAppFilePath(context, "cache/download") + File.separator + fileName;
        return fileName;
    }

    private static String getAppFilePath(Context context, String directoryPath) {
        String sdcardPath = getSdcardPath(context);
        if (context != null) {
            String path = sdcardPath + File.separator + HQHFramework.appShortName + File.separator + directoryPath;
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            return path;
        } else {
            return sdcardPath;
        }


    }

    private static String getSdcardPath(Context context) {

        String sdcardPath;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            sdcardPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdcardPath = context.getCacheDir().getPath();
        }
        return sdcardPath;
    }


    public static void createFile(String tmpFileName) throws IOException {
        File file = new File(tmpFileName);
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();

    }

    public static void rename(String tmpFileName, String fileName) {

        File file = new File(tmpFileName);
        if (file.exists()){
            File objectFile = new File(fileName);
            if (objectFile.exists()){
                objectFile.delete();
            }
            file.renameTo(objectFile);
        }

    }

}
