package com.example.cslab.future.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.question.question1_activity;
import com.example.cslab.future.util.CustomVideoView;
import com.example.cslab.future.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    private EditText user;
    private EditText password;
    private Button login;
    private Button forget;
    private Button register;
    private String url;
    private String data;

    //创建播放视频的控件对象
    private CustomVideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        //加载数据
        initView();

        user = (EditText) findViewById(R.id.num);
        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String userName = user.getText().toString();
                    if (userName.equals("")) {
                        TastyToast.makeText(getApplicationContext(), "用户名不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }else {
                        checkNameAndEmail(userName);
                    }
                }
            }
        });

        password = (EditText) findViewById(R.id.pass);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b==false){
                    String pwd = password.getText().toString();
                    if (pwd.equals("")) {
                        TastyToast.makeText(getApplicationContext(), "密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user.getText().toString();
                String pwd = password.getText().toString();
                if (userName.equals("")){
                    TastyToast.makeText(getApplicationContext(), "用户名不能为空", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else if (pwd.equals("")){
                    TastyToast.makeText(getApplicationContext(), "密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else {
                    checkPassAndName(userName,pwd);
                }
            }
        });

        forget = (Button) findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this,ForgetPasActivity.class);
                startActivity(it);
                finish();
            }
        });

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this, question1_activity.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void checkNameAndEmail(String userName){
        url = "http://192.168.56.1/Home/LogApi/checkNameandMail";
        try {
            data = "login="+ URLEncoder.encode(userName, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCheckNameAndEmail.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckNameAndEmail =new Handler(){
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

    public void checkPassAndName(String username,String pwd){
        url = "http://192.168.56.1/Home/LogApi/checkPassAndName";
        try {
            data = "login="+ URLEncoder.encode(username, "UTF-8")+
                    "&password="+URLEncoder.encode(pwd, "UTF-8");
            Log.i("mes",data);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCheckPwdAndName.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckPwdAndName =new Handler(){
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
                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent it=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(it);
                    User.uToken = json.optString("token");          //赋值token
                    finish();
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };

    private void initView(){
        //加载视频资源控件
        videoview = (CustomVideoView)findViewById(R.id.videoview);
        //设置加载路径
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }
    //返回重启加载
    protected void onRestart(){
        initView();
        super.onRestart();
    }
    //防止锁屏或者切出的时候，音乐在播放
    protected void onStop(){
        videoview.stopPlayback();
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
