package com.example.cslab.future.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.Plan;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.sql.PlanSql;
import com.example.cslab.future.util.AttrEntity;
import com.example.cslab.future.util.CircleLayout;
import com.example.cslab.future.util.CustomDialog;
import com.example.cslab.future.util.GlobalData;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.SysApplication;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/18.
 */

public class PlanActivity extends AppCompatActivity {
    public static PlanActivity instance;
    private String centerTitle = "制定计划";
    private String[] aroundCircleTitleCn = {"生活", "娱乐", "学习", "健康"};
    private int[] circleIcon = {R.drawable.ic_life, R.drawable.ic_entertainment, R.drawable.ic_study, R.drawable.ic_health};
    private int[] circleCompleteStatusList = {AttrEntity.DOING,
            AttrEntity.DOING,
            AttrEntity.DOING,
            AttrEntity.DOING};

    private int aroundCircleCount = 4;
    private int[] aroundSmallCircleIndex = {0, 0, 0, 0};
    private Button cancel;
    private Button submit_plan;
    private TextView plan_title;
    private String plan = null;
    private CustomDialog dialog;
    private boolean hasMade=false;
    private boolean hasSubmit=false;
    private CircleLayout circleLayout;
    private Plan latestPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalData.init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_plan);
        circleLayout = (CircleLayout) findViewById(R.id.circle_layout);
        circleLayout.setView(centerTitle, aroundCircleTitleCn, circleIcon, aroundCircleCount, circleCompleteStatusList, aroundSmallCircleIndex);
        circleLayout.setProgressNum(1);
        circleLayout.initView();
        circleLayout.addAround();
        Animation animation = AnimationUtils.loadAnimation(PlanActivity.this, R.anim.viewtoshowanim);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(0.3f);
        circleLayout.setLayoutAnimation(lac);
        View popview = getLayoutInflater().inflate(R.layout.plan_mark, null);
        dialog = new CustomDialog(this);
        instance=this;
        SysApplication.getInstance().addActivity(this);
        PlanSql.init(this);
        latestPlan=PlanSql.queryFirst();
        PlanSql.close();

        cancel = (Button) findViewById(R.id.cancel);
        submit_plan = (Button) findViewById(R.id.center_button);
        plan_title = (TextView) findViewById(R.id.plan_title);
        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        final int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        int second=c.get(Calendar.SECOND);
        String nowTime=year+"-"+month+"-"+day;
        if(hour<21){
            //没到制定计划时间
            submit_plan.setClickable(false);
            centerTitle="时间未到";
            circleLayout.setCenterTitle(centerTitle);
        }
        if(latestPlan!=null&&nowTime.equals(latestPlan.getuTime().substring(0,10))){
            //已制定计划
            hasMade=true;
            centerTitle="已制定";
            plan_title.setText(latestPlan.getuType());
            plan_title.setTextColor(Color.RED);
            submit_plan.setClickable(false);
        }

//        if(plan!=null){
//            circleLayout.startAnim(360f * 1/1);
//            centerTitle="已提交";
//            circleLayout.setCenterTitle(centerTitle);
//            plan_title.setText(plan);
//            plan_title.setTextColor(Color.RED);
//            submit_plan.setVisibility(View.GONE);
//        }
        final Intent it = getIntent();
        if (it.getExtras() != null) {
            //获取第三层的计划。
            plan = it.getStringExtra("Title");
            centerTitle = "确定";
            circleLayout.setCenterTitle(centerTitle);
            plan_title.setText(plan);
            plan_title.setTextColor(Color.RED);
            submit_plan.setClickable(false);
        }
        circleLayout.setOnClickListener(new CircleLayout.circleClickListener() {
            @Override
            public void click(final int tag) {
                if(hasMade){

                }else{
                    if(hour<21){
                        //没到制定计划时间
                        new SweetAlertDialog(PlanActivity.this,SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING")
                                .setContentText("只有21:00到24:00之间才可以制定计划")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                }).show();

                    }else{
                        if(plan!=null){
                            //制定了计划，想要放弃
                            new SweetAlertDialog(PlanActivity.this,SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("WARNING")
                                    .setContentText("是否放弃已选择的计划?")
                                    .setCancelText("取消")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            plan=null;
                                            plan_title.setText("制定计划");
                                            sweetAlertDialog.dismissWithAnimation();

                                        }
                                    }).show();
                        }else{
                            //进入下一层
                            Intent it = new Intent(PlanActivity.this, SecondPlanActivity.class);
                            it.putExtra("Title", aroundCircleTitleCn[tag - 1]);
                            startActivity(it);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                }
            }
        });

        submit_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasMade){
                    //已制定计划
                    Toast.makeText(PlanActivity.this,"你已经制定过计划了",Toast.LENGTH_LONG).show();
                }else{
                    if(hour<21){
                        //没到制定计划时间
                        centerTitle="时间未到";
                        circleLayout.setCenterTitle(centerTitle);
                    }else {
                            if(plan==null){
                                new SweetAlertDialog(PlanActivity.this,SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("WARNING")
                                        .setContentText("你还没有选择计划")
                                        .setConfirmText("好的")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        }).show();
                            }else{
                                dialog.show();
                                dialog.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(PlanActivity.this, dialog.getText(), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                            //            circleLayout.startAnim(360f * 1 / 1);
                                        String type = plan;
                                        String remark = dialog.getText();
                                        makePlan(User.uToken,type,remark);
                                    }
                                });
                            }
                        //可以提交
                      //  submit_plan.setClickable(false);

                    }


                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(plan!=null&&hasSubmit==false){
                    new SweetAlertDialog(PlanActivity.this,SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("NOTIFICATION")
                            .setContentText("你还没有提交，是否继续推出？")
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmText("离开")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    Intent it=new Intent(PlanActivity.this,MainActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            }).show();
                }else{
                    Intent it=new Intent(PlanActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();
                }
//                SysApplication.getInstance().exit();
//                SysApplication.getInstance().exit();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(plan!=null&&hasSubmit==false){
                new SweetAlertDialog(PlanActivity.this,SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("NOTIFICATION")
                        .setContentText("你还没有提交，是否继续推出？")
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmText("离开")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Intent it=new Intent(PlanActivity.this,MainActivity.class);
                                startActivity(it);
                                finish();
                            }
                        }).show();
            }else{
                Intent it=new Intent(PlanActivity.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        }
        return true;
    }


    public void makePlan(String token,String type,String remark){
        final String url = "http://192.168.56.1/Home/TodayApi/makePlan";
        try {
            final String data = "token="+ URLEncoder.encode(token, "UTF-8")+
                                "&type="+ URLEncoder.encode(type, "UTF-8")+
                                "&remark="+ URLEncoder.encode(remark, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    Log.i("data",data);
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForMakePlan.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForMakePlan =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("success")!=""){
                    Log.i("22",json.optString("success"));
                    JSONArray jsonArray =  new JSONArray(json.optString("todayPlan"));
                    JSONObject j = (JSONObject)jsonArray.get(0);
                    PlanSql.init(PlanActivity.this);
                    PlanSql.insert(0,User.getuId(),plan,j.optInt("id"),dialog.getText(),0,j.optString("uTime"));
                    PlanSql.close();


                    new SweetAlertDialog(PlanActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(json.optString("success"))
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    centerTitle = "已提交";
                                    circleLayout.setCenterTitle(centerTitle);
                                    Intent it=new Intent(PlanActivity.this,MainActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            })
                            .show();
                    hasSubmit=true;

                }else {
                    Log.i("22",json.optString("error"));
                    new SweetAlertDialog(PlanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent it=new Intent(PlanActivity.this,MainActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };

}
