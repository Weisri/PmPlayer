package com.pm.player.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by WeiSir on 2018/6/9.
 */

public class DownLoadService extends Service {
    //文件的保存路径
    public static final String DownLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/download";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
