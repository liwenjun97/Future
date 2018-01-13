package com.example.cslab.future.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.cslab.future.R;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.Plan;
import com.example.cslab.future.entity.User;
import com.ramotion.expandingcollection.ECCardData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDataImpl implements ECCardData<String> {

    private String cardTitle;
    private Integer mainBackgroundResource;
    private Integer headBackgroundResource;
    private List<String> listItems;

    public CardDataImpl(String cardTitle, Integer mainBackgroundResource, Integer headBackgroundResource, List<String> listItems) {

        this.mainBackgroundResource = mainBackgroundResource;
        this.headBackgroundResource = headBackgroundResource;
        this.listItems = listItems;
        this.cardTitle = cardTitle;
    }

    @Override
    public Integer getMainBackgroundResource() {
        return mainBackgroundResource;
    }

    @Override
    public Integer getHeadBackgroundResource() {
        return headBackgroundResource;
    }

    @Override
    public List<String> getListItems() {
        return listItems;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public static List<ECCardData> generateExampleData() {
        List<ECCardData> list = new ArrayList<>();
//        List<Map<String ,String >> listContent = new ArrayList<>();
        int plansLen = 0;
        int diariesLen = 0;
        if (User.plans!=null){
            plansLen = User.plans.length;
        }
        if (User.diaries!=null){
            diariesLen = User.diaries.length;
        }
        Log.i("planlen", String.valueOf(plansLen));
        Log.i("diarylen", String.valueOf(diariesLen));
        int planNum=0;
        int diaryNum=0;
        for(;planNum<plansLen&&diaryNum<diariesLen;){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date timePlan = null;
            Date timeDiary = null;
            try {
                timePlan = format.parse(User.plans[planNum].getuTime());
                timeDiary = format.parse(User.diaries[diaryNum].getuTime());
                //如果compareTo返回0，表示两个日期相等，返回小于0的值，表示timePlan在timeDiary之前，大于0表示timePlan在timeDiary之后
                int flag = timePlan.compareTo(timeDiary);
                Log.i("flag", String.valueOf(flag));
                if (flag==0){       //两日期相等则匹配
                    String planStr = User.plans[planNum].getuTime() + "    计划:\r\n" + "Type:\r\n" + User.plans[planNum].getuType() + "\r\n简介:\r\n" + User.plans[planNum].getuRemark() + "\r\n完成情况:\r\n" + User.plans[planNum].getuFinish() + " %";
                    Map<String, String> map = new HashMap<>();
                    map.put("planStr", planStr);


                    String diaryStr = User.diaries[diaryNum].getuTime() + "    日记:\r\n" + "  " + User.diaries[diaryNum].getuDiary();
                    map.put("diaryStr", diaryStr);



                    list.add(new CardDataImpl(User.plans[planNum].getuTime().substring(User.plans[planNum].getuTime().indexOf("2"),User.plans[planNum].getuTime().indexOf(" ")),
                            R.mipmap.attractions, R.mipmap.attractions_head, createItemsList(map)));
                    planNum++;
                    diaryNum++;
                }else if (flag>0){      //timePlan > timeDiary 则timeDiary在该天没有写日记
                    String planStr = User.plans[planNum].getuTime() + "    计划:\r\n" + "Type:\r\n" + User.plans[planNum].getuType() + "\r\n简介:\r\n" + User.plans[planNum].getuRemark() + "\r\n完成情况:\r\n" + User.plans[planNum].getuFinish() + " %";
                    Map<String, String> map = new HashMap<>();
                    map.put("planStr", planStr);

                    String diaryStr = "该日没有写日记";
                    map.put("diaryStr", diaryStr);


                    list.add(new CardDataImpl(User.plans[planNum].getuTime().substring(User.plans[planNum].getuTime().indexOf("2"),
                            User.plans[planNum].getuTime().indexOf(" ")), R.mipmap.attractions, R.mipmap.attractions_head, createItemsList(map)));
                    planNum++;
                }else if (flag<0){  //timeDiary > timePlan 之前制定 则在该天没有写计划
                    String planStr = "该日没有制定计划!";
                    Map<String, String> map = new HashMap<>();
                    map.put("planStr", planStr);

                    String diaryStr = User.diaries[diaryNum].getuTime().substring(User.plans[planNum].getuTime().indexOf("2"),
                            User.plans[planNum].getuTime().indexOf(" ")) + "    日记:\r\n" + "  " + User.diaries[diaryNum].getuDiary();
                    map.put("diaryStr", diaryStr);


                    list.add(new CardDataImpl(User.diaries[diaryNum].getuTime().substring(User.plans[planNum].getuTime().indexOf("2"),
                            User.plans[planNum].getuTime().indexOf(" ")), R.mipmap.attractions, R.mipmap.attractions_head, createItemsList(map)));
                    diaryNum++;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (;planNum<plansLen;planNum++){          //如果plan还有剩下的
            String planStr = User.plans[planNum].getuTime() + "    计划:\r\n" + "Type:\r\n" + User.plans[planNum].getuType() + "\r\n简介:\r\n" + User.plans[planNum].getuRemark() + "\r\n完成情况:\r\n" + User.plans[planNum].getuFinish() + " %";
            Map<String, String> map = new HashMap<>();
            map.put("planStr", planStr);

            String diaryStr = "该日没有写日记";
            map.put("diaryStr", diaryStr);
            list.add(new CardDataImpl(User.plans[planNum].getuTime().substring(User.plans[planNum].getuTime().indexOf("2"),
                    User.plans[planNum].getuTime().indexOf(" ")), R.mipmap.attractions, R.mipmap.attractions_head, createItemsList(map)));

        }
        for (;diaryNum<diariesLen;diaryNum++){      //如果diary还有剩下的
            String planStr = "该日没有制定计划!";
            Map<String, String> map = new HashMap<>();
            map.put("planStr", planStr);

            String diaryStr = User.diaries[diaryNum].getuTime() + "    日记:\r\n" + "  " + User.diaries[diaryNum].getuDiary();
            map.put("diaryStr", diaryStr);
            list.add(new CardDataImpl(User.diaries[diaryNum].getuTime().substring(User.plans[planNum].getuTime().indexOf("2"),
                    User.plans[planNum].getuTime().indexOf(" ")), R.mipmap.attractions, R.mipmap.attractions_head, createItemsList(map)));

//            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_user_bg_34);
        }


//        list.add(new CardDataImpl("DATA", R.mipmap.attractions, R.mipmap.attractions_head, createItemsList("Card 1")));
//        list.add(new CardDataImpl("DATA", R.mipmap.city_scape, R.mipmap.city_scape_head, createItemsList("Card 2")));
//        list.add(new CardDataImpl("DATA", R.mipmap.nature, R.mipmap.nature_head, createItemsList("Card 3")));
        return list;
    }

    public static List<String> createItemsList(Map<String ,String > map) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(
                map.get("planStr"),
                map.get("diaryStr")
        ));
        return list;
    }

}
