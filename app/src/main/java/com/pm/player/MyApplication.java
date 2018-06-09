package com.pm.player;

import android.app.Application;


/**
 * Created by WeiSir on 2018/6/8.
 */

public class MyApplication extends Application {
    private   static  MyApplication instaces;
    private DaoSession daoSession;

    public static MyApplication getInstaces(){
        return instaces;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
        initFastDownLoad();
    }

    private void initFastDownLoad() {

    }

    /**
     * 初始花greenDao
     */
    private void initGreenDao() {
//        DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(this,"download-db");
//        SQLiteDatabase db = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        daoSession =daoMaster.newSession();
    }

    /**
     * Gets dao session.
     *
     * @return the dao session
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
