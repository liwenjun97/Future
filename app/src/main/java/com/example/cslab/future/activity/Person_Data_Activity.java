package com.example.cslab.future.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.Screen;
import com.example.cslab.future.util.head_pic_choice;
import com.example.cslab.future.util.picture_choice;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/16.
 */

public class Person_Data_Activity extends AppCompatActivity {
    public static Person_Data_Activity me;

    private EditText name;
    private EditText city;
    private TextView sex;
    private TextView age;
    private TextView email;
    private TextView dream;
    private LinearLayout dream_picture;
    private LinearLayout dream_picture_linear;
    private ImageView imageView;
    private ImageView head_picture;
    private Button back;
    private Button modify;
    private Button save;

    private String urlForChangeUser;
    private String dataForChangeUser;
    private String urlForGetRandomHead;                 //获取随机头像地址
    private String urlForGetHead;                       //服务器存放头像的地址
    private String urlForChangeHead;                    //更改头像的url访问地址
    private String urlForChangeDream;                   //更改dream的url地址
    private String urlForGetRandomDream;
    private String urlForGetDream;                      //服务器存放dream的地址
    private String saveForGetHead;
    private String saveForGetDream;
    private String dirImg_head;                         //本地head存放的文件夹
    private String dirImg_dream;                        //本地dream存放的文件夹


    int width,height,left,right,top,bottom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_data);
        me = this;

        name = (EditText)findViewById(R.id.name);
        name.setText(User.uName);
        city = (EditText)findViewById(R.id.city);
        city.setText(User.uCity);
        sex  = (TextView) findViewById(R.id.sex);
        String sexStr = ((User.uSex==0) ? "女" : (User.uSex==2)? "其他" : "男");
        sex.setText(sexStr);
        age = (TextView) findViewById(R.id.age);
        age.setText(String.valueOf(User.uAge));
        email = (TextView) findViewById(R.id.email);
        email.setText(User.uEmail);
        dream = (TextView) findViewById(R.id.dream);
        dream.setText(User.uDream);
        imageView =(ImageView)findViewById(R.id.head_picture);
        head_picture =(ImageView)findViewById(R.id.head_picture);
        dream_picture = (LinearLayout)findViewById(R.id.dream_picture);
        dream_picture_linear = (LinearLayout)findViewById(R.id.dream_picture_linear);       //这个才是dream背景图
        back = (Button)findViewById(R.id.back);
        save = (Button)findViewById(R.id.save);
        modify = (Button)findViewById(R.id.modify);
        Screen.init(this);

        //创建存头像的文件夹
        dirImg_head = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_head/";
        Log.i("filepath",dirImg_head);
        File ImgDir = new File(dirImg_head);
        if (!ImgDir.exists()) {
            ImgDir.mkdirs();
        }
        //获取头像
        if (User.uImgSrc==null||User.uImgSrc.equals("")){            //imgsrc为空服务器返回随机图片
            Log.i("走这里了","走这里了");
            getRandomHead(User.uToken);
        }else {
            String head_pic_path = dirImg_head + User.uToken + "." + User.uImgSrc.substring(User.uImgSrc.lastIndexOf(".")+1);  //图片路径
            File head_pic = new File(head_pic_path);
            Log.i("headPath",head_pic_path);
            if (!head_pic.exists()){            //如果本地不存在，则去服务器获取
                getMyHeadPic(User.uToken);
            }else {                         //存在则渲染
                Log.i("headPath2",head_pic_path);
                Bitmap bmp= BitmapFactory.decodeFile(head_pic_path);
                head_picture.setImageBitmap(bmp);
            }
        }


        //创建存dream的文件夹
        dirImg_dream = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_dream/";
        Log.i("filepath",dirImg_dream);
        File ImgDir2 = new File(dirImg_dream);
        if (!ImgDir2.exists()) {
            ImgDir2.mkdirs();
        }
        //获取dream图片
        if (User.uDreamPic==null||User.uDreamPic.equals("")){            //imgsrc为空服务器返回随机图片
            Log.i("走这里了","走这里了2");
            getRandomDream(User.uToken);
        }else {
            String dream_pic_path = dirImg_dream + User.uToken + "." + User.uImgSrc.substring(User.uImgSrc.lastIndexOf(".")+1);  //图片路径
            File dream_pic = new File(dream_pic_path);
            Log.i("dreamPath",dream_pic_path);
            if (!dream_pic.exists()){            //如果本地不存在，则去服务器获取
                getMyDreamPic(User.uToken);
            }else {                         //存在则渲染
                Log.i("headPath2",dream_pic_path);
//                Bitmap bmp_change = dream_picture.getDrawingCache();// 将线性布局中的整个内容转换为位图
//                Bitmap bmp= BitmapFactory.decodeFile(dream_pic_path);
//                bmp_change.setImageBitmap(bmp);
                Drawable d=Drawable.createFromPath(dream_pic_path);
                dream_picture_linear.setBackgroundDrawable(d);
            }
        }


        dream_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Person_Data_Activity.this,picture_choice.class));
            }
        });
        head_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Person_Data_Activity.this,head_pic_choice.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Person_Data_Activity.this,MainActivity.class));
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = name.getText().toString();
                String ucity = city.getText().toString();
                User.uName = uname;
                User.uCity = ucity;
                //缓存到本地数据库
                //...
                //保存到远程服务器
                Log.i("token",User.uToken);
                changeUserInfo(User.uToken,uname,ucity);
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Person_Data_Activity.this,ChangePasActivity.class));
            }
        });
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

    public void onClick(View v){
        switch (v.getId()){
            case  R.id.data:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
        }
    }

    public void changeUserInfo(String token,String uname,String ucity){
        urlForChangeUser = "http://192.168.56.1/Home/UserApi/changeUserInfo";
        try {
            dataForChangeUser = "name="+ URLEncoder.encode(uname, "UTF-8")+
                                    "&city="+ URLEncoder.encode(ucity, "UTF-8")+
                                    "&token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForChangeUser, dataForChangeUser);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForChangeUser.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForChangeUser =new Handler(){
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
                    new SweetAlertDialog(Person_Data_Activity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(json.optString("success"))
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    finish();
                                }
                            })
                            .show();
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };

    public void getRandomHead(String token){
        int randomImg = (int)(Math.random()*17) + 1;
        urlForGetRandomHead = "http://192.168.56.1/Img/system_head/" + randomImg + ".jpg";
        saveForGetHead = dirImg_head + User.uToken + ".jpg";
        Thread t = new Thread(){
            @Override
            public void run() {
                boolean returnFlag;
                String returnStr = null;
                returnFlag = PostUtils.saveUrlAs(urlForGetRandomHead, saveForGetHead);
                if (returnFlag){                //如果下载成功则需上传到服务器
                    urlForChangeHead = "http://192.168.56.1/Home/UserApi/changeHeadImg";
                    urlForGetHead = "http://192.168.56.1/Img/head/" + User.uToken + ".jpg";
                    String fileName = User.uToken + ".jpg";
                    returnStr = PostUtils.uploadFile(urlForChangeHead,fileName,saveForGetHead,User.uToken);
                }
                //下载随机图片
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("value", returnFlag);
                data.putString("value2",returnStr);
                msg.setData(data);
                handlerForGetRandomHead.sendMessage(msg);

            }
        };
        t.start();
    }

    private Handler handlerForGetRandomHead =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            Boolean val = data.getBoolean("value");
            String val2 = data.getString("value2");
            if (!val){
                TastyToast.makeText(getApplicationContext(), "获取随机图片网络错误!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
            JSONTokener jsonParser = new JSONTokener(val2);
            try {
                JSONObject json = (JSONObject) jsonParser.nextValue();
                if (json.optString("success")!=""){
                    User.uImgSrc = urlForGetHead;
                    String head_pic_path = dirImg_head + User.uToken + "." + User.uImgSrc.substring(User.uImgSrc.lastIndexOf(".")+1);
                    Log.i("path",head_pic_path);
                    Bitmap bmp= BitmapFactory.decodeFile(head_pic_path);
                    head_picture.setImageBitmap(bmp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("val2",val2);
        }
    };


    public void getMyHeadPic(String token){
//        urlForGetHead = User.uImgSrc;
        urlForGetHead = "http://192.168.56.1/Img/head/" + User.uToken + ".jpg";
        saveForGetHead = dirImg_head + User.uToken + ".jpg";
        Thread t = new Thread(){
            @Override
            public void run() {
                boolean returnFlag;
                returnFlag = PostUtils.saveUrlAs(urlForGetHead, saveForGetHead);
                //下载随机图片
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("value", returnFlag);
                msg.setData(data);
                handlerForGetMyHead.sendMessage(msg);

            }
        };
        t.start();
    }

    private Handler handlerForGetMyHead =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            Boolean val = data.getBoolean("value");
            if (!val){
                TastyToast.makeText(getApplicationContext(), "获取随机图片网络错误!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }else {
                String head_pic_path = dirImg_head + User.uToken + "." + User.uImgSrc.substring(User.uImgSrc.lastIndexOf(".")+1);
                Log.i("path",head_pic_path);
                Bitmap bmp= BitmapFactory.decodeFile(head_pic_path);
                head_picture.setImageBitmap(bmp);
            }
            Log.i("val本地没有head了", String.valueOf(val));
        }
    };


    public void getRandomDream(String token){
        int randomImg = (int)(Math.random()*17) + 1;
        urlForGetRandomDream = "http://192.168.56.1/Img/system_dream/" + randomImg + ".jpg";
        saveForGetDream = dirImg_dream + User.uToken + ".jpg";
        Thread t = new Thread(){
            @Override
            public void run() {
                boolean returnFlag;
                String returnStr = null;
                returnFlag = PostUtils.saveUrlAs(urlForGetRandomDream, saveForGetDream);
                if (returnFlag){                //如果下载成功则需上传到服务器
                    urlForChangeDream = "http://192.168.56.1/Home/UserApi/changeDreamImg";
                    urlForGetDream = "http://192.168.56.1/Img/dream/" + User.uToken + ".jpg";
                    String fileName = User.uToken + ".jpg";
                    returnStr = PostUtils.uploadFile(urlForChangeDream,fileName,saveForGetDream,User.uToken);
                }
                //下载随机图片
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("value", returnFlag);
                data.putString("value2",returnStr);
                msg.setData(data);
                handlerForGetRandomDream.sendMessage(msg);

            }
        };
        t.start();
    }

    private Handler handlerForGetRandomDream =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            Boolean val = data.getBoolean("value");
            String val2 = data.getString("value2");
            Log.i("val", String.valueOf(val));
            Log.i("val2",val2);
            if (!val){
                TastyToast.makeText(getApplicationContext(), "获取随机图片网络错误!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
            JSONTokener jsonParser = new JSONTokener(val2);
            try {
                JSONObject json = (JSONObject) jsonParser.nextValue();
                if (json.optString("success")!=""){
                    User.uDreamPic = urlForGetDream;
                    String dream_pic_path = dirImg_dream + User.uToken + "." + User.uDreamPic.substring(User.uDreamPic.lastIndexOf(".")+1);
                    Log.i("path",dream_pic_path);
                    Drawable d=Drawable.createFromPath(dream_pic_path);
                    dream_picture_linear.setBackgroundDrawable(d);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public void getMyDreamPic(String token){
//        urlForGetDream = User.uDreamPic;
        urlForGetDream = "http://192.168.56.1/Img/dream/" + User.uToken + ".jpg";
        saveForGetDream = dirImg_dream + User.uToken + ".jpg";
        Thread t = new Thread(){
            @Override
            public void run() {
                boolean returnFlag;
                returnFlag = PostUtils.saveUrlAs(urlForGetDream, saveForGetDream);
                //下载随机图片
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("value", returnFlag);
                msg.setData(data);
                handlerForGetMyDream.sendMessage(msg);

            }
        };
        t.start();
    }

    private Handler handlerForGetMyDream =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            Boolean val = data.getBoolean("value");
            if (!val){
                TastyToast.makeText(getApplicationContext(), "获取随机图片网络错误!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }else {
                String dream_pic_path = dirImg_dream + User.uToken + "." + User.uDreamPic.substring(User.uDreamPic.lastIndexOf(".")+1);
                Log.i("path",dream_pic_path);
                Drawable d=Drawable.createFromPath(dream_pic_path);
                dream_picture_linear.setBackgroundDrawable(d);
            }
            Log.i("val本地没有dream图片了", String.valueOf(val));
        }
    };

}
