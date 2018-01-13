package com.example.cslab.future.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.question.question1_activity;
import com.example.cslab.future.question.question2_activity;
import com.example.cslab.future.question.question_transmit;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.SysApplication;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by CSLab on 2017/7/15.
 */

public class RegisterActivity extends AppCompatActivity{
    private Button btn_back;
    private Button btn_reg;
    private Button getCode;
    private EditText email;
    private EditText code;
    private EditText password;
    private EditText ensurePassword;
    private String url;
    private String data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);
        SysApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        final question_transmit p = (question_transmit) intent.getSerializableExtra("person");

        btn_back =(Button)findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(_intent);
            }
        });

        email = (EditText)findViewById(R.id.email);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String emailText = email.getText().toString();
                    if (emailText.equals("")) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "邮箱不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }else {
                        checkEmailRepeat(emailText);
                    }
                }
            }
        });

        getCode = (Button) findViewById(R.id.getCode);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                if (emailText.equals("")) {
//                    Toast toast = Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT);
//                    toast.show();
                    TastyToast.makeText(getApplicationContext(), "邮箱不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else {
                    sendCode(emailText);
                }
            }
        });

        code = (EditText)findViewById(R.id.code);
        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String codeText = code.getText().toString();
                    if (codeText.equals("")) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "验证码为6位", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "验证码为6位", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        password = (EditText)findViewById(R.id.password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String pwdText = password.getText().toString();
                    if (pwdText.equals("")) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        ensurePassword = (EditText)findViewById(R.id.ensurePassword);
        ensurePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false){                   //失去焦点触发
                    String ensurePwdText = ensurePassword.getText().toString();
                    String pwdText = password.getText().toString();
                    if (!ensurePwdText.equals(pwdText)) {
//                        Toast toast = Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT);
//                        toast.show();
                        TastyToast.makeText(getApplicationContext(), "两次密码输入不一致", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                }
            }
        });

        btn_reg = (Button)findViewById(R.id.reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ensurePwdText = ensurePassword.getText().toString();
                String pwdText = password.getText().toString();
                if (!ensurePwdText.equals(pwdText)) {

                    TastyToast.makeText(getApplicationContext(), "两次密码输入不一致", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }else {
                    String emailText = email.getText().toString();
                    String codeText = code.getText().toString();
                    String name = p.getName();
                    String city = p.getCity();
                    String birth = p.getBirth();
                    int sex = (p.getSex().equals("女生")) ? 0 : (p.getSex().equals("其他")? 2 : 1);
                    String interest = p.getInterest();
                    String dream = p.getDream();
                    Log.i("birth",birth);
                    regUser(name,birth,emailText,codeText,pwdText,city,sex,interest,dream);
                }
            }
        });
    }

    public void checkEmailRepeat(String emailText){
        url = "http://192.168.56.1/Home/RegApi/checkEmailRepeat";
        try {
            data = "email="+ URLEncoder.encode(emailText, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCheckEmailRepeat.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckEmailRepeat =new Handler(){
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

    public                                                                                                                                              void sendCode(String emailText){
        url = "http://192.168.56.1/Home/RegApi/sendCode";
        try {
            data = "email="+ URLEncoder.encode(emailText, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForSendCode.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForSendCode =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            TastyToast.makeText(getApplicationContext(), "邮箱已发送至邮箱", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            Log.i("22",val);
        }
    };

    public void regUser(String name,String birth,String emailText,String codeText,String pwdText,String city,int sex,String interest,String dream){
        url = "http://192.168.56.1/Home/RegApi/checkCode";
        try {
            data = "email="+ URLEncoder.encode(emailText, "UTF-8")+
                    "&code="+ URLEncoder.encode(codeText, "UTF-8")+
                    "&name="+ URLEncoder.encode(name, "UTF-8")+
                    "&password="+ URLEncoder.encode(pwdText, "UTF-8")+
                    "&city="+ URLEncoder.encode(city, "UTF-8")+
                    "&birth="+ URLEncoder.encode(birth, "UTF-8")+
                    "&sex="+ URLEncoder.encode(String.valueOf(sex), "UTF-8")+
                    "&interest="+ URLEncoder.encode(interest, "UTF-8")+
                    "&dream="+ URLEncoder.encode(dream, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForReg.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForReg =new Handler(){
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
                    Intent it=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(it);
                    SysApplication.getInstance().exit();
                    User.uToken = json.optString("token");          //赋值token
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
}
