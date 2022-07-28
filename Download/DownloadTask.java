package com.example.review_internet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// 开启一个下载任务
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    // 下载状态代码
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private final DownloadListener listener;

    private boolean isCanceled = false;
    private boolean isPaused = false;

    private int lastProgress;

    // 用来调用context.getExternalFilesDir()获取目录
    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public DownloadTask(DownloadListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;      // 随机访问文件
        File file = null;
        try {
            long downloadLength = 0;    // 已下载的长度
            String downloadUrl = params[0];     // 从execute()传入的参数中获取url
            // 截取url地址的最后一段作为文件名
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            //String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            // 获取目录
            String directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
            // 在获得的目录下使用解析出来的文件名创建一个File对象
            file = new File(directory + fileName);
            // 如果File对象对应的文件已经存在，计算文件长度
            if (file.exists()) {
                downloadLength = file.length();
            }
            // 计算出下载链接的文件长度
            long contentLength = getContentLength(downloadUrl);
            // 根据已下载文件长度和所需下载文件的长度，判断下载状态
            if (contentLength <= 0) return TYPE_FAILED;     // url有问题
            else if (contentLength <= downloadLength) return TYPE_SUCCESS;      // 表示下载成功
            // 使用OkHttpClient发送一个GET请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadLength + "-")    // 断点下载
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            // 获取InputStream，如果response.body()为null，Object.requireNonNull()会抛出异常
            is = Objects.requireNonNull(response.body()).byteStream();
            // 由File对象创建一个RandomAccessFile对象，采用读写模式
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(downloadLength);     // 设置偏移量，实现断点下载
            byte[] b = new byte[1024];          // 每次读取1024字节
            int total = 0;  // 读取总字节数
            int len;        // 单次读取字节数
            /*      下载、写入文件中        */
            while ((len = is.read(b)) != -1) {
                if (isCanceled) {               // 下载过程中，任务被取消了
                    return TYPE_CANCELED;
                } else if (isPaused) {          // 下载过程中，任务被暂停了
                    return TYPE_PAUSED;
                } else {
                    total += len;
                    savedFile.write(b, 0, len);
                    // 计算已下载的百分比
                    int progress = (int) ((total + downloadLength) * 100 / contentLength);
                    publishProgress(progress);
                }
            }
            Objects.requireNonNull(response.body()).close();
            return TYPE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                // 如果写入过程中任务被取消了，就删除已经下载的部分
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果下载过程中抛出异常，就表示下载失败
        return TYPE_FAILED;
    }

    // 更新下载进度
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {      // 如果下载进度百分比发生变化就调用onProgress()方法更新UI，否则就不更新
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    // 处理下载结果
    @Override
    public void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    // 由给定的url获取所需下载的文件的大小
    public long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccessful()) {
            long contentLength = Objects.requireNonNull(response.body()).contentLength();
            response.close();
            return contentLength;
        }
        return 0;       // 失败了就返回0
    }
}
