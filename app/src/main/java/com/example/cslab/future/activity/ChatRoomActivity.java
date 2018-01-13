package com.example.cslab.future.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.Messages;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by CSLab on 2017/7/15.
 */

public class ChatRoomActivity extends AppCompatActivity{
    private Button outChatroom;
    private ListView conversationList;
    private List<MessageBean> listData = new ArrayList<>();

    private final int MSG_TYPE_SEND = 0;
    private final int MSG_TYPE_RECEIVE = 1;

    private String dirImg_friend;

    private EditText words;
    private Button sendMessage;
    private TextView chatroomTitle;
    private Button chatPeople;

    private ConversationAdapter adapter;
    private Thread getlatestmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        conversationList = (ListView) findViewById(R.id.converation_list);
        outChatroom = (Button) findViewById(R.id.outChatroom);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        chatroomTitle = (TextView) findViewById(R.id.chatroomTitle);
        chatPeople = (Button) findViewById(R.id.chatPeople);
        words = (EditText) findViewById(R.id.words);
        dirImg_friend = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_friendHead/";

        chatroomTitle.setText(User.plans[0].getuType());

        if (User.messages==null){
            getAllMessage(User.uToken,User.plans[0].getTypeId());
        }else {
            //渲染message
            MessageBean []messageBeen = new MessageBean[User.messages.length];
            for (int i=0;i<User.messages.length;i++) {
                int from = (User.messages[i].getuId() == User.uId) ? 1 : 0;
                String uname = User.messages[i].getuName();
                int sex = 2;
                int fire = 0;

                if (User.friends!=null) {
                    for (int j = 0; j < User.friends.length; j++) {
                        if (User.messages[i].getuId()==User.friends[j].getuId()){
                            sex = (User.friends[j].appearNum>0)? User.friends[j].getuSex():2;
                            fire = (User.friends[j].appearNum>0)? 1:0;
                        }
                    }
                }
                String saveForGetFriendImg = dirImg_friend + User.messages[i].getuId() + ".jpg";
                messageBeen[i] = new MessageBean(from, User.messages[i].getContent(), saveForGetFriendImg,uname,sex,fire);
            }
            initData(messageBeen);
            adapter = new ConversationAdapter();
            conversationList.setAdapter(adapter);
            conversationList.setSelection(conversationList.getBottom());
        }
        //定时器...
        if (User.messages!=null) {
            Log.i("message", "1");
        }else {
            Log.i("message","2");
        }
        if (User.messages!=null){
            //实时渲染数据
            getlatestmessage = new Thread(){
                @Override
                public void run() {

                    while (!isInterrupted()){ //非阻塞过程中通过判断中断标志来退出
                        try{
                            for (;;) {
                                Thread.sleep(1000);
                                getLatestMessage(User.uToken, User.messages[User.messages.length - 1].time, User.plans[0].getTypeId());
                            }
                        }catch(InterruptedException e){
                            e.printStackTrace();
                            break;//捕获到异常之后，执行break跳出循环。
                        }
                    }
                }
            };
            getlatestmessage.start();
        }
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = words.getText().toString();
                if (message.equals("")){
                    TastyToast.makeText(getApplicationContext(), "发送消息不能为空", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                }else{
                    sendMessageFun(User.uToken,User.uId,User.uName,User.uImgSrc,message,User.plans[0].getTypeId());
                }
            }
        });

        outChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlatestmessage.interrupt();
                finish();
            }
        });

        chatPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(ChatRoomActivity.this,GroupActivity.class);
                getlatestmessage.interrupt();
                startActivity(it);
                finish();
            }
        });
    }

    public void getAllMessage(String token,int typeId){
        final String url = "http://192.168.56.1/Home/TomorrowApi/getAllMessage";
        try {
            final String data = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&typeId="+URLEncoder.encode(String.valueOf(typeId), "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);


                    try {
                        JSONTokener jsonParser = new JSONTokener(returnStr);
                        JSONObject json = (JSONObject) jsonParser.nextValue();
                        JSONArray jsonArray =  new JSONArray(json.optString("message"));
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject j = (JSONObject)jsonArray.get(i);
                            //下载头像
                            boolean returnFlag;
                            String urlGetFriendImg = j.optString("uImgSrc") + ".jpg";
                            String saveForGetFriendImgInDir = dirImg_friend + j.optInt("uId") + ".jpg";
                            File friend_pic = new File(saveForGetFriendImgInDir);
                            Log.i("friend_pic",saveForGetFriendImgInDir);
                            if (!friend_pic.exists()){            //如果本地不存在，则去服务器获取
                                returnFlag = PostUtils.saveUrlAs(urlGetFriendImg, saveForGetFriendImgInDir);
                                Log.i("friendFlag", String.valueOf(returnFlag));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetAllMessage.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetAllMessage =new Handler(){
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
                    JSONArray jsonArray =  new JSONArray(json.optString("message"));
                    MessageBean []messageBeen = new MessageBean[jsonArray.length()];
                    Messages []messages = new Messages[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject j = (JSONObject)jsonArray.get(i);
                        messages[i] = new Messages();
                        messages[i].setId(j.optInt("id"));
                        messages[i].setuId(j.optInt("uId"));
                        messages[i].setuName(j.optString("uName"));
                        messages[i].setuImgSrc(j.optString("uImgSrc"));


//                        //下载头像
//                        boolean returnFlag;
//                        String urlGetFriendImg = messages[i].getuImgSrc();
//                        String saveForGetFriendImgInDir = dirImg_friend + messages[i].getuId() + ".jpg";
//                        File friend_pic = new File(saveForGetFriendImgInDir);
//                        Log.i("friend_pic",saveForGetFriendImgInDir);
//                        if (!friend_pic.exists()){            //如果本地不存在，则去服务器获取
//                            returnFlag = PostUtils.saveUrlAs(urlGetFriendImg, saveForGetFriendImgInDir);
//                            Log.i("friendFlag", String.valueOf(returnFlag));
//                        }


                        messages[i].setContent(j.optString("content"));
                        messages[i].setTime(j.optString("time"));
                        messages[i].setDate(j.optString("date"));
                        messages[i].setTimeSeconds(j.optString("timeSeconds"));
                        messages[i].setTypeId(j.optInt("typeId"));
                        messages[i].setValid(j.optInt("valid"));

                        int from = (j.optInt("uId")==User.uId)? 1:0;
                        String uname = messages[i].getuName();
                        int sex = 2;
                        int fire = 0;

                        if (User.friends!=null) {
                            for (int k = 0; k < User.friends.length; k++) {
                                if (messages[i].getuId()==User.friends[k].getuId()){
                                    sex = (User.friends[k].appearNum>0)? User.friends[k].getuSex():2;
                                    fire = (User.friends[k].appearNum>0)? 1:0;
                                }
                            }
                        }
                        String saveForGetFriendImg = dirImg_friend + messages[i].getuId() + ".jpg";
                        messageBeen[i] = new MessageBean(from,messages[i].getContent(),saveForGetFriendImg,uname,sex,fire);
                    }
                    //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    //设置到本地并且缓存到本地数据库
                    User.setMessage(messages);
                    initData(messageBeen);
                    adapter = new ConversationAdapter();
                    conversationList.setAdapter(adapter);
                    conversationList.setSelection(conversationList.getBottom());
                    Log.i("ziyu","dwad");

                    //实时渲染数据
                    getlatestmessage = new Thread(){
                        @Override
                        public void run() {

                            while (!isInterrupted()){ //非阻塞过程中通过判断中断标志来退出
                                try{
                                    for (;;) {
                                        Thread.sleep(1000);
                                        Log.i("len", String.valueOf(User.messages.length));
                                        if (User.messages.length > 0) {
                                            getLatestMessage(User.uToken, User.messages[User.messages.length - 1].time, User.plans[0].getTypeId());
                                        }else {
                                            //获取当前系统时间
                                            Calendar c = Calendar.getInstance();
                                            String year = String.valueOf(c.get(Calendar.YEAR));
                                            String month = String.valueOf(c.get(Calendar.MONTH) + 1);
                                            String date = String.valueOf(c.get(Calendar.DATE));
                                            String time = year + "-" + month + "-" + date + " 21:00:00";
                                            Log.i("time",time);
                                            getLatestMessage(User.uToken, time, User.plans[0].getTypeId());
                                        }
                                    }
                                }catch(InterruptedException e){
                                    e.printStackTrace();
                                    break;//捕获到异常之后，执行break跳出循环。
                                }
                            }
                        }
                    };
                    getlatestmessage.start();

                }else {

              }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("message",val);
        }
    };


    public void getLatestMessage(String token,String time,int typeId){
        final String url = "http://192.168.56.1/Home/TomorrowApi/getLatestMessage";
        try {
            final String data = "token="+ URLEncoder.encode(token, "UTF-8")+
                                "&time="+URLEncoder.encode(time, "UTF-8")+
                                 "&typeId="+URLEncoder.encode(String.valueOf(typeId), "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetLatestMessage.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetLatestMessage =new Handler(){
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
                JSONArray jsonArray =  new JSONArray(json.optString("allData"));

                MessageBean []messageBeen = new MessageBean[jsonArray.length()];
                Messages []messages = new Messages[jsonArray.length()];
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject j = (JSONObject)jsonArray.get(i);
                    messages[i] = new Messages();
                    messages[i].setId(j.optInt("id"));
                    messages[i].setuId(j.optInt("uId"));
                    messages[i].setuName(j.optString("uName"));
                    messages[i].setuImgSrc(j.optString("uImgSrc"));

                    //下载头像
                    boolean returnFlag;
                    String urlGetFriendImg = messages[i].getuImgSrc();
                    String saveForGetFriendImgInDir = dirImg_friend + messages[i].getuId() + ".jpg";
                    File friend_pic = new File(saveForGetFriendImgInDir);
                    Log.i("friend_pic",saveForGetFriendImgInDir);
                    if (!friend_pic.exists()){            //如果本地不存在，则去服务器获取
                        returnFlag = PostUtils.saveUrlAs(urlGetFriendImg, saveForGetFriendImgInDir);
                        Log.i("friendFlag", String.valueOf(returnFlag));
                    }

                    messages[i].setContent(j.optString("content"));
                    messages[i].setTime(j.optString("time"));
                    messages[i].setDate(j.optString("date"));
                    messages[i].setTimeSeconds(j.optString("timeSeconds"));
                    messages[i].setTypeId(j.optInt("typeId"));
                    messages[i].setValid(j.optInt("valid"));

                    int from = (j.optInt("uId")==User.uId)? 1:0;
                    String uname = messages[i].getuName();
                    int sex = 2;
                    int fire = 0;

                    if (User.friends!=null) {
                        for (int k = 0; k < User.friends.length; k++) {
                            if (messages[i].getuId()==User.friends[k].getuId()){
                                sex = (User.friends[k].appearNum>0)? User.friends[k].getuSex():2;
                                fire = (User.friends[k].appearNum>0)? 1:0;
                                break;
                            }
                        }
                    }
                    String saveForGetFriendImg = dirImg_friend + messages[i].getuId() + ".jpg";
                    messageBeen[i] = new MessageBean(from,messages[i].getContent(),saveForGetFriendImg,uname,sex,fire);
                }
                //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                //设置到本地并且缓存到本地数据库
                if (messages.length!=0) {
                    User.addMessage(messages);
                    addData(messageBeen);
                    adapter.notifyDataSetChanged();
                    conversationList.setSelection(conversationList.getBottom());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("message",val);
        }
    };


    public void sendMessageFun(String token,int uId,String uName,String uImgSrc,String message,int typeId){
        final String url = "http://192.168.56.1/Home/TomorrowApi/sendMessage";
        try {
            final String data = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&uId="+URLEncoder.encode(String.valueOf(uId), "UTF-8")+
                    "&uName="+URLEncoder.encode(uName, "UTF-8")+
                    "&uImgSrc="+URLEncoder.encode(uImgSrc, "UTF-8")+
                    "&content="+URLEncoder.encode(message, "UTF-8")+
                    "&typeId="+URLEncoder.encode(String.valueOf(typeId), "UTF-8");
            Log.i("msg",data);
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url, data);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForSendMsg.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForSendMsg =new Handler(){
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
                    //渲染数据
                    //先将EditText清空
                    words.setText("");

                }else {
                    Log.i("22",json.optString("error"));
                    new SweetAlertDialog(ChatRoomActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it=new Intent(ChatRoomActivity.this,LoginActivity.class);
                                    startActivity(it);
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



    private void addData(MessageBean []message){
        if (message!=null){
            for (int i=0;i<message.length;i++){
                listData.add(message[i]);
            }
        }
    }

    private void initData(MessageBean []message) {
        if (message!=null) {
            for (int i = 0; i < message.length; i++) {
                listData.add(message[i]);
            }
        }


//        listData.add(new MessageBean(0, "五一去哪玩"));
//        listData.add(new MessageBean(1, "还不知道呢"));
//        listData.add(new MessageBean(1, "你呢？什么打算"));
//        listData.add(new MessageBean(0, "没什么打算"));
//        listData.add(new MessageBean(0, "感觉出去人好多"));
//        listData.add(new MessageBean(1, "去上海怎么样"));
//        listData.add(new MessageBean(0, "上海好玩吗？"));
//        listData.add(new MessageBean(1, "不知道，没去过"));
//        listData.add(new MessageBean(1, "应该还不错吧"));
//        listData.add(new MessageBean(0, "行，那去看看"));
//        listData.add(new MessageBean(0, "到时候电话联系"));
//        listData.add(new MessageBean(1, "OK"));
    }

    private class ConversationAdapter extends BaseAdapter {
        /**
         * 布局类型的数量
         */
        private final int VIWE_TYPE_COUNT = 2;

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return VIWE_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            return listData.get(position).from;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OtherViewHolder otherHolder = null;
            MyViewHolder myHolder = null;
            int type = listData.get(position).from;
            String filePath = listData.get(position).icon;
            int sex_user = listData.get(position).sex;
            int isfire = listData.get(position).fire;
            String  nickName = listData.get(position).uname;
            if (convertView == null) {
                switch (type) {
                    case MSG_TYPE_SEND:
                        myHolder = new MyViewHolder();
                        convertView = getLayoutInflater().from(ChatRoomActivity.this).inflate(R.layout.item_conversation_right_layout, parent, false);
                        myHolder.icon = (ImageView) convertView.findViewById(R.id.user_icon);
                        myHolder.contentText = (TextView)convertView.findViewById(R.id.content_text);
                        myHolder.sex = (TextView) convertView.findViewById(R.id.sex);
                        myHolder.fire = (TextView) convertView.findViewById(R.id.fire);
                        myHolder.nickName = (TextView) convertView.findViewById(R.id.nickName);
                        convertView.setTag(myHolder);
                        break;
                    case MSG_TYPE_RECEIVE:
                        otherHolder = new OtherViewHolder();
                        convertView = getLayoutInflater().from(ChatRoomActivity.this).inflate(R.layout.item_conversation_left_layout, parent, false);
                        otherHolder.icon = (ImageView) convertView.findViewById(R.id.user_icon);
                        otherHolder.contentText = (TextView)convertView.findViewById(R.id.content_text);
                        otherHolder.sex = (TextView) convertView.findViewById(R.id.sex);
                        otherHolder.fire = (TextView) convertView.findViewById(R.id.fire);
                        otherHolder.nickName = (TextView) convertView.findViewById(R.id.nickName);
                        convertView.setTag(otherHolder);
                        break;
                    default:
                        break;
                }
            } else {
                switch (type) {
                    case MSG_TYPE_SEND:
                        myHolder = (MyViewHolder) convertView.getTag();
                        break;
                    case MSG_TYPE_RECEIVE:
                        otherHolder = (OtherViewHolder) convertView.getTag();
                        break;
                    default:
                        break;
                }
            }
            Log.i("dwa", String.valueOf(sex_user));
            Log.i("dwa", String.valueOf(isfire));
            Log.i("dwa", String.valueOf(nickName));
            if (type == MSG_TYPE_RECEIVE) {
                Log.i("dwa", String.valueOf(nickName));
                setItemValue(otherHolder, position,filePath,sex_user,isfire,nickName);
            } else {
                setItemValue(myHolder, position,filePath,User.uSex,isfire,nickName);
            }

            return convertView;
        }

        /**
         * 设置子布局的内容
         * @param viewHolder
         * @param position
         */
        private void setItemValue(ViewHolder viewHolder, int position,String filePath,int sex_user,int isfire,String nickName) {
            viewHolder.contentText.setText(listData.get(position).content);
            Log.i("sex", String.valueOf(sex_user));
            if (sex_user==1){
                viewHolder.sex.setBackgroundResource(R.mipmap.boy);
            }else if (sex_user==0){
                viewHolder.sex.setBackgroundResource(R.mipmap.girl);
            }



            if (listData.get(position).from == 0) {
//                viewHolder.icon.setImageResource(R.mipmap.my_icon);
                Drawable d=Drawable.createFromPath(filePath);    //头像
                viewHolder.icon.setBackgroundDrawable(d);
                if (isfire==1){         //擦出火花
                    viewHolder.fire.setBackgroundResource(R.mipmap.fire);
                    viewHolder.nickName.setText(nickName);
                }else {
                    viewHolder.nickName.setText("陌生人xxx");
                }

            } else {
//                viewHolder.icon.setImageResource(R.mipmap.other_icon);
                Drawable d=Drawable.createFromPath(filePath);    //头像
                viewHolder.icon.setBackgroundDrawable(d);
                viewHolder.nickName.setText(User.uName);
            }
        }
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView contentText;
        public TextView sex;
        public TextView fire;
        public TextView nickName;
    }

    private class OtherViewHolder extends ViewHolder {

    }

    private class MyViewHolder extends ViewHolder  {

    }

    private class MessageBean {
        public String icon;

        /**
         *  0表示发送的消息, 1表示收到的消息
         */
        public int from;
        public String content;

        public int sex;
        public String uname;
        public int fire;

        public MessageBean(int from, String content,String imgPath,String uName,int sex_user,int isfire) {
            this.icon = imgPath;
            this.from = from;
            this.content = content;
            this.sex = sex_user;
            this.uname = uName;
            this.fire = isfire;
        }
    }

    //返回主界面
    @Override
    public void onBackPressed()
    {
        finish();
    }
}

