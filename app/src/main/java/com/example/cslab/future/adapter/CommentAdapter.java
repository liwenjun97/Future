package com.example.cslab.future.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.activity.CommentActivity;
import com.example.cslab.future.activity.LoginActivity;
import com.example.cslab.future.activity.MainActivity;
import com.example.cslab.future.activity.SquareActivity;
import com.example.cslab.future.entity.Comment;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.Screen;
import com.sdsmdg.tastytoast.TastyToast;

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

/**
 * Created by CSLab on 2017/7/18.
 */

public class CommentAdapter extends BaseAdapter {
    private Context ctx;
    private List<CommentItem> allValues;
    private CommentItem commentItem;

    TextView dreamHead;
    TextView dreamId;
    TextView dreamTime;
    TextView image;
    TextView dream_Txt;
    TextView commentNum;
    Button commentBtn;
    EditText commentContent;
    Button ensureCommentBtn;
    TextView clickNum;
    Button clickBtn;

    TextView commentName;
    TextView action;
    TextView reviewerName;
    TextView comment;
    TextView commentTime;
    String comm=null;
    int type;
    public CommentAdapter(Context ctx, List<CommentItem> allValues) {
        this.ctx = ctx;
        this.allValues = allValues;
    }

    @Override
    public int getItemViewType(int position) {
        type = allValues.get(position).type;
        Log.i("ddd", "result=" + type);
        return type;
    }

    @Override
    public int getCount() {
        return allValues.size();
    }

    @Override
    public CommentItem getItem(int position) {
        return allValues.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            //构建ListView中一行的界面
            switch (getItemViewType(position)){
                case 0 :
                    {
                        view = LayoutInflater.from(ctx).inflate(R.layout.comment_1item, null);
                        //取得当前行中所有的组件
                        dreamHead = (TextView)view.findViewById(R.id.dreamHead);       //头像
                        dreamId = (TextView)view.findViewById(R.id.dreamId);           //昵称
                        dreamTime = (TextView)view.findViewById(R.id.dreamTime);       //(梦想时间)注册时间
                        image = (TextView)view.findViewById(R.id.image);               //dream图
                        dream_Txt = (TextView)view.findViewById(R.id.dream_Txt);       //dream内容
                        commentNum = (TextView)view.findViewById(R.id.commentNum);     //评论数
                        commentBtn = (Button)view.findViewById(R.id.commentBtn);         //评论按钮

                        commentContent = (EditText)view.findViewById(R.id.commentText);        //评论内容
                        ensureCommentBtn = (Button)view.findViewById(R.id.ensureCommentBtn);   //发表评论按钮
                        clickNum = (TextView)view.findViewById(R.id.clickNum);         //点赞数
                        clickBtn = (Button)view.findViewById(R.id.clickBtn);             //点赞按钮


                        Map<String, Object> map = allValues.get(position).map;
                        Drawable d=Drawable.createFromPath(map.get("head_pic_path").toString());    //头像
                        dreamHead.setBackgroundDrawable(d);

                        Drawable e=Drawable.createFromPath(map.get("dream_pic_path").toString());   //dream图片
                        image.setBackgroundDrawable(e);

                        dreamId.setText(map.get("uName").toString());
                        dreamTime.setText(map.get("uRegTime").toString());
                        dream_Txt.setText(map.get("dream_content").toString());
                        commentNum.setText(map.get("commentNum").toString());
                        clickNum.setText(map.get("clickNum").toString());

                        ensureCommentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("111","评论按钮被点了");
                            }
                        });
                    }break;
//                case 1 : view = LayoutInflater.from(ctx).inflate(R.layout.comment_item, null);break;
            }
//            view = LayoutInflater.from(ctx).inflate(R.layout.comment_1item, null);
            //设置行的宽和高
//            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Screen.SCREEN_HEIGHT / 2));

//            view = LayoutInflater.from(ctx).inflate(R.layout.comment_item, null);
//            //设置行的宽和高
//            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Screen.SCREEN_HEIGHT / 4));
        }
//        else{
//            switch (getItemViewType(position)){
//                case 0 : view = LayoutInflater.from(ctx).inflate(R.layout.comment_1item, null);break;
//                case 1 : {
//                    view = LayoutInflater.from(ctx).inflate(R.layout.comment_item, null);
//                    commentName = (TextView)view.findViewById(R.id.commentName);   //评论人
//                    comment = (TextView)view.findViewById(R.id.comment);            //评论内容
//                    commentTime = (TextView)view.findViewById(R.id.timeText);      //评论时间
//                }break;
//            }
//        }

        switch (getItemViewType(position)){
            case 0 : {
                view = LayoutInflater.from(ctx).inflate(R.layout.comment_1item, null);
                view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
                //取得当前行中所有的组件
                dreamHead = (TextView)view.findViewById(R.id.dreamHead);       //头像
                dreamId = (TextView)view.findViewById(R.id.dreamId);           //昵称
                dreamTime = (TextView)view.findViewById(R.id.dreamTime);       //(梦想时间)注册时间
                image = (TextView)view.findViewById(R.id.image);               //dream图
                dream_Txt = (TextView)view.findViewById(R.id.dream_Txt);       //dream内容
                commentNum = (TextView)view.findViewById(R.id.commentNum);     //评论数
                commentBtn = (Button)view.findViewById(R.id.commentBtn);         //评论按钮

                commentContent = (EditText)view.findViewById(R.id.commentText);        //评论内容
                ensureCommentBtn = (Button)view.findViewById(R.id.ensureCommentBtn);   //发表评论按钮

                clickNum = (TextView)view.findViewById(R.id.clickNum);         //点赞数
                clickBtn = (Button)view.findViewById(R.id.clickBtn);             //点赞按钮


                Map<String, Object> map = allValues.get(position).map;
                Drawable d=Drawable.createFromPath(map.get("head_pic_path").toString());    //头像
                dreamHead.setBackgroundDrawable(d);

                Drawable e=Drawable.createFromPath(map.get("dream_pic_path").toString());   //dream图片
                image.setBackgroundDrawable(e);

                dreamId.setText(map.get("uName").toString());
                dreamTime.setText(map.get("uRegTime").toString());
                dream_Txt.setText(map.get("dream_content").toString());
                commentNum.setText(map.get("commentNum").toString());
                clickNum.setText(map.get("clickNum").toString());

                clickBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = allValues.get(position).map;
                        if (map.get("isClick").equals("0")) {
                            int clickNum = Integer.parseInt(map.get("clickNum").toString());
                            clickNum++;
                            map.put("clickNum",String.valueOf(clickNum));
                            map.put("isClick","1");
                            Log.i("di按咱", "没点赞");

                            int clickId = User.uId;
                            int uId = Integer.parseInt(map.get("uId").toString());
                            clickGood(User.uToken,clickId,uId,position);
                        }else {
                            int clickNum = Integer.parseInt(map.get("clickNum").toString());
                            clickNum--;
                            map.put("clickNum",String.valueOf(clickNum));
                            map.put("isClick","0");
                            Log.i("di按咱", "点赞了");

                            int tableId = Integer.parseInt(map.get("tableId").toString());
                            delClick(User.uToken,tableId);
                        }
                        notifyDataSetChanged();
                    }
                });
                commentContent = (EditText)view.findViewById(R.id.commentText);
                commentContent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        comm = commentContent.getText().toString();
                        System.out.println("---------输入--------"+charSequence);
//                        System.out.println("---------输入--------"+comm);
                        comm = comm + charSequence;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
//                        comm = commentContent.getText().toString();
                        System.out.println("-------输入后---"+comm);
                    }
                });
                ensureCommentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("111","评论按钮被点了");

                        Map<String, Object> map = allValues.get(position).map;
                        int commentId = User.uId;
                        int commentedId = (int) map.get("uId");         //被评论者就是梦想所属人
                        int uId = (int) map.get("uId");
//                        commentContent.setText("444444444");
                        String comment_Text=null;
                        comment_Text = comm;
                        Toast.makeText(view.getContext(),comment_Text, Toast.LENGTH_LONG).show();
//                       comment_Text = commentContent.getText().toString();
                        Log.i("commentId", String.valueOf(commentId));
                        Log.i("commentedId", String.valueOf(commentedId));
                        Log.i("uId", String.valueOf(uId));
                        if (comment_Text.equals("")){
                            new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR")
                                    .setContentText("评论内容不能为空")
                                    .setConfirmText("了解")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }else {
                            makeComment(User.uToken, commentId, commentedId, uId, comment_Text);
                        }
                    }
                });
            }break;
            case 1 : {
                view = LayoutInflater.from(ctx).inflate(R.layout.comment_item, null);
                view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
                commentName = (TextView)view.findViewById(R.id.commentName);   //评论人
                action=(TextView)view.findViewById(R.id.action);
                reviewerName=(TextView)view.findViewById(R.id.reviewerName);
                comment = (TextView)view.findViewById(R.id.comment);            //评论内容
                commentTime = (TextView)view.findViewById(R.id.timeText);      //评论时间
            }break;
        }

//        TextView time = (TextView)view.findViewById(R.id.time_txt);

        //获取当前行的数据，并将数据放在对应的组件上面
        Map<String, Object> map = allValues.get(position).map;
//        image.setBackgroundResource((int)map.get("image"));
        if (allValues.get(position).type==0) {
            Drawable d=Drawable.createFromPath(map.get("head_pic_path").toString());    //头像
            dreamHead.setBackgroundDrawable(d);

            Drawable e=Drawable.createFromPath(map.get("dream_pic_path").toString());   //dream图片
            image.setBackgroundDrawable(e);

            dreamId.setText(map.get("uName").toString());
            dreamTime.setText(map.get("uRegTime").toString());
            dream_Txt.setText(map.get("dream_content").toString());
            commentNum.setText(map.get("commentNum").toString());
            clickNum.setText(map.get("clickNum").toString());
            if (map.get("isClick").equals("0")) {
                Log.i("di按咱", "没点赞2");
                clickBtn.setBackgroundResource(R.mipmap.click3);
            }else {
                Log.i("di按咱", "点赞了2");
                clickBtn.setBackgroundResource(R.mipmap.click2);
            }
            Log.i("走这里了", String.valueOf(map.get("head_pic_path")));
            Log.i("走这里了", String.valueOf(map.get("dream_pic_path")));
        }else {
            commentName.setText(map.get("commentName").toString());
            action.setText(map.get("action").toString());
            reviewerName.setText(map.get("reviewerName").toString());
            comment.setText(map.get("commentContent").toString());
            commentTime.setText(map.get("commentTime").toString());
            Log.i("4444","44444");
        }
        return view;
    }



    private String urlForClickGood;
    private String dataForClickGood;
    public void clickGood(String token, int clickId, int uId, final int position){
        urlForClickGood = "http://192.168.56.1/Home/TomorrowApi/clickGood";
        try {
            dataForClickGood = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&clickId="+URLEncoder.encode(String.valueOf(clickId), "UTF-8")+
                    "&uId="+URLEncoder.encode(String.valueOf(uId), "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForClickGood, dataForClickGood);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    data.putInt("position",position);
                    msg.setData(data);
                    handlerForClick.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForClick =new Handler(){
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
                    JSONArray jsonArray = new JSONArray(json.optString("clickData"));
                    JSONObject j = (JSONObject)jsonArray.get(0);
                    int position = data.getInt("position");
                    int tableId = j.optInt("id");
                    Map<String, Object> map = allValues.get(position).map;
                    map.put("tableId",tableId);
                    notifyDataSetChanged();

                    Log.i("22",json.optString("success"));
                    TastyToast.makeText(ctx, json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(ctx, json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };

    private String urlForDelClick;
    private String dataForDelClick;
    public void delClick(String token,int clickTableId){
        urlForDelClick = "http://192.168.56.1/Home/TomorrowApi/delClick";
        try {
            dataForDelClick = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&clickTableId="+URLEncoder.encode(String.valueOf(clickTableId), "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForDelClick, dataForDelClick);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForDelClick.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForDelClick =new Handler(){
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
                    TastyToast.makeText(ctx, json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(ctx, json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };



    private String urlForMakeComment;
    private String dataForMakeComment;
    public void makeComment(String token,int commentId,int commentedId,int uId,String commentText){
        urlForMakeComment = "http://192.168.56.1/Home/TomorrowApi/makeComment";
        try {
            dataForMakeComment = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&commentId="+ URLEncoder.encode(String.valueOf(commentId), "UTF-8")+
                    "&commentedId="+ URLEncoder.encode(String.valueOf(commentedId), "UTF-8")+
                    "&uId="+ URLEncoder.encode(String.valueOf(uId), "UTF-8")+
                    "&commentText="+ URLEncoder.encode(commentText, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForMakeComment, dataForMakeComment);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForMakeComment.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForMakeComment =new Handler(){
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
                    if(json.optString("commentData").length()!=0) {
                        JSONArray jsonArray = new JSONArray(json.optString("commentData"));
                        JSONObject j = (JSONObject)jsonArray.get(0);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("id", j.optInt("id"));
                        map2.put("commentId", j.optInt("commentId"));
                        map2.put("commentName", j.optString("commentName"));
                        map2.put("reviewerName", "");
                        map2.put("action", "评论");
                        map2.put("commentedId", j.optInt("commentedId"));
                        map2.put("commentedName", j.optString("commentedName"));
                        map2.put("uId", j.optInt("uId"));
                        map2.put("uName", j.optString("uName"));
                        map2.put("commentContent", j.optString("commentContent"));
                        map2.put("commentTime", j.optString("commentTime"));
                        commentItem = new CommentItem(1, (HashMap<String, Object>) map2);
                        allValues.add(commentItem);
                        notifyDataSetChanged();
                        commentContent.setText("");
                        comm = "";
                        Log.i("111","走这里了");
                        new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SUCCESS")
                                .setContentText(json.optString("success"))
                                .setConfirmText("收到")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }

                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(ctx, json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };
}

