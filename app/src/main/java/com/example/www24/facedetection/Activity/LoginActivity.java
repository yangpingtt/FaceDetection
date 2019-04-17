package com.example.www24.facedetection.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.www24.facedetection.Model.User;
import com.example.www24.facedetection.R;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "Login_ActivityLog";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_name = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        bt_username_clear = findViewById(R.id.bt_username_clear);
        bt_password_eye = findViewById(R.id.bt_pwd_eyes);
        bt_password_clear = findViewById(R.id.bt_pwd_clear);

//        bt_username_clear.setOnClickListener(this);
//        bt_password_eye.setOnClickListener(this);
//        bt_password_clear.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_password.addTextChangedListener(password_watcher);

        mLoginButton = findViewById(R.id.login);
        mLoginError = findViewById(R.id.forget_password);
        mRegister = findViewById(R.id.register);
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

//        HttpUtil.sendOkHttpRequest(Constants.SEVER_URL + "testLogin", gson.toJson(user), new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.v(TAG,Constants.SEVER_URL+"testLogin");
//                Log.v(TAG,"Http请求失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.v(TAG,Constants.SEVER_URL+"testLogin");
//                Log.v(TAG,"http请求成功");
//                String requestData = response.body().string();
//                Log.v(TAG,requestData);
//                //解析从服务端接收到的数据
//                JSONObject jsonResult = null;
//                try {
//                    jsonResult = new JSONObject(requestData);
//                    String result = jsonResult.getString("result");
//                    Log.v(TAG,result);
//                    if(result.equals("1")) {
//                        //Toast.makeText(MyApplication.getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        Log.v(TAG,"登陆成功");
//                    } else
//                    {
//                        Log.v("msg","Login");
//                        //
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        HttpUtil.sendHttpRequest("http://123.206.17.25:5000/testLogin", gson.toJson(user), new HttpCallbackListen() {
//            @Override
//            public void onFinish(String response) {
//                Log.v(TAG,Constants.SEVER_URL+"testLogin");
//                Log.v(TAG,"http请求成功");
//                String requestData = response;
//                Log.v(TAG,requestData);
//                //解析从服务端接收到的数据
//                JsonObject jsonResult = new JsonObject().getAsJsonObject(requestData);
//                String result = jsonResult.get("result").toString();
//
//                if(result.equals('1')) {
//                    Toast.makeText(MyApplication.getContext(), "登陆成功", Toast.LENGTH_SHORT);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                } else
//                {
//                    Log.v("msg","Login");
//                    //
//                }
//            }
//            @Override
//            public void onError(Exception e) {
//                Log.v(TAG,Constants.SEVER_URL+"testLogin");
//                Log.v(TAG,"Http请求失败");
//            }
//        });
    }



}
