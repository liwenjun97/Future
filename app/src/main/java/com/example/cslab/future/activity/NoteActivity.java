package com.example.cslab.future.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by CSLab on 2017/7/18.
 */

public class NoteActivity extends AppCompatActivity {
    private EditText note;
    private Spinner year;
    private Spinner month;
    private Spinner day;
    private CheckBox stick;
    private Button back;
    private Button save;
    private int year_count=2017;
    private String temp;
    private String[] year_arr = new String[101];
    private String[] month_arr = new String[13];
    private String[] day_arr1 = new String[31];
    private String[] day_arr2 = new String[30];
    private String[] day_arr3 = new String[29];
    private String[] day_arr4 = new String[28];
    private String[] day_none = new String[1];
    private ArrayAdapter adapter;
    private ArrayAdapter adapter1;
    private ArrayAdapter adapter2;
    private ArrayAdapter adapter3;
    private ArrayAdapter adapter4;
    private String str1;
    private String str2;
    private int run;
    private int runyue;
    private boolean judge1;
    private boolean judge2;
    private String url;
    private String data;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        note = (EditText) findViewById(R.id.note);
        year = (Spinner) findViewById(R.id.year);
        month = (Spinner) findViewById(R.id.month);
        day = (Spinner) findViewById(R.id.day);
        stick = (CheckBox) findViewById(R.id.stick);
        back = (Button) findViewById(R.id.back);
//        save1 = (Button) findViewById(R.id.save1);
        save = (Button) findViewById(R.id.save);
        for (int i = 0; i < 100; i++) {
            temp = String.valueOf(year_count + i);
            year_arr[99-i] = temp;
        }
        year_arr[100] = "年份";
        for (int i = 0; i < 12; i++) {
            temp = String.valueOf(1 + i);
            month_arr[11-i] = temp;
        }
        month_arr[12] = "月份";
        day_none[0] = "日期";

        adapter = new ArrayAdapter(this, R.layout.spinner_item_1, year_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        year.setAdapter(adapter);
        year.setSelection(100, true);
        adapter = new ArrayAdapter(this, R.layout.spinner_item_1, month_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        month.setAdapter(adapter);
        month.setSelection(12, true);
        adapter = new ArrayAdapter(this, R.layout.spinner_item_1, day_none);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        day.setAdapter(adapter);
        day.setSelection(0, true);

        for (int i = 0; i < 31; i++) {
            temp = String.valueOf(1 + i);
            day_arr1[i] = temp;
        }
        adapter1 = new ArrayAdapter(this, R.layout.spinner_item_1, day_arr1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        for (int i = 0; i < 30; i++) {
            temp = String.valueOf(1 + i);
            day_arr2[i] = temp;
        }
        adapter2 = new ArrayAdapter(this, R.layout.spinner_item_1, day_arr2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        for (int i = 0; i < 29; i++) {
            temp = String.valueOf(1 + i);
            day_arr3[i] = temp;
        }
        adapter3 = new ArrayAdapter(this, R.layout.spinner_item_1, day_arr3);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        for (int i = 0; i < 28; i++) {
            temp = String.valueOf(1 + i);
            day_arr4[i] = temp;
        }
        adapter4 = new ArrayAdapter(this, R.layout.spinner_item_1, day_arr4);
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        str1 = (String) year.getSelectedItem();
        str2 = (String) month.getSelectedItem();

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str1 = (String) year.getSelectedItem();
                if (str1 != "年份" && str2 != "月份") {
                    run = Integer.parseInt(str1);
                    runyue = Integer.parseInt(str2);
                    judge1 = ((run % 100 == 0) && (run % 400 == 0)) || ((run % 100 != 0) && (run % 4 == 0));
                    if (runyue == 1 || runyue == 3 || runyue == 5 || runyue == 7 || runyue == 8 || runyue == 10 || runyue == 12) {
                        day.setAdapter(adapter1);
                    } else if (runyue == 2 && judge1) {
                        day.setAdapter(adapter3);
                    } else if (runyue == 2 && !judge1) {
                        day.setAdapter(adapter4);
                    } else {
                        day.setAdapter(adapter2);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str2 = (String) month.getSelectedItem();
                if (str1 != "年份" && str2 != "月份") {
                    run = Integer.parseInt(str1);
                    runyue = Integer.parseInt(str2);
                    judge1 = ((run % 100 == 0) && (run % 400 == 0)) || ((run % 100 != 0) && (run % 4 == 0));
                    if (runyue == 1 || runyue == 3 || runyue == 5 || runyue == 7 || runyue == 8 || runyue == 10 || runyue == 12) {
                        day.setAdapter(adapter1);
                    } else if (runyue == 2 && judge1) {
                        day.setAdapter(adapter3);
                    } else if (runyue == 2 && !judge1) {
                        day.setAdapter(adapter4);
                    } else {
                        day.setAdapter(adapter2);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        boolean ss = stick.isChecked();

        back.setOnClickListener(new Button.OnClickListener() {//创建监听
            public void onClick(View v) {
                if (!note.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                    builder.setTitle("提示信息");
                    builder.setMessage("确定要放弃保存?");
                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent it=new Intent(NoteActivity.this,MainActivity.class);
                            startActivity(it);
                            finish();
                        }
                    });
                    builder.create().show();
                } else {
                    Intent it=new Intent(NoteActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        });

//        save1.setOnClickListener(new Button.OnClickListener() {//创建监听
//            public void onClick(View v) {
//                if (note.getText().toString().equals("") || str1.equals("年份") || str2.equals("月份") || day.getSelectedItem().toString().equals("日期")) {
//                    TastyToast.makeText(getApplicationContext(), "信息未填完整", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
//                    return;
//                } else {
//                    addNote(User.uToken,note.getText().toString(),str1,str2,day.getSelectedItem().toString(),String.valueOf(stick.isChecked()));
//                }
//            }
//        });
        save.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                if(note.getText().toString().equals("")||str1.equals("年份")||str2.equals("月份")||day.getSelectedItem().toString().equals("日期")){
                    TastyToast.makeText(getApplicationContext(), "信息未填完整", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    return;
                }
                else{
                    addNote(User.uToken,note.getText().toString(),str1,str2,day.getSelectedItem().toString(),String.valueOf(stick.isChecked()));
                }
            }
        });
    }
    public void addNote(String token,String txt,String year,String month,String day,String stick){
        url = "http://192.168.56.1/Home/NoteApi/writeNote";
        String date_he = year+"-"+month+"-"+day;
        String stick_judge = new String();
        if(stick.equals("true"))
            stick_judge="1";
        else if(stick.equals("false"))
            stick_judge="0";
        try {
            data = "token=" + URLEncoder.encode(token, "UTF-8") +
                    "&date=" + URLEncoder.encode(date_he, "UTF-8") +
                    "&stick=" + URLEncoder.encode(stick_judge, "UTF-8") +
                    "&note=" + URLEncoder.encode(txt, "UTF-8");
            Thread t = new Thread(){
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForWriteNote.sendMessage(msg);
                }
            };
            t.start();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForWriteNote=new Handler(){
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
                    TastyToast.makeText(getApplicationContext(),json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Intent it=new Intent(NoteActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getApplicationContext(),json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        Intent it=new Intent(NoteActivity.this,MainActivity.class);
        startActivity(it);
        finish();
    }
}
