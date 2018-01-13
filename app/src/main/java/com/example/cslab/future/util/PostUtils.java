package com.example.cslab.future.util;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostUtils {
    public static String LOGIN_URL = "http://localhost/Home/UserApi/getUserInfo";



    public static boolean saveUrlAs(String photoUrl,String fileName){
        //HTTP协议
        try {
            URL url = new URL(photoUrl);
            HttpURLConnection connection =(HttpURLConnection)url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            byte []buffer = new byte[4096];
            int count = 0;
            while((count=in.read(buffer))>0){
                out.write(buffer,0,count);
            }
            out.close();
            in.close();
            return true;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static String uploadFile(String url,String fileName,String uploadFile,String token){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String msg = "";

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			/*允許INPUT output不允许cache*/
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
			/*设置发送的方式*/
            conn.setRequestMethod("POST");
			/*設置头*/
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			/*設置DataOutputStream*/
            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());

            ds.writeBytes(twoHyphens+boundary+end);
            ds.writeBytes("Content-Disposition: form-data;"+"name=\"token\""+end);
            ds.writeBytes(end);
            ds.writeBytes(token);
            ds.writeBytes(end);

            ds.writeBytes(twoHyphens+boundary+end);
            ds.writeBytes("Content-Disposition: form-data;"+"name=\"file\";filename=\""+fileName+"\""+end);
            ds.writeBytes(end);



			/*取得文件的FileInputStream*/
            FileInputStream fStream = new FileInputStream(uploadFile);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/*从文件读取到数据缓冲区*/
            while((length = fStream.read(buffer))!=-1){
                ds.write(buffer,0,length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens+boundary+twoHyphens+end);
            fStream.close();
            ds.flush();

            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer2[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer2)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer2, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                msg = new String(message.toByteArray());
                return msg;
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msg;
    }


    public static String sendPost(String url,String data)
    {
        String msg = "";
        try{
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //设置运行输入,输出:
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //Post方式不能缓存,需手动设置为false
            conn.setUseCaches(false);
            //我们请求的数据:
          /*  String data = "token="+ URLEncoder.encode("3bc05d37782e4c14202fdbf69af72df2", "UTF-8")+
                    "&time="+ URLEncoder.encode("2017-07-10 18:42:56", "UTF-8")+
                    "&clickId="+ URLEncoder.encode("5", "UTF-8")+
                    "&commentedId="+ URLEncoder.encode("5", "UTF-8")+
                    "&typeId="+ URLEncoder.encode("2", "UTF-8")+
                    "&commentText="+ URLEncoder.encode("零零落落", "UTF-8")+
                    "&clickTableId=" + URLEncoder.encode("7", "UTF-8");*/
            //这里可以写一些请求头的东东...
            //获取输出流
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                msg = new String(message.toByteArray());
                return msg;
            }
        }catch(Exception e){e.printStackTrace();}
        return msg;
    }
}