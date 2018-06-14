package com.pm.player.base;
import android.content.res.Configuration;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.pm.player.R;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by WeiSir on 2018/6/7.
 */

public class PlayerBaseActivity extends BaseActivity implements MediaPlayer.OnInfoListener,MediaPlayer.OnBufferingUpdateListener {

    private final static String TAG = "PlayerBaseActivity";
    public VideoView mVideoView;
    public MediaController mMediaController;
    //是否需要自动恢复播放，用于自动暂停，恢复播放
    private boolean needResume  =true;

    @Override
    protected void findViews() {
        mVideoView = this.findViewById(R.id.vv_main);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initViews() {
        Vitamio.isInitialized(getApplicationContext());
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = PlayerBaseActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        mMediaController=new MediaController(this);
        mMediaController.show(2000);
        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();
        //视频质量
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.setOnInfoListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓冲 暂停播放
                needResume = true;
                mp.pause();
                Log.i(TAG, "onInfo:缓冲..... ：");

                // TODO: 2018/6/14 显示正在缓冲的进度条
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓冲结束，继续播放
                if (needResume) {
                    mp.start();
                }
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                //显示下载网速
                Log.i(TAG, "onInfo:当前网速 ："+extra+"kb/s");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // TODO: 2018/6/14 更新缓冲进度条
    }

    @Override
    protected void handlerMyMessage(Message msg) {

    }


    /**
     * 参数layout(缩放参数)参见MediaPlayer的常量：VIDEO_LAYOUT_ORIGIN(原始大小)、VIDEO_LAYOUT_SCALE(画面全屏)、
     * VIDEO_LAYOUT_STRETCH(画面拉伸)、VIDEO_LAYOUT_ZOOM(画面裁剪)、VIDEO_LAYOUT_FIT_PARENT(画面铺满)
     *  参数aspectRation(宽高比)，为0将自动检测
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView!=null){
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE,0);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
