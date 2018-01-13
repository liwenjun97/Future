package com.example.cslab.future.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.util.AttrEntity;
import com.example.cslab.future.util.CircleLayout;
import com.example.cslab.future.util.GlobalData;
import com.example.cslab.future.util.SysApplication;

/**
 * Created by CSLab on 2017/7/20.
 */

public class ThirdPlanActivity extends AppCompatActivity {
    private String centerTitle;
    private String[] aroundCircleTitleCn;
    private int[] circleIcon;
    private int[] circleCompleteStatusList;
    private int aroundCircleCount;
    private int[] aroundSmallCircleIndex ;
    private Button cancel;
    private Button back;
    private TextView plan_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_plan);
        SysApplication.getInstance().addActivity(this);
        cancel=(Button)findViewById(R.id.cancel);
        back=(Button)findViewById(R.id.center_button);
        plan_title=(TextView) findViewById(R.id.plan_title);

        Intent it=getIntent();
        plan_title.setText(it.getStringExtra("Title"));
        plan_title.setTextColor(Color.RED);
        centerTitle=it.getStringExtra("Title").substring(it.getStringExtra("Title").lastIndexOf("-")+1);
        aroundCircleTitleCn=(String []) GlobalData.L2data.get(centerTitle).get("aroundCircleTitleCn");
        circleIcon=(int []) GlobalData.L2data.get(centerTitle).get("circleIcon");
        circleCompleteStatusList=(int []) GlobalData.L2data.get(centerTitle).get("circleCompleteStatusList");
        aroundCircleCount=((int[]) GlobalData.L2data.get(centerTitle).get("circleIcon")).length;
        aroundSmallCircleIndex=(int []) GlobalData.L2data.get(centerTitle).get("aroundSmallCircleIndex");

        final CircleLayout circleLayout = (CircleLayout) findViewById(R.id.circle_layout);
        circleLayout.setView(centerTitle,aroundCircleTitleCn, circleIcon, aroundCircleCount, circleCompleteStatusList,aroundSmallCircleIndex);
        circleLayout.setProgressNum(0);
        circleLayout.initView();
        circleLayout.addAround();
        Animation animation = AnimationUtils.loadAnimation(ThirdPlanActivity.this, R.anim.viewtoshowanim);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(0.3f);
        circleLayout.setLayoutAnimation(lac);
        circleLayout.startAnim(360f * 0 / 1);

        circleLayout.setOnClickListener(new CircleLayout.circleClickListener() {
            @Override
            public void click(int tag) {
                if(circleCompleteStatusList[tag-1]== AttrEntity.DOING){

                }else{
//                    aroundSmallCircleIndex[tag-1]+=1;
                    circleLayout.addAround();
                    Intent it=new Intent(ThirdPlanActivity.this,PlanActivity.class);
                    it.putExtra("Title",plan_title.getText()+"-"+aroundCircleTitleCn[tag-1]);
                    startActivity(it);
                    finish();
                    PlanActivity.instance.finish();
                    SecondPlanActivity.instance.finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            }
        });


//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
//            }
//        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        return true;
    }
}
