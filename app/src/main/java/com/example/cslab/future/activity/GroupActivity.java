package com.example.cslab.future.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private Context ctx;
    private List<Map<String,Object>>allValues = new ArrayList<>();
    private GroupAdapter groupAdapter;
    private ListView groupList;
    private String dirImg_friend;
    private Button outGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        groupList =(ListView)findViewById(R.id.groupList);
        outGroup = (Button)findViewById(R.id.outGroup);
        dirImg_friend = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_friendHead/";
//        for (int i =0;i<10;i++)
//        {
//            Map<String ,Object> map = new HashMap<>();
//            map.put("userName","测试昵称"+i);
//            allValues.add(map);
//        }

        if (User.friends!=null) {
            for (int i = 0; i < User.friends.length; i++){
                Map<String ,Object> map = new HashMap<>();
                Log.i("user", User.friends[i].uName);
                if (User.friends[i].appearNum>0) {
                    map.put("sex", User.friends[i].uSex);
                    map.put("userName",User.friends[i].uName);
                    map.put("filePath",dirImg_friend + User.friends[i].uId + ".jpg");
                    map.put("isFire","1");
                    map.put("appearNum",User.friends[i].appearNum);
                }else {
                    map.put("sex", "保密");
                    map.put("userName","保密");
                    map.put("filePath",dirImg_friend + User.friends[i].uId + ".jpg");
                    map.put("isFire","0");
                    map.put("appearNum",User.friends[i].appearNum);
                }
                allValues.add(map);
            }
        }
        groupAdapter = new GroupAdapter(this,allValues);
        groupList.setAdapter(groupAdapter);
        outGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(GroupActivity.this,ChatRoomActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent it=new Intent(GroupActivity.this,ChatRoomActivity.class);
            startActivity(it);
            finish();
        }
        return true;
    }

    private class GroupAdapter extends BaseAdapter{
          private Context ctx;
          private List<Map<String, Object>> allValues;

          public GroupAdapter(Context ctx,List<Map<String,Object>>allValues) {
              this.ctx =ctx;
              this.allValues=allValues;
          }

          @Override
        public int getCount() {
            return allValues.size();
        }

        @Override
        public Object getItem(int position) {
            return allValues.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view==null) {
                //构建ListView中一行的界面
                view = LayoutInflater.from(ctx).inflate(R.layout.group_item, null);
            }

            Map<String, Object> map = allValues.get(position);
            ImageView headImg = (ImageView)view.findViewById(R.id.headImg);
            TextView userName = (TextView)view.findViewById(R.id.userName);
            TextView friendly = (TextView)view.findViewById(R.id.friendly);

            Drawable d=Drawable.createFromPath(map.get("filePath").toString());    //头像
            headImg.setBackgroundDrawable(d);
            if (map.get("isFire").equals("1")) {
                friendly.setBackgroundResource(R.mipmap.fire);
            }else {
                friendly.setText(String.valueOf(map.get("appearNum")));
            }
            userName.setText(map.get("userName").toString());
            return view;
        }
    }
}
