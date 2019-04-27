package com.example.www24.facedetection.Bean;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * userId : 1
     * username : yuanchong
     * password : 123456
     * age : 22
     * sex : M
     * mail : yuanchongtt@163.com
     */

    private int userId;
    private String username;
    private String password;
    private int age;
    private char sex;
    private String mail;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
