package com.example.cslab.future.entity;

/**
 * Created by CSLab on 2017/7/14.
 */

public class Messages {
    public int id;
    public int uId;
    public String uName;
    public String uImgSrc;
    public String content;
    public String time;
    public String date;
    public String timeSeconds;
    public int typeId;
    public int valid;

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

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImgSrc() {
        return uImgSrc;
    }

    public void setuImgSrc(String uImgSrc) {
        this.uImgSrc = uImgSrc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(String timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }
}
