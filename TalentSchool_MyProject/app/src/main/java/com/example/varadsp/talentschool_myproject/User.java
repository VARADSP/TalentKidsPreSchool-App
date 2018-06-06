package com.example.varadsp.talentschool_myproject;

/**
 * Created by Sumit on 17-09-2017.
 */

//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private Integer id;
    private String username, email, gender, category,mobile,certificate;


    public User(Integer id, String username, String email, String gender, String category, String mobile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.category = category;

        this.mobile = mobile;
    }

    public User(Integer id, String username, String email, String gender, String category, String mobile, String certificate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.category = category;
        this.certificate = certificate;
        this.mobile = mobile;
    }


    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getCategory() { return category; }
}