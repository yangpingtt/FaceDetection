package com.example.www24.facedetection.Bean;

import java.util.List;

public class ResultFromFacePP {

    /**
     * image_id : 7y4+Bh+ZOvWNuJnaD5uFVg==
     * request_id : 1556443364,aaba9653-8b84-441f-ab4c-fb7aed4afe6d
     * time_used : 365
     * faces : [{"attributes":{"eyestatus":{"left_eye_status":{"normal_glass_eye_open":0,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":100,"normal_glass_eye_close":0,"dark_glasses":0},"right_eye_status":{"normal_glass_eye_open":0,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":100,"normal_glass_eye_close":0,"dark_glasses":0}},"mouthstatus":{"close":100,"surgical_mask_or_respirator":0,"open":0,"other_occlusion":0},"glass":{"value":"None"}},"face_rectangle":{"width":103,"top":122,"left":68,"height":103},"face_token":"7c6b29b5f5c72b1f7fbdfe5adebb4ea2"}]
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<FacesBean> faces;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public static class FacesBean {
        /**
         * attributes : {"eyestatus":{"left_eye_status":{"normal_glass_eye_open":0,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":100,"normal_glass_eye_close":0,"dark_glasses":0},"right_eye_status":{"normal_glass_eye_open":0,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":100,"normal_glass_eye_close":0,"dark_glasses":0}},"mouthstatus":{"close":100,"surgical_mask_or_respirator":0,"open":0,"other_occlusion":0},"glass":{"value":"None"}}
         * face_rectangle : {"width":103,"top":122,"left":68,"height":103}
         * face_token : 7c6b29b5f5c72b1f7fbdfe5adebb4ea2
         */

        private AttributesBean attributes;
        private FaceRectangleBean face_rectangle;
        private String face_token;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public FaceRectangleBean getFace_rectangle() {
            return face_rectangle;
        }

        public void setFace_rectangle(FaceRectangleBean face_rectangle) {
            this.face_rectangle = face_rectangle;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class AttributesBean {
            /**
             * eyestatus : {"left_eye_status":{"normal_glass_eye_open":0,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":100,"normal_glass_eye_close":0,"dark_glasses":0},"right_eye_status":{"normal_glass_eye_open":0,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":100,"normal_glass_eye_close":0,"dark_glasses":0}}
             * mouthstatus : {"close":100,"surgical_mask_or_respirator":0,"open":0,"other_occlusion":0}
             * glass : {"value":"None"}
             */

            private EyestatusBean eyestatus;
            private MouthstatusBean mouthstatus;
            private GlassBean glass;

            public EyestatusBean getEyestatus() {
                return eyestatus;
            }

            public void setEyestatus(EyestatusBean eyestatus) {
                this.eyestatus = eyestatus;
            }

            public MouthstatusBean getMouthstatus() {
                return mouthstatus;
            }

            public void setMouthstatus(MouthstatusBean mouthstatus) {
                this.mouthstatus = mouthstatus;
            }

            public GlassBean getGlass() {
                return glass;
            }

            public void setGlass(GlassBean glass) {
                this.glass = glass;
            }


            public static class GlassBean {
                /**
                 * value : None
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }

        public static class FaceRectangleBean {
            /**
             * width : 103
             * top : 122
             * left : 68
             * height : 103
             */

            private int width;
            private int top;
            private int left;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
