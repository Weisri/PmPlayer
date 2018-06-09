package com.pm.player.base;
import android.content.res.Configuration;
import android.os.Message;
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

public class PlayerBaseActivity extends BaseActivity {
    public VideoView mVideoView;
    public MediaController mMediaController;

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
        //视频质量
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * /**
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
    protected void handlerMyMessage(Message msg) {

    }
}
