package com.example.huangqihua.filedownloadapplication.manager;

import android.content.Context;
import android.os.Handler;
import com.example.huangqihua.filedownloadapplication.HQHDataListener;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangqihua on 16/9/27.
 */
public class HQHFileDownloadManager {

    private static final int threadPoolNum = 5;

    private static HQHFileDownloadManager mInstance;

    private Context context;

    private Map<String, DownloadTask> downloadTask = new Hashtable<>();

    private ExecutorService mService = Executors.newFixedThreadPool(threadPoolNum); //使用線程池

    private Handler mHandler = new Handler();

    public HQHFileDownloadManager(Context context) {
        this.context = context;
    }


    public static HQHFileDownloadManager getInstance(Context context) {

        synchronized (HQHFileDownloadManager.class) {

            if (mInstance == null) {
                mInstance = new HQHFileDownloadManager(context);
            }
        }
        return mInstance;
    }

    public static HQHFileDownloadManager getInstance() {
        return mInstance;
    }

    public synchronized void request(String url, HQHDataListener<String> listener) {
        String fileName = HQHFileManager.existFileForUrl(context, url);
        if (fileName != null) {
            listener.onResponse(fileName);
        } else {
            DownloadTask task = downloadTask.get(url);
            if (task != null) {
                task.addListener(listener);
            } else {
                DownloadTask loadTask = new DownloadTask(url, listener);
                downloadTask.put(url, loadTask);
                mService.execute(loadTask);
            }
        }
    }

    class DownloadTask implements Runnable {

        private String address;
        private List<HQHDataListener<String>> listeners = new ArrayList<>();
        private String fileName;
        private String tmpFileName;

        public DownloadTask(String url, HQHDataListener<String> listener) {
            this.address = url;
            this.listeners.add(listener);
            this.fileName = HQHFileManager.fileForUrl(context, address);
            this.tmpFileName = this.fileName + "_tmp";
        }

        public void addListener(HQHDataListener<String> listener) {
            this.listeners.add(listener);
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                java.net.URL url = new java.net.URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int code = connection.getResponseCode();

                if (code == 200) {
                    HQHFileManager.createFile(this.tmpFileName);
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.tmpFileName));
                    inputStream = connection.getInputStream();
                    byte[] cache = new byte[2048];
                    int len;
                    while ((len = inputStream.read(cache, 0, cache.length)) != -1) {
                        bufferedOutputStream.write(cache, 0, len);
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedOutputStream = null;
                    inputStream.close();
                    inputStream = null;
                    HQHFileManager.rename(this.tmpFileName, fileName);
                    final String fname = fileName;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (HQHDataListener listener : listeners) {
                                listener.onResponse(fname);
                            }
                        }
                    });
                } else {
                    final String c = String.valueOf(code);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            CEDataException exception = new CEDataException(-1,c);
//                            for (CEDataListener listener : listeners) {
//                                listener.onResponse(null, exception);
//                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();

                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                catch (Exception e) {

                }
                finishDownloadTask(address);
            }

        }


    }

    private void finishDownloadTask(String url) {
        downloadTask.remove(url);
    }


}
