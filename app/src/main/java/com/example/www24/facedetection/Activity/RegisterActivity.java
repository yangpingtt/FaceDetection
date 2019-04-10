package com.example.www24.facedetection.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.www24.facedetection.Common.Constants;
import com.example.www24.facedetection.Model.User;
import com.example.www24.facedetection.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_password;
    private EditText et_age;
    private EditText et_sex;
    private EditText et_mail;

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
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(user));

        Request request = new Request.Builder()
                .url(Constants.SEVER_URL)
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String requestData = response.body().toString();


        //解析从服务端接收到的数据
        JsonObject jsonResult = new JsonObject().getAsJsonObject(requestData);
        String result = jsonResult.get("result").toString();

        if(result.equals('1'))
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        else
        {
            Log.v("msg","Login");
            //
        }
    }
}
