package com.example.www24.facedetection.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.www24.facedetection.Bean.User;
import com.example.www24.facedetection.Common.Constants;
import com.example.www24.facedetection.MyApplication;
import com.example.www24.facedetection.R;
import com.example.www24.facedetection.util.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="RegisterActivity";

    private EditText et_name;
    private EditText et_password;
    private EditText et_age;
    private EditText et_sex;
    private EditText et_mail;

    private static final int ERROR_USERNAME = 1;

    //Handler定义
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            RegisterActivity activity = (RegisterActivity) reference.get();
            if(activity != null){
                switch (msg.what){
                    case ERROR_USERNAME :
                        Toast.makeText(MyApplication.getContext(),"用户已存在",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }
    private MyHandler myhandle = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = findViewById(R.id.register_button);
        button.setOnClickListener(this);

        et_name = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_age = findViewById(R.id.et_age);
        et_sex = findViewById(R.id.et_sex);
        et_mail = findViewById(R.id.et_mail);

    }

    @Override
    public void onClick(View v) {
        User user = new User();
        user.setUsername(et_name.getText().toString());
        user.setPassword(et_password.getText().toString());
        user.setAge(Integer.parseInt(et_age.getText().toString()));
        user.setSex(et_sex.getText().toString().toCharArray()[0]);
        user.setMail(et_mail.getText().toString());

        Gson gson = new Gson();


        HttpUtil.sendOkHttpRequest(Constants.SEVER_URL + "register", gson.toJson(user), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG,"OkHttp访问失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String requestData = response.body().string();
                Log.v(TAG,requestData);
                //解析从服务端接收到的数据
                JSONObject jsonResult = null;
                try {
                    jsonResult = new JSONObject(requestData);
                    int result = jsonResult.getInt("result");
                    if(result == 1){
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                    else
                    {
                        if(jsonResult.getInt("error_code") == 1){
                            Message msg = new Message();
                            msg.what = ERROR_USERNAME;
                            myhandle.sendMessage(msg);
                        }
                        Log.v(TAG,"注册失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
