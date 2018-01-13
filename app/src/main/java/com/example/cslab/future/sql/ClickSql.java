package com.example.cslab.future.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cslab.future.entity.Click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CSLab on 2017/7/15.
 */

public class ClickSql {
    public static SQLiteDatabase db;
    public static void init(Context context){
        String sqlPath = context.getFilesDir().getAbsolutePath()+"/future.db";
        db = SQLiteDatabase.openOrCreateDatabase(sqlPath,  null);
        //如果不存在Dream表则创建
        db.execSQL("create table if not exists Click(id integer, clickId integer,clickName varchar(50),uId integer,uName varchar(50),clickNum integer,clickTime varchar(50))");
        Log.i("db2","初始化数据库");
    }

    public static void addClick(Context context,int id,int clickId,String clickName,int uId,String uName,int clickNum,String clickTime){     //用户点赞时添加然后从服务器得到这个数据,先添加服务器再添加本地
        ClickSql.init(context);
        ContentValues data_insert = new ContentValues();
        data_insert.put("id", id);
        data_insert.put("clickId",clickId);
        data_insert.put("clickName", clickName);
        data_insert.put("uId", uId);
        data_insert.put("uName", uName);
        data_insert.put("clickNum", clickNum);
        data_insert.put("clickTime", clickTime);
        db.insert("Click",null,data_insert);

        //给对应的Dream的clickNum次数加1
    }

    public static void deleteClickFromId(Context context,String deleteId){      //这个是删除指定的取消点赞,先删除本地再删除服务器
        init(context);
        db.delete("Click", "id = ?", new String[]{deleteId});
        //给对应的Dream的clickNum次数减1
    }

    public static void addClicksToSql(Context context,List<Map<String ,Object>> insert_list){   //从服务器读取添加例如点击梦想详情的时候调用
        ClickSql.init(context);

        for (int i=0;i<insert_list.size();i++){
            //ContentValues以键值对的形式存放数据
            ContentValues data_insert = new ContentValues();
            Map<String ,Object> map = insert_list.get(i);
            data_insert.put("id", String.valueOf(map.get("id")));
            data_insert.put("clickId", String.valueOf(map.get("clickId")));
            data_insert.put("clickName", String.valueOf(map.get("clickName")));
            data_insert.put("uId", String.valueOf(map.get("uId")));
            data_insert.put("uName", String.valueOf(map.get("uName")));
            data_insert.put("clickNum", String.valueOf(map.get("clickNum")));
            data_insert.put("clickTime", String.valueOf(map.get("clickTime")));
            db.insert("Click",null,data_insert);
        }

    }


    public static void deleteClickFromDream(Context context,String deleteUid){      //这个删除是根据Dream的删除来的,Dream删除后对应的click点赞也应该删除
        init(context);
        Log.i("deleteId",deleteUid);
        db.delete("Click", "uId < ?", new String[]{deleteUid});
    }

    //按id逆序输出所有Dream
    public static List<Map<String,Object>> queryAllDream(Context context){
        List<Map<String ,Object>> return_list = new ArrayList<>();
        DreamSql.init(context);
//        Cursor c = db.rawQuery("SELECT * FROM Dream WHERE uId >= ?", new String[]{"1"});
        Cursor c = db.rawQuery("SELECT * FROM Dream ORDER BY id DESC",null);          //id大的时间肯定在后面
        while (c.moveToNext()) {
            Map<String ,Object> map = new HashMap<>();
            map.put("id",c.getInt(c.getColumnIndex("id")));
            map.put("clickId",c.getInt(c.getColumnIndex("clickId")));
            map.put("clickName",c.getString(c.getColumnIndex("clickName")));
            map.put("uId",c.getInt(c.getColumnIndex("uId")));
            map.put("uName",c.getString(c.getColumnIndex("uName")));
            map.put("clickNum",c.getInt(c.getColumnIndex("clickNum")));
            map.put("clickTime",c.getString(c.getColumnIndex("clickTime")));
            return_list.add(map);

        }
        c.close();

        return return_list;
    }

    public static void closeSql(){
        db.close();
    }
}
