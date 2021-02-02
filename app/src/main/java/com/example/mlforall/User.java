package com.example.mlforall;

public class User {

    private String username;
    private String password;
    private int[][] machineLearningModels;

    public User(String username, String password, int[][] machineLearningModels) {
        this.username = username;
        this.password = password;
        this.machineLearningModels = machineLearningModels;
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

    public int[][] getMachineLearningModels() {
        return machineLearningModels;
    }

    public void setMachineLearningModels(int[][] machineLearningModels) {
        this.machineLearningModels = machineLearningModels;
    }
}
