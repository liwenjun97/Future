package com.example.cslab.future.entity;

/**
 * Created by CSLab on 2017/7/14.
 */

public class Dream {
    public int uId;
    public String uName;
    public String uEmail;
    public String uRegTime;
    public String uImgSrc;
    public String uCity;
    public int uAge;
    public String uBirth;
    public int uSex;
    public String uInterest;
    public String uDream;
    public String uDreamPic;
    public int dreamClickNum;
    public int dreamCommentNum;
    public Comment[] commentContent;
    public Click[] clickContent;

    public String getuDreamPic() {
        return uDreamPic;
    }

    public void setuDreamPic(String uDreamPic) {
        this.uDreamPic = uDreamPic;
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

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuRegTime() {
        return uRegTime;
    }

    public void setuRegTime(String uRegTime) {
        this.uRegTime = uRegTime;
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

    public String getuBirth() {
        return uBirth;
    }

    public void setuBirth(String uBirth) {
        this.uBirth = uBirth;
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

    public String getuDream() {
        return uDream;
    }

    public void setuDream(String uDream) {
        this.uDream = uDream;
    }

    public int getDreamClickNum() {
        return dreamClickNum;
    }

    public void setDreamClickNum(int dreamClickNum) {
        this.dreamClickNum = dreamClickNum;
    }

    public int getDreamCommentNum() {
        return dreamCommentNum;
    }

    public void setDreamCommentNum(int dreamCommentNum) {
        this.dreamCommentNum = dreamCommentNum;
    }

    public Comment[] getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(Comment[] commentContent) {
        this.commentContent = commentContent;
    }

    public Click[] getClickContent() {
        return clickContent;
    }

    public void setClickContent(Click[] clickContent) {
        this.clickContent = clickContent;
    }


}
