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

import com.example.cslab.future.R;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/15.
 */

public class ChangePasActivity extends AppCompatActivity{
    private EditText old_password;
    private EditText password;
    private EditText ensure_password;
    private Button confirm;
    private Button cancel;

    private String urlForChangePas;
    private String dataForChangePas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        old_password = (EditText) findViewById(R.id.old_password);
        password = (EditText) findViewById(R.id.password);
        ensure_password = (EditText) findViewById(R.id.ensurePassword);
        confirm = (Button)findViewById(R.id.confirm);
        cancel = (Button)findViewById(R.id.cancel);

        old_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {                   //失去焦点触发
                    String old_ps = old_password.getText().toString();
                    if (old_ps.equals("")) {

                        TastyToast.makeText(getApplicationContext(), "旧密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    } else {
                        ////////////
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {                   //失去焦点触发
                    String pass_w = password.getText().toString();
                    if (pass_w.equals("")) {

                        TastyToast.makeText(getApplicationContext(), "新密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    } else {
                        ////////////
                    }
                }
            }
        });

        ensure_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {                   //失去焦点触发
                    String en_pass = old_password.getText().toString();
                    if (en_pass.equals("")) {

                        TastyToast.makeText(getApplicationContext(), "确认密码不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    } else {
                        ////////////
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd_old = old_password.getText().toString();
                String pwd_new = password.getText().toString();
                String pwd_new2 = ensure_password.getText().toString();
                if (pwd_new.equals(pwd_new2)){
                    changePassword(User.uToken,pwd_old,pwd_new);
                }else {
                    TastyToast.makeText(getApplicationContext(), "两次密码输入不一致", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });

    }

    public void changePassword(String token,String pwd_old,String pwd_new){
        urlForChangePas = "http://192.168.56.1/Home/UserApi/changePassword";
        try {
            dataForChangePas = "passwordpast="+ URLEncoder.encode(pwd_old, "UTF-8")+
                    "&passwordnew="+ URLEncoder.encode(pwd_new, "UTF-8")+
                    "&token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForChangePas, dataForChangePas);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForChangePas.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForChangePas =new Handler(){
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
                    new SweetAlertDialog(ChangePasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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


}
