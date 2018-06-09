package com.pm.player.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/6/7.
 * 扫描本地视频媒体库
 */
public class MediaScannerTask extends AsyncTask<Void,Integer,List<VideoInfo>> {

    private List<VideoInfo> videoInfos =new ArrayList<VideoInfo>();

    @Override
    protected List<VideoInfo> doInBackground(Void... voids) {
        videoInfos=getVieoFile(videoInfos, Environment.getExternalStorageDirectory());
        return videoInfos;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<VideoInfo> videoInfos) {
        super.onPostExecute(videoInfos);
    }

    private List<VideoInfo> getVieoFile(final List<VideoInfo> list,  File file) {
            file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String name = pathname.getName();
                    int i =name.indexOf('.');
                    if (i!=-1) {
                        name=name.substring(i);
                        if (name.equalsIgnoreCase(".mp4")
                             || name.equalsIgnoreCase(".3gp")
                                || name.equalsIgnoreCase(".wmv")
                                || name.equalsIgnoreCase(".ts")
                                || name.equalsIgnoreCase(".rmvb")
                                || name.equalsIgnoreCase(".mov")
                                || name.equalsIgnoreCase(".m4v")
                                || name.equalsIgnoreCase(".avi")
                                || name.equalsIgnoreCase(".m3u8")
                                || name.equalsIgnoreCase(".3gpp")
                                || name.equalsIgnoreCase(".3gpp2")
                                || name.equalsIgnoreCase(".mkv")
                                || name.equalsIgnoreCase(".flv")
                                || name.equalsIgnoreCase(".divx")
                                || name.equalsIgnoreCase(".f4v")
                                || name.equalsIgnoreCase(".rm")
                                || name.equalsIgnoreCase(".asf")
                                || name.equalsIgnoreCase(".ram")
                                || name.equalsIgnoreCase(".mpg")
                                || name.equalsIgnoreCase(".v8")
                                || name.equalsIgnoreCase(".swf")
                                || name.equalsIgnoreCase(".m2v")
                                || name.equalsIgnoreCase(".asx")
                                || name.equalsIgnoreCase(".ra")
                                || name.equalsIgnoreCase(".ndivx")
                                || name.equalsIgnoreCase(".xvid")){
                            VideoInfo videoInfo =new VideoInfo();
                            pathname.getUsableSpace();
                            videoInfo.setDisplayName(pathname.getName());
                            videoInfo.setPath(pathname.getAbsolutePath());
                            Log.i("打印本地所有视频路径", "name"+videoInfo.getPath());
                            list.add(videoInfo);
                            return true;
                        }
                    }else if (pathname.isDirectory()){
                            getVieoFile(list,pathname);
                    }
                    return false;
                }
            });
            return list;
    }
}
