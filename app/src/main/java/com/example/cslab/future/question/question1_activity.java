package com.example.cslab.future.question;

import android.content.Intent;
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
import com.example.cslab.future.activity.ForgetPasActivity;
import com.example.cslab.future.activity.LoginActivity;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.SysApplication;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.example.cslab.future.R.id.login;


/**
 * Created by CSLab on 2017/7/16.
 */

public class question1_activity extends AppCompatActivity {
    private String url;
    private String data;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_1);
        SysApplication.getInstance().addActivity(this);

        Button button_submit = (Button)findViewById(R.id.submit);
        Button button_back = (Button)findViewById(R.id.back);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = (EditText) findViewById(R.id.editText);
                //判断重复
                String userName = name.getText().toString();
                if (!userName.equals("")) {
                    checkNameRepeat(name.getText().toString());
                }else {
                    TastyToast.makeText(getApplicationContext(), "用户名不能为空", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ql_intent = new Intent(question1_activity.this, LoginActivity.class);
                startActivity(ql_intent);
                SysApplication.getInstance().exit();
            }
        });
    }

    public void checkNameRepeat(String userName){
        url = "http://192.168.56.1/Home/RegApi/checkNameRepeat";
        try {
            data = "name="+ URLEncoder.encode(userName, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForCheckNameRepeat.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForCheckNameRepeat =new Handler(){
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
//                    Log.i("22",json.optString("success"));
//                    Toast toast = Toast.makeText(question1_activity.this, json.optString("success"), Toast.LENGTH_SHORT);
//                    toast.show();
                    question_transmit p = new question_transmit();
                    p.setName(name.getText().toString());
                    Bundle data_temp = new Bundle();
                    data_temp.putSerializable("person", p);
                    Intent ql_intent = new Intent(question1_activity.this, question2_activity.class);
                    ql_intent.putExtras(data_temp);
                    startActivity(ql_intent);

                }else {
                    Log.i("22",json.optString("error"));
//                    Toast toast = Toast.makeText(question1_activity.this, json.optString("error"), Toast.LENGTH_SHORT);
//                    toast.show();
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent it=new Intent(question1_activity.this,LoginActivity.class);
            startActivity(it);
            finish();
        }
        return true;
    }

}
