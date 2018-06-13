package com.pm.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Toast;
import com.pm.player.base.PlayerBaseActivity;
import com.pm.player.utils.Constans;
import com.pm.player.view.DownLoadActivity;
import com.pm.player.widget.MediaScannerTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;

public class MainActivity extends PlayerBaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        View.OnClickListener {
    public static final String ACTION_LOCAL_FILE = "ACTION_LOCAL_FILE";
    private PathRecive mRrcive;
    private Button btnTest;
    private List<String> videoPaths = new ArrayList<>();
    //播放第几个视频
    private static int pos = 1;

    @Override
    protected void findViews() {
        super.findViews();
        btnTest =this.findViewById(R.id.btn_test);
        btnTest.setOnClickListener(this);
    }

    @Override
    protected void initViews(){
        super.initViews();
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mRrcive=new PathRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOCAL_FILE);
        registerReceiver(mRrcive,filter);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //扫描本地视频
        MediaScannerTask task =new MediaScannerTask(this);
        task.execute();
        File video=new File("/storage/emulated/0/DCIM/video"+pos+".mp4");
//        mVideoView.setVideoURI(Uri.parse(Constans.url8));
        if (video.exists()) {
            mVideoView.setVideoPath(video.getAbsolutePath());
//        mVideoView.setVideoURI(Uri.parse("storage/emulated/0/update/测试.mp4"));
            mVideoView.start();

        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this,"开始播放",Toast.LENGTH_SHORT).show();

    }

    /**
     * 视频播放完成后的处理
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
            pos+=1; //将视频地址变为下一个
        File video2 = new File("/storage/emulated/0/DCIM/video"+pos+".mp4");
        if (video2.exists()) {
            try {
                mVideoView.setVideoPath(video2.getAbsolutePath());
                mVideoView.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"播放完成",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this,"播放错误",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                Intent intent = new Intent(MainActivity.this, DownLoadActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class PathRecive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_LOCAL_FILE.equals(intent.getAction())) {
            videoPaths= intent.getStringArrayListExtra("videoPaths");
                Log.e("MainActivity", "onReceive: 本地路劲"+videoPaths );
            }
        }
    }
}
