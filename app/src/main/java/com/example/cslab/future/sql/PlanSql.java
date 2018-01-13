package com.example.cslab.future.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cslab.future.entity.Plan;
import com.example.cslab.future.entity.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CSLab on 2017/7/15.
 */

public class PlanSql {

    private static SQLiteDatabase db;

    public static void init(Context context){
        String sqlPath = context.getFilesDir().getAbsolutePath()+"/future.db";
        db = SQLiteDatabase.openOrCreateDatabase(sqlPath,  null);
            //创建Plan表
        db.execSQL("create table if not exists Plan (id int , uId int , uType text , typeId int , uRemark text , uFinish int , uTime text)");
    }

    public static void insert(int id,int uId,String uType,int typeId,String uRemark,int uFinish,String uTime){
        db.execSQL("insert into Plan values(?,?,?,?,?,?,?)",new Object[]{id,uId,uType,typeId,uRemark,uFinish,uTime});
        db.execSQL("update Plan set id =id+1");
        Log.i("asjfklafjkl","IDIDIDIDIDIDI");
        db.execSQL("delete from Plan where id=31");

    }

    public  static List<Plan> queryAll(){
        List<Plan> list=new ArrayList<>();
        Cursor c=db.rawQuery("select * from Plan",null);
        while(c.moveToNext()){
            Plan p=new Plan();
            p.setId(c.getInt(c.getColumnIndex("id")));
            p.setuId(c.getInt(c.getColumnIndex("uId")));
            p.setuType(c.getString(c.getColumnIndex("uType")));
            p.setTypeId(c.getInt(c.getColumnIndex("typeId")));
            p.setuRemark(c.getString(c.getColumnIndex("uRemark")));
            p.setuFinish(c.getInt(c.getColumnIndex("uFinish")));
            p.setuTime(c.getString(c.getColumnIndex("uTime")));
            list.add(p);
        }
        c.close();
        return list;
    }
    public static Plan queryFirst(){
        Plan p=new Plan();
        Cursor c=db.rawQuery("select * from Plan where id=1",null);
        if(c.getCount()==0){
            p=null;
        }else{
            c.moveToFirst();
            p.setId(c.getInt(c.getColumnIndex("id")));
            p.setuId(c.getInt(c.getColumnIndex("uId")));
            p.setuType(c.getString(c.getColumnIndex("uType")));
            p.setTypeId(c.getInt(c.getColumnIndex("typeId")));
            p.setuRemark(c.getString(c.getColumnIndex("uRemark")));
            p.setuFinish(c.getInt(c.getColumnIndex("uFinish")));
            p.setuTime(c.getString(c.getColumnIndex("uTime")));
        }
        return p;
    }
    public static void close(){
        db.close();
    }

//    public static void initSql(Context context){
//        //打开或创建test.db数据库
//        String sqlPath = context.getFilesDir().getAbsolutePath()+"future.db";
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(sqlPath,  null);
//        db.execSQL("DROP TABLE IF EXISTS user");
//        //创建person表
//        db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
//        User person = new User();
//        User.uName = "john";
//        User.uAge = 30;
//        //插入数据
//        db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{User.uName, User.uAge});
//
//        User.uName = "david";
//        User.uAge = 33;
//        //ContentValues以键值对的形式存放数据
//        ContentValues cv = new ContentValues();
//        cv.put("name", User.uName);
//        cv.put("age", User.uAge);
//        //插入ContentValues中的数据
//        db.insert("person", null, cv);
//
//        cv = new ContentValues();
//        cv.put("age", 35);
//        //更新数据
//        db.update("person", cv, "name = ?", new String[]{"john"});
//
//        Cursor c = db.rawQuery("SELECT * FROM person WHERE age >= ?", new String[]{"33"});
//        while (c.moveToNext()) {
//            int _id = c.getInt(c.getColumnIndex("_id"));
//            String name = c.getString(c.getColumnIndex("name"));
//            int age = c.getInt(c.getColumnIndex("age"));
//            Log.i("db", "_id=>" + _id + ", name=>" + name + ", age=>" + age);
//        }
//        c.close();
//
//        //删除数据
//        db.delete("person", "age < ?", new String[]{"35"});
//
//        //关闭当前数据库
//        db.close();
//
//        //删除test.db数据库
////      deleteDatabase("test.db");
//    }
}
