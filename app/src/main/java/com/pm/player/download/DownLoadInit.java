package com.pm.player.download;

import android.content.Context;

import com.tamic.rx.fastdown.core.RxDownloadManager;

/**
 * Created by WeiSir on 2018/6/8.
 */

public final class DownLoadInit {
    private DownLoadInit() {
    }
    public static void init(Context aContext){
        RxDownloadManager manager = RxDownloadManager.getInstance();
//        manager.init(aContext,new DownLoadAdapter());
    }
}
