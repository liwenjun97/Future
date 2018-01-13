package com.example.cslab.future.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cslab.future.entity.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CSLab on 2017/7/15.
 */

public class DiarySql {

    private static SQLiteDatabase db;
    public static void init(Context context) {
        String sqlPath = context.getFilesDir().getAbsolutePath()+"/future.db";
        db=SQLiteDatabase.openOrCreateDatabase(sqlPath,null);
        Cursor c=db.rawQuery("select count(*) from sqlite_master where type = 'table' and name = 'Diary'",null);
            //创建Plan表
        db.execSQL("create table if not exists Diary (id int,uId int,uDiary text,uStyle text,uTime text)");
    }

    public static void insert(int id,int uId,String uDiary,String uStyle,String uTime){
        db.execSQL("insert into Diary values(?,?,?,?,?)",new Object[]{id,uId,uDiary,uStyle,uTime});
        db.execSQL("update Diary set id=id+1");
        db.execSQL("delete from Diary where id=31");
    }

    public static List<Diary> queryAll(){
        List<Diary> list=new ArrayList<>();
        Cursor c=db.rawQuery("select * from Diary",null);
        while(c.moveToNext()){
            Diary d=new Diary();
            d.setId(c.getInt(c.getColumnIndex("id")));
            d.setuId(c.getInt(c.getColumnIndex("uId")));
            d.setuDiary(c.getString(c.getColumnIndex("uDiary")));
            d.setuStyle(c.getString(c.getColumnIndex("uStyle")));
            d.setuTime(c.getString(c.getColumnIndex("uTime")));
            list.add(d);
        }
        c.close();
        return list;
    }

    public static void close(){
        db.close();
    }

}
