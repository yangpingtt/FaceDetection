package com.example.www24.facedetection;

public interface HttpCallbackListen {
    void onFinish(String response);
    void onError(Exception e);
}
