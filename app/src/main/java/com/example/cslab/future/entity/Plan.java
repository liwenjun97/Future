package com.example.cslab.future.entity;

import android.app.ListActivity;
import android.content.Context;

import com.example.cslab.future.sql.PlanSql;

import java.util.List;

/**
 * Created by CSLab on 2017/7/14.
 */

public class Plan {
    private int id;
    private int uId;
    private String uType;
    private int typeId;
    private String uRemark;
    private int uFinish;
    private String uTime;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

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

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getuRemark() {
        return uRemark;
    }

    public void setuRemark(String uRemark) {
        this.uRemark = uRemark;
    }

    public int getuFinish() {
        return uFinish;
    }

    public void setuFinish(int uFinish) {
        this.uFinish = uFinish;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }

    public Context context;

//    public void init(){
//        PlanSql.init(context);
//    }
//
//    public void insert(){
//        PlanSql.insert(id,uId,uType,typeId,uRemark,uFinish,uTime);
//    }
//
//    public List<Plan> queryAll(){
//        return PlanSql.queryAll();
//    }
//    public void close(){
//        PlanSql.close();
//    }
}
