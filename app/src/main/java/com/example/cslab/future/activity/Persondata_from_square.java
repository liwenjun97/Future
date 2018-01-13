package com.example.cslab.future.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cslab.future.R;
import com.example.cslab.future.util.Screen;

import static org.feezu.liuli.timeselector.Utils.ScreenUtil.height;
import static org.feezu.liuli.timeselector.Utils.ScreenUtil.width;

/**
 * Created by CSLab on 2017/7/22.
 */

public class Persondata_from_square extends AppCompatActivity {
    int width,height,left,right,top,bottom;
    private  ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_datafromsquare);
        imageView =(ImageView)findViewById(R.id.head_picture2);
        Screen.init(this);
        width = (int) Screen.SCREEN_WIDTH/4;
        height = (int) Screen.SCREEN_HEIGHT/7;
        left = (int) Screen.SCREEN_WIDTH *3/8;
        top = (int) Screen.SCREEN_HEIGHT *7 /30;
        RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
        layout.setMargins(left,top,0,0);

        layout.width = width;
        layout.height = height;

        imageView.setLayoutParams(layout);
    }
    //返回主界面
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(Persondata_from_square.this,SquareActivity.class));
    }
}
