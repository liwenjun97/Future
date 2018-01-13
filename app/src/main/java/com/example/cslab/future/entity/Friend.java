package com.example.cslab.future.entity;

/**
 * Created by CSLab on 2017/7/14.
 */

public class Friend {
    public int uId;
    public String uName;
    public String uImgSrc;
    public String uCity;
    public int uAge;
    public int uSex;
    public String uInterest;
    public String planTime;
    public String groupType;
    public int typeId;

    public int getAppearNum() {
        return appearNum;
    }

    public void setAppearNum(int appearNum) {
        this.appearNum = appearNum;
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

    public String getuCity() {
        return uCity;
    }

    public void setuCity(String uCity) {
        this.uCity = uCity;
    }

    public int getuAge() {
        return uAge;
    }

    public void setuAge(int uAge) {
        this.uAge = uAge;
    }

    public int getuSex() {
        return uSex;
    }

    public void setuSex(int uSex) {
        this.uSex = uSex;
    }

    public String getuInterest() {
        return uInterest;
    }

    public void setuInterest(String uInterest) {
        this.uInterest = uInterest;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int appearNum;
}
