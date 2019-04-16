package com.example.www24.facedetection.util;

import android.util.Log;

import com.example.www24.facedetection.HttpCallbackListen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,String value,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendHttpRequest(String address, String value, HttpCallbackListen listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("HttpUtil","进入访问请求");
                HttpURLConnection connection = null;
                try {
                    Log.v("HttpUtil","1");
                    URL url = new URL(address);
                    Log.v("HttpUtil","2");
                    connection = (HttpURLConnection) url.openConnection();
                    Log.v("HttpUtil","3");
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();
                    Log.v("HttpUtil",value);
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    Log.v("HttpUtil","4");
                    outputStream.writeBytes(value);
                    Log.v("HttpUtil","5");
                    outputStream.flush();
                    outputStream.close();
                    Log.v("HttpUtil","提交数据");
                    int resultCode = connection.getResponseCode();
                    Log.v("HttpUtil","提交数据结果"+resultCode);
                    if(resultCode == HttpURLConnection.HTTP_OK){
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        Log.v("HttpUtil",response.toString());
                        if(listener != null){
                            //回调onFinish方法
                            listener.onFinish(response.toString());
                        }
                    }

                }catch (Exception e){
                    if(listener !=null){
                        //回调onError方法
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
