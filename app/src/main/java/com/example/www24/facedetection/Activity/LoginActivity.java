package com.example.www24.facedetection.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.www24.facedetection.Bean.User;
import com.example.www24.facedetection.Common.Constants;
import com.example.www24.facedetection.R;
import com.example.www24.facedetection.util.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "Login_ActivityLog";
    private static final int USERNOTEXIST = 0;
    private static final int PASSWORDNOTRIGHT = 1;

    private EditText et_name;
    private EditText et_password;
    private Button bt_username_clear;
    private Button bt_password_eye;
    private Button bt_password_clear;
    private Button mLoginButton;
    private Button mLoginError;
    private Button mRegister;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_name = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        bt_username_clear = findViewById(R.id.bt_username_clear);
        bt_password_eye = findViewById(R.id.bt_pwd_eyes);
        bt_password_clear = findViewById(R.id.bt_pwd_clear);

        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_password.addTextChangedListener(password_watcher);

        mLoginButton = findViewById(R.id.login);
        mLoginError = findViewById(R.id.forget_password);
        mRegister = findViewById(R.id.register);

        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        editor = sp.edit();
//        ONLYTEST.setOnClickListener(this);
//        ONLYTEST.setOnLongClickListener((View.OnLongClickListener) this);
        onClick();
    }

    private void onClick() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        mLoginError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initWatcher() {
        username_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_password.setText("");
                if (editable.toString().length()>0){
                    bt_username_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }

            }
        };
        password_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length()>0){
                    bt_password_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_password_clear.setVisibility(View.INVISIBLE);
                }

            }
        };
    }



    //登陆操作
    private void login() {
        String username = et_name.getText().toString();
        String password = et_password.getText().toString();

        //利用Gson将user转成json
        Gson gson = new Gson();
        User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        Log.v(TAG,gson.toJson(user));


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        mLoginButton.setClickable(false);
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在登录中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        HttpUtil.sendOkHttpRequest(Constants.SEVER_URL + "login", gson.toJson(user), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG,"Http请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v(TAG,"http请求成功");
                String requestData = response.body().string();
                Log.v(TAG,requestData);
                //解析从服务端接收到的数据
                JSONObject jsonResult = null;
                try {
                    jsonResult = new JSONObject(requestData);
                    int result = jsonResult.getInt("result");
                    Log.v(TAG,String.valueOf(result));
                    if(result == 1) {
                        editor.putBoolean("isLogined",true);
                        editor.putString("name",username);
                        editor.apply();
                        //Toast.makeText(MyApplication.getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        progress.dismiss();
                        finish();
                        Log.v(TAG,"登陆成功");
                    } else
                    {
                        editor.putBoolean("isLogined",false);
                        editor.apply();
                        //Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                        mLoginButton.setClickable(true);
                        progress.dismiss();
                        Log.v(TAG,"登陆失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }



}
