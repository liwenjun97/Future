package com.example.cslab.future.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by CSLab on 2017/7/19.
 */

public class CommentItem {
    public int type;
    public HashMap<String, Object> map;

    public CommentItem(int type, HashMap<String, Object> map) {
        this.type = type;
        this.map = map;
    }

    private List getDatas() {
        List newList = new ArrayList<CommentItem>();
        newList.add(new CommentItem(0, getHashMapFirstType()));
        for(int i = 0;i<5;i++)
            newList.add(new CommentItem(1,getHashMapSecondType()));
        return  newList;
    }

    private HashMap<String, Object> getHashMapFirstType() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        return hashMap;
    }

    private HashMap<String, Object> getHashMapSecondType() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        return hashMap;
    }
}
