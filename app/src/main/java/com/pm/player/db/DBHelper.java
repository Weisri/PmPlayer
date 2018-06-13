package com.pm.player.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WeiSir on 2018/6/9.
 */

public class DBHelper extends SQLiteOpenHelper {
    /**-----------------------thread_info--------------------------------- ----------*/
   //        ID          THREAD_ID      URL        START      END   FINISHED
   //------  ----------  ----------  ----------  --------------------------
    /**----------------------------------------------------------------------*/
    // 表名
    private static final String DB_NAME = "thread.db";
    // 版本号
    private static final int VERSION = 1;
    // 建表语句
    private static final String SQL_CREATE =
            "create table thread_info(" +
            "_id integer primary key autoincrement,"+
            "thread_id integer," +
            "url text," +
            "start integer," +
            "end integer," +
            "finished integer)";
    private static final String SQL_DROP = "drop table if exists thread_info";



    private DBHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    private static DBHelper sHelper = null;

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static DBHelper getInstanceDBHelper(Context context)
    {
        //提高效率
        if(sHelper == null)
        {
            //同步锁
            synchronized (DBHelper.class)
            {
                if(sHelper == null)
                    sHelper = new DBHelper(context);
            }
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 建表
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
