package com.pm.player.download;

import android.content.Context;
import android.content.pm.FeatureInfo;

import com.pm.player.db.ThreadDao;
import com.pm.player.db.ThreadDaoImp;
import com.pm.player.entity.FileInfo;
import com.pm.player.entity.ThreadInfo;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by WeiSir on 2018/6/9.
 */

public class DownLoadTask {
    private Context mContext=null;
    private FileInfo mFileInfo=null;
    private ThreadDao mDao=null;
    //下载线程结束位置
    private int mFinished =0;
    //下载的线程数
    private int mThreadCount=1;
    //是否暂停
    private boolean isPause = false;
    private List<DownLoadThread> mThreadList = null;
    public static ExecutorService sExecutorService = Executors.newCachedThreadPool();

    public DownLoadTask(Context mContext, FileInfo mFileInfo, int mThreadCount) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadCount = mThreadCount;
        this.mDao = new ThreadDaoImp(mContext);
    }

    /**
     * 下载
     */
    public void downLoad(){
        //从数据库获取下载的信息
        List<ThreadInfo> list = mDao.queryThread(mFileInfo.getUrl());
        if (list.size()==0) {
            int length = mFileInfo.getLength();
            int block = length/mThreadCount;
            for (int i =0;i<mThreadCount;i++) {
                //划分每个线程开始下载和结束下载的位置
                int start = i*block;
                int end =  (i+1)* block -1;
                if (i == mThreadCount - 1) {
                    end= length-1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i,mFileInfo.getUrl(),start,end,0 );
                list.add(threadInfo);
            }
        }
        mThreadList = new ArrayList<DownLoadThread>();

    }

    private class DownLoadThread extends Thread{
        private ThreadInfo threadInfo =null;
        //表示线程是否执行完毕
        public  boolean isFinished= false;

        public DownLoadThread(ThreadInfo threadInfo){
            this.threadInfo=threadInfo;
    }
        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream mIput = null;
            try {
                URL url= new URL(mFileInfo.getUrl());
                conn=(HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5*1000);
                conn.setRequestMethod("GET");
                int start = threadInfo.getStart() + threadInfo.getFinished();
                conn.setRequestProperty("Range","bytes=" + start + "-"+threadInfo.getEnd());
                File file = new File(DownLoadService.DownLoadPath,mFileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);
                mFinished+=threadInfo.getFinished();
                




            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    }
}
