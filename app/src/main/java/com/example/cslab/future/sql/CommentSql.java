package com.example.cslab.future.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cslab.future.entity.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CSLab on 2017/7/15.
 */

public class CommentSql {
    private static SQLiteDatabase db;
    public static void init(Context context){
        String sqlPath = context.getFilesDir().getAbsolutePath()+"/future.db";
        db = SQLiteDatabase.openOrCreateDatabase(sqlPath,  null);
        //创建Plan表
        db.execSQL("create table if not exists Comment (id int , commentId int,commentName text,commentedId int,commentedName text,uId int,uName text,commentContent text,commentTime text)");
    }
//    public int id;
//    public int commentId;
//    public String commentName;
//    public int commentedId;
//    public String commentedName;
//    public int uId;
//    public String uName;
//    public String commentContent;
//    public String commentTime;
    public static void insert(int id,int commentId,String commentName,int commentedId,String commentedName,int uId,String uName,String commentContent,String commentTime){
        db.execSQL("insert into Plan values(?,?,?,?,?,?,?,?,?)",new Object[]{id,commentId,commentName,commentedId,commentedName,uId,uName,commentContent,commentTime});
    }
    public static List<Comment> queryAll(){
        List<Comment> list=new ArrayList<>();
        Cursor c=db.rawQuery("select * from Comment",null);
        while(c.moveToNext()){
            Comment com=new Comment();
            com.setId(c.getInt(c.getColumnIndex("id")));
            com.setCommentedId(c.getInt(c.getColumnIndex("commentId")));
            com.setCommentName(c.getString(c.getColumnIndex("commentName")));
            com.setCommentedId(c.getInt(c.getColumnIndex("commentedId")));
            com.setCommentedName(c.getString(c.getColumnIndex("commentedName")));
            com.setuId(c.getInt(c.getColumnIndex("uId")));
            com.setuName(c.getString(c.getColumnIndex("uNmae")));
            com.setCommentContent(c.getString(c.getColumnIndex("commentContent")));
            com.setCommentTime(c.getString(c.getColumnIndex("commentTime")));
            list.add(com);
        }
        return list;
    }
    public static void delete(int id){
        db.execSQL("delete from Comment where id=?",new Object[]{id});
    }
    public static void close(){
        db.close();
    }
}
