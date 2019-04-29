package com.example.www24.facedetection.Bean;

public class MouthstatusBean {
    /**
     * close : 100
     * surgical_mask_or_respirator : 0
     * open : 0
     * other_occlusion : 0
     */

    private double close;
    private double surgical_mask_or_respirator;
    private double open;
    private double other_occlusion;

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getSurgical_mask_or_respirator() {
        return surgical_mask_or_respirator;
    }

    public void setSurgical_mask_or_respirator(double surgical_mask_or_respirator) {
        this.surgical_mask_or_respirator = surgical_mask_or_respirator;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getOther_occlusion() {
        return other_occlusion;
    }

    public void setOther_occlusion(double other_occlusion) {
        this.other_occlusion = other_occlusion;
    }
}
