package com.example.cslab.future.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cslab.future.entity.Dream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CSLab on 2017/7/15.
 */

public class DreamSql {
    public static SQLiteDatabase db;
    public static void init(Context context){
        String sqlPath = context.getFilesDir().getAbsolutePath()+"/future.db";
        db = SQLiteDatabase.openOrCreateDatabase(sqlPath,  null);
        //如果不存在Dream表则创建
        db.execSQL("create table if not exists Dream(id,integer,uId integer, uName varchar(10),uEmail varchar(50),uRegTime varchar(50),uImgSrc text,uCity varchar(10),uAge integer,uBirth varchar(50),uSex integer,uInterest text,uDream text,uDreamPic text,dreamClickNum integer,dreamCommentNum integer)");
        Log.i("db2","初始化数据库");
    }

    public static boolean addOneDream(Context context ,int uId,String uName,String uEmail,String uRegTime,String uImgSrc,String uCity,int uAge,String uBirth,int uSex,String uInterest,String uDream,String uDreamPic,int dreamClickNum,int dreamCommentNum){
        DreamSql.init(context);
        //ContentValues以键值对的形式存放数据
        ContentValues data_insert = new ContentValues();
        data_insert.put("id",0);
        data_insert.put("uId", uId);
        data_insert.put("uName", uName);
        data_insert.put("uEmail", uEmail);
        data_insert.put("uRegTime", uRegTime);
        data_insert.put("uImgSrc", uImgSrc);
        data_insert.put("uCity", uCity);
        data_insert.put("uAge", uAge);
        data_insert.put("uBirth", uBirth);
        data_insert.put("uSex", uSex);
        data_insert.put("uInterest", uInterest);
        data_insert.put("uDream", uDream);
        data_insert.put("uDreamPic", uDreamPic);
        data_insert.put("dreamClickNum", dreamClickNum);
        data_insert.put("dreamCommentNum", dreamCommentNum);
        db.insert("Dream",null,data_insert);
        db.execSQL("update Dream set id=id+1");
        db.execSQL("delete from Dream where id=31");
        return true;
    }

    //添加服务器的Dream到数据库中
    public static void addDreamsToSql(Context context,List<Dream> insert_list){
        DreamSql.init(context);

        for (int i=0;i<insert_list.size();i++){
            //ContentValues以键值对的形式存放数据
            ContentValues data_insert = new ContentValues();
            Dream d = insert_list.get(i);
            data_insert.put("uId",d.getuId());
            data_insert.put("uName", d.getuName());
            data_insert.put("uEmail", d.getuEmail());
            data_insert.put("uRegTime", d.getuRegTime());
            data_insert.put("uImgSrc", d.getuImgSrc());
            data_insert.put("uCity", d.getuCity());
            data_insert.put("uAge", d.getuAge());
            data_insert.put("uBirth", d.getuBirth());
            data_insert.put("uSex", d.getuSex());
            data_insert.put("uInterest", d.getuInterest());
            data_insert.put("uDream", d.getuDream());
            data_insert.put("uDreamPic", d.getuDreamPic());
            data_insert.put("dreamClickNum", d.getDreamClickNum());
            data_insert.put("dreamCommentNum", d.getDreamClickNum());
            db.insert("Dream",null,data_insert);
        }

        List<Dream> all_list = new ArrayList<>();
        all_list = queryAllDream(context);
    }

    //按id逆序输出所有Dream
    public static List<Dream> queryAllDream(Context context){
        List<Dream> return_list = new ArrayList<>();
        DreamSql.init(context);
        Cursor c = db.rawQuery("SELECT * FROM Dream ORDER BY uId DESC",null);
        while (c.moveToNext()) {
            Dream d=new Dream();
            d.setuId(c.getInt(c.getColumnIndex("uId")));
            d.setuName(c.getString(c.getColumnIndex("uName")));
            d.setuEmail(c.getString(c.getColumnIndex("uEmail")));
            d.setuRegTime(c.getString(c.getColumnIndex("uRegTime")));
            d.setuImgSrc(c.getString(c.getColumnIndex("uImgSrc")));
            d.setuCity(c.getString(c.getColumnIndex("uCity")));
            d.setuAge(c.getInt(c.getColumnIndex("uAge")));
            d.setuBirth(c.getString(c.getColumnIndex("uBirth")));
            d.setuSex(c.getInt(c.getColumnIndex("uSex")));
            d.setuInterest(c.getString(c.getColumnIndex("uInterest")));
            d.setuDream(c.getString(c.getColumnIndex("uDream")));
            d.setuDreamPic(c.getString(c.getColumnIndex("uDreamPic")));
            d.setDreamClickNum(c.getInt(c.getColumnIndex("dreamClickNum")));
            d.setDreamCommentNum(c.getInt(c.getColumnIndex("dreamCommentNum")));
            return_list.add(d);

//            int _id = c.getInt(c.getColumnIndex("uId"));
//            String name = c.getString(c.getColumnIndex("uName"));
//            int age = c.getInt(c.getColumnIndex("uAge"));
//            Log.i("db", "_id=>" + _id + ", name=>" + name + ", age=>" + age);
        }
        c.close();

        return return_list;
    }


    public static void closeSql(){
        db.close();
    }
}
