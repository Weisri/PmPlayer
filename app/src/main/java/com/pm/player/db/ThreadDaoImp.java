package com.pm.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pm.player.entity.ThreadInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/6/9.
 */

public class ThreadDaoImp implements ThreadDao {
    private DBHelper mHelper = null;


    public ThreadDaoImp(Context context) {
        super();
        this.mHelper=DBHelper.getInstanceDBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("thread_id",threadInfo.getId());
        values.put("url",threadInfo.getUrl());
        values.put("start",threadInfo.getStart());
        values.put("end",threadInfo.getEnd());
        values.put("finished",threadInfo.getFinished());
        db.insert("thread_info",null,values);
        db.close();
    }

    @Override
    public void deleteThread(String url) {
         SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete("thread_info","url=?",new String[]{url});
        db.close();
    }

    @Override
    public void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.execSQL("update thread_info set finished = '?' where url= '?' and thread_id = '?'",new Object[]{finished,url,thread_id});

    }

    @Override
    public List<ThreadInfo> queryThread(String url) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        Cursor cursor = db.query("thread_info",null,"url=?",new String[]{url},null,null,null);
        while (cursor.moveToNext()) {
            ThreadInfo thread = new ThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            thread.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(thread);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExit(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("thread_info", null, "url=? and thread_id = ?", new String[]{url,thread_id+""},null,null,null);
        boolean exits = cursor.moveToNext();
        db.close();
        cursor.close();
        return exits;
    }
}

