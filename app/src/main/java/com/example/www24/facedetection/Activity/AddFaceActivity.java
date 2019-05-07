package com.example.www24.facedetection.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.example.www24.facedetection.Bean.EyestatusBean;
import com.example.www24.facedetection.Bean.MouthstatusBean;
import com.example.www24.facedetection.Bean.ResultFromFacePP;
import com.example.www24.facedetection.HttpCallbackListen;
import com.example.www24.facedetection.Model.DrawInfo;
import com.example.www24.facedetection.Model.FacePreviewInfo;
import com.example.www24.facedetection.MyApplication;
import com.example.www24.facedetection.R;
import com.example.www24.facedetection.faceserver.CompareResult;
import com.example.www24.facedetection.faceserver.FaceServer;
import com.example.www24.facedetection.util.ConfigUtil;
import com.example.www24.facedetection.util.DrawHelper;
import com.example.www24.facedetection.util.camera.CameraHelper;
import com.example.www24.facedetection.util.camera.CameraListener;
import com.example.www24.facedetection.util.face.FaceHelper;
import com.example.www24.facedetection.util.face.FaceListener;
import com.example.www24.facedetection.util.face.RequestFeatureStatus;
import com.example.www24.facedetection.widget.FaceRectView;
import com.example.www24.facedetection.widget.ShowFaceInfoAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.www24.facedetection.Common.Constants.EYECLOSE_CONFIDENCE_THRESHOLD;
import static com.example.www24.facedetection.Common.Constants.EYEOPEN_CONFIDENCE_THRESHOLD;
import static com.example.www24.facedetection.Common.Constants.FACEPP_API_KEY;
import static com.example.www24.facedetection.Common.Constants.FACEPP_API_SECRET;
import static com.example.www24.facedetection.Common.Constants.MOUTHOPEN_CONFIDENCE_THRESHOLD;
import static com.example.www24.facedetection.Common.Constants.RETURN_ATTRIBUTES;
import static com.example.www24.facedetection.util.HttpUtil.post;

public class AddFaceActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {
    private static final String TAG = "Add_Face_ActivityLog";
    private static final int MAX_DETECT_NUM = 5;

    private Toast toast = null;
    private View previewView;

    /**
     * 当FR成功，活体未成功时，FR等待活体的时间
     */
    private static final int WAIT_LIVENESS_INTERVAL = 50;
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;

    private FaceHelper faceHelper;

    //活体检测
    private boolean livenessDetect = true;
    private boolean[] behavorDectect = new boolean[4];
    private static final int GESTUREDETECT_STATUS_READY = 0;
    private static final int GESTUREDETECT_STATUS_PROCESSING = 1;
    private static final int GESTUREDETECT_STATUS_DONE = 2;
    private int gestureDetect_status;
    private static final int MOUTHDETECT_STATUS_READY = 0;
    private static final int MOUTHDETECT_STATUS_PROCESSING = 1;
    private static final int MOUTHDETECT_STATUS_DONR = 2;
    private int mouthDetect_status;
    private static final int EYEDETECT_STATUS_READY = 0;
    private static final int EYEDETECT_STATUS_PROCESSING = 1;
    private static final int EYEDETECT_STATUS_DONE = 2;
    //eyesDetect[0] = true 左眼open
    //eyesDetect[1] = true 左眼close
    //eyesDetect[2] = true 右眼open
    //eyesDetect[3] = true 右眼close
    private boolean[] eyesDetect = new boolean[4];
    private int eyesDetect_status;
    //人脸框提示信息
    private String hintMessage;



    //人脸比对结果参数
    private List<CompareResult> compareResultList;
    private ShowFaceInfoAdapter adapter;
    /**
     * 注册人脸状态码，准备注册
     */
    private static final int REGISTER_STATUS_READY = 0;
    /**
     * 注册人脸状态码，注册中
     */
    private static final int REGISTER_STATUS_PROCESSING = 1;
    /**
     * 注册人脸状态码，注册结束（无论成功失败）
     */
    private static final int REGISTER_STATUS_DONE = 2;

    private int registerStatus = REGISTER_STATUS_DONE;

    private int afCode = -1;
    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Face3DAngle> face3DAngleMap = new ConcurrentHashMap<>();
    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();



    //打开摄像头
    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private FaceEngine faceEngine;
    private FaceRectView faceRectView;

    public static final int FAIL = 1;
    public static final int SUCCESS = 2;

    private SharedPreferences sp;

    //Handler定义
    private MyHandler myhandle = new MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            AddFaceActivity activity = (AddFaceActivity) reference.get();
            if(activity != null){
                switch (msg.what){
                    case FAIL :
                        Toast.makeText(MyApplication.getContext(),"活体检测失败",Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Toast.makeText(MyApplication.getContext(),"活体检测成功",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_face);
        Log.v(TAG,"进入添加人脸activity");
        //保持亮屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //本地人脸库初始化
        FaceServer.getInstance().init(this);

        previewView = findViewById(R.id.texture_preview);
        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        faceRectView = findViewById(R.id.face_rect_view);

        RecyclerView recyclerShowFaceInfo = findViewById(R.id.recycler_view_person);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int spanCount = (int) (dm.widthPixels / (getResources().getDisplayMetrics().density * 100 + 0.5f));
        recyclerShowFaceInfo.setLayoutManager(new GridLayoutManager(AddFaceActivity.this, spanCount));
        recyclerShowFaceInfo.setItemAnimator(new DefaultItemAnimator());
        compareResultList = new ArrayList<>();
        adapter = new ShowFaceInfoAdapter(compareResultList,this);
        recyclerShowFaceInfo.setAdapter(adapter);

        sp = getSharedPreferences("userInfo",MODE_PRIVATE);

        //活体检测状态初始化
        initBehavorDetect();

        Button button = findViewById(R.id.register_face);
        button.setOnClickListener(this);
    }

    private void initBehavorDetect() {
        gestureDetect_status = GESTUREDETECT_STATUS_READY;
        mouthDetect_status = MOUTHDETECT_STATUS_READY;
        eyesDetect_status = EYEDETECT_STATUS_READY;
        behavorDectect[0] = false;
        behavorDectect[1] = false;
        behavorDectect[2] = false;
        behavorDectect[3] = false;
        eyesDetect[0] = false;
        eyesDetect[1] = false;
        eyesDetect[2] = false;
        eyesDetect[3] = false;
        hintMessage = "";
    }


    /**
     * 在{@link #previewView}第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
     */
    @Override
    public void onGlobalLayout() {
        Log.v(TAG,"第一次布局");
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if(!checkPermissions(NEEDED_PERMISSIONS)){
            Log.v(TAG,"获取权限失败，准备动态获取权限");
            ActivityCompat.requestPermissions(this,NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            Log.v(TAG,"动态获取权限");
        }else {
            initEngine();
            initCamera();
        }
    }

    private void initCamera() {
        Log.v(TAG,"初始化相机");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "onFail: " + e.getMessage());
            }

            //请求FR的回调
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId) {
                //FR成功
                if (faceFeature != null) {
                    //Log.v(TAG, face3DAngleMap.get(requestId).toString());
//                    Log.i(TAG, "onPreview: fr end = " + System.currentTimeMillis() + " trackId = " + requestId);

                    if (gestureDetect_status == GESTUREDETECT_STATUS_DONE && mouthDetect_status == MOUTHDETECT_STATUS_DONR && eyesDetect_status == EYEDETECT_STATUS_DONE) {
                        //活体检测通过，搜索特征
                        if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.ALIVE) {
                            Log.v(TAG, ".........活体检测成功");
                            hintMessage = "活体检测成功";
//                            Message msg = new Message();
//                            msg.what = SUCCESS;
//                            myhandle.sendMessage(msg);
                            registerStatus = REGISTER_STATUS_READY;
                        }
                        //活体检测未出结果，延迟100ms再执行该函数
                        else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.UNKNOWN) {
                            getFeatureDelayedDisposables.add(Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                    .subscribe(aLong -> onFaceFeatureInfoGet(faceFeature, requestId)));
                        }else{
                            //活体检测失败
                            Message msg = new Message();
                            msg.what = FAIL;
                            myhandle.sendMessage(msg);
                            hintMessage = "活体检测失败，重新检测";
                            initBehavorDetect();
                        }
                    }else {
                        getFeatureDelayedDisposables.add(Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                .subscribe(aLong -> onFaceFeatureInfoGet(faceFeature, requestId)));
                    }
                }//FR 失败
                else {
                    requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                }
            }
        };


        CameraListener cameraListener = new CameraListener() {

            private byte[] jpegData;

            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror);

                faceHelper = new FaceHelper.Builder()
                        .faceEngine(faceEngine)
                        .frThreadNum(MAX_DETECT_NUM)
                        .previewSize(previewSize)
                        .faceListener(faceListener)
                        .currentTrackId(ConfigUtil.getTrackId(AddFaceActivity.this.getApplicationContext()))
                        .build();
            }


            @Override
            public void onPreview(final byte[] nv21, Camera camera) {

                //Log.v(TAG,"进入预览");
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame(nv21);
                if (facePreviewInfoList != null && faceRectView != null && drawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    String name = hintMessage;
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        drawInfoList.add(new DrawInfo(facePreviewInfoList.get(i).getFaceInfo().getRect(), GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, LivenessInfo.UNKNOWN,
                                name));
                    }
                    drawHelper.draw(faceRectView, drawInfoList);
                }

                //摇头抬头检测
                if(gestureDetect_status == GESTUREDETECT_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0 ){
                            if(!behavorDectect[0]){
                                hintMessage = "请向左转头";
                                Log.v(TAG,".........请左摇头");
                                //drawHelper.draw(faceRectView,);
                            }else if(!behavorDectect[1]){
                                hintMessage = "请向右转头";
                                Log.v(TAG,".........请右摇头");
                            }else if(!behavorDectect[2]){
                                hintMessage = "请向上抬头";
                                Log.v(TAG,".........请上抬头");
                            }else if(!behavorDectect[3]){
                                hintMessage = "请向下低头";
                                Log.v(TAG,".........请下低头");
                            }else{
                                gestureDetect_status = GESTUREDETECT_STATUS_DONE;
                                hintMessage = "张张嘴";
                        Log.v(TAG,"动作检测成功");
                    }
                    if(!behavorDectect[0] && facePreviewInfoList.get(0).getFace3DAngle().getYaw() > 10){
                        behavorDectect[0] = true;
                    }
                    if(behavorDectect[0] && !behavorDectect[1] && facePreviewInfoList.get(0).getFace3DAngle().getYaw() < -10){
                        behavorDectect[1] = true;
                    }
                    if(behavorDectect[0] && behavorDectect[1] && !behavorDectect[2] && facePreviewInfoList.get(0).getFace3DAngle().getPitch() > 10){
                        behavorDectect[2] = true;
                    }
                    if(behavorDectect[0] && behavorDectect[1] && behavorDectect[2] && !behavorDectect[3] && facePreviewInfoList.get(0).getFace3DAngle().getPitch() < -10){
                        behavorDectect[3] = true;
                    }
                }


                //张嘴检测
                if(gestureDetect_status == GESTUREDETECT_STATUS_DONE && facePreviewInfoList != null && facePreviewInfoList.size() > 0 &&
                        mouthDetect_status == MOUTHDETECT_STATUS_READY ){
                    mouthDetect_status = MOUTHDETECT_STATUS_PROCESSING;
                    Log.v(TAG,"进入张嘴检测");
                    Observable.create((ObservableOnSubscribe<byte[]>) emitter -> {
                        jpegData = FaceServer.getInstance().getJpegImage(nv21.clone(),previewSize.width, previewSize.height);
                        if(jpegData != null){
                            emitter.onNext(jpegData);
                        } else {
                            emitter.onError(null);
                        }
                    })
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<byte[]>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }
                                @Override
                                public void onNext(byte[] jpegData) {
                                    String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
                                    HashMap<String, String> map = new HashMap<>();
                                    HashMap<String, byte[]> byteMap = new HashMap<>();
                                    map.put("api_key", FACEPP_API_KEY);
                                    map.put("api_secret", FACEPP_API_SECRET);
                                    map.put("return_landmark", "0");
                                    map.put("return_attributes", RETURN_ATTRIBUTES);
                                    byteMap.put("image_file", jpegData);
                                    Log.v(TAG,"提交至旷视服务器前");
                                    post(url, map, byteMap, new HttpCallbackListen() {
                                        @Override
                                        public void onFinish(String response) {
                                            Log.v(TAG,response);
                                            JSONObject jsonResult = null;
                                            try {
                                                jsonResult = new JSONObject(response);
                                                Log.v(TAG,jsonResult.toString());
                                                String face = jsonResult.get("faces").toString();
                                                if(!face.equals("[]")){
                                                    Gson gson = new Gson();
                                                    ResultFromFacePP resultFromFacePP = gson.fromJson(String.valueOf(jsonResult),ResultFromFacePP.class);
                                                    List<ResultFromFacePP.FacesBean> faces = resultFromFacePP.getFaces();
                                                    MouthstatusBean mouthstatus = faces.get(0).getAttributes().getMouthstatus();

                                                    if(mouthstatus.getOpen() > MOUTHOPEN_CONFIDENCE_THRESHOLD){
                                                        hintMessage = "眨眨眼";
                                                        mouthDetect_status = MOUTHDETECT_STATUS_DONR;
                                                    }else{
                                                        hintMessage = "张张嘴";
                                                        mouthDetect_status = GESTUREDETECT_STATUS_READY;
                                                    }
                                                } else{
                                                    hintMessage = "张张嘴";
                                                    mouthDetect_status = GESTUREDETECT_STATUS_READY;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(Exception e) {
                                            Log.v(TAG,"利用旷视API处理人脸数据异常");
                                        }
                                    });
                                }
                                @Override
                                public void onError(Throwable e) {


                                }
                                @Override
                                public void onComplete() {

                                }
                            });
                }


                //眨眼检测
                if(gestureDetect_status == GESTUREDETECT_STATUS_DONE && facePreviewInfoList != null && facePreviewInfoList.size() > 0 &&
                        mouthDetect_status == MOUTHDETECT_STATUS_DONR && eyesDetect_status == EYEDETECT_STATUS_READY){
                    eyesDetect_status = EYEDETECT_STATUS_PROCESSING;
                    Log.v(TAG,"进入眨眼检测");
                    if(eyesDetect[0] && eyesDetect[1] && eyesDetect[2] && eyesDetect[3]){
                        hintMessage = "动作检测完成";
                        eyesDetect_status = EYEDETECT_STATUS_DONE;
                    }else{
                        Observable.create((ObservableOnSubscribe<byte[]>) emitter -> {
                            jpegData = FaceServer.getInstance().getJpegImage(nv21.clone(),previewSize.width, previewSize.height);
                            if(jpegData != null){
                                emitter.onNext(jpegData);
                            } else {
                                emitter.onError(null);
                            }
                        })
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<byte[]>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }
                                    @Override
                                    public void onNext(byte[] jpegData) {
                                        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
                                        HashMap<String, String> map = new HashMap<>();
                                        HashMap<String, byte[]> byteMap = new HashMap<>();
                                        map.put("api_key", FACEPP_API_KEY);
                                        map.put("api_secret", FACEPP_API_SECRET);
                                        map.put("return_landmark", "0");
                                        map.put("return_attributes", RETURN_ATTRIBUTES);
                                        byteMap.put("image_file", jpegData);
                                        Log.v(TAG,"提交至旷视服务器前");
                                        post(url, map, byteMap, new HttpCallbackListen() {
                                            @Override
                                            public void onFinish(String response) {
                                                Log.v(TAG,response);
                                                JSONObject jsonResult = null;
                                                try {
                                                    jsonResult = new JSONObject(response);
                                                    Log.v(TAG,jsonResult.toString());
                                                    String face = jsonResult.get("faces").toString();
                                                    if(!face.equals("[]")){
                                                        Gson gson = new Gson();
                                                        ResultFromFacePP resultFromFacePP = gson.fromJson(String.valueOf(jsonResult),ResultFromFacePP.class);
                                                        List<ResultFromFacePP.FacesBean> faces = resultFromFacePP.getFaces();
                                                        EyestatusBean eyestatus = faces.get(0).getAttributes().getEyestatus();

                                                        if((eyestatus.getLeft_eye_status().getNo_glass_eye_open() > EYEOPEN_CONFIDENCE_THRESHOLD ||
                                                                eyestatus.getLeft_eye_status().getNormal_glass_eye_open() > EYEOPEN_CONFIDENCE_THRESHOLD)&&(eyestatus.getRight_eye_status().getNo_glass_eye_open() > EYEOPEN_CONFIDENCE_THRESHOLD ||
                                                                eyestatus.getRight_eye_status().getNormal_glass_eye_open() > EYEOPEN_CONFIDENCE_THRESHOLD)){
                                                            hintMessage = "眨眨眼";
                                                            eyesDetect[0] = true;
                                                            eyesDetect[2] = true;
                                                            eyesDetect_status = EYEDETECT_STATUS_READY;
                                                        }else if((eyestatus.getLeft_eye_status().getNo_glass_eye_close() > EYECLOSE_CONFIDENCE_THRESHOLD ||
                                                                eyestatus.getLeft_eye_status().getNormal_glass_eye_close() > EYECLOSE_CONFIDENCE_THRESHOLD)&&(eyestatus.getRight_eye_status().getNo_glass_eye_close() > EYECLOSE_CONFIDENCE_THRESHOLD ||
                                                                eyestatus.getRight_eye_status().getNormal_glass_eye_close() > EYECLOSE_CONFIDENCE_THRESHOLD)){
                                                            hintMessage = "眨眨眼";
                                                            eyesDetect[1] = true;
                                                            eyesDetect[3] = true;
                                                            eyesDetect_status = EYEDETECT_STATUS_READY;
                                                        }
                                                    } else{
                                                        hintMessage = "眨眨眼";
                                                        eyesDetect_status = EYEDETECT_STATUS_READY;
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            @Override
                                            public void onError(Exception e) {
                                                Log.v(TAG,"利用旷视API处理人脸数据异常");
                                            }
                                        });
                                    }
                                    @Override
                                    public void onError(Throwable e) {
                                        Log.v(TAG,"获取jpegData数据失败");

                                    }
                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }


                //注册人脸
                if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
                    registerStatus = REGISTER_STATUS_PROCESSING;
                    Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                        boolean success = FaceServer.getInstance().register(AddFaceActivity.this, nv21.clone(), previewSize.width, previewSize.height, sp.getString("name","registered "));
                        emitter.onNext(success);
                    })
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Boolean success) {
                                    String result = success ? "register success!" : "register failed!";
                                    Toast.makeText(AddFaceActivity.this, result, Toast.LENGTH_SHORT).show();
                                    if(success){
                                        startActivity(new Intent(AddFaceActivity.this,MainActivity.class));
                                        registerStatus = REGISTER_STATUS_DONE;
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(AddFaceActivity.this, "register failed!", Toast.LENGTH_SHORT).show();
                                    registerStatus = REGISTER_STATUS_DONE;
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

                clearLeftFace(facePreviewInfoList);

                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {

                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        if (livenessDetect) {
                            livenessMap.put(facePreviewInfoList.get(i).getTrackId(), facePreviewInfoList.get(i).getLivenessInfo().getLiveness());
                            face3DAngleMap.put(facePreviewInfoList.get(i).getTrackId(),facePreviewInfoList.get(i).getFace3DAngle());
                        }
                        /**
                         * 对于每个人脸，若状态为空或者为失败，则请求FR（可根据需要添加其他判断以限制FR次数），
                         * FR回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer)}中回传
                         */
                        if (requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == null
                                || requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == RequestFeatureStatus.FAILED) {
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
//                            Log.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackId());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(),previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
    }



    /**
     * 初始化引擎
     */
    private void initEngine() {
        Log.v(TAG,"初始化引擎");
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_LIVENESS | FaceEngine.ASF_FACE3DANGLE);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);

        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(R.string.init_failed, afCode), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 销毁引擎
     */
    private void unInitEngine() {

        if (afCode == ErrorInfo.MOK) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }


    /**
     * 删除已经离开的人脸
     *
     * @param facePreviewInfoList 人脸和trackId列表
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        Set<Integer> keySet = requestFeatureStatusMap.keySet();
        if (compareResultList != null) {
            for (int i = compareResultList.size() - 1; i >= 0; i--) {
                if (!keySet.contains(compareResultList.get(i).getTrackId())) {
                    compareResultList.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            return;
        }

        for (Integer integer : keySet) {
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == integer) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(integer);
                livenessMap.remove(integer);
            }
        }

    }

    /**
     * 将准备注册的状态置为{@link #REGISTER_STATUS_READY}
     *
     */
    public void register() {
        if (registerStatus == REGISTER_STATUS_DONE) {
            registerStatus = REGISTER_STATUS_READY;
        }
    }

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

    private void showToast(String s) {
        try{
            if (toast == null) {
                toast = Toast.makeText(AddFaceActivity.this, s, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                toast.setText(s);
                toast.show();
            }
        }catch (Exception e){
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_face:
                register();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        myhandle.removeCallbacksAndMessages(null);
    }
}
