package com.pm.player.widget;

import com.pm.player.entity.VideoInfo;

import java.util.List;

/**
 * Created by WeiSir on 2018/6/15.
 */

public interface ScanerAsynResponse {
    void onDataRecivedSuccess(List<VideoInfo> infoList);
}
