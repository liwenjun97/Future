package com.example.cslab.future.util;

import com.example.cslab.future.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CSLab on 2017/7/20.
 */

public class GlobalData {
    public static String plan;
    public static Map<String,Map<String,Object>> L1data =new HashMap<>();
    public static Map<String,Map<String,Object>> L2data =new HashMap<>();
    public static void init(){
        Map<String,Object> study=new HashMap<>();
        study.put("centerTitle","学习");
        study.put("aroundCircleTitleCn",new String[]{"驾校","阅读","上课","自习","图书馆","考试","讲座"});
        study.put("circleIcon",new int[]{R.drawable.ic_drive, R.drawable.ic_read,R.drawable.ic_selfstudy ,R.drawable.ic_class, R.drawable.ic_library, R.drawable.ic_test, R.drawable.ic_lecture});
        study.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        study.put("aroundSmallCircleIndex",new int []{0,0,0,0,0,0,0});
        study.put("hasSub",null);
        L1data.put("学习",study);

        Map<String,Object> life =new HashMap<>();
        life.put("centerTitle","生活");
        life.put("aroundCircleTitleCn",new String[]{"志愿服务","社团工作","旅行","美食","情感","兼职","购物"});
        life.put("circleIcon",new int[]{R.drawable.ic_volunteer, R.drawable.ic_organization, R.drawable.ic_travel, R.drawable.ic_food, R.drawable.ic_motion, R.drawable.ic_job, R.drawable.ic_shopping});
        life.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DOING, AttrEntity.DEF, AttrEntity.DEF});
        life.put("aroundSmallCircleIndex",new int []{0,0,0,0,0,0,0});
        life.put("hasSub",new String[]{"情感"});
        L1data.put("生活",life);

        Map<String,Object> entertainment=new HashMap<>();
        entertainment.put("centerTitle","娱乐");
        entertainment.put("aroundCircleTitleCn",new String[]{"影视","音乐","游玩","二次元"});
        entertainment.put("circleIcon",new int[]{R.drawable.ic_tv, R.drawable.ic_music, R.drawable.ic_tour, R.drawable.ic_secondworld});
        entertainment.put("circleCompleteStatusList",new int[]{AttrEntity.DOING, AttrEntity.DOING, AttrEntity.DOING, AttrEntity.DOING});
        entertainment.put("aroundSmallCircleIndex",new int []{0,0,0,0});
        entertainment.put("hasSub",new String[]{"影视","音乐","游玩","二次元"});
        L1data.put("娱乐",entertainment);

        Map<String,Object> health=new HashMap<>();
        health.put("centerTitle","健康");
        health.put("aroundCircleTitleCn",new String[]{"运动","养生"});
        health.put("circleIcon",new int[]{R.drawable.ic_sport, R.drawable.ic_betterlife});
        health.put("circleCompleteStatusList",new int[]{AttrEntity.DOING, AttrEntity.DOING});
        health.put("aroundSmallCircleIndex",new int[]{0,0});
        health.put("hasSub",new String[]{"运动","养生"});
        L1data.put("健康",health);

        Map<String,Object> motion=new HashMap<>();
        motion.put("centerTitle","情感");
        motion.put("aroundCircleTitleCn",new String[]{"约会","聚会","陪伴家人"});
        motion.put("circleIcon",new int[]{R.drawable.ic_date, R.drawable.ic_together, R.drawable.ic_famil});
        motion.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        motion.put("aroundSmallCircleIndex",new int[]{0,0,0});
        L2data.put("情感",motion);

        Map<String,Object> movie=new HashMap<>();
        movie.put("centerTitle","影视");
        movie.put("aroundCircleTitleCn",new String[]{"追剧","追番","电影"});
        movie.put("circleIcon",new int[]{R.drawable.ic_sober, R.drawable.ic_comic, R.drawable.ic_movie});
        movie.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        movie.put("aroundSmallCircleIndex",new int[]{0,0,0});
        L2data.put("影视",movie);

        Map<String,Object> music=new HashMap<>();
        music.put("centerTitle","音乐");
        music.put("aroundCircleTitleCn",new String[]{"K歌","演唱会","音乐会"});
        music.put("circleIcon",new int[]{R.drawable.ic_sing, R.drawable.ic_concert, R.drawable.ic_musicale});
        music.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        music.put("aroundSmallCircleIndex",new int[]{0,0,0});
        L2data.put("音乐",music);

        Map<String,Object> sport=new HashMap<>();
        sport.put("centerTitle","运动");
        sport.put("aroundCircleTitleCn",new String[]{"健身","球类","骑行","游泳"});
        sport.put("circleIcon",new int[]{R.drawable.ic_stronger, R.drawable.ic_ball, R.drawable.ic_cycle, R.drawable.ic_swim});
        sport.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        sport.put("aroundSmallCircleIndex",new int[]{0,0,0,0});
        L2data.put("运动",sport);

        Map<String,Object> healthy_life =new HashMap<>();
        healthy_life.put("centerTitle","养生");
        healthy_life.put("aroundCircleTitleCn",new String[]{"早睡早起","足疗","瑜伽","瘦身"});
        healthy_life.put("circleIcon",new int[]{R.drawable.ic_sleep, R.drawable.ic_foot, R.drawable.ic_yoga, R.drawable.ic_thinner});
        healthy_life.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        healthy_life.put("aroundSmallCircleIndex",new int[]{0,0,0,0});
        L2data.put("养生",healthy_life);

        Map<String,Object> tour=new HashMap<>();
        tour.put("centerTitle","游玩");
        tour.put("aroundCircleTitleCn",new String[]{"动物园","海洋馆","科技馆","漫展","博物馆","游乐园"});
        tour.put("circleIcon",new int[]{R.drawable.ic_zoo, R.drawable.ic_sea, R.drawable.ic_technology, R.drawable.ic_showcomic, R.drawable.ic_museum, R.drawable.ic_park});
        tour.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        tour.put("aroundSmallCircleIndex",new int[]{0,0,0,0,0,0});
        L2data.put("游玩",tour);

        Map<String,Object> comic=new HashMap<>();
        comic.put("centerTitle","二次元");
        comic.put("aroundCircleTitleCn",new String[]{"漫展","游戏","COSPLAY"});
        comic.put("circleIcon",new int[]{R.drawable.ic_showcomic, R.drawable.ic_game, R.drawable.ic_cosplay});
        comic.put("circleCompleteStatusList",new int[]{AttrEntity.DEF, AttrEntity.DEF, AttrEntity.DEF});
        comic.put("aroundSmallCircleIndex",new int[]{0,0,0});
        L2data.put("二次元",comic);
    }
}
