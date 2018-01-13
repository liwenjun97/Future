package com.example.cslab.future.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.adapter.CardDataImpl;
import com.example.cslab.future.adapter.CardListItemAdapter;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.Plan;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.BroadCastManager;
import com.example.cslab.future.util.PostUtils;
import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * Created by CSLab on 2017/7/15.
 */

public class YesterdayActivity extends Fragment{
    private String urlForGetPlan;
    private String dataForGetPlan;
    private String urlForGetDiary;
    private String dataForGetDiary;
    private List<ECCardData> dataset;
    private ECPagerView ecPagerView;
    public static BroadcastReceiver mReceiver;
    public static YesterdayActivity yesterdayActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.yesterday,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        yesterdayActivity = this;


//        获取plan和diary
        /*查询本地数据库*/
        //if(!User.getPlan()){      如果本地不存在
        getPlanAndDiary(User.uToken);       //访问服务器
        //}


    }

    public void getPlanAndDiary(String token){
        urlForGetPlan = "http://192.168.56.1/Home/YesterdayApi/showAllPlan";
        urlForGetDiary = "http://192.168.56.1/Home/YesterdayApi/showAllDiary";
        try {
            dataForGetPlan = "token="+ URLEncoder.encode(token, "UTF-8");
            dataForGetDiary = "token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetPlan, dataForGetPlan);
                    String returnStr2;
                    returnStr2 = PostUtils.sendPost(urlForGetDiary, dataForGetDiary);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("valuePlan", returnStr);
                    data.putString("valueDiary", returnStr2);
                    msg.setData(data);
                    handlerForGetPlanAndDiary.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetPlanAndDiary =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String valPlan = data.getString("valuePlan");
            String valDiary = data.getString("valueDiary");
            //获取计划
            try {
                JSONTokener jsonParser = new JSONTokener(valPlan);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("error")==""){
                    Log.i("22",json.optString("allPlan"));
                    if(json.optString("allPlan")!=null){
                        JSONArray jsonArray =  new JSONArray(json.optString("allPlan"));

                        Log.i("json",((JSONObject) jsonArray.get(0)).optString("typeId"));
                        Plan[]plans = new Plan[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject j = (JSONObject)jsonArray.get(i);
                            plans[i] = new Plan();
                            plans[i].setId(j.optInt("id"));
                            plans[i].setuId(j.optInt("uId"));
                            plans[i].setuType(j.optString("uType"));
                            plans[i].setTypeId(j.optInt("typeId"));
                            plans[i].setuRemark(j.optString("uRemark"));
                            plans[i].setuFinish(j.optInt("uFinish"));
                            plans[i].setuTime(j.optString("uTime"));
                        }
                        //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        //设置到本地并且缓存到本地数据库
                        User.setPlan(plans);
                    }

                }else {
                    Log.i("22",json.optString("error"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it=new Intent(getActivity(),LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //获取日记
            try {
                JSONTokener jsonParser = new JSONTokener(valDiary);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("error")==""){
                    Log.i("22",json.optString("allDiary"));
                    if (json.optString("allDiary")!=null){
                        JSONArray jsonArray =  new JSONArray(json.optString("allDiary"));


                        //                    Log.i("json",((JSONObject) jsonArray.get(0)).optString("typeId"));
                        Diary[]diaries = new Diary[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject j = (JSONObject)jsonArray.get(i);
                            diaries[i] = new Diary();
                            diaries[i].setuStyle(j.optString("uStyle"));
                            diaries[i].setId(j.optInt("id"));
                            diaries[i].setuTime(j.optString("uTime"));
                            diaries[i].setuDiary(j.optString("uDiary"));
                            diaries[i].setuId(j.optInt("uId"));
                            diaries[i].setuDiaryPic(j.optString("uDiaryPic"));
                        }
                        //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        //设置到本地并且缓存到本地数据库
                        User.setDiary(diaries);
                    }

                }else {
                    Log.i("22",json.optString("error"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it=new Intent(getActivity(),LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",valPlan);
            Log.i("22",valDiary);

            class LocalReceiver extends BroadcastReceiver {

                @Override
                public void onReceive(Context context, Intent intent) {
                    //收到广播后的处理
                    String orderid = intent.getStringExtra("order");
                    Log.i("广播",orderid);
                    String planStr = "该天没有制定计划!";
                    Map<String, String> map = new HashMap<>();
                    map.put("planStr", planStr);

                    // Get pager from layout
                    ecPagerView = (ECPagerView) getActivity().findViewById(R.id.ec_pager_element);
                    // Generate example dataset
                    // 生成示例数据
                    dataset = CardDataImpl.generateExampleData();
                    //适配器
                    ecPagerView.setPagerViewAdapter(new ECPagerViewAdapter(getActivity().getApplicationContext(), dataset) {
                        @Override
                        public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, ListView list, ECCardData data) {
                            // Data object for current card
                            // 当前数据对象
                            CardDataImpl cardData = (CardDataImpl) data;

                            // Set adapter and items to current card content list
                            // 将适配器和项目设置为当前卡牌的内容
                            list.setAdapter(new CardListItemAdapter(getActivity().getApplicationContext(), cardData.getListItems()));
                            // Also some visual tuning can be done here
                            // list的背景颜色   视觉调整
                            list.setBackgroundColor(getResources().getColor(R.color.black));

                            // Here we can create elements for head view or inflate layout from xml using inflater service
                            TextView cardTitle = new TextView(getActivity().getApplicationContext());
                            cardTitle.setText(cardData.getCardTitle());
                            cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20);
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.CENTER;
                            head.addView(cardTitle, layoutParams);

                            // Card toggling by click on head element view
                            //点击中间图片展开
                            head.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    ecPagerView.toggle();
                                }
                            });
                        }
                    });
                    //设置背景图

                    ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) getActivity().findViewById(R.id.ec_bg_switcher_element));

                }

            };

            //接收广播
            try {
                IntentFilter filter = new IntentFilter();
                filter.addAction("fragment_home_left");
                mReceiver = new LocalReceiver();
                BroadCastManager.getInstance().registerReceiver(getActivity(),
                        mReceiver, filter);//注册广播接收者
            } catch (Exception e) {
                e.printStackTrace();
            }




            // Get pager from layout
            ecPagerView = (ECPagerView) getActivity().findViewById(R.id.ec_pager_element);
            // Generate example dataset
            // 生成示例数据
            dataset = CardDataImpl.generateExampleData();

            //适配器
            ecPagerView.setPagerViewAdapter(new ECPagerViewAdapter(getActivity().getApplicationContext(), dataset) {
                @Override
                public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, ListView list, ECCardData data) {
                    // Data object for current card
                    // 当前数据对象
                    CardDataImpl cardData = (CardDataImpl) data;

                    // Set adapter and items to current card content list
                    // 将适配器和项目设置为当前卡牌的内容
                    list.setAdapter(new CardListItemAdapter(getActivity().getApplicationContext(), cardData.getListItems()));
                    // Also some visual tuning can be done here
                    // list的背景颜色   视觉调整
                    list.setBackgroundColor(getResources().getColor(R.color.black));

                    // Here we can create elements for head view or inflate layout from xml using inflater service
                    TextView cardTitle = new TextView(getActivity().getApplicationContext());
                    cardTitle.setText(cardData.getCardTitle());
                    cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    head.addView(cardTitle, layoutParams);

                    // Card toggling by click on head element view
                    //点击中间图片展开
                    head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            ecPagerView.toggle();
                        }
                    });
                }
            });

          ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) getActivity().findViewById(R.id.ec_bg_switcher_element));
        }
    };
}

