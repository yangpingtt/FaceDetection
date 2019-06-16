package com.example.www24.facedetection.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.www24.facedetection.R;

public class RecognitionOK extends AppCompatActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_ok);

        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        TextView textView = findViewById(R.id.hello_user);
        textView.setText("Hello! "+sp.getString("name",null));

    }
}
