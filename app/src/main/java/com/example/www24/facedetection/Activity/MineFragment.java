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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.www24.facedetection.Common.Constants;
import com.example.www24.facedetection.R;
import com.example.www24.facedetection.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment {
    private String content;
    private TextView textView;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private List<String> adminItem;
    private ArrayAdapter<String> adapter;
    private final static String TAG = "MineFragment";
    private Spinner spinner;


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

        adminItem = initAdminItem();

        sp = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);

        Button add_face_data = Objects.requireNonNull(getActivity()).findViewById(R.id.add_face_data);
        Button login_exit = getActivity().findViewById(R.id.login_exit);
        spinner = getActivity().findViewById(R.id.chooseadmin);
        //将管理员数据与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, adminItem);
        adapter.setNotifyOnChange(true);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter添加到spinner
        spinner.setAdapter(adapter);

        add_face_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加人脸数据
                Log.v("MineFragment","准备进入添加人脸activity");
                startActivity(new Intent(getActivity(),AddFaceActivity.class));
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "进入选择项");
                String text = spinner.getItemAtPosition(position).toString();
                Log.v(TAG, text);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("userId", sp.getInt("userId",-1));
                    obj.put("adminName", text);
                    HttpUtil.sendOkHttpRequest(Constants.SEVER_URL + "choodeAdmin", obj.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.v(TAG, "选择管理员失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String requestData = response.body().string();

                            try{
                                JSONObject jsonObject = new JSONObject(requestData);
                                int result = jsonObject.getInt("result");
                                if(result == 1) {
                                    Log.v(TAG, "选择管理员成功");
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private List<String> initAdminItem() {
        List<String> admin = new ArrayList<String>();

        admin.add("请选择管理员");

        HttpUtil.sendGetOkHttpRequest(Constants.SEVER_URL + "getAdminInfo", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG,"获取管理员信息失败");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String requestData = response.body().string();

                try{
                    JSONObject jsonObject = new JSONObject(requestData);
                    int result = jsonObject.getInt("result");
                    if(result == 1) {
                        String adminInfo = jsonObject.getString("admins");
                        Log.v(TAG, adminInfo);
                        String adminInfosub = adminInfo.substring(1, adminInfo.length()-1);
                        String dataArray[] = adminInfosub.split(",");

                        for(int i=0; i<dataArray.length; i++){
                            admin.add(dataArray[i].substring(1, dataArray[i].length()-1));
                            //Log.v(TAG, dataArray[i]);
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.v(TAG, admin.toString());
        return admin;
    }

}
