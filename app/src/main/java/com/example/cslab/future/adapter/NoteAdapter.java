package com.example.cslab.future.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cslab.future.R;

import java.util.List;
import java.util.Map;

import com.example.cslab.future.util.Screen;

public class NoteAdapter extends BaseAdapter {
    private Context ctx;
    private List<Map<String, Object>> allValues;

    public NoteAdapter(Context ctx, List<Map<String, Object>> allValues) {
        this.ctx = ctx;
        this.allValues = allValues;
    }
    public int getCount() {
        return allValues.size();
    }
    public Object getItem(int i) {
        return allValues.get(i);
    }
    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.list_item_note, null);
           //view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Screen.SCREEN_HEIGHT / 15));
        }

        TextView note = (TextView)view.findViewById(R.id.list_item_note);
        TextView last = (TextView)view.findViewById(R.id.list_item_last);

        Map<String, Object> map = allValues.get(i);
        note.setText(map.get("note").toString());
        last.setText(map.get("last").toString());
        return view;
    }
}