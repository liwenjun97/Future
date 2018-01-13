package com.example.cslab.future.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.adapter.CardDataImpl;
import com.example.cslab.future.adapter.CardListItemAdapter;
import com.example.cslab.future.adapter.NoteAdapter;
import com.example.cslab.future.entity.Diary;
import com.example.cslab.future.entity.Note;
import com.example.cslab.future.entity.Plan;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * Created by CSLab on 2017/7/15.
 */

public class TodayActivity extends Fragment{
    private String url_note;
    private String data_note;
    private TextView note_lay;
    private TextView date_lay;
    private TextView last;
    private int days;
    private ListView list;
    private List<Map<String, Object>> allValues = new ArrayList<>();
    private NoteAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.today,container,false);
        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        获取note
        /*查询本地数据库*/
        //if(!User.getNote()){      如果本地不存在
        getAllNotes(User.uToken);       //访问服务器
        //}
    }
    public void getAllNotes(String token){
        url_note = "http://192.168.56.1/Home/NoteApi/returnAllNotes";
        try {
            data_note = "token="+ URLEncoder.encode(token, "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(url_note, data_note);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("valueNote", returnStr);
                    msg.setData(data);
                    handlerForGetNote.sendMessage(msg);
                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void swap(Note a,Note b){
        Note np = new  Note();
        np.setId(a.getId());
        np.setuId(a.getuId());
        np.setuNote(a.getuNote());
        np.setuStick(a.getuStick());
        np.setuTime(a.getuTime());
        np.setuDate(a.getuDate());
        np.setContext(a.getContext());

        a.setId(b.getId());
        a.setuId(b.getuId());
        a.setuNote(b.getuNote());
        a.setuStick(b.getuStick());
        a.setuTime(b.getuTime());
        a.setuDate(b.getuDate());
        a.setContext(b.getContext());

        b.setId(np.getId());
        b.setuId(np.getuId());
        b.setuNote(np.getuNote());
        b.setuStick(np.getuStick());
        b.setuTime(np.getuTime());
        b.setuDate(np.getuDate());
        b.setContext(np.getContext());
    }
    public static final int daysBetween(Date early, Date late) {

        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;

        return days;
    }
    private Handler handlerForGetNote = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String valNote = data.getString("valueNote");
            try {
                JSONTokener jsonParser = new JSONTokener(valNote);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if (json.optString("error") == "") {
                    Log.i("22", json.optString("allNotes"));
                    if (json.optString("allNotes") != null) {
                        JSONArray jsonArray = new JSONArray(json.optString("allNotes"));
                        Note[] notes = new Note[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            notes[i] = new Note();
                            notes[i].setId(j.optInt("id"));
                            notes[i].setuId(j.optInt("uId"));
                            notes[i].setuDate(j.optString("uDate"));
                            notes[i].setuStick(j.optInt("uStick"));
                            notes[i].setuNote(j.optString("uNote"));
                            notes[i].setuTime(j.optString("uTime"));
                        }
                        //                    TastyToast.makeText(getApplicationContext(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        //设置到本地并且缓存到本地数据库
                        User.setNotes(notes);
                    }
                } else {
                    Log.i("22", json.optString("error"));
//                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22", valNote);
            note_lay = (TextView) getActivity().findViewById(R.id.note);
            date_lay = (TextView) getActivity().findViewById(R.id.date);
            last = (TextView) getActivity().findViewById(R.id.last);
            if (User.notes == null)
                return;
            else {
                for (int i = 0; i < User.notes.length; i++) {
                    if (User.notes[i].getuStick() == 1) {
                        String t1 = User.notes[i].getuDate();
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date2 = sdf.parse(t1);
                            days = (int) daysBetween(d, date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (days == 0) {
                            note_lay.setText(User.notes[i].getuNote() + "就是今天");
                            date_lay.setText("目标日:" + t1);
                            last.setText(String.valueOf(days));
                        } else if (days < 0) {
                            note_lay.setText(User.notes[i].getuNote() + "已经");
                            date_lay.setText("起始日:" + t1);
                            last.setText(String.valueOf(-days));
                        } else if (days > 0) {
                            note_lay.setText(User.notes[i].getuNote() + "还有");
                            date_lay.setText("目标日:" + t1);
                            last.setText(String.valueOf(days));
                        }
                        break;
                    }
                }
                final int[] a = new int[User.notes.length];
                for (int i = 0; i < User.notes.length; i++) {
                    Map<String, Object> map = new HashMap<>();
                    String t1 = User.notes[i].getuDate();
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date2 = sdf.parse(t1);
                        days = (int) daysBetween(d, date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    a[i] = days;
                }

                for (int i = 0; i < User.notes.length; i++)
                    for (int j = 0; j < User.notes.length - i - 1; j++) {
                        if (a[j] > a[j + 1]) {
                            int temp = a[j];
                            a[j] = a[j + 1];
                            a[j + 1] = temp;
                            swap(User.notes[j], User.notes[j + 1]);
                        }
                    }
                int plus_number = 0;
                for (int i = 0; i < User.notes.length; i++) {
                    if (a[i] < 0)
                        plus_number++;
                }
                for (int i = plus_number; i < (User.notes.length + plus_number) / 2; i++) {
                    swap(User.notes[i], User.notes[User.notes.length - 1 - i + plus_number]);
                }
                for (int i = 0; i < User.notes.length / 2; i++) {
                    swap(User.notes[i], User.notes[User.notes.length - 1 - i]);
                }

                for (int i = 0; i < User.notes.length; i++) {
                    Map<String, Object> map = new HashMap<>();
                    String t1 = User.notes[i].getuDate();
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date2 = sdf.parse(t1);
                        days = (int) daysBetween(d, date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (days == 0) {
                        map.put("note", User.notes[i].getuNote() + "就是今天");
                        map.put("last", String.valueOf(days));
                    } else if (days < 0) {
                        map.put("note", User.notes[i].getuNote() + "已经");
                        map.put("last", String.valueOf(-days));
                    } else if (days > 0) {
                        map.put("note", User.notes[i].getuNote() + "还有");
                        map.put("last", String.valueOf(days));
                    }
                    map.put("noteId",User.notes[i].getId());
                    allValues.add(map);
                }
                list = (ListView) getActivity().findViewById(R.id.notes);
                adapter = new NoteAdapter(getActivity(), allValues);
                list.setAdapter(adapter);

                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {

                        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(TodayActivity.super.getContext());
                        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialogBuilder.setTitle("警告");
                        alertDialogBuilder.setMessage("是否删除这条推荐吗？");
                        alertDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Map<String ,Object> map = allValues.get(position);
                                int noteId = Integer.parseInt(map.get("noteId").toString());
                                deleteNote(User.uToken,noteId);
                                Log.i("noteId", String.valueOf(noteId));
                                allValues.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        alertDialogBuilder.show();
                        return false;
                    }
                });

            }
        }
    };

    private String urlForDelNote;
    private String dataForDelNote;
    public void deleteNote(String token,int noteId){
        urlForDelNote = "http://192.168.56.1/Home/NoteApi/deleteNote";
        try {
            dataForDelNote = "token="+ URLEncoder.encode(token, "UTF-8")+
                    "&noteid="+URLEncoder.encode(String.valueOf(noteId), "UTF-8");
            Thread t = new Thread(){
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForDelNote, dataForDelNote);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForDelNote.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForDelNote =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if(json.optString("success")!=""){
                    Log.i("22",json.optString("success"));
                    TastyToast.makeText(getActivity(), json.optString("success"), TastyToast.LENGTH_SHORT, TastyToast.INFO);
                }else {
                    Log.i("22",json.optString("error"));
                    TastyToast.makeText(getActivity(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22",val);
        }
    };
}
