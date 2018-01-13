package com.example.cslab.future.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.adapter.CardDataImpl;
import com.example.cslab.future.adapter.CardListItemAdapter;
import com.example.cslab.future.adapter.MyFragmentPageAdapter;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.Dream;
import com.example.cslab.future.entity.Plan;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.question.question1_activity;
import com.example.cslab.future.sql.DreamSql;
import com.example.cslab.future.sql.PlanSql;
import com.example.cslab.future.util.BroadCastManager;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.Screen;
import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;
import com.sdsmdg.tastytoast.TastyToast;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.R.attr.data;
import static android.R.attr.format;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static com.example.cslab.future.R.id.date;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private String urlForGetUserInfo;
    private String dataForGetUserInfo;
    private String urlForGetPlan;
    private String dataForGetPlan;
    private String urlForGetDiary;
    private String dataForGetDiary;


    private RadioGroup bottom_tab_bar;
    private RadioButton bottom_today;
    private RadioButton bottom_yesterday;
    private RadioButton bottom_tomorrow;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private TextView id_username;
    private TextView id_dream;

    private List<Fragment> list;
    private ViewPager mainContent;
    private MyFragmentPageAdapter adapter;

    private TimeSelector timeSelector;
    public static final int YESTERDAY = 0;
    public static final int TODAY = 1;
    public static final int TOMORROW = 2;
    ////////////////////////////////
    private FrameLayout frame;
    private int word = 1, STOP_UP_flag, UP_flag, walk_L_flag, stand_flag, fall_flag, climbUp_L_flag, walk_flag,
            crawl_top_R_flag, crawl_top_L_flag, fall_lie_flag, walk_R_flag, climbUp_R_flag, walk_LR;
    float downX, downY;
    float moveX, moveY;
    private float HEIGHT;
    private float WIDTH;

    private float Xspeed;
    private float Yspeed;
    private VelocityTracker mVelocityTracker;
    int result = 0;
    int i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0, i6 = 0, i7 = 0, i8 = 0, i9 = 0, j1 = 0, j2 = 0, j3 = 0, j4 = 0, j5 = 0, j6 = 0;
    private String dirImg_head;
    private String urlForGetRandomHead;                 //获取随机头像地址
    private String urlForGetHead;                       //服务器存放头像的地址
    private String urlForChangeHead;                    //更改头像的url访问地址
    private String saveForGetHead;
    private  ImageView account;
    //////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews();
        initData();
        mainContent = (ViewPager) findViewById(R.id.mainContent);
        adapter = new MyFragmentPageAdapter(getSupportFragmentManager(), list);
        mainContent.setAdapter(adapter);
        mainContent.setCurrentItem(1);
        bottom_today.setChecked(true);
        mainContent.addOnPageChangeListener(this);
///////////////////////////////////////////
        frame = (FrameLayout) findViewById(R.id.mylayout);
        Screen.init(this);
        HEIGHT = Screen.SCREEN_HEIGHT;
        WIDTH = Screen.SCREEN_WIDTH;
        frame.getLayoutParams().height = (int) HEIGHT / 8;
        frame.getLayoutParams().width = (int) WIDTH / 5;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        frame.setForeground(getResources().getDrawable(R.drawable.shime1));
        comprehensive();
        ll();

//        ------------------------------以下都是使用实例-------------------------
////        PlanSql.initSql(this);
//        DreamSql.init(this);
//        DreamSql.addOneDream(this,1,"kid","291371205@qq.com","2017-7-15","D://wamp","南京",20,"1997-08-15",1,"篮球","啦啦啦","D://wanp",1000,1200);
//        List<Map<String ,Object>> list = new ArrayList<>();
//        for(int i=0;i<30;i++){
//            Map<String, Object> data_insert = new HashMap<>();
//            data_insert.put("uId", i);
//            data_insert.put("uName", "kid");
//            data_insert.put("uEmail", "291371205@qq.com");
//            data_insert.put("uRegTime", "2017-7-15");
//            data_insert.put("uImgSrc", "D://wamp");
//            data_insert.put("uCity", "南京");
//            data_insert.put("uAge", 20);
//            data_insert.put("uBirth", "1997-08-15");
//            data_insert.put("uSex", 1);
//            data_insert.put("uInterest", "l安丘");
//            data_insert.put("uDream", "啦啦啦");
//            data_insert.put("uDreamPic","D://wamp" );
//            data_insert.put("dreamClickNum",i );
//            data_insert.put("dreamCommentNum",i );
//            list.add(data_insert);
//        }
//        DreamSql.addDreamsToSql(this,list);
//        list = DreamSql.queryAllDream(this);
//        for (int j=0;j<list.size();j++){
//            Map<String ,Object> map = new HashMap<>();
//            map = list.get(j);
//            Log.i("dbbbb", String.valueOf(map.get("uId")));
//            Log.i("dbbbb", String.valueOf(map.get("uName")));
//        }

        //cj   --------------------
        PlanSql.init(this);
        PlanSql.insert(0, 1, "Type", 2, "askfjals", 1, "2017-07-18");
        List<Plan> list = PlanSql.queryAll();
        for (int i = 0; i < list.size(); i++) {
            Plan p = list.get(i);
            Log.i("SQL", String.valueOf(p.getId()) + "-" + String.valueOf(p.getuId()) + "-" + p.getuType() + "-" + String.valueOf(p.getuTime()));
        }


        //MainActivity为默认开始页面   首先判断数据库是否有token   如果没有则返回loginActivity

        //查询数据库******暂时不做

        //判断token
        if (User.uToken.equals("")) {
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        } else {
            Log.i("token", User.uToken);

            //        获取userInfo
            /*查询本地数据库*/
            //if(!User.getUserInfo()){      如果本地不存在
            getUserInfo(User.uToken);       //访问服务器
            //}
            timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
//                    Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();

                    //请求服务器
                    //                    先找本地
//                    if(!getDiaryAndPlan(token,time)){
                    getDiaryAndPlanForTime(User.uToken, time);
//                    }
                    //可选择的起止时间    在屏幕上只用截取年月日
                }
            }, "2000-7-18 00:00", "2020-12-1 00:00");
        }
    }

    public void comprehensive() {
        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 1)              //站立
                {
                    frame.setForeground(getResources().getDrawable(R.drawable.shime1));
                }
                if (msg.what == 2)            //下落
                {
                    frame.setForeground(getResources().getDrawable(R.drawable.shime45));
                    frame.setY(frame.getY() + HEIGHT / 1500);
                    if (frame.getY() >= HEIGHT - frame.getHeight() * 1.5) {
                        fall_flag = 0;
                        walk_flag = 1;
                        fall_lie_flag = 1;
                        frame.setY(HEIGHT - frame.getHeight() * 3 / 2);
                        if (walk_LR == 0) {
                            frame.setForeground(getResources().getDrawable(R.drawable.shime2_r));
                        } else {
                            frame.setForeground(getResources().getDrawable(R.drawable.shime2_r));
                        }
                    }
                }
                if (msg.what == 3)              //向左走
                {

                    i3++;
                    Drawable a = getResources().getDrawable(R.drawable.shime2);
                    Drawable b = getResources().getDrawable(R.drawable.shime3);
                    switch (i3 % 400) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 200:
                            frame.setForeground(b);
                            break;
                    }
                    frame.setX(frame.getX() - WIDTH / 5000);
                    if (frame.getX() < -WIDTH * 0.05) {
                        walk_L_flag = 0;
                        frame.setX(-WIDTH / 10);
                        climbUp_L_flag = 1;
                        frame.setForeground(getResources().getDrawable(R.drawable.shime14));
                    }

                }
                if (msg.what == 4)                //向右走
                {
                    i4++;
                    Drawable a = getResources().getDrawable(R.drawable.shime2_r);
                    Drawable b = getResources().getDrawable(R.drawable.shime3_r);
                    switch (i4 % 400) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 200:
                            frame.setForeground(b);
                            break;
                    }
                    frame.setX(frame.getX() + WIDTH / 5000);
                    if (frame.getX() > WIDTH * 0.85) {
                        walk_R_flag = 0;
                        frame.setX(WIDTH * 9 / 10);
                        climbUp_R_flag = 1;
                        frame.setForeground(getResources().getDrawable(R.drawable.shime14_r));
                    }
                }
                if (msg.what == 5)           //左上爬
                {
                    i5++;
                    Drawable a = getResources().getDrawable(R.drawable.shime13);
                    Drawable b = getResources().getDrawable(R.drawable.shime12);
                    Drawable c = getResources().getDrawable(R.drawable.shime14);

                    switch (i5 % 600) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 200:
                            frame.setForeground(b);
                            break;
                        case 400:
                            frame.setForeground(c);
                            break;
                    }
                    frame.setY(frame.getY() - HEIGHT / 7500);
                    if (frame.getY() < result / 2) {
                        frame.setY(-frame.getHeight() * 2 / 5);
                        climbUp_L_flag = 0;
                        crawl_top_R_flag = 1;
                        frame.setForeground(getResources().getDrawable(R.drawable.shime23_r));
                    }
                }
                if (msg.what == 6)             //右上爬
                {
                    i6++;
                    Drawable a = getResources().getDrawable(R.drawable.shime13_r);
                    Drawable b = getResources().getDrawable(R.drawable.shime12_r);
                    Drawable c = getResources().getDrawable(R.drawable.shime14_r);

                    switch (i6 % 600) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 200:
                            frame.setForeground(b);
                            break;
                        case 400:
                            frame.setForeground(c);
                            break;
                    }
                    frame.setY(frame.getY() - HEIGHT / 7500);
                    if (frame.getY() < result / 2) {
                        frame.setY(-frame.getHeight() * 2 / 5);
                        climbUp_R_flag = 0;
                        crawl_top_L_flag = 1;
                        frame.setForeground(getResources().getDrawable(R.drawable.shime23));
                    }
                }
                if (msg.what == 7)         //TOP右爬
                {
                    i7++;
                    Drawable a = getResources().getDrawable(R.drawable.shime25_r);
                    Drawable b = getResources().getDrawable(R.drawable.shime24_r);
                    Drawable c = getResources().getDrawable(R.drawable.shime23_r);
                    switch (i7 % 600) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 200:
                            frame.setForeground(b);
                            break;
                        case 400:
                            frame.setForeground(c);
                            break;
                    }
                    frame.setX(frame.getX() + WIDTH / 3000);
                    if (frame.getX() > WIDTH * 0.5 - frame.getWidth() / 2) {
                        crawl_top_R_flag = 0;
                        fall_flag = 1;
                    }
                }
                if (msg.what == 8)           //TOP左爬
                {
                    i8++;
                    Drawable a = getResources().getDrawable(R.drawable.shime25);
                    Drawable b = getResources().getDrawable(R.drawable.shime24);
                    Drawable c = getResources().getDrawable(R.drawable.shime23);
                    switch (i8 % 600) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 200:
                            frame.setForeground(b);
                            break;
                        case 400:
                            frame.setForeground(c);
                            break;
                    }
                    frame.setX(frame.getX() - WIDTH / 3000);
                    if (frame.getX() < WIDTH * 0.5 - frame.getWidth() / 2) {
                        crawl_top_L_flag = 0;
                        fall_flag = 1;
                    }
                }
                if (msg.what == 9) {
                    i9++;
                    Drawable a = getResources().getDrawable(R.drawable.shime18);
                    Drawable b = getResources().getDrawable(R.drawable.shime19);
                    switch (i9 % 2) {
                        case 0:
                            frame.setForeground(a);
                            break;
                        case 1:
                            frame.setForeground(b);
                            fall_lie_flag = 0;
                            stand_flag = 1;
                            break;
                    }
                }
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //发送一条空信息来通知系统改变前景图片
                if(word == 1) {
                    if (frame.getY() >= HEIGHT - frame.getHeight() * 3 / 2 && walk_flag != 1) {
                        if (walk_LR == 0) {
                            walk_R_flag = 1;
                        } else {
                            walk_L_flag = 1;
                        }
                    }
                    if (stand_flag == 1) {
                        handler.sendEmptyMessage(1);
                    }
                    if (fall_flag == 1) {
                        handler.sendEmptyMessage(2);
                    }
                    if (walk_L_flag == 1) {
                        handler.sendEmptyMessage(3);
                    }
                    if (walk_R_flag == 1) {
                        handler.sendEmptyMessage(4);
                    }
                    if (climbUp_L_flag == 1) {
                        handler.sendEmptyMessage(5);
                    }
                    if (climbUp_R_flag == 1) {
                        handler.sendEmptyMessage(6);
                    }
                    if (crawl_top_R_flag == 1) {
                        handler.sendEmptyMessage(7);
                    }
                    if (crawl_top_L_flag == 1) {
                        handler.sendEmptyMessage(8);
                    }
                    if (fall_lie_flag == 1) {
                        handler.sendEmptyMessage(9);
                    }
                    if (stand_flag == 1 || fall_flag == 1 || walk_L_flag == 1 || walk_R_flag == 1 || climbUp_L_flag == 1 || climbUp_R_flag == 1 || crawl_top_R_flag == 1 || crawl_top_L_flag == 1 || fall_lie_flag == 1) {
                        STOP_UP_flag = 1;
                    }
                }
            }
        }, 0, 1);
    }//桌宠

    public void ll() {
        frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(word ==1) {
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(motionEvent);//将事件加入到VelocityTracker类实例中
                    //判断当ev事件是MotionEvent.ACTION_UP时：计算速率
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    // 1000 provides pixels per second
                    velocityTracker.computeCurrentVelocity(1, (float) 0.01); //设置maxVelocity值为0.1时，速率大于0.01时，显示的速率都是0.01,速率小于0.01时，显示正常
                    //  Log.i("test","velocityTraker"+velocityTracker.getXVelocity());
                    velocityTracker.computeCurrentVelocity(1000); //设置units的值为1000，意思为一秒时间内运动了多少个像素
                    Log.i("test", "velocityTraker" + velocityTracker.getXVelocity());
                    Xspeed = velocityTracker.getXVelocity();
                    Yspeed = velocityTracker.getYVelocity();
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            Log.i("frame", "touch");
                            downX = motionEvent.getRawX();
                            downY = motionEvent.getRawY();
                            UP_flag = 0;
                            STOP_UP_flag = 0;
                            walk_L_flag = 0;
                            walk_R_flag = 0;
                            crawl_top_R_flag = 0;
                            crawl_top_L_flag = 0;
                            stand_flag = 0;
                            fall_flag = 0;
                            climbUp_L_flag = 0;
                            climbUp_R_flag = 0;
                            fall_lie_flag = 0;
                            walk_flag = 0;
                            frame.setForeground(getResources().getDrawable(R.drawable.shime1));
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            STOP_UP_flag = 0;
                            walk_L_flag = 0;
                            walk_R_flag = 0;
                            crawl_top_R_flag = 0;
                            crawl_top_L_flag = 0;
                            stand_flag = 0;
                            fall_flag = 0;
                            climbUp_L_flag = 0;
                            climbUp_R_flag = 0;
                            fall_lie_flag = 0;
                            walk_flag = 0;
                            moveX = motionEvent.getRawX();
                            moveY = motionEvent.getRawY();
                            frame.setX(frame.getX() + (moveX - downX));
                            frame.setY(frame.getY() + (moveY - downY));
                            downX = moveX;
                            downY = moveY;
                            UP_flag = 0;

                            frame.setForeground(getResources().getDrawable(R.drawable.shime1));
                            break;

                        }
                        case MotionEvent.ACTION_UP: {
                            UP_flag = 1;
                            walk_L_flag = 0;
                            walk_R_flag = 0;
                            crawl_top_R_flag = 0;
                            crawl_top_L_flag = 0;
                            stand_flag = 0;
                            fall_flag = 0;
                            climbUp_L_flag = 0;
                            climbUp_R_flag = 0;
                            fall_lie_flag = 0;
                            walk_flag = 0;
                            walk_LR = (int) (Math.random() * 2);
                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (STOP_UP_flag != 1) {
                                        if (frame.getY() < HEIGHT - frame.getHeight() * 1.5) {
                                            if (Xspeed <= 0) {
                                                frame.setForeground(getResources().getDrawable(R.drawable.shime4));
                                            } else {
                                                frame.setForeground(getResources().getDrawable(R.drawable.shime4_r));
                                            }

                                            if (frame.getX() > WIDTH * 0.85 && frame.getY() <= HEIGHT + frame.getHeight() * 10 && frame.getY() > frame.getHeight() * 1 / 3) {
                                                frame.setX(WIDTH * 9 / 10);
                                                frame.setForeground(getResources().getDrawable(R.drawable.shime13_r));

                                            } else {
                                                if (frame.getX() < -WIDTH * 0.05 && frame.getY() <= HEIGHT + frame.getHeight() * 10 && frame.getY() > frame.getHeight() * 1 / 3) {
                                                    frame.setX(0 - WIDTH / 10);
                                                    frame.setForeground(getResources().getDrawable(R.drawable.shime13));

                                                } else {
                                                    if (Xspeed > WIDTH * 4) {
                                                        Xspeed = WIDTH * 4;
                                                    }
                                                    if (Xspeed < -WIDTH * 4) {
                                                        Xspeed = -WIDTH * 4;
                                                    }
                                                    if (Yspeed < -HEIGHT * 5) {
                                                        Yspeed = -HEIGHT * 5;
                                                    }
                                                    if (Yspeed < 0) {
                                                        frame.setY(frame.getY() + HEIGHT / 1000 + Yspeed / 1000);
                                                        frame.setX(frame.getX() + Xspeed / 6000);
                                                        Yspeed = Yspeed + HEIGHT / 1000;
                                                    } else {
                                                        frame.setY(frame.getY() + HEIGHT / 2000);
                                                        frame.setX(frame.getX() + Xspeed / 6000);
                                                    }
                                                }
                                            }

                                        } else {
                                            frame.setY(HEIGHT - frame.getHeight() * 3 / 2);

                                            frame.setForeground(getResources().getDrawable(R.drawable.shime1));
                                        }
                                        if (frame.getY() < -HEIGHT / 2 || (frame.getY() <= -frame.getHeight() * 2 / 3 && frame.getX() <= -frame.getWidth()) || (frame.getY() <= -frame.getHeight() * 2 / 3 && frame.getX() >= WIDTH)) {
                                            frame.setX(WIDTH / 2);
                                            frame.setY(-HEIGHT / 3);
                                            Xspeed = 0;
                                            Yspeed = 0;
                                        }
                                    }
                                }
                            };
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (UP_flag == 1) {
                                        handler.sendEmptyMessage(0);
                                    } else {
                                        this.cancel();
                                    }
                                }
                            }, 0, 1);
                            break;
                        }
                    }
                }
                return true;// 返回true 表示 处理Touch事件
            }


        });
    }//桌宠


    public void getUserInfo(String token) {
        urlForGetUserInfo = "http://192.168.56.1/Home/UserApi/getUserInfo";
        try {
            dataForGetUserInfo = "token=" + URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetUserInfo, dataForGetUserInfo);

                    //下载自己的头像到聊天室使用
                    boolean returnFlag2;
                    JSONTokener jsonParser = new JSONTokener(returnStr);
                    try {
                        JSONObject json = (JSONObject) jsonParser.nextValue();
                        String urlGetMyImg = json.optString("imgsrc");
                        String saveForGetMyImg = getApplicationContext().getFilesDir().getAbsolutePath() + "/Img_friendHead/" + json.optInt("id") + ".jpg";
                        File my_pic = new File(saveForGetMyImg);
                        Log.i("friend_pic", saveForGetMyImg);
                        if (!my_pic.exists()) {            //如果本地不存在，则去服务器获取
                            returnFlag2 = PostUtils.saveUrlAs(urlGetMyImg, saveForGetMyImg);
                            Log.i("friendFlag", String.valueOf(returnFlag2));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetUserInfo.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }//个人信息

    private Handler handlerForGetUserInfo = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();
                Log.i("23", val);
                if (json.optString("success") != "") {
                    Log.i("22", json.optString("success"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    //设置到本地并且缓存到本地数据库
                    User.setUserInfo(json.optInt("id"), json.optString("name"), json.optString("email"), json.optString("password"), json.optString("regtime"), json.optString("imgsrc"), json.optString("city"), json.optInt("age"), json.optString("birth"), json.optInt("sex"), json.optString("interest"), json.optString("dream"), json.optString("dreampic"), json.optInt("dreamclicknum"), json.optInt("dreamcommentnum"));
                    id_dream.setText(User.uDream);
                    id_username.setText(User.uName);
                } else {
                    Log.i("22", json.optString("error"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22", val);
        }
    };


    public void getDiaryAndPlanForTime(String token, String time) {
        urlForGetPlan = "http://192.168.56.1/Home/YesterdayApi/showAnyPlan";
        urlForGetDiary = "http://192.168.56.1/Home/YesterdayApi/showAnyDiary";
        try {
            dataForGetPlan = "token=" + URLEncoder.encode(token, "UTF-8") +
                    "&time=" + URLEncoder.encode(time, "UTF-8");
            dataForGetDiary = "token=" + URLEncoder.encode(token, "UTF-8") +
                    "&time=" + URLEncoder.encode(time, "UTF-8");
            Thread t = new Thread() {
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
                    handlerForGetPlanAndDiaryForTime.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetPlanAndDiaryForTime = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String valPlan = data.getString("valuePlan");
            String valDiary = data.getString("valueDiary");
            //获取计划
            try {
                JSONTokener jsonParser1 = new JSONTokener(valPlan);
                JSONObject jsonPlan = (JSONObject) jsonParser1.nextValue();
                if (jsonPlan.optString("error") == "") {
                    Plan plan = new Plan();
                    plan.setId(jsonPlan.optInt("id"));
                    plan.setuId(jsonPlan.optInt("uId"));
                    plan.setuType(jsonPlan.optString("uType"));
                    plan.setTypeId(jsonPlan.optInt("typeId"));
                    plan.setuRemark(jsonPlan.optString("uRemark"));
                    plan.setuFinish(jsonPlan.optInt("uFinish"));
                    plan.setuTime(jsonPlan.optString("uTime"));
                    User.addPlan(plan);
                } else {
                    TastyToast.makeText(getApplicationContext(), jsonPlan.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }

//                for (int i=0;i<User.plans.length;i++){
//                    Log.i("plans",User.plans[i].getuTime());
//                }

                JSONTokener jsonParser2 = new JSONTokener(valDiary);
                JSONObject jsonDiary = (JSONObject) jsonParser2.nextValue();
                if (jsonDiary.optString("error") == "") {
                    Diary diary = new Diary();
                    diary.setuStyle(jsonDiary.optString("uStyle"));
                    diary.setId(jsonDiary.optInt("id"));
                    diary.setuTime(jsonDiary.optString("uTime"));
                    diary.setuDiary(jsonDiary.optString("uDiary"));
                    diary.setuId(jsonDiary.optInt("uId"));
                    diary.setuDiaryPic(jsonDiary.optString("uDiaryPic"));
                    User.addDiary(diary);
                } else {
                    TastyToast.makeText(getApplicationContext(), jsonDiary.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }

//                for (int i=0;i<User.diaries.length;i++){
//                    Log.i("diaries",User.diaries[i].getuTime());
//                }

                //发送广播
                Intent intent = new Intent();
                intent.putExtra("order", "421412");
                intent.setAction("fragment_home_left");
                BroadCastManager.getInstance().sendBroadCast(MainActivity.this, intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("22", valPlan);
            Log.i("22", valDiary);
        }
    };


    public void initViews() {
        bottom_tab_bar = (RadioGroup) findViewById(R.id.bottom_tab_bar);
        bottom_today = (RadioButton) findViewById(R.id.today);
        bottom_yesterday = (RadioButton) findViewById(R.id.yesterday);
        bottom_tomorrow = (RadioButton) findViewById(R.id.tomorroy);
        bottom_tab_bar.setOnCheckedChangeListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    Intent t1 = new Intent(MainActivity.this, PlanActivity.class);
                    startActivity(t1);
                    finish();
                } else if (menuItemId == R.id.action_notification) {
                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    Intent t2 = new Intent(MainActivity.this, DiaryActivity.class);
                    startActivity(t2);
                    finish();
                } else if (menuItemId == R.id.action_item1) {
                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    Intent t3 = new Intent(MainActivity.this, NoteActivity.class);
                    startActivity(t3);
                    finish();
                }
                return true;

            }
        });

        list = new ArrayList<Fragment>();
        list.add(new YesterdayActivity());
        list.add(new TodayActivity());
        list.add(new TomorrowActivity());

        mNavigation = (NavigationView) findViewById(R.id.navigation_view);
        setNavigationView(mNavigation);
        mNavigation.setItemIconTintList(null);
        //获取头布局文件
        View drawerView = mNavigation.inflateHeaderView(R.layout.header_just_username);
        account = (ImageView) drawerView.findViewById(R.id.header);
        id_username=(TextView)drawerView.findViewById(R.id.id_username);
        id_dream=(TextView)drawerView.findViewById(R.id.id_dream);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Person_Data_Activity.class));
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

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
                account.setImageBitmap(bmp);
            }
        }
    }

    private void initData() {

        // 参数：开启抽屉的activity、DrawerLayout的对象、toolbar按钮打开关闭的对象、描述open drawer、描述close drawer
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        // 添加抽屉按钮，通过点击按钮实现打开和关闭功能; 如果不想要抽屉按钮，只允许在侧边边界拉出侧边栏，可以不写此行代码
        mDrawerToggle.syncState();
        // 设置按钮的动画效果; 如果不想要打开关闭抽屉时的箭头动画效果，可以不写此行代码
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    Date date = new Date();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String time = format.format(date);

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int CheckId) {
        switch (CheckId) {
            case R.id.yesterday:
                mainContent.setCurrentItem(YESTERDAY);
                TextView textView = (TextView) findViewById(R.id.tv_title);
                textView.setText("选择日期");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeSelector.setMode(TimeSelector.MODE.YMD);
                        timeSelector.show();
                    }
                });
                break;
            case R.id.today:
                mainContent.setCurrentItem(TODAY);
                TextView textView1 = (TextView) findViewById(R.id.tv_title);
                textView1.setText("future");
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case R.id.tomorroy:
                mainContent.setCurrentItem(TOMORROW);
                TextView textView2 = (TextView) findViewById(R.id.tv_title);
                textView2.setText("future");
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (mainContent.getCurrentItem()) {
                case YESTERDAY:
                    bottom_yesterday.setChecked(true);
                    break;
                case TODAY:
                    bottom_today.setChecked(true);
                    break;
                case TOMORROW:
                    bottom_tomorrow.setChecked(true);
                    break;
            }

        }
    }

    //返回关闭抽屉
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
            finish();
        }
    }


    public void setNavigationView(NavigationView navigationView)   //设置菜单点击事件
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId2 = item.getItemId();
                if (menuItemId2 == R.id.nav_info) {
                    Intent ql_intent = new Intent(MainActivity.this, Person_Data_Activity.class);
                    startActivity(ql_intent);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                    drawer.closeDrawer(GravityCompat.START);

                } else if (menuItemId2 == R.id.nav_messages) {
                    word = ~(0^word);
                    if(word ==  -2){
                        frame.setClickable(false);
                        frame.setVisibility(View.GONE);
                        frame.getLayoutParams().height =  0;
                        frame.getLayoutParams().width =  0;
                        Toast.makeText(MainActivity.this,"桌宠已关闭",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        frame.setVisibility(View.VISIBLE);
                        frame.setClickable(true);
                        frame.getLayoutParams().height = (int) HEIGHT / 8;
                        frame.getLayoutParams().width = (int) WIDTH / 5;
                        Toast.makeText(MainActivity.this,"桌宠已打开",Toast.LENGTH_SHORT).show();
                    }
                } else if (menuItemId2 == R.id.nav_aboutus) {
                    new  AlertDialog.Builder(MainActivity.this)
                            .setTitle("出品团队" )
                            .setMessage("南航我爱喝雪碧团队" )
                            .setPositiveButton("确定" ,  null )
                            .show();
                } else if (menuItemId2 == R.id.nav_version) {
                    new  AlertDialog.Builder(MainActivity.this)
                            .setTitle("版本信息" )
                            .setMessage("time v3.1.0" )
                            .setPositiveButton("确定" ,  null )
                            .show();
                } else if (menuItemId2 == R.id.cancellation) {          //注销
                    User.userLogout();
                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(it);
                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    finish();
                } else if (menuItemId2 == R.id.exit) {
                    YesterdayActivity.yesterdayActivity.getContext().unregisterReceiver(YesterdayActivity.mReceiver);
                    finish();
                }
                return true;
            }

        });
    }
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
                    account.setImageBitmap(bmp);
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
                account.setImageBitmap(bmp);
            }
            Log.i("val本地没有head了", String.valueOf(val));
        }
    };

}
