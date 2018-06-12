package com.pm.player.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pm.player.MainActivity;
import com.pm.player.R;
import com.pm.player.adapter.DownLoadListAdapter;
import com.pm.player.base.BaseActivity;
import com.pm.player.download.DownLoadService;
import com.pm.player.entity.FileInfo;
import com.pm.player.utils.Constans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/6/12.
 */

public class DownLoadActivity extends BaseActivity {
    private ListView mList ;
    private List<FileInfo> mFileList = null;
    private DownLoadListAdapter mAdapter;
    private UIRecive mRecive;
    @Override
    protected void findViews() {
        mList = this.findViewById(R.id.lv_download);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initViews() {
        mFileList=new ArrayList<>();
        //初始化文件对象
        FileInfo fileInfo1 = new FileInfo(0, Constans.urlone, getfileName(Constans.urlone), 0, 0);
        FileInfo fileInfo2 = new FileInfo(1, Constans.urltwo, getfileName(Constans.urltwo), 0, 0);
        FileInfo fileInfo3 = new FileInfo(2, Constans.urlthree, getfileName(Constans.urlthree), 0, 0);
        FileInfo fileInfo4 = new FileInfo(3, Constans.urlfour, getfileName(Constans.urlfour), 0, 0);
        mFileList.add(fileInfo1);
        mFileList.add(fileInfo2);
        mFileList.add(fileInfo3);
        mFileList.add(fileInfo4);

        mAdapter = new DownLoadListAdapter(mFileList,this);
        mList.setAdapter(mAdapter);
        mRecive = new UIRecive();

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownLoadService.ACTION_START);
        filter.addAction(DownLoadService.ACTION_FINISHED);
        filter.addAction(DownLoadService.ACTION_UPDATE);
        registerReceiver(mRecive,filter);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_layout_download;
    }

    @Override
    protected void handlerMyMessage(Message msg) {

    }
    //从downloadTask中获取下载信息，更新UI
    private class UIRecive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownLoadService.ACTION_START.equals(intent.getAction())) {

            } else if (DownLoadService.ACTION_FINISHED.equals(intent.getAction())) {
                //下载结束的时候
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                mAdapter.updateProress(fileInfo.getId(),0);
                Toast.makeText(DownLoadActivity.this, mFileList.get(fileInfo.getId()).getFileName() + "下载完毕", Toast.LENGTH_SHORT).show();
            } else if (DownLoadService.ACTION_UPDATE.equals(intent.getAction())) {
                //更新进度条的时候
                int finished = intent.getIntExtra("finished",0);
                int id = intent.getIntExtra("id",0);
                mAdapter.updateProress(id,finished);
            }


        }
    }
    //从URL中获取下载文件名，即/后面的字符
    private String getfileName(String url) {

        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRecive);
    }
}
