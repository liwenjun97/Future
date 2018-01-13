package com.example.cslab.future.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.adapter.Myadapter;
import com.example.cslab.future.entity.Comment;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.Dream;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.Screen;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/15.
 */

public class SquareActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static SquareActivity squareActivity = null;
    private Button outSquare;
    private ListView dreamList;
    private List<Map<String, Object>> allValues = new ArrayList<>();
    private Myadapter myAdapter;
    private String dirImg_square;
    private String dirImg_allHead;

    private SweetAlertDialog progressDialog;

    private String urlForGetNextThirtyDream;
    private String dataForGetNextThirtyDream;

    private SwipeRefreshLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.square);
        squareActivity = this;
        //下拉刷新
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(150);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.blue);
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
        //创建本地文件夹存放梦想图片
        dirImg_square = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_square/";
        Log.i("filepath",dirImg_square);
        File ImgDir = new File(dirImg_square);
        if (!ImgDir.exists()) {
            ImgDir.mkdirs();
        }
        //创建本地文件夹存放所有人的头像
        dirImg_allHead = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_allHead/";
        Log.i("filepath",dirImg_allHead);
        File ImgDir2 = new File(dirImg_allHead);
        if (!ImgDir2.exists()) {
            ImgDir2.mkdirs();
        }


        progressDialog = new SweetAlertDialog(SquareActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("WAIT")
                .setContentText("数据加载中...");
        progressDialog.show();

        String type = "uRegTime";

        getThirtyDream(User.uToken,type,0);

        dreamList = (ListView) findViewById(R.id.dreamList);
        outSquare = (Button) findViewById(R.id.outSquare);


        dreamList.setVerticalScrollBarEnabled(true);//不管有没有活动都隐藏
        dreamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        outSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dreamList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        dreamList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int itemCount, int totalItem) {

            }
        });
    }
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新

//                finish();
//                Intent it=new Intent(SquareActivity.this,SquareActivity.class);
//                startActivity(it);
                progressDialog = new SweetAlertDialog(SquareActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("WAIT")
                        .setContentText("数据加载中...");
                progressDialog.show();

                String type = "uRegTime";
                int uId = 0;
                if (User.dreams!=null){
                    uId = User.dreams[0].getuId();
                }
                onRefreshDream(User.uToken,type,uId);
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000); // 1秒后，停止刷新
    }


    public void onRefreshDream(String token,String type,int uId){
        urlForGetNextThirtyDream = "http://192.168.56.1/Home/TomorrowApi/getLatestDream";
        try {
            dataForGetNextThirtyDream = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&type="+URLEncoder.encode(type, "UTF-8")+
                    "&uId="+URLEncoder.encode(String.valueOf(uId), "UTF-8");
            Log.i("data",dataForGetNextThirtyDream);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetNextThirtyDream, dataForGetNextThirtyDream);

                    //获取图片到本地
                    try {
                        JSONTokener jsonParser = new JSONTokener(returnStr);
                        JSONObject json = (JSONObject) jsonParser.nextValue();
                        JSONArray jsonArray =  new JSONArray(json.optString("allDream"));
                        boolean returnFlag1;
                        boolean returnFlag2;
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            String urlForGetDreamPic =  j.optString("uDreamPic");
                            String saveForGetDream = dirImg_square + j.optString("uId") + ".jpg";       //保存到本地
                            returnFlag1 = PostUtils.saveUrlAs(urlForGetDreamPic, saveForGetDream);

                            String urlForGetHeadPic = j.optString("uImgSrc");
                            String saveForGetHead = dirImg_allHead + j.optString("uId") + ".jpg";
                            returnFlag2 = PostUtils.saveUrlAs(urlForGetHeadPic, saveForGetHead);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForonRefreshDream.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForonRefreshDream =new Handler(){
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
                progressDialog.cancel();
                if(json.optString("error")==""){
                    JSONArray jsonArray =  new JSONArray(json.optString("allDream"));

                    if (jsonArray.length()==0){
                        new SweetAlertDialog(SquareActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Info")
                                .setContentText("啊哦，没有了")
                                .setConfirmText("了解")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }else {
                        Dream[] dreams = new Dream[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            dreams[i] = new Dream();
                            dreams[i].setuId(j.optInt("uId"));
                            dreams[i].setuName(j.optString("uName"));
                            dreams[i].setuEmail(j.optString("uEmail"));
                            dreams[i].setuRegTime(j.optString("uRegTime"));
                            dreams[i].setuImgSrc(j.optString("uImgSrc"));
                            dreams[i].setuCity(j.optString("uCity"));
                            dreams[i].setuAge(j.optInt("uAge"));
                            dreams[i].setuBirth(j.optString("uBirth"));
                            dreams[i].setuSex(j.optInt("uSex"));
                            dreams[i].setuInterest(j.optString("uInterest"));
                            dreams[i].setuDream(j.optString("uDream"));
                            dreams[i].setuDreamPic(j.optString("uDreamPic"));
                            dreams[i].setDreamClickNum(j.optInt("dreamClickNum"));
                            dreams[i].setDreamCommentNum(j.optInt("dreamCommentNum"));
                        }
                        //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        //设置到本地并且缓存到本地数据库
//                        User.setDream(dreams);
                        User.addDreamAtTop(dreams);             //在数组上面添加数据


                        //获取成功渲染数据
                        if (User.dreams!=null){
                            for (int i = 0; i < dreams.length; i++) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("uId",User.dreams[i].uId);
                                map.put("dream_pic_path",dirImg_square + User.dreams[i].uId + ".jpg");
                                map.put("uDream",User.dreams[i].uDream);
                                map.put("commentNum", User.dreams[i].dreamCommentNum);
                                map.put("clickNum", User.dreams[i].dreamClickNum);
                                allValues.add(0,map);                   //最新的加在前面
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }

                }else {
                    new SweetAlertDialog(SquareActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it=new Intent(SquareActivity.this,LoginActivity.class);
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





    public void getThirtyDream(String token,String type,int uId){
        urlForGetNextThirtyDream = "http://192.168.56.1/Home/TomorrowApi/getNextThirtyDream";
        try {
            dataForGetNextThirtyDream = "token="+ URLEncoder.encode(token, "UTF-8")+
                                        "&type="+URLEncoder.encode(type, "UTF-8")+
                                        "&uId="+URLEncoder.encode(String.valueOf(uId), "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetNextThirtyDream, dataForGetNextThirtyDream);

                    //获取图片到本地
                    try {
                        JSONTokener jsonParser = new JSONTokener(returnStr);
                        JSONObject json = (JSONObject) jsonParser.nextValue();
                        JSONArray jsonArray =  new JSONArray(json.optString("allDream"));
                        boolean returnFlag1;
                        boolean returnFlag2;
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            String urlForGetDreamPic =  j.optString("uDreamPic");
                            String saveForGetDream = dirImg_square + j.optString("uId") + ".jpg";       //保存到本地
                            returnFlag1 = PostUtils.saveUrlAs(urlForGetDreamPic, saveForGetDream);

                            String urlForGetHeadPic = j.optString("uImgSrc");
                            String saveForGetHead = dirImg_allHead + j.optString("uId") + ".jpg";
                            returnFlag2 = PostUtils.saveUrlAs(urlForGetHeadPic, saveForGetHead);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetThirtyDream.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetThirtyDream =new Handler(){
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

                if(json.optString("error")==""){
                    JSONArray jsonArray =  new JSONArray(json.optString("allDream"));

                    if (jsonArray.length()==0){
                        new SweetAlertDialog(SquareActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Info")
                                .setContentText("啊哦，没有了")
                                .setConfirmText("了解")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }else {
                        Dream[] dreams = new Dream[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            dreams[i] = new Dream();
                            dreams[i].setuId(j.optInt("uId"));
                            dreams[i].setuName(j.optString("uName"));
                            dreams[i].setuEmail(j.optString("uEmail"));
                            dreams[i].setuRegTime(j.optString("uRegTime"));
                            dreams[i].setuImgSrc(j.optString("uImgSrc"));
                            dreams[i].setuCity(j.optString("uCity"));
                            dreams[i].setuAge(j.optInt("uAge"));
                            dreams[i].setuBirth(j.optString("uBirth"));
                            dreams[i].setuSex(j.optInt("uSex"));
                            dreams[i].setuInterest(j.optString("uInterest"));
                            dreams[i].setuDream(j.optString("uDream"));
                            dreams[i].setuDreamPic(j.optString("uDreamPic"));
                            dreams[i].setDreamClickNum(j.optInt("dreamClickNum"));
                            dreams[i].setDreamCommentNum(j.optInt("dreamCommentNum"));
                        }
                        //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        //设置到本地并且缓存到本地数据库
                        User.setDream(dreams);


                        //获取成功渲染数据
                        if (User.dreams!=null){
                            for (int i = 0; i < User.dreams.length; i++) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("uId",User.dreams[i].uId);
                                map.put("dream_pic_path",dirImg_square + User.dreams[i].uId + ".jpg");
                                map.put("uDream",User.dreams[i].uDream);
                                map.put("commentNum", User.dreams[i].dreamCommentNum);
                                map.put("clickNum", User.dreams[i].dreamClickNum);
                                allValues.add(map);
                            }
                            myAdapter = new Myadapter(SquareActivity.this, allValues);
                            dreamList.setAdapter(myAdapter);
                        }

                        progressDialog.cancel();
                    }

                }else {
                    new SweetAlertDialog(SquareActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it=new Intent(SquareActivity.this,LoginActivity.class);
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

    //返回主界面
    @Override
    public void onBackPressed()
    {
        finish();
    }
}