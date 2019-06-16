package com.example.www24.facedetection;

public class testMethods {
    public static void main(String[] args) {
        String data = "[\"zhanghexin\", \"yuantingzhou\"]";
        String datasub = data.substring(1, data.length()-1);
        String dataArray[] = datasub.split(", ");

        for(int i=0; i<dataArray.length; i++){
            System.out.println(dataArray[i]);
        }
        System.out.println(dataArray.toString());
        System.out.println("Hello World");
    }
}
