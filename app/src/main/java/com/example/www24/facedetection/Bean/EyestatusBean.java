package com.example.www24.facedetection.Bean;

public class EyestatusBean {

    /**
     * left_eye_status : {"normal_glass_eye_open":99.995,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":0.005,"normal_glass_eye_close":0,"dark_glasses":0}
     * right_eye_status : {"normal_glass_eye_open":99.906,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":0.094,"normal_glass_eye_close":0,"dark_glasses":0}
     */

    private LeftEyeStatusBean left_eye_status;
    private RightEyeStatusBean right_eye_status;

    public LeftEyeStatusBean getLeft_eye_status() {
        return left_eye_status;
    }

    public void setLeft_eye_status(LeftEyeStatusBean left_eye_status) {
        this.left_eye_status = left_eye_status;
    }

    public RightEyeStatusBean getRight_eye_status() {
        return right_eye_status;
    }

    public void setRight_eye_status(RightEyeStatusBean right_eye_status) {
        this.right_eye_status = right_eye_status;
    }

    public static class LeftEyeStatusBean {
        /**
         * normal_glass_eye_open : 99.995
         * no_glass_eye_close : 0
         * occlusion : 0
         * no_glass_eye_open : 0.005
         * normal_glass_eye_close : 0
         * dark_glasses : 0
         */

        private double normal_glass_eye_open;
        private double no_glass_eye_close;
        private double occlusion;
        private double no_glass_eye_open;
        private double normal_glass_eye_close;
        private double dark_glasses;

        public double getNormal_glass_eye_open() {
            return normal_glass_eye_open;
        }

        public void setNormal_glass_eye_open(double normal_glass_eye_open) {
            this.normal_glass_eye_open = normal_glass_eye_open;
        }

        public double getNo_glass_eye_close() {
            return no_glass_eye_close;
        }

        public void setNo_glass_eye_close(double no_glass_eye_close) {
            this.no_glass_eye_close = no_glass_eye_close;
        }

        public double getOcclusion() {
            return occlusion;
        }

        public void setOcclusion(double occlusion) {
            this.occlusion = occlusion;
        }

        public double getNo_glass_eye_open() {
            return no_glass_eye_open;
        }

        public void setNo_glass_eye_open(double no_glass_eye_open) {
            this.no_glass_eye_open = no_glass_eye_open;
        }

        public double getNormal_glass_eye_close() {
            return normal_glass_eye_close;
        }

        public void setNormal_glass_eye_close(double normal_glass_eye_close) {
            this.normal_glass_eye_close = normal_glass_eye_close;
        }

        public double getDark_glasses() {
            return dark_glasses;
        }

        public void setDark_glasses(double dark_glasses) {
            this.dark_glasses = dark_glasses;
        }
    }

    public static class RightEyeStatusBean {
        /**
         * normal_glass_eye_open : 99.906
         * no_glass_eye_close : 0
         * occlusion : 0
         * no_glass_eye_open : 0.094
         * normal_glass_eye_close : 0
         * dark_glasses : 0
         */

        private double normal_glass_eye_open;
        private double no_glass_eye_close;
        private double occlusion;
        private double no_glass_eye_open;
        private double normal_glass_eye_close;
        private double dark_glasses;

        public double getNormal_glass_eye_open() {
            return normal_glass_eye_open;
        }

        public void setNormal_glass_eye_open(double normal_glass_eye_open) {
            this.normal_glass_eye_open = normal_glass_eye_open;
        }

        public double getNo_glass_eye_close() {
            return no_glass_eye_close;
        }

        public void setNo_glass_eye_close(double no_glass_eye_close) {
            this.no_glass_eye_close = no_glass_eye_close;
        }

        public double getOcclusion() {
            return occlusion;
        }

        public void setOcclusion(double occlusion) {
            this.occlusion = occlusion;
        }

        public double getNo_glass_eye_open() {
            return no_glass_eye_open;
        }

        public void setNo_glass_eye_open(double no_glass_eye_open) {
            this.no_glass_eye_open = no_glass_eye_open;
        }

        public double getNormal_glass_eye_close() {
            return normal_glass_eye_close;
        }

        public void setNormal_glass_eye_close(double normal_glass_eye_close) {
            this.normal_glass_eye_close = normal_glass_eye_close;
        }

        public double getDark_glasses() {
            return dark_glasses;
        }

        public void setDark_glasses(double dark_glasses) {
            this.dark_glasses = dark_glasses;
        }
    }
}
