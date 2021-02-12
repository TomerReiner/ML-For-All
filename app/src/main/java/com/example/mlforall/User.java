package com.example.mlforall;

import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private ArrayList<double[]> machineLearningModels;

    public User(String username, String password, ArrayList<double[]> machineLearningModels) {
        this.username = username;
        this.password = password;
        this.machineLearningModels = machineLearningModels;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<double[]> getMachineLearningModels() {
        return machineLearningModels;
    }
}
