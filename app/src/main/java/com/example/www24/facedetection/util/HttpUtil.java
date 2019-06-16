package com.example.www24.facedetection.util;

import android.util.Log;

import com.example.www24.facedetection.HttpCallbackListen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.www24.facedetection.Common.Constants.JSON_TYPE;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,String value,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON_TYPE, value);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendGetOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    private final static String TAG = "HttpUtil";
    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();
    public static void post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap, HttpCallbackListen listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG,"进入到post方法中");
                HttpURLConnection conne;
                try{
                    URL url1 = new URL(url);
                    conne = (HttpURLConnection) url1.openConnection();
                    conne.setDoOutput(true);
                    conne.setUseCaches(false);
                    conne.setRequestMethod("POST");
                    conne.setConnectTimeout(CONNECT_TIME_OUT);
                    conne.setReadTimeout(READ_OUT_TIME);
                    conne.setRequestProperty("accept", "*/*");
                    conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
                    conne.setRequestProperty("connection", "Keep-Alive");
                    conne.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
                    DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
                    Iterator iter = map.entrySet().iterator();
                    Log.v(TAG,"信息设置完毕");
                    while(iter.hasNext()){
                        Map.Entry<String, String> entry = (Map.Entry) iter.next();
                        String key = entry.getKey();
                        String value = entry.getValue();
                        obos.writeBytes("--" + boundaryString + "\r\n");
                        obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                                + "\"\r\n");
                        obos.writeBytes("\r\n");
                        obos.writeBytes(value + "\r\n");
                    }
                    Log.v(TAG,"API等信息设置完毕");
                    if(fileMap != null && fileMap.size() > 0){
                        Iterator fileIter = fileMap.entrySet().iterator();
                        while(fileIter.hasNext()){
                            Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                            obos.writeBytes("--" + boundaryString + "\r\n");
                            obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                                    + "\"; filename=\"" + encode(" ") + "\"\r\n");
                            obos.writeBytes("\r\n");
                            obos.write(fileEntry.getValue());
                            obos.writeBytes("\r\n");
                        }
                    }
                    Log.v(TAG,"图片上传完成");
                    obos.writeBytes("--" + boundaryString + "--" + "\r\n");
                    obos.writeBytes("\r\n");
                    obos.flush();
                    obos.close();
                    InputStream ins = null;
                    int code = conne.getResponseCode();
                    Log.v(TAG,"conne返回code为"+Integer.toString(code));
                    try{
                        if(code == 200){
                            ins = conne.getInputStream();
                        }else{
                            ins = conne.getErrorStream();
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
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
                        ins.close();
                    }catch (SSLException e){
                        e.printStackTrace();
                        Log.v(TAG,"SSLException");
                    }
                }catch (Exception e){
                    if(listener !=null){
                        //回调onError方法
                        listener.onError(e);
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    private static String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
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
