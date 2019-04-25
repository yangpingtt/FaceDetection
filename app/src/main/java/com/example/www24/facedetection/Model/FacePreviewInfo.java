package com.example.www24.facedetection.Model;

import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.LivenessInfo;

public class FacePreviewInfo {
    private FaceInfo faceInfo;
    private LivenessInfo livenessInfo;
    private Face3DAngle face3DAngle;
    private int trackId;

    public FacePreviewInfo(FaceInfo faceInfo, LivenessInfo livenessInfo, Face3DAngle face3DAngle, int trackId) {
        this.faceInfo = faceInfo;
        this.livenessInfo = livenessInfo;
        this.face3DAngle = face3DAngle;
        this.trackId = trackId;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public LivenessInfo getLivenessInfo() {
        return livenessInfo;
    }

    public void setLivenessInfo(LivenessInfo livenessInfo) {
        this.livenessInfo = livenessInfo;
    }

    public Face3DAngle getFace3DAngle() { return face3DAngle; }

    public void setFace3DAngle(Face3DAngle face3DAngle) {this.face3DAngle = face3DAngle;}

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
