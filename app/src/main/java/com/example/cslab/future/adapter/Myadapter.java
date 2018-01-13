package com.example.cslab.future.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.activity.CommentActivity;
import com.example.cslab.future.activity.MainActivity;
import com.example.cslab.future.activity.SquareActivity;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.Screen;

import java.util.List;
import java.util.Map;

/**
 * Created by CSLab on 2017/7/14.
 */

public class Myadapter extends BaseAdapter {

    private Context ctx;
    private List<Map<String, Object>> allValues;

    public Myadapter(Context ctx, List<Map<String, Object>> allValues) {
        this.ctx = ctx;
        this.allValues = allValues;
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
            view = LayoutInflater.from(ctx).inflate(R.layout.dream_item, null);
            //设置行的宽和高
//            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Screen.SCREEN_HEIGHT / 3));
        }

        //取得当前行中所有的组件
        TextView commentNum = (TextView)view.findViewById(R.id.commentNum);
        TextView clickNum = (TextView)view.findViewById(R.id.clickNum);
        Button image = (Button)view.findViewById(R.id.imageBtn);
        TextView dream_Txt = (TextView)view.findViewById(R.id.dream_Txt);

//        TextView time = (TextView)view.findViewById(R.id.time_txt);

        //获取当前行的数据，并将数据放在对应的组件上面
        final Map<String, Object> map = allValues.get(position);

        Drawable d=Drawable.createFromPath(map.get("dream_pic_path").toString());
        image.setBackgroundDrawable(d);
//        image.setBackgroundResource((int)map.get("image"));
        commentNum.setText(map.get("commentNum").toString());
        clickNum.setText(map.get("clickNum").toString());
        dream_Txt.setText(map.get("uDream").toString());


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uId = map.get("uId").toString();
                Bundle data = new Bundle();
                data.putString("uId",uId);
                Intent it=new Intent(ctx,CommentActivity.class);
                it.putExtras(data);
                ctx.startActivity(it);
                SquareActivity.squareActivity.finish();
            }
        });
        return view;
    }
}

