package com.example.cslab.future.entity;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.cslab.future.sql.UserSql;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by CSLab on 2017/7/14.
 */

public class User {
    public static int uId;
    public static String uToken = "";           //默认为空
    public static String uName;
    public static String uEmail;
    public static String uPas;   //password;
    public static String uRegTime;
    public static String uImgSrc;
    public static String uCity;
    public static int uAge;
    public static String uBirth;
    public static int uSex;
    public static String uInterest;
    public static String uDream;
    public static String uDreamPic;
    public static int dreamClickNum;
    public static int dreamCommentNum;
    public static Plan[] plans;
    public static Note[] notes;
    public static Diary[] diaries;
    public static Friend[] friends;
    public static Messages[] messages;
    public static Dream[] dreams;


    public static void setUserInfo(int id,String name,String email,String pwd,String regTime,String imgSrc,String city,int age,String birth,int sex,String interest,String dream,String dreamPic,int dreamclicknum,int dreamcommentnum){
        uId = id;
        uName = name;
        uEmail = email;
        uPas = pwd;
        uRegTime = regTime;
        uImgSrc = imgSrc;
        uCity = city;
        uBirth = birth;
        uAge = age;
        uSex = sex;
        uInterest = interest;
        uDream = dream;
        uDreamPic = dreamPic;
        dreamClickNum = dreamclicknum;
        dreamCommentNum = dreamcommentnum;
//        UserSql.addToSql();               //保存到本地数据库
    }

    //Get||Set
    public static int getuId() {
        return uId;
    }

    public static String getuName() {
        return uName;
    }

    public static void setuId(int id) {
        uId = id;
    }

    public static void setPlan(Plan []plansFromService){
        plans = new Plan[plansFromService.length];
        for(int i=0;i<plansFromService.length;i++){
            plans[i] = new Plan();
            plans[i].setId(plansFromService[i].getId());
            plans[i].setuId(plansFromService[i].getuId());
            plans[i].setuFinish(plansFromService[i].getuFinish());
            plans[i].setuTime(plansFromService[i].getuTime());
            plans[i].setTypeId(plansFromService[i].getTypeId());
            plans[i].setuRemark(plansFromService[i].getuRemark());
            plans[i].setuType(plansFromService[i].getuType());
        }
        //        PlanSql.addToSql();               //保存到本地数据库
    }
    public static void addPlan(Plan plan){
        int len = plans.length + 1;
        Plan []planTemp = new Plan[len-1];
        for (int i=0;i<len-1;i++){
            planTemp[i] = new Plan();
            planTemp[i].setId(plans[i].getId());
            planTemp[i].setuId(plans[i].getuId());
            planTemp[i].setuFinish(plans[i].getuFinish());
            planTemp[i].setuTime(plans[i].getuTime());
            planTemp[i].setTypeId(plans[i].getTypeId());
            planTemp[i].setuRemark(plans[i].getuRemark());
            planTemp[i].setuType(plans[i].getuType());
        }
        plans = new Plan[len];
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date timePlan = null;
        Date timePlanPre = null;
        Date timePlanNext = null;
        boolean isFlag = false;
        int i=0,j=0;
        for(;i<len-1;i++){
            plans[i] = new Plan();
            try {
                Log.i("planTime",plan.getuTime());
                timePlan = format.parse(plan.getuTime());
                timePlanPre = format.parse(planTemp[i].getuTime());
                if (i<len-2) {
                    timePlanNext = format.parse(planTemp[i + 1].getuTime());
                    int flagPre = timePlan.compareTo(timePlanPre);
                    int flagNext = timePlan.compareTo(timePlanNext);
                    if (flagPre<=0&&flagNext>0){
                        plans[i].setId(plan.getId());
                        plans[i].setuId(plan.getuId());
                        plans[i].setuFinish(plan.getuFinish());
                        plans[i].setuTime(plan.getuTime());
                        plans[i].setTypeId(plan.getTypeId());
                        plans[i].setuRemark(plan.getuRemark());
                        plans[i].setuType(plan.getuType());
                        isFlag = true;
                    }else {
                        plans[i].setId(planTemp[j].getId());
                        plans[i].setuId(planTemp[j].getuId());
                        plans[i].setuFinish(planTemp[j].getuFinish());
                        plans[i].setuTime(planTemp[j].getuTime());
                        plans[i].setTypeId(planTemp[j].getTypeId());
                        plans[i].setuRemark(planTemp[j].getuRemark());
                        plans[i].setuType(planTemp[j].getuType());
                        j++;
                    }
                }else {
                    int flagPre = timePlan.compareTo(timePlanPre);
                    if (flagPre<=0){
                        plans[i].setId(plan.getId());
                        plans[i].setuId(plan.getuId());
                        plans[i].setuFinish(plan.getuFinish());
                        plans[i].setuTime(plan.getuTime());
                        plans[i].setTypeId(plan.getTypeId());
                        plans[i].setuRemark(plan.getuRemark());
                        plans[i].setuType(plan.getuType());
                        isFlag = true;
                    }else {
                        plans[i].setId(planTemp[j].getId());
                        plans[i].setuId(planTemp[j].getuId());
                        plans[i].setuFinish(planTemp[j].getuFinish());
                        plans[i].setuTime(planTemp[j].getuTime());
                        plans[i].setTypeId(planTemp[j].getTypeId());
                        plans[i].setuRemark(planTemp[j].getuRemark());
                        plans[i].setuType(planTemp[j].getuType());
                        j++;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (isFlag){
            plans[i] = new Plan();
            plans[i].setId(planTemp[j].getId());
            plans[i].setuId(planTemp[j].getuId());
            plans[i].setuFinish(planTemp[j].getuFinish());
            plans[i].setuTime(planTemp[j].getuTime());
            plans[i].setTypeId(planTemp[j].getTypeId());
            plans[i].setuRemark(planTemp[j].getuRemark());
            plans[i].setuType(planTemp[j].getuType());
        }else {
            plans[i] = new Plan();
            plans[i].setId(plan.getId());
            plans[i].setuId(plan.getuId());
            plans[i].setuFinish(plan.getuFinish());
            plans[i].setuTime(plan.getuTime());
            plans[i].setTypeId(plan.getTypeId());
            plans[i].setuRemark(plan.getuRemark());
            plans[i].setuType(plan.getuType());
        }

    }

    public static void addDiary(Diary diary){
        int len;
        if (diaries==null){
            len = 1;
        }else {
            len = diaries.length + 1;
        }

        Diary []diaryTemp = new Diary[len-1];
        for (int i=0;i<len-1;i++){
            diaryTemp[i] = new Diary();
            diaryTemp[i].setuStyle(diaries[i].getuStyle());
            diaryTemp[i].setId(diaries[i].getId());
            diaryTemp[i].setuTime(diaries[i].getuTime());
            diaryTemp[i].setuDiary(diaries[i].getuDiary());
            diaryTemp[i].setuId(diaries[i].getuId());
            diaryTemp[i].setuDiaryPic(diaries[i].getuDiaryPic());
        }
        diaries = new Diary[len];
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date timeDiary = null;
        Date timeDiaryPre = null;
        Date timeDiaryNext = null;
        boolean isFlag = false;
        int i=0,j=0;
        for(;i<len-1;i++){
            diaries[i] = new Diary();
            try {
                timeDiary = format.parse(diary.getuTime());
                timeDiaryPre = format.parse(diaryTemp[i].getuTime());
                if (i<len-2) {
                    timeDiaryNext = format.parse(diaryTemp[i + 1].getuTime());
                    int flagPre = timeDiary.compareTo(timeDiaryPre);
                    int flagNext = timeDiary.compareTo(timeDiaryNext);
                    if (flagPre<=0&&flagNext>0){
                        diaries[i].setuStyle(diary.getuStyle());
                        diaries[i].setId(diary.getId());
                        diaries[i].setuTime(diary.getuTime());
                        diaries[i].setuDiary(diary.getuDiary());
                        diaries[i].setuId(diary.getuId());
                        diaries[i].setuDiaryPic(diary.getuDiaryPic());
                        isFlag = true;
                    }else {
                        diaries[i].setuStyle(diaryTemp[j].getuStyle());
                        diaries[i].setId(diaryTemp[j].getId());
                        diaries[i].setuTime(diaryTemp[j].getuTime());
                        diaries[i].setuDiary(diaryTemp[j].getuDiary());
                        diaries[i].setuId(diaryTemp[j].getuId());
                        diaries[i].setuDiaryPic(diaryTemp[j].getuDiaryPic());
                        j++;
                    }
                }else {
                    int flagPre = timeDiary.compareTo(timeDiaryPre);
                    if (flagPre<=0){
                        diaries[i].setuStyle(diary.getuStyle());
                        diaries[i].setId(diary.getId());
                        diaries[i].setuTime(diary.getuTime());
                        diaries[i].setuDiary(diary.getuDiary());
                        diaries[i].setuId(diary.getuId());
                        diaries[i].setuDiaryPic(diary.getuDiaryPic());
                        isFlag = true;
                    }else {
                        diaries[i].setuStyle(diaryTemp[j].getuStyle());
                        diaries[i].setId(diaryTemp[j].getId());
                        diaries[i].setuTime(diaryTemp[j].getuTime());
                        diaries[i].setuDiary(diaryTemp[j].getuDiary());
                        diaries[i].setuId(diaryTemp[j].getuId());
                        diaries[i].setuDiaryPic(diaryTemp[j].getuDiaryPic());
                        j++;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (isFlag){
            diaries[i] = new Diary();
            diaries[i].setuStyle(diaryTemp[j].getuStyle());
            diaries[i].setId(diaryTemp[j].getId());
            diaries[i].setuTime(diaryTemp[j].getuTime());
            diaries[i].setuDiary(diaryTemp[j].getuDiary());
            diaries[i].setuId(diaryTemp[j].getuId());
            diaries[i].setuDiaryPic(diaryTemp[j].getuDiaryPic());
        }else {
            diaries[i] = new Diary();
            diaries[i].setuStyle(diary.getuStyle());
            diaries[i].setId(diary.getId());
            diaries[i].setuTime(diary.getuTime());
            diaries[i].setuDiary(diary.getuDiary());
            diaries[i].setuId(diary.getuId());
            diaries[i].setuDiaryPic(diary.getuDiaryPic());
        }

    }

    public static void setDiary(Diary []diariesFromService){
        diaries = new Diary[diariesFromService.length];
        for(int i=0;i<diariesFromService.length;i++){
            diaries[i] = diariesFromService[i];
        }
        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void setMessage(Messages []messagesFromService){
        messages = new Messages[messagesFromService.length];
        for(int i=0;i<messagesFromService.length;i++){
            messages[i] = messagesFromService[i];
        }
        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void addMessage(Messages []messagesFromService){
        int len;
        if (messages==null){
            len = messagesFromService.length;
        }else {
            len = messages.length + messagesFromService.length;
        }

        Messages []messagesTemp = new Messages[len-messagesFromService.length];
        for (int i=0;i<len-messagesFromService.length;i++){
            messagesTemp[i] = new Messages();
            messagesTemp[i].setId(messages[i].getId());
            messagesTemp[i].setuId(messages[i].getuId());
            messagesTemp[i].setuName(messages[i].getuName());
            messagesTemp[i].setuImgSrc(messages[i].getuImgSrc());
            messagesTemp[i].setContent(messages[i].getContent());
            messagesTemp[i].setTime(messages[i].getTime());
            messagesTemp[i].setDate(messages[i].getDate());
            messagesTemp[i].setTimeSeconds(messages[i].getTimeSeconds());
            messagesTemp[i].setTypeId(messages[i].getTypeId());
            messagesTemp[i].setValid(messages[i].getValid());
        }
        messages = new Messages[len];
        for (int i=0,j=0;i<len;i++){
            if (i<len-messagesFromService.length) {
                messages[i] = messagesTemp[i];
            }else {
                messages[i] = messagesFromService[j];
                j++;
            }
            Log.i("message",messages[i].getTime());
        }

        Log.i("ww","ww");

        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void setNotes(Note []notesFromService){
        notes = new Note[notesFromService.length];
        for(int i=0;i<notesFromService.length;i++){
            notes[i] = new Note();
            notes[i].setId(notesFromService[i].getId());
            notes[i].setuId(notesFromService[i].getuId());
            notes[i].setuDate(notesFromService[i].getuDate());
            notes[i].setuTime(notesFromService[i].getuTime());
            notes[i].setuStick(notesFromService[i].getuStick());
            notes[i].setuNote(notesFromService[i].getuNote());
        }
        //        NoteSql.addToSql();               //保存到本地数据库
    }

    public static void setDream(Dream []dreamsFromService){
        dreams = new Dream[dreamsFromService.length];
        for(int i=0;i<dreamsFromService.length;i++){
            dreams[i] = new Dream();
            dreams[i] = dreamsFromService[i];
        }
        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void addDreamAtTop(Dream []dreamsFromService){
        int len;
        if (dreamsFromService!=null) {
            if (dreams == null) {
                len = dreamsFromService.length;
            }else {
                len = dreams.length + dreamsFromService.length;
            }

            Dream []dreamsTemp = new Dream[len-dreamsFromService.length];
            for (int i=0;i<len-dreamsFromService.length;i++){
                dreamsTemp[i] = new Dream();
                dreamsTemp[i].setuId(dreams[i].getuId());
                dreamsTemp[i].setuName(dreams[i].getuName());
                dreamsTemp[i].setuEmail(dreams[i].getuEmail());
                dreamsTemp[i].setuRegTime(dreams[i].getuRegTime());
                dreamsTemp[i].setuImgSrc(dreams[i].getuImgSrc());
                dreamsTemp[i].setuCity(dreams[i].getuCity());
                dreamsTemp[i].setuAge(dreams[i].getuAge());
                dreamsTemp[i].setuBirth(dreams[i].getuBirth());
                dreamsTemp[i].setuSex(dreams[i].getuSex());
                dreamsTemp[i].setuInterest(dreams[i].getuInterest());
                dreamsTemp[i].setuDream(dreams[i].getuDream());
                dreamsTemp[i].setuDreamPic(dreams[i].getuDreamPic());
                dreamsTemp[i].setDreamClickNum(dreams[i].getDreamClickNum());
                dreamsTemp[i].setDreamCommentNum(dreams[i].getDreamCommentNum());
            }


            dreams = new Dream[len];
            for (int i=0,j=0;i<len;i++){
                dreams[i] = new Dream();
                if (i<len-dreamsFromService.length) {
                    dreams[i] = dreamsTemp[i];
                }else {
                    dreams[i] = dreamsFromService[j];
                    j++;
                }
                Log.i("dreams",dreams[i].getuName());
            }
        }

    }


    public static void setFriend(Friend []friendsFromService){
        friends = new Friend[friendsFromService.length];
        for(int i=0;i<friendsFromService.length;i++){
            friends[i] = new Friend();
            friends[i] = friendsFromService[i];
        }
        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void setDreamComment(int id,Comment []commentsFromService){
        if (dreams!=null) {
            for (int i = 0; i < dreams.length; i++) {
                if (dreams[i].uId == id) {
                    dreams[i].commentContent = new Comment[commentsFromService.length];
                    for (int j = 0; j < commentsFromService.length; j++) {
                        dreams[i].commentContent[j] = new Comment();
                        dreams[i].commentContent[j] = commentsFromService[j];
                    }
                }
            }
        }
        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void setDreamClick(int id,Click []clicksFromService){
        if (dreams!=null){
            for (int i=0;i<dreams.length;i++){
                if (dreams[i].uId==id){
                    dreams[i].clickContent = new Click[clicksFromService.length];
                    for (int j=0;j<clicksFromService.length;j++){
                        dreams[i].clickContent[j] = new Click();
                        dreams[i].clickContent[j] = clicksFromService[j];
                    }
                }
            }
        }
        //        DiarySql.addToSql();               //保存到本地数据库
    }

    public static void userLogout(){
        User.uToken = "";
        plans = null;
        notes = null;
        diaries = null;
        friends = null;
        messages = null;
        dreams = null;
    }

    public static boolean getUserInfo(){

        return true;
    }
}
