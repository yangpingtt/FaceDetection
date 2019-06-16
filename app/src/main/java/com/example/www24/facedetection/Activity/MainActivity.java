package com.example.www24.facedetection.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.example.www24.facedetection.Common.Constants;
import com.example.www24.facedetection.R;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivityLog";

    private Toast toast = null;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private Button add_face_data;
    private Button face_recognition;
    private SharedPreferences sp;
    private TextView topBar;
    private TextView tabFirstPage;
    private TextView tabMine;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        Log.v(TAG,String.valueOf(sp.getBoolean("isLogined",false)));
        if(sp.getBoolean("isLogined",false)){
            activeEngine();
            findView();
            onClick();
            replaceFragment(new FirstPageFragment());
        }else{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void findView() {
        topBar = findViewById(R.id.txt_top);
        tabFirstPage = findViewById(R.id.txt_firstpage);
        tabMine = findViewById(R.id.txt_mine);
        //ly_content = findViewById(R.id.fragment_container);

    }

    //重置所有文本的选中状态
    public void selected(){
        tabFirstPage.setSelected(false);
        tabMine.setSelected(false);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    private void onClick() {

        tabFirstPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBar.setText("首页");
                selected();
                tabFirstPage.setSelected(true);
                replaceFragment(new FirstPageFragment());
            }
        });

        tabMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBar.setText("我");
                selected();
                tabMine.setSelected(true);
                Log.v(TAG,sp.getString("name","hello"));
                replaceFragment(new MineFragment(sp.getString("name","hello")));
            }
        });
    }


    /**
     * 激活引擎
     */
    public void activeEngine() {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            Log.v(TAG,"权限获取失败");
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            Log.v(TAG,"重新获取权限");
            //return;
        }

        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            FaceEngine faceEngine = new FaceEngine();
            Log.v(TAG,"激活引擎前");
            int activeCode = faceEngine.active(MainActivity.this, Constants.APP_ID, Constants.SDK_KEY);
            Log.v(TAG,"激活引擎后");
            Log.v(TAG, String.valueOf(activeCode));
            emitter.onNext(activeCode);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    //检查权限
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                activeEngine();
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    private void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }

}
