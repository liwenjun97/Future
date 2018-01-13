package com.example.cslab.future.entity;

import android.content.Context;

import com.example.cslab.future.sql.DiarySql;

import java.util.List;

/**
 * Created by CSLab on 2017/7/14.
 */

public class Diary {
    private int id;
    private int uId;
    private String uDiary;
    private String uStyle;
    private String uTime;
    private String uDiaryPic;
    private Context context;


    public String getuDiaryPic() {
        return uDiaryPic;
    }

    public void setuDiaryPic(String uDiaryPic) {
        this.uDiaryPic = uDiaryPic;
    }


//    public static void init(Context context){
//        DiarySql.init(context);
//    }
//
//    public static void insert(int id,int uId,String uDiary,String uStyle,String uTime){
//        DiarySql.insert(id,uId,uDiary,uStyle,uTime);
//    }
//
//    public static List<Diary> queryAll(){
//        return DiarySql.queryAll();
//    }
//    public static void close(){
//        DiarySql.close();
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuDiary() {
        return uDiary;
    }

    public void setuDiary(String uDiary) {
        this.uDiary = uDiary;
    }

    public String getuStyle() {
        return uStyle;
    }

    public void setuStyle(String uStyle) {
        this.uStyle = uStyle;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
