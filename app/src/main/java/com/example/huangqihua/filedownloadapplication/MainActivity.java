package com.example.huangqihua.filedownloadapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.huangqihua.filedownloadapplication.manager.HQHFileDownloadManager;
import com.example.huangqihua.filedownloadapplication.util.ACache;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView mShowImage;

    private String url = "http://img2.ph.126.net/btvSJIi-tIvP1yFkfMKXSg==/3156178913956681931.jpg";

    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCache = ACache.get(this);

        mShowImage = (ImageView) findViewById(R.id.imageView);

        loadImage();

        showImage();
    }


    private void loadImage() {

        HQHFileDownloadManager.getInstance().request(url, new HQHDataListener<String>() {
            @Override
            public void onResponse(String data) {
                if (data != null) {
                    mCache.put("show_image", data);
                } else {
                    Toast.makeText(MainActivity.this, "图片地址是空的", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void showImage() {


        File file = null;

        String downloadImage = mCache.getAsString("show_image");
        if (downloadImage != null) {
            file = new File(downloadImage);
        }
        if (downloadImage != null && file.exists()) {
            ImageLoader.getInstance().displayImage("file://" + downloadImage, mShowImage);
        } else {
            assert mShowImage != null;
            mShowImage.setImageResource(R.drawable.show_girl);
        }

    }
}
