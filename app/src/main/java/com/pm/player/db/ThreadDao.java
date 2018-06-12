package com.pm.player.db;


import com.pm.player.entity.ThreadInfo;

import java.util.List;

/**
 * The interface Thread dao.
 * 线程数据库增删改查
 */
public interface ThreadDao  {
    /**
     * 添加线程
     */
     void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程
     */
     void deleteThread(String url);

    /**
     * 更新线程
     */
     void updateThread(String url,int thread_id,int finished);

    /**
     * 查询线程
     */
     List<ThreadInfo> queryThread(String url);

    /**
     * 是否退出 @return the boolean
     */
     boolean isExit(String url,int threadId);
}
