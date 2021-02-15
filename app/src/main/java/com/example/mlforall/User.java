package com.example.mlforall;

import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private ArrayList<MachineLearningModel> models;

    public User(String username, String password, ArrayList<MachineLearningModel> models) {
        this.username = username;
        this.password = password;
        this.models = models;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<MachineLearningModel> getModels() {
        return models;
    }
}
