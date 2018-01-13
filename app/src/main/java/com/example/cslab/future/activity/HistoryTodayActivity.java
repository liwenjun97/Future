package com.example.cslab.future.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.adapter.HistoryTodayAdapter;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.question.question1_activity;
import com.example.cslab.future.sql.PlanSql;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.Screen;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.logging.SimpleFormatter;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.cslab.future.R.id.date;
import static com.example.cslab.future.R.id.radio;

public class HistoryTodayActivity extends AppCompatActivity {
    private String urlForGetDiary;
    private String dataForGetDiary;
    private ListView list;
    private List<Map<String, Object>> allValues = new ArrayList<>();
    private HistoryTodayAdapter myAdapter;
    private int[] allImgs = new int[]{R.mipmap.art, R.mipmap.game, R.mipmap.movies, R.mipmap.music,
            R.mipmap.science, R.mipmap.sports,R.mipmap.travel,R.mipmap.personal,R.mipmap.food,R.mipmap.else1,R.mipmap.war,R.mipmap.history};
    private String[] allTxt = new String[]{"其他","其他","其他","其他","其他","其他","其他","个人","其他","其他","战争","历史"};
    private int [] like_star = new int[]{R.mipmap.star,R.mipmap.add_like_star};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screen.init(this);
        setContentView(R.layout.history_today);
//        list = (ListView) findViewById(R.id.list);
//        Random r = new Random();
//        for (int i = 0; i < 50; i++) {
//            int j = r.nextInt(allImgs.length);
//            Map<String, Object> map = new HashMap<>();
//            map.put("image", allImgs[j]);
//            map.put("title", allTxt[j]);
//            map.put("content", "这里是历史上的今天");
//            map.put("star", like_star[0]);
//            allValues.add(map);
//        }
//        myAdapter = new HistoryTodayAdapter(this, allValues);
//        list.setAdapter(myAdapter);
///////////////////////
        getOthers();
        getDiary(User.uToken);


//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//        });
//
//        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HistoryTodayActivity.this);
//                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//                alertDialogBuilder.setTitle("警告");
//                alertDialogBuilder.setMessage("是否删除这条推荐吗？");
//                alertDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                alertDialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        allValues.remove(position);
//                        myAdapter.notifyDataSetChanged();
//                    }
//                });
//                alertDialogBuilder.show();
//                return false;
//            }
//        });
//
//        list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int firtItem, int itemCount, int totalItem) {
//
//            }
//        });

        Button btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //返回主界面
    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void getDiary(String token){
        urlForGetDiary = "http://192.168.56.1/Home/LastYearTodayApi/showLastDiary";
        try {
            dataForGetDiary = "token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetDiary, dataForGetDiary);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForDiary.sendMessage(msg);
                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Handler handlerForDiary =new Handler(){
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
                Log.i("2343534564",json.optString("error"));
                list = (ListView) findViewById(R.id.list);
                if(json.optString("error")!="") {
                    Map<String, Object> map = new HashMap<>();
                    map.put("image", allImgs[7]);
                    map.put("title", allTxt[7]);
                    map.put("content", json.optString("error"));
                    map.put("star", like_star[0]);
                    allValues.add(map);
                }
                else{
                    //添加以前的日记
                    JSONArray jsonArray =  new JSONArray(json.optString("1"));
                    Log.i("111113345556",jsonArray.toString());
                    for(int i = 0 ; i<jsonArray.length();i++){
                        JSONObject json2=jsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();
                            map.put("image", allImgs[7]);
                            map.put("title",allTxt[7]);
                            map.put("star", like_star[0]);
                            map.put("content",json2.optString("uDiary"));
                            allValues.add(map);
                    }
            }
            myAdapter = new HistoryTodayAdapter(HistoryTodayActivity.this, allValues);
                list.setAdapter(myAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                });

                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HistoryTodayActivity.this);
                        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialogBuilder.setTitle("警告");
                        alertDialogBuilder.setMessage("是否删除这条推荐吗？");
                        alertDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                allValues.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });
                        alertDialogBuilder.show();
                        return false;
                    }
                });

                list.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firtItem, int itemCount, int totalItem) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private  String urlForGetOthers;
    private  String dataForGetOthers;
    public void getOthers(){
        Log.i("asd","342423");
        dataForGetOthers=null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        Log.i("asd",str);
        urlForGetOthers = "http://api.avatardata.cn/HistoryToday/LookUp";
        Log.i("-----",urlForGetOthers);
        dataForGetOthers="key="+"e9542f4a14e34d3e8477378849eea815"+"&yue="+str.charAt(5)+str.charAt(6)+"&ri="+str.charAt(8)+str.charAt(9)+"&type=2&page=1&rows=10";
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetOthers, dataForGetOthers);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForOthers.sendMessage(msg);
                }

            };
         t.start();
    }
    private Handler handlerForOthers =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("val",val);
            try {
                    JSONTokener jsonParser = new JSONTokener(val);
                    JSONObject json = (JSONObject) jsonParser.nextValue();
                    JSONArray jsonArray =  new JSONArray(json.optString("result"));
                    Log.i("111113345556",jsonArray.toString());
                    for(int i = 0 ; i<10;i++){
                        JSONObject json2=jsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();
                        if(i<5){
                            map.put("image", allImgs[11]);
                            map.put("title",allTxt[11]);;}
                        else {
                            map.put("image", allImgs[10]);
                            map.put("title",allTxt[10]);
                        }
                        map.put("star", like_star[0]);
                        map.put("content",json2.optString("title"));
                        allValues.add(map);
                    }
                    //添加以前的日记




                myAdapter = new HistoryTodayAdapter(HistoryTodayActivity.this, allValues);
                list.setAdapter(myAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                });

                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HistoryTodayActivity.this);
                        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialogBuilder.setTitle("警告");
                        alertDialogBuilder.setMessage("是否删除这条推荐吗？");
                        alertDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                allValues.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });
                        alertDialogBuilder.show();
                        return false;
                    }
                });

                list.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firtItem, int itemCount, int totalItem) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
