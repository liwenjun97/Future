package com.example.cslab.future.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.Friend;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/15.
 */

public class TomorrowActivity extends Fragment{
    private TextView messageGroup;
    private TextView square;
    private TextView historytoday;
    private String dirImg_friend;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.tomorrow,container,false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //创建存储用户头像的文件夹
        dirImg_friend = getActivity().getFilesDir().getAbsolutePath()+"/Img_friendHead/";
        Log.i("filepath",dirImg_friend);
        File ImgDir2 = new File(dirImg_friend);
        if (!ImgDir2.exists()) {
            ImgDir2.mkdirs();
        }

        getTomorrowPlanInfo(User.uToken);          //明天顯示有多少人和你有相同的計劃 出现在讨论组的人会记录下来，如果超过3次以上会擦出火花


    }

    public void getTomorrowPlanInfo(String token){
        final String url = "http://192.168.56.1/Home/TomorrowApi/getTomorrowPlanInfo";
        try {
            final String data = "token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    try {
                        JSONTokener jsonParser = new JSONTokener(returnStr);
                        JSONObject json = (JSONObject) jsonParser.nextValue();
                        //存入本地

                        JSONArray jsonArray =  new JSONArray(json.optString("returnFriendArray"));
                        if (jsonArray.length()!=0){
                            Friend[]friends = new Friend[jsonArray.length()];
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject j = (JSONObject)jsonArray.get(i);
                                friends[i] = new Friend();
                                friends[i].setuId(j.optInt("uId"));
                                friends[i].setuName(j.optString("uName"));
                                friends[i].setuImgSrc(j.optString("uImgSrc"));
                                //下载头像
                                boolean returnFlag;
                                String urlGetFriendImg = friends[i].getuImgSrc();
                                String saveForGetFriendImg = dirImg_friend + friends[i].getuId() + ".jpg";
                                File friend_pic = new File(saveForGetFriendImg);
                                Log.i("friend_pic",saveForGetFriendImg);
                                if (!friend_pic.exists()){            //如果本地不存在，则去服务器获取
                                    returnFlag = PostUtils.saveUrlAs(urlGetFriendImg, saveForGetFriendImg);
                                    Log.i("friendFlag", String.valueOf(returnFlag));
                                }


                                friends[i].setuCity(j.optString("uCity"));
                                friends[i].setuAge(j.optInt("uAge"));
                                friends[i].setuSex(j.optInt("uSex"));
                                friends[i].setuInterest(j.optString("uInterest"));
                                friends[i].setPlanTime(j.optString("planTime"));
                                friends[i].setGroupType(j.optString("groupType"));
                                friends[i].setTypeId(j.optInt("typeId"));
                                friends[i].setAppearNum(j.optInt("appearNum"));
                            }
                            //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            //设置到本地并且缓存到本地数据库
                            User.setFriend(friends);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Message msg = new Message();
                    Bundle data = new Bundle();
                    Log.i("getTomorrowPlanInfo",returnStr);
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetTomorrowPlanInfo.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetTomorrowPlanInfo =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                final JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("error")==""){

                    messageGroup = (TextView) getActivity().findViewById(R.id.messageGroup);
                    square = (TextView) getActivity().findViewById(R.id.square );
                    historytoday = (TextView)getActivity().findViewById(R.id.history_today);

                    messageGroup.setText("讨论组:"+json.optString("info"));
                    messageGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it=new Intent(getActivity(),ChatRoomActivity.class);
                            startActivity(it);
                        }
                    });
                    square.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it=new Intent(getActivity(),SquareActivity.class);
                            startActivity(it);
                        }
                    });
                    historytoday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it=new Intent(getActivity(),HistoryTodayActivity.class);
                            startActivity(it);
                        }
                    });

                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    messageGroup = (TextView) getActivity().findViewById(R.id.messageGroup);
                    square = (TextView) getActivity().findViewById(R.id.square );
                    historytoday = (TextView) getActivity().findViewById(R.id.history_today);

                    messageGroup.setText("讨论组-----"+json.optString("error"));
                    messageGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR")
                                    .setContentText(json.optString("error"))
                                    .setConfirmText("别点了,点你也点不进去")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    });
                    square.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it=new Intent(getActivity(),SquareActivity.class);
                            startActivity(it);
                        }
                    });
                    historytoday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it_1=new Intent(getActivity(),HistoryTodayActivity.class);
                            startActivity(it_1);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };
}
