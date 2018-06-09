package com.pm.player;

import android.net.Uri;
import android.widget.Toast;
import com.pm.player.base.PlayerBaseActivity;
import com.pm.player.widget.MediaScannerTask;
import io.vov.vitamio.MediaPlayer;

public class MainActivity extends PlayerBaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {


    @Override
    protected void initViews(){
        super.initViews();
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        MediaScannerTask task =new MediaScannerTask();
        task.execute();
        mVideoView.setVideoURI(Uri.parse("storage/emulated/0/update/测试.mp4"));
        mVideoView.start();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this,"开始播放",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this,"播放完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this,"播放错误",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
    }


}
