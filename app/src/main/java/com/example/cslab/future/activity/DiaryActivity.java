package com.example.cslab.future.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.Diary_bg_choice;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.picture_choice;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/18.
 */

public class DiaryActivity extends AppCompatActivity {
    public static DiaryActivity me;
    private EditText diary_editText;
    private TextView diary_time;
    private ImageView imageView1;       //选择图片
    private ImageView imageView2;       //保存
    private Button returnback;
    private StringBuffer time;          //写日记时间
    private String file;                //日记背景保存路径

    private ImageView imagecolor1 ;
    private ImageView imagecolor2 ;
    private ImageView imagecolor3 ;
    private ImageView imagecolor4 ;
    private  ImageView fabIcon ;
    private int colornum;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_diary);
        me = this;
        diary_editText = (EditText) findViewById(R.id.diary);
        imageView2 = (ImageView) findViewById(R.id.upcloud);
        diary_time=(TextView)findViewById(R.id.diary_time);
        returnback=(Button)findViewById(R.id.returntoday);
        colornum=0;         //0黑色，1白色，2黄色，3蓝色

        //获取当前系统时间
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        time=new StringBuffer();
        time.append(String.valueOf(year)+"-");
        time.append(String.valueOf(month)+"-");
        time.append(String.valueOf(date)+"-");
        diary_time.setText(time);


        //获取背景路径并设为背景

        Bundle bundle=new Bundle();
        bundle=this.getIntent().getExtras();
        if(bundle!=null) {
             file = bundle.getString("bg_location");
            Log.i("where----", file);
//            Toast.makeText(getApplicationContext(),file,Toast.LENGTH_LONG).show();
            try {
                FileInputStream fis=new FileInputStream(file);
                Bitmap bg= BitmapFactory.decodeStream(fis);
                diary_editText.setBackground(new BitmapDrawable(bg));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }



        //保存日记
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diary = diary_editText.getText().toString();
                String style = "fontColor-" + colornum;
                String filePath = file;
                if (file==null){
                    new SweetAlertDialog(DiaryActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText("写日记必须要上传一张背景图")
                            .setConfirmText("了解")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }else if (diary.equals("")){
                    new SweetAlertDialog(DiaryActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText("写点东西吧，不然不给你提交")
                            .setConfirmText("了解")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
                    writeDiary(User.uToken,diary,style,filePath,fileName);
                }


            }
        });

        ///////////////////////悬浮按钮///////////////
        imagecolor1 = new ImageView(this);
        imagecolor2 = new ImageView(this);
        imagecolor3 = new ImageView(this);
        imagecolor4 = new ImageView(this);
        fabIcon = new ImageView(this);
        fabIcon.setBackground(getResources().getDrawable(R.drawable.ic_add));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIcon)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);


        imagecolor1.setImageDrawable(getResources().getDrawable(R.drawable.ic_white));
        imagecolor2.setImageDrawable(getResources().getDrawable(R.drawable.ic_blue));
        imagecolor4.setImageDrawable(getResources().getDrawable(R.drawable.ic_red));
        imagecolor3.setImageDrawable(getResources().getDrawable(R.drawable.ic_pic));

        SubActionButton buttonQuit = itemBuilder.setContentView(imagecolor1).build();
        SubActionButton buttonPalette = itemBuilder.setContentView(imagecolor3).build();
        SubActionButton buttonTool = itemBuilder.setContentView(imagecolor2).build();
        SubActionButton buttonCamera = itemBuilder.setContentView(imagecolor4).build();


        final FloatingActionMenu buttonToolMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonPalette)
                .addSubActionView(buttonCamera)
                .addSubActionView(buttonTool)
                .addSubActionView(buttonQuit)
                .setStartAngle(-90)
                .setEndAngle(-180)
                .attachTo(actionButton)
                .build();


        imagecolor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colornum==1)
                    diary_editText.setTextColor(getResources().getColor(R.color.black));
                else
                    diary_editText.setTextColor(getResources().getColor(R.color.white));


            }
        });

        imagecolor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colornum==2)
                    diary_editText.setTextColor(getResources().getColor(R.color.black));
                else
                    diary_editText.setTextColor(getResources().getColor(R.color.blue));
            }
        });

        imagecolor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colornum==3)
                    diary_editText.setTextColor(getResources().getColor(R.color.black));
                else
                    diary_editText.setTextColor(getResources().getColor(R.color.red));
            }
        });

        //插入背景图片
        imagecolor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiaryActivity.this, Diary_bg_choice.class));
            }
        });


        buttonToolMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // 增加按钮中的+号图标顺时针旋转45度
                // Rotate the icon of fabButton 45 degrees clockwise
                fabIcon.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIcon, pvhR);
                animation.start();
            }
            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // 增加按钮中的+号图标逆时针旋转45度
                // Rotate the icon of fabButton 45 degrees
                // counter-clockwise
                fabIcon.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIcon, pvhR);
                animation.start();
            }
        });
        //返回按键监听事件
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(DiaryActivity.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        });

    }



    //返回主界面
    @Override
    public void onBackPressed()
    {
        Intent it=new Intent(DiaryActivity.this,MainActivity.class);
        startActivity(it);
        finish();
    }

    private String urlForWriteDiary;
    private String dataForWriteDiary;
    private String urlForUploadDiaryImg;
    private String urlForGetDiaryImg;
    public void writeDiary(String token, final String diary, String style, final String filePath, final String fileName){
        urlForWriteDiary = "http://192.168.56.1/Home/TodayApi/writeDiary";
        try {
            dataForWriteDiary = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&diary="+ URLEncoder.encode(diary, "UTF-8")+
                    "&style="+ URLEncoder.encode(style, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStrForWriteDiary;
                    String returnStrForUploadImg = null;
                    returnStrForWriteDiary = PostUtils.sendPost(urlForWriteDiary, dataForWriteDiary);

                    try {
                        Log.i("value",returnStrForWriteDiary);
                        JSONTokener jsonParser = new JSONTokener(returnStrForWriteDiary);
                        JSONObject json1 = (JSONObject) jsonParser.nextValue();
                        if (json1.optString("success")!=""){
                            JSONArray jsonArray =  new JSONArray(json1.optString("todayDiary"));
                            JSONObject j = (JSONObject)jsonArray.get(0);
                            String diaryId = j.optString("id");
                            Log.i("diaryId",diaryId);
                            Log.i("fileName",fileName);
                            Log.i("filePath",filePath);
                            Log.i("fileName",fileName);

                            urlForUploadDiaryImg = "http://192.168.56.1/Home/TodayApi/uploadDiaryImg";
                            urlForGetDiaryImg = "http://192.168.56.1/Img/diary/" + fileName;
                            returnStrForUploadImg = PostUtils.uploadFile(urlForUploadDiaryImg,fileName,filePath,diaryId);
//                            Log.i("returnStrForUploadImg",returnStrForUploadImg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStrForWriteDiary);
                    if (returnStrForUploadImg!=null) {
                        data.putString("value2", returnStrForUploadImg);
                    }
                    msg.setData(data);
                    handlerForWriteDiary.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForWriteDiary =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            String val2 = data.getString("value2");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();
                if(json.optString("success")!=""){
                    JSONArray jsonArray =  new JSONArray(json.optString("todayDiary"));
                    JSONTokener jsonParser2 = new JSONTokener(val2);
                    JSONObject json2 = (JSONObject) jsonParser2.nextValue();
                    if (json2.optString("success")!=""){
                        JSONObject j = (JSONObject)jsonArray.get(0);
                        Diary diary = new Diary();
                        diary.setuDiaryPic(json2.optString("uDiaryPic"));
                        diary.setuStyle(j.optString("uStyle"));
                        diary.setId(j.optInt("id"));
                        diary.setuTime(j.optString("uTime"));
                        diary.setuDiary(j.optString("uDiary"));
                        diary.setuId(j.optInt("uId"));
                        User.addDiary(diary);
                    }

                    new SweetAlertDialog(DiaryActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(json.optString("success"))
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent it=new Intent(DiaryActivity.this,MainActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            })
                            .show();
                }else {
                    new SweetAlertDialog(DiaryActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent it=new Intent(DiaryActivity.this,MainActivity.class);
                                    startActivity(it);
                                    finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };



}
