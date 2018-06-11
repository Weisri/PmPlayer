package com.pm.player.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pm.player.entity.FileInfo;

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
    //文件的保存路径
    public static final String DownLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/download";
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //初始化下载线程，获取下载文件信息
    private class InitThread extends Thread{
        public InitThread(FileInfo fileInfo) {


        }

        @Override
        public void run() {
            super.run();
        }
    }
}
