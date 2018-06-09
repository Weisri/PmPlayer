package com.pm.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by WeiSir on 2018/6/6.
 */

public class CustomController extends MediaController {

    public CustomController(Context context, boolean isFromXml, View container){
        super(context);
    }

    public CustomController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomController(Context context) {
        super(context);
    }
}
