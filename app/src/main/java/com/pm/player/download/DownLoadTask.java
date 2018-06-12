package com.pm.player.download;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pm.player.db.ThreadDao;
import com.pm.player.db.ThreadDaoImp;
import com.pm.player.entity.FileInfo;
import com.pm.player.entity.ThreadInfo;

import java.io.File;
import java.io.IOException;
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
    private static final String TAG = "DownLoad";
    private Context mContext=null;
    private FileInfo mFileInfo=null;
    private ThreadDao mDao=null;
    //下载线程结束位置
    private int mFinished =0;
    //下载的线程数
    private int mThreadCount=3;
    //是否暂停
    public boolean isPause = false;
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
        for (ThreadInfo threadInfo : list) {
            DownLoadThread downLoadThread = new DownLoadThread(threadInfo);
            //使用线程池执行下载任务
            DownLoadTask.sExecutorService.execute(downLoadThread);
            mThreadList.add(downLoadThread);
            //如果数据库不存在下载信息，添加下载信息
            mDao.insertThread(threadInfo);
        }
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
                Intent intent =new Intent();
                intent.setAction(DownLoadService.ACTION_UPDATE);
                int code = conn.getResponseCode();
                if (code ==HttpURLConnection.HTTP_PARTIAL) {
                    mIput = conn.getInputStream();
                    byte[] bt = new byte[1024];
                    int leg =-1;
                    //UI刷新时间
                    long time = System.currentTimeMillis();
                    while ((leg = mIput.read(bt))!=-1) {
                        raf.write(bt,0,leg);
                        //累计整个文件完成进度
                        mFinished+=leg;
                        //累加每个线程完成的进度
                        threadInfo.setFinished(threadInfo.getFinished()+leg);
                        //设置为每500ms更新一次
                        if (System.currentTimeMillis()-time>1000) {
                            time = System.currentTimeMillis();
                            //发送已完成多少
                            intent.putExtra("finished",mFinished*100/mFileInfo.getLength());
                            //正在下载文件的id
                            intent.putExtra("id",mFileInfo.getId());
                            Log.i(TAG, "run:下载中："+mFinished*100/mFileInfo.getLength()+"");
                            //发送广播给activity
                            mContext.sendBroadcast(intent);
                        }
                        if (isPause) {
                            mDao.updateThread(threadInfo.getUrl(),threadInfo.getId(),threadInfo.getFinished());
                            return;
                        }
                    }
                }
                //表示线程是否执行完成
                isFinished = true;
                //判断所有线程是否执行完成
                checkAllFinished();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (mIput!=null){
                        mIput.close();
                    }
                    if (raf != null) {
                        raf.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }
    }

    /**
     * 判断所有线程是否线程是否执行完成
     */
    private synchronized void checkAllFinished() {
        boolean allFinished = true;
        for (DownLoadThread thread : mThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished==true){
            //下载完成后 删除数据库信息
            mDao.deleteThread(mFileInfo.getUrl());
            //通知ui 哪个线程完成下载
            Intent intent = new Intent(DownLoadService.ACTION_FINISHED);
            intent.putExtra("fileInfo",mFileInfo);
            mContext.sendBroadcast(intent);
        }

    }
}
