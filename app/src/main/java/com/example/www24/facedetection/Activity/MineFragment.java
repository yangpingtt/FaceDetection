package com.example.www24.facedetection.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.www24.facedetection.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment {
    private String content;
    private TextView textView;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public MineFragment(String content){
        this.content = content;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment,container,false);

        textView = view.findViewById(R.id.username_in_mine);
        textView.setText(content);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button add_face_data = Objects.requireNonNull(getActivity()).findViewById(R.id.add_face_data);
        Button login_exit = getActivity().findViewById(R.id.login_exit);

        add_face_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加人脸数据
                Log.v("MineFragment","准备进入添加人脸activity");
                startActivity(new Intent(getActivity(),AddFaceActivity.class));
            }
        });

        login_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
                editor = sp.edit();
                editor.putBoolean("isLogined", false);
                editor.apply();

                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
}
