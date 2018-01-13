package com.example.cslab.future.util;

/**
 * Created by CSLab on 2017/7/18.
 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.activity.Person_Data_Activity;
import com.example.cslab.future.entity.User;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class head_pic_choice extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLocal, buttonCamera, btn_cancel;
    private ImageView imageView;
    private LinearLayout layout;
    //相机拍摄的头像文件(本次演示存放在SD卡根目录下)
    private static final File USER_ICON = new File(Environment.getExternalStorageDirectory(), "user_icon.jpg");
    //请求识别码(分别为本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;



    private String urlForChangeHead;
    private String urlForGetHead;
    private String saveForGetHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.head_choice);
        buttonLocal = (Button) findViewById(R.id.buttonLocal);
        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonLocal.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
        //////////////////////////////////////////////////////////
//添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"提示：点击窗口外部关闭窗口！",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        //添加按钮监听
//        buttonCamera.setOnClickListener(this);
//        buttonLocal.setOnClickListener(this);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
/////////////////////////////////////////////////////////
    }
    ////实现onTouchEvent触屏函数但点击屏幕时销毁本Activity  
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    //设置点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLocal:
                //调用获取本地图片的方法
                getPicFromLocal();
                break;
            case R.id.buttonCamera:
                //调用相机拍照的方法
                getPicFromCamera();
                break;
            default:
                break;
        }

    }

    /**
     *    * 从本机相册获取图片
     *    
     */
    private void getPicFromLocal() {
        Intent intent = new Intent();
        // 获取本地相册方法一
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //获取本地相册方法二
//    intent.setAction(Intent.ACTION_PICK);
//    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        "image/*");
        startActivityForResult(intent, CODE_PHOTO_REQUEST);
    }

    /**
     *    * 通过相机拍摄获取图片，
     *    * 并存入设置的路径中
     *    
     */
    private void getPicFromCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(USER_ICON));
        startActivityForResult(intent, CODE_CAMERA_REQUEST);
    }

    /**
     *    * 图片裁剪
     *    *
     *    * @param uri
     *    
     */
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
       /*outputX outputY 是裁剪图片宽高
     *这里仅仅是头像展示，不建议将值设置过高
     * 否则超过binder机制的缓存大小的1M限制
     * 报TransactionTooLargeException
     */
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_PHOTO_CLIP);
    }

    /**
     *    * 提取保存裁剪之后的图片数据，并设置头像部分的View
     *    
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(photo);
            //保存到本地
            try {
                saveBitmap(photo, User.uToken+".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //上传至服务器
            changeHeadImg();
        }
    }


    private void saveBitmap(Bitmap bitmap,String bitName) throws IOException
    {
        File file = new File(getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_head/"+bitName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void changeHeadImg(){
        urlForChangeHead = "http://192.168.56.1/Home/UserApi/changeHeadImg";
        urlForGetHead = "http://192.168.56.1/Img/head/" + User.uToken + ".jpg";
        Thread t = new Thread(){
            @Override
            public void run() {
                String returnStr = null;
                String fileName = User.uToken + ".jpg";
                saveForGetHead = getApplicationContext().getFilesDir().getAbsolutePath()+"/Img_head/"+fileName;
                returnStr = PostUtils.uploadFile(urlForChangeHead,fileName,saveForGetHead,User.uToken);
                //下载随机图片
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value2",returnStr);
                msg.setData(data);
                handlerForChangeHeadImg.sendMessage(msg);
            }
        };
        t.start();
    }
    private Handler handlerForChangeHeadImg =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            Bundle data = msg.getData();
            String val2 = data.getString("value2");
            Log.i("val2",val2);
            JSONTokener jsonParser = new JSONTokener(val2);
            try {
                JSONObject json = (JSONObject) jsonParser.nextValue();
                if (json.optString("success")!=""){
                    User.uImgSrc = urlForGetHead;
                    //跳转刷新

                    Person_Data_Activity.me.finish();
                    Intent it=new Intent(head_pic_choice.this, Person_Data_Activity.class);
                    startActivity(it);
                    finish();
                }else {
                    new SweetAlertDialog(head_pic_choice.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ERROR")
                            .setContentText(json.optString("error"))
                            .setConfirmText("更改图片失败")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Person_Data_Activity.me.finish();
                                    Intent it=new Intent(head_pic_choice.this, Person_Data_Activity.class);
                                    startActivity(it);
                                    finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(head_pic_choice.this, "取消", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case CODE_CAMERA_REQUEST:
                if (USER_ICON.exists()) {
                    photoClip(Uri.fromFile(USER_ICON));
                }
                break;
            case CODE_PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case CODE_PHOTO_CLIP:
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

