package com.pm.player;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.pm.player.base.PlayerBaseActivity;
import com.pm.player.utils.Constans;
import com.pm.player.view.DownLoadActivity;
import com.pm.player.widget.MediaScannerTask;
import io.vov.vitamio.MediaPlayer;

public class MainActivity extends PlayerBaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        View.OnClickListener {
    private Button btnTest;

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

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        MediaScannerTask task =new MediaScannerTask();
        task.execute();
        mVideoView.setVideoURI(Uri.parse(Constans.url1));
//        mVideoView.setVideoURI(Uri.parse("storage/emulated/0/update/测试.mp4"));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                Intent intent = new Intent(MainActivity.this, DownLoadActivity.class);
                startActivity(intent);
        }
    }
}
