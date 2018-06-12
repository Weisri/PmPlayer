package com.pm.player.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pm.player.R;
import com.pm.player.download.DownLoadService;
import com.pm.player.entity.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/6/12.
 * 下载列表适配器
 */
public class DownLoadListAdapter extends BaseAdapter{
    private List<FileInfo> mList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public DownLoadListAdapter(List<FileInfo> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler =null;
        final FileInfo fileInfo = mList.get(position);
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_layout_download,null);
            hodler=new ViewHodler();
            hodler.mFileText=(TextView)convertView.findViewById(R.id.file_textview);
            hodler.mProBar =(ProgressBar)convertView.findViewById(R.id.progressBar2);
            hodler.btStart=(Button)convertView.findViewById(R.id.start_button);
            hodler.btStop=(Button)convertView.findViewById(R.id.stop_button);
            hodler.mFileText.setText(fileInfo.getFileName());
            hodler.mProBar.setMax(100);
            hodler.btStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DownLoadService.class);
                    intent.setAction(DownLoadService.ACTION_STOP);
                    intent.putExtra("fileinfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            hodler.btStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,DownLoadService.class);
                    intent.setAction(DownLoadService.ACTION_START);
                    intent.putExtra("fileinfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            convertView.setTag(hodler);
        }else {
            hodler=(ViewHodler)convertView.getTag();
        }
        hodler.mProBar.setProgress(fileInfo.getFinished());

        return convertView;
    }
    public void updateProress(int id,int progress){
        FileInfo fileInfo = mList.get(id);
        fileInfo.setFinished(progress);
        notifyDataSetChanged();

    }
    static class ViewHodler{
        //文件名
        private TextView mFileText;
        //下载进度
        private ProgressBar mProBar;
        //开始
        private Button btStart;
        //停止
        private Button btStop;
    }
    }
