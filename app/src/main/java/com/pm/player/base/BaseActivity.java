package com.pm.player.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

/**
 * Created by WeiSir on 2018/6/7.
 */

public abstract class BaseActivity extends Activity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            handlerMyMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        findViews();
        initViews();
        initDatas();

    }

    protected abstract void findViews() ;

    protected abstract void initDatas();

    protected abstract void initViews();

    protected abstract int setLayoutId();

    protected abstract void handlerMyMessage(Message msg);


}
