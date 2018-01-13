package com.example.cslab.future.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.VideoView;

/**
 * Created by CSLab on 2017/7/15.
 */

public class CustomVideoView extends VideoView {
    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        //重新计算高度
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l){
        super.setOnPreparedListener(l);
    }

    public boolean onKeyDown(int keycode, KeyEvent event){
        return super.onKeyDown(keycode,event);
    }
}

