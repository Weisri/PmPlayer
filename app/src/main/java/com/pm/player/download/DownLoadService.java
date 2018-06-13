package com.pm.player.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pm.player.MainActivity;
import com.pm.player.entity.FileInfo;
import com.pm.player.utils.Constans;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

/**
 * Created by WeiSir on 2018/6/9.
 */

public class DownLoadService extends Service {

    public static final String ACTION_START = "ACTION_START";//开始下载
    public static final String ACTION_STOP = "ACTION_STOP";//暂停
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";

    public static final int MSG_INIT =0;

    private Map<Integer,DownLoadTask> mTasks = new LinkedHashMap<Integer, DownLoadTask>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得activity传过来的参数
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            Log.i("Services", "onStartCommand: start"+fileInfo.toString());
            InitThread initThread =new InitThread(fileInfo);
            DownLoadTask.sExecutorService.execute(initThread);
        }
        if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            DownLoadTask task = mTasks.get(fileInfo.getId());
            if (task != null) {
                //停止下载任务
                task.isPause=true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 接收initThread线程中的FileInfo信息,然后开始下载
     */
       Handler mHandler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_INIT:
                    FileInfo fileInfo =(FileInfo)msg.obj;
                    Log.i("===", "handleMessage:init "+fileInfo.toString());
                    //获取FileInfo对象 开始下载任务
                    DownLoadTask task = new DownLoadTask(DownLoadService.this,fileInfo,3);
                    task.downLoad();
                    //把下载任务添加到集合中
                    mTasks.put(fileInfo.getId(),task);
                    //发送启动下载的通知
                    Intent intent= new Intent(ACTION_START);
                    intent.putExtra("fileinfo",fileInfo);
                    sendBroadcast(intent);
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //初始化下载线程，获取下载文件信息
    private class InitThread extends Thread{
        private FileInfo mFileInfo = null;
        public InitThread(FileInfo fileInfo) {
            super();
            this.mFileInfo=fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf =null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                int length = -1;
                if (code == HttpURLConnection.HTTP_OK) {
                    length = conn.getContentLength();
                }
                //如果文件长度小于零，则获取文件失败
                if (length<=0) {
                    return;
                }
                File dir = new File(Constans.DownLoadPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                //创建本地下载文件
                File file =new File(dir,mFileInfo.getFileName());
                raf=new RandomAccessFile(file,"rwd");
                raf.setLength(length);
                //设置文件长度
                mFileInfo.setLength(length);
                //将fileinfo对象传递给handler
                Message msg = Message.obtain();
                msg.obj = mFileInfo;
                msg.what = MSG_INIT;
                mHandler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            super.run();
        }
    }
}
