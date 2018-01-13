package com.example.cslab.future.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.adapter.CommentAdapter;
import com.example.cslab.future.adapter.CommentItem;
import com.example.cslab.future.entity.Click;
import com.example.cslab.future.entity.Comment;
import com.example.cslab.future.entity.User;
import com.example.cslab.future.util.PostUtils;
import com.example.cslab.future.util.Screen;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.cslab.future.R.id.forget;
import static com.example.cslab.future.R.id.num;

public class CommentActivity extends AppCompatActivity {
    private String urlForGetDreamDetail;
    private String dataForGetDreamDetail;
    private String urlForMakeComment;
    private String dataForMakeComment;
    private PopupWindow mPopupWindow;

    private String dirImg_square;
    private String dirImg_allHead;
    private EditText commentOther;
    private Button send;
    private Button outComment;
    private SweetAlertDialog progressDialog;
    private ListView commentList;
    private ListView commentListHead;
    private List<CommentItem> allValues = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private CommentItem commentItem;
    private Button ensureCommentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        outComment = (Button) findViewById(R.id.outComment);
        dirImg_square = getApplicationContext().getFilesDir().getAbsolutePath() + "/Img_square/";     //dream图片
        dirImg_allHead = getApplicationContext().getFilesDir().getAbsolutePath() + "/Img_allHead/";     //head图片

        commentList = (ListView) findViewById(R.id.commentList);

        progressDialog = new SweetAlertDialog(CommentActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("WAIT")
                .setContentText("数据加载中...");
        progressDialog.show();

        String uId = (String) getIntent().getSerializableExtra("uId");
        getDreamDetail(User.uToken, uId);                //获取该条评论

        final View popupView = getLayoutInflater().inflate(R.layout.item, null);
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(true);
        commentOther = (EditText) popupView.findViewById(R.id.commentContext);
        send = (Button) popupView.findViewById(R.id.send);

        commentList.setVerticalScrollBarEnabled(false);//不管有没有活动都隐藏
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View listview, final int position, long l) {
//            int[] location=new int[2];
//            listview.getLocationOnScreen(location);
//            commentList.smoothScrollByOffset(location[1]);

                mPopupWindow.showAtLocation(listview, Gravity.BOTTOM, 0, 0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommentItem info = (CommentItem) adapterView.getItemAtPosition(position);
                        Toast.makeText(view.getContext(), commentOther.getText().toString(), Toast.LENGTH_SHORT).show();


                        int commentId = User.uId;
                        int commentedId = (int) info.map.get("commentId");
                        int uId = (int) info.map.get("uId");
                        String commentText = commentOther.getText().toString();
                        Log.i("commentId", String.valueOf(commentId));
                        Log.i("commentedId", String.valueOf(commentedId));
                        Log.i("uId", String.valueOf(uId));
                        makeComment(User.uToken, commentId, commentedId, uId, commentText);


//                    Map<String, Object> map2 = new HashMap<>();
//                    map2.put("id",123);
//                    map2.put("commentId",User.getuId());
//                    map2.put("commentName","askfljalskf");
//                    map2.put("reviewerName",info.map.get("commentName"));
//                    map2.put("action","回复");
//                    map2.put("commentedId",info.map.get("commentedId"));
//                    map2.put("commentedName","commentName");
//                    map2.put("uId",info.map.get("uId"));
//                    map2.put("uName",info.map.get("uName"));
//                    map2.put("commentContent",commentOther.getText().toString());
//                    map2.put("commentTime","2017-07-22");
//                    commentItem = new CommentItem(1, (HashMap<String, Object>) map2);
//                    allValues.add(commentItem);
//                    commentOther.setText("");
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if(imm !=null){
//                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
//                    }
//                    mPopupWindow.dismiss();
                    }
                });
            }
        });

        outComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CommentActivity.this, SquareActivity.class);
                startActivity(it);
                allValues.clear();
                finish();
            }
        });

        commentList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firtItem, int itemCount, int totalItem) {

            }
        });
    }

    //返回主界面
    @Override
    public void onBackPressed()
    {
        Intent it = new Intent(CommentActivity.this, SquareActivity.class);
        startActivity(it);
        allValues.clear();
        finish();
    }


    public void makeComment(String token, int commentId, int commentedId, int uId, String commentText) {
        urlForMakeComment = "http://192.168.56.1/Home/TomorrowApi/makeComment";
        try {
            dataForMakeComment = "token=" + URLEncoder.encode(token, "UTF-8") +
                    "&commentId=" + URLEncoder.encode(String.valueOf(commentId), "UTF-8") +
                    "&commentedId=" + URLEncoder.encode(String.valueOf(commentedId), "UTF-8") +
                    "&uId=" + URLEncoder.encode(String.valueOf(uId), "UTF-8") +
                    "&commentText=" + URLEncoder.encode(commentText, "UTF-8");
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForMakeComment, dataForMakeComment);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForMakeComment.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForMakeComment = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if (json.optString("success") != "") {
                    if (json.optString("commentData").length() != 0) {
                        JSONArray jsonArray = new JSONArray(json.optString("commentData"));
                        JSONObject j = (JSONObject) jsonArray.get(0);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("id", j.optInt("id"));
                        map2.put("commentId", j.optInt("commentId"));
                        map2.put("commentName", j.optString("commentName"));
                        map2.put("reviewerName", j.optString("commentedName"));
                        map2.put("action", "回复");
                        map2.put("commentedId", j.optInt("commentedId"));
                        map2.put("commentedName", j.optString("commentedName"));
                        map2.put("uId", j.optInt("uId"));
                        map2.put("uName", j.optString("uName"));
                        map2.put("commentContent", j.optString("commentContent"));
                        map2.put("commentTime", j.optString("commentTime"));
                        commentItem = new CommentItem(1, (HashMap<String, Object>) map2);
                        allValues.add(commentItem);
                        commentOther.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        }
                        commentAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();

                        new SweetAlertDialog(CommentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SUCCESS")
                                .setContentText(json.optString("success"))
                                .setConfirmText("收到")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }

                } else {
                    Log.i("22", json.optString("error"));
                    TastyToast.makeText(getApplicationContext(), json.optString("error"), TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22", val);
        }
    };

    public void getDreamDetail(String token, String uId) {
        urlForGetDreamDetail = "http://192.168.56.1/Home/TomorrowApi/getDreamDetail";
        try {
            dataForGetDreamDetail = "token=" + URLEncoder.encode(token, "UTF-8") +
                    "&id=" + URLEncoder.encode(uId, "UTF-8");
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForGetDreamDetail, dataForGetDreamDetail);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    msg.setData(data);
                    handlerForGetDreamDetail.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForGetDreamDetail = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();
                int num = 0;                //用来标记是第几个
                if (json.optString("error") == "") {
                    Log.i("走这里1", "这批");
                    if (User.dreams != null) {
                        Log.i("走这里2", "这批");
                        for (int i = 0; i < User.dreams.length; i++) {
                            if (User.dreams[i].uId == json.optInt("uId")) {
                                num = i;
                                JSONArray jsonArray_comment = new JSONArray(json.optString("comment"));
                                JSONArray jsonArray_click = new JSONArray(json.optString("clickGood"));
                                if (jsonArray_comment.length() != 0) {
                                    Comment[] commentContent = new Comment[jsonArray_comment.length()];
                                    for (int j = 0; j < jsonArray_comment.length(); j++) {
                                        JSONObject oneComment = (JSONObject) jsonArray_comment.get(j);
                                        commentContent[j] = new Comment();
                                        commentContent[j].setId(oneComment.optInt("id"));
                                        commentContent[j].setCommentId(oneComment.optInt("commentId"));
                                        commentContent[j].setCommentName(oneComment.optString("commentName"));
                                        commentContent[j].setCommentedId(oneComment.optInt("commentedId"));
                                        commentContent[j].setCommentedName(oneComment.optString("commentedName"));
                                        commentContent[j].setuId(oneComment.optInt("uId"));
                                        commentContent[j].setuName(oneComment.optString("uName"));
                                        commentContent[j].setCommentContent(oneComment.optString("commentContent"));
                                        commentContent[j].setCommentTime(oneComment.optString("commentTime"));
                                    }

                                    User.setDreamComment(json.optInt("uId"), commentContent);
                                }
                                if (jsonArray_click.length() != 0) {
                                    Click[] clickContent = new Click[jsonArray_click.length()];
                                    for (int j = 0; j < jsonArray_click.length(); j++) {
                                        JSONObject oneClick = (JSONObject) jsonArray_click.get(j);
                                        clickContent[j] = new Click();
                                        clickContent[j].setId(oneClick.optInt("id"));
                                        clickContent[j].setClickId(oneClick.optInt("clickId"));
                                        clickContent[j].setClickName(oneClick.optString("clickName"));
                                        clickContent[j].setuId(oneClick.optInt("uId"));
                                        clickContent[j].setuName(oneClick.optString("uName"));
                                        clickContent[j].setClickNum(oneClick.optInt("clickNum"));
                                        clickContent[j].setClickTime(oneClick.optString("clickTime"));
                                    }
                                    User.setDreamClick(json.optInt("uId"), clickContent);
                                }
                                break;
                            }
                        }

                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("uId", User.dreams[num].uId);
                        map1.put("uName", User.dreams[num].uName);
                        map1.put("uRegTime", User.dreams[num].uRegTime);
                        map1.put("head_pic_path", dirImg_allHead + User.dreams[num].uId + ".jpg");
                        map1.put("dream_pic_path", dirImg_square + User.dreams[num].uId + ".jpg");
                        map1.put("dream_content", User.dreams[num].uDream);
                        map1.put("commentNum", User.dreams[num].dreamCommentNum);
                        map1.put("clickNum", User.dreams[num].dreamClickNum);
                        if (User.dreams[num].clickContent != null) {
                            boolean clickFlag = false;
                            for (int j = 0; j < User.dreams[num].clickContent.length; j++) {
                                if (User.dreams[num].clickContent[j].clickId == User.uId) {
                                    map1.put("isClick", "1");               //已经点过赞了
                                    map1.put("tableId", User.dreams[num].clickContent[j].id);
                                    clickFlag = true;
                                    break;
                                }
                            }
                            if (!clickFlag) {
                                map1.put("isClick", "0");                //没点赞
                            }
                        } else {
                            map1.put("isClick", "0");                        //还没点赞
                        }

                        commentItem = new CommentItem(0, (HashMap<String, Object>) map1);
                        allValues.add(commentItem);
                        if (User.dreams[num].commentContent != null) {
                            for (int i = 0; i < User.dreams[num].commentContent.length; i++) {
                                Map<String, Object> map2 = new HashMap<>();
                                map2.put("id", User.dreams[num].commentContent[i].id);
                                map2.put("commentId", User.dreams[num].commentContent[i].commentId);
                                map2.put("commentName", User.dreams[num].commentContent[i].commentName);
                                if (User.dreams[num].commentContent[i].commentedId == User.dreams[num].commentContent[i].uId) {
                                    map2.put("action", "评论");
                                    map2.put("reviewerName", "");
                                } else {
                                    map2.put("action", "回复");
                                    map2.put("reviewerName", User.dreams[num].commentContent[i].commentedName);
                                }
                                map2.put("commentedId", User.dreams[num].commentContent[i].commentedId);
                                map2.put("commentedName", User.dreams[num].commentContent[i].commentedName);
                                map2.put("uId", User.dreams[num].commentContent[i].uId);
                                map2.put("uName", User.dreams[num].commentContent[i].uName);
                                map2.put("commentContent", User.dreams[num].commentContent[i].commentContent);
                                map2.put("commentTime", User.dreams[num].commentContent[i].commentTime);
                                commentItem = new CommentItem(1, (HashMap<String, Object>) map2);
                                allValues.add(commentItem);
                            }
                        }
                        commentAdapter = new CommentAdapter(CommentActivity.this, allValues);
                        commentList.setAdapter(commentAdapter);
                        progressDialog.cancel();

                        commentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                Log.i("我要删除你了", "我要删除你了哦");
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
//                                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//                                alertDialogBuilder.setTitle("警告");
//                                alertDialogBuilder.setMessage("是否删除这条评论？");
//                                alertDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                });
//                                alertDialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        Map<String, Object> map = allValues.get(position).map;
//                                        int commentTableId = Integer.parseInt(map.get("id").toString());
//                                        delComment(User.uToken, commentTableId);
//                                        allValues.remove(position);
//                                        commentAdapter.notifyDataSetChanged();
//                                    }
//                                });
//                                alertDialogBuilder.show();



                                new SweetAlertDialog(CommentActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("你确定要删除该条评论嘛？")
                                        .setCancelText("No")
                                        .setConfirmText("Yes")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                Map<String, Object> map = allValues.get(position).map;
                                                int commentTableId = Integer.parseInt(map.get("id").toString());
                                                delComment(User.uToken, commentTableId,position);
                                            }
                                        })
                                        .show();

                                return false;
                            }
                        });

                    }
                } else {
                    Log.i("22", json.optString("error"));
                    new SweetAlertDialog(CommentActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("重新登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    User.uToken = "";       //来个按钮
                                    Intent it = new Intent(CommentActivity.this, LoginActivity.class);
                                    startActivity(it);
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22", val);
        }
    };


    private String urlForDelComment;
    private String dataForDelComment;

    public void delComment(String token, int commentTableId, final int position) {
        urlForDelComment = "http://192.168.56.1/Home/TomorrowApi/delComment";
        try {
            dataForDelComment = "token=" + URLEncoder.encode(token, "UTF-8") +
                    "&commentTableId=" + URLEncoder.encode(String.valueOf(commentTableId), "UTF-8");
            Thread t = new Thread() {
                @Override
                public void run() {
                    String returnStr;
                    returnStr = PostUtils.sendPost(urlForDelComment, dataForDelComment);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", returnStr);
                    data.putInt("position",position);
                    msg.setData(data);
                    handlerForDelComment.sendMessage(msg);

                }
            };
            t.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Handler handlerForDelComment = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val = data.getString("value");
            int position = data.getInt("position");
            try {
                JSONTokener jsonParser = new JSONTokener(val);
                JSONObject json = (JSONObject) jsonParser.nextValue();

                if (json.optString("success") != "") {
                    Log.i("22", json.optString("success"));
                    allValues.remove(position);
                    commentAdapter.notifyDataSetChanged();
                    new SweetAlertDialog(CommentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS")
                            .setContentText(json.optString("success"))
                            .setConfirmText("了解")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    Log.i("22", json.optString("error"));
                    new SweetAlertDialog(CommentActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("收到")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("22", val);
        }
    };

}