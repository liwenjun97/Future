package com.example.cslab.future.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;

import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/7/14.
 */

public class HistoryTodayAdapter extends BaseAdapter {

    private Context ctx;
    private List<Map<String, Object>> allValues;

    public HistoryTodayAdapter(Context ctx, List<Map<String, Object>> allValues) {
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

        if (view == null) {
            //构建ListView中一行的界面
            view = LayoutInflater.from(ctx).inflate(R.layout.history_today_item, null);
            //设置行的宽和高
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        }

        //取得当前行中所有的组件
        TextView image = (TextView) view.findViewById(R.id.img_txt);
        TextView title = (TextView) view.findViewById(R.id.title_txt);
        TextView content = (TextView) view.findViewById(R.id.content_txt);
        TextView star = (TextView) view.findViewById(R.id.add_like);
        //获取当行数据
        final Map<String, Object> map = allValues.get(position);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)map.get("star")==R.mipmap.star) {
                    map.put("star", R.mipmap.add_like_star);
                    Toast.makeText(view.getContext(),"添加喜爱成功",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                else{
                    map.put("star", R.mipmap.star);
                    Toast.makeText(view.getContext(),"取消喜爱",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });

        //将数据放在对应的组件上面
        image.setBackgroundResource((int) map.get("image"));
        title.setText(map.get("title").toString());
        content.setText(map.get("content").toString());
        star.setBackgroundResource((int) map.get("star"));
        return view;
    }
}
