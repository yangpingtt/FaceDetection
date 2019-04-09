package com.example.www24.facedetection;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //消息处理
    Handler UIMessageHandler = new Handler(){

    };
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

        bt_username_clear.setOnClickListener(this);
        bt_password_eye.setOnClickListener(this);
        bt_password_clear.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_password.addTextChangedListener(password_watcher);

        mLoginButton = findViewById(R.id.login);
        mLoginError = findViewById(R.id.forget_password);
        mRegister = findViewById(R.id.register);
//        ONLYTEST.setOnClickListener(this);
//        ONLYTEST.setOnLongClickListener((View.OnLongClickListener) this);
        mLoginButton.setOnClickListener(this);
        mLoginError.setOnClickListener(this);
        mRegister.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                //登陆
                login();
            case R.id.register:
                //注册
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            case R.id.forget_password:
                //忘记密码
                startActivity(new Intent(LoginActivity.this,ForgetPwdActivity.class));
        }
    }

    //登陆操作
    private void login() {
        String username = et_name.getText().toString();
        String password = et_password.getText().toString();



    }



}
