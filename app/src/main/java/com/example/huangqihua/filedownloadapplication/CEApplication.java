package com.example.huangqihua.filedownloadapplication;

import android.app.Application;
import com.example.huangqihua.filedownloadapplication.manager.HQHFileDownloadManager;
import com.example.huangqihua.filedownloadapplication.util.HQHFramework;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by huangqihua on 16/9/27.
 */
public class CEApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HQHFramework.appShortName = "hqh";

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        HQHFileDownloadManager.getInstance(this);
    }
}
