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
import com.pm.player.entity.VideoInfo;
import com.pm.player.utils.Constans;
import com.pm.player.view.DownLoadActivity;
import com.pm.player.widget.MediaScannerTask;
import com.pm.player.widget.ScanerAsynResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;

public class MainActivity extends PlayerBaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        View.OnClickListener,
        ScanerAsynResponse {
    private static final String TAG = "MainActivity";
    public static final String ACTION_LOCAL_FILE = "ACTION_LOCAL_FILE";
    private PathRecive mRrcive;
    private Button btnTest;
    private ArrayList<String> videoPaths = new ArrayList<>();
    private List<VideoInfo> infos = null;
    private String path;
    //播放第几个视频
    private int flag = 0;

    @Override
    protected void findViews() {
        super.findViews();
        btnTest = this.findViewById(R.id.btn_test);
        btnTest.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        //注册广播
        mRrcive = new PathRecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOCAL_FILE);
        registerReceiver(mRrcive, filter);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //扫描本地视频
        infos = new ArrayList<>();
        MediaScannerTask task = new MediaScannerTask(this);
        task.setOnAsynResponse(this);
        task.execute();
        playVideo();
    }

    private void playVideo() {
        Log.e(TAG, "playVideo: startPlay" + path);
        if (videoPaths.size() != 0) {
            path = videoPaths.get(flag);
            Log.e("====233", "playVideo: "+path);
            mVideoView.setVideoURI(Uri.parse(path));
//            mVideoView.setVideoURI(Uri.parse("storage/emulated/0/DCIM/Video/V80218-110005.mp4"));
            mVideoView.requestFocus();
            mVideoView.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (videoPaths.get(flag).startsWith("http")) {
            //网络缓冲
            mVideoView.setBufferSize(1024 * 1000);
        } else {
            //本地直接播放
            mVideoView.setBufferSize(0);
        }
//        mp.start();
//        //设置轮循播放
//        mp.setLooping(true);
        Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();
    }

    /**
     * 视频播放完成后的处理
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        flag += 1;
        if (videoPaths.size() > 0 && flag < videoPaths.size()) {
            try {
                mVideoView.setVideoURI(Uri.parse(videoPaths.get(flag)));
                mVideoView.start();
                Log.i(TAG, "onCompletion: 轮循播放开始");
            } catch (Exception e) {
                e.printStackTrace();
            }
            playVideo();
        } else if (flag == infos.size()) {
            flag=0;
            playVideo();
            Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
        }
        mVideoView.stopPlayback();
        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "播放错误", Toast.LENGTH_SHORT).show();
        return false;
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

    @Override
    public void onDataRecivedSuccess(List<VideoInfo> infoList) {
        Log.e("===", "onDataRecivedSuccess: "+infoList.size() );
        Log.d(TAG, "onDataRecivedSuccess");
        infos = infoList;
    }


    /**
     * 接收扫描到的本地视频路径
     */
    private class PathRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_LOCAL_FILE.equals(intent.getAction())) {
                videoPaths = intent.getStringArrayListExtra("videoPaths");
               playVideo();
                Log.e("MainActivity", "onReceive: 本地路劲" + videoPaths);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView.setVisibility(View.GONE);
        }
        super.onDestroy();
        unregisterReceiver(mRrcive);
    }
}
