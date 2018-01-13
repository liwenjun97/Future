package com.example.cslab.future.entity;

import android.content.Context;


public class Note {
    private int id;
    private int uId;
    private String uDate;
    private int uStick;
    private String uNote;
    private String uTime;
    public Context context;
    public Note(){

    }
    public Note(Note a){
        this.id=a.id;
        this.uId=a.uId;
        this.uDate=a.uDate;
        this.uStick=a.uStick;
        this.uNote=a.uNote;
        this.uTime=a.uTime;
    }
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

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getuStick() {
        return uStick;
    }

    public void setuStick(int uStick) {
        this.uStick = uStick;
    }

    public String getuNote() {
        return uNote;
    }

    public void setuNote(String uNote) {
        this.uNote = uNote;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }
}
