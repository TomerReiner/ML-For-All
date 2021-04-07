package com.example.mlforall;

public class MachineLearningModel {

    private final LinearEquation linearEquation;
    private final double score;

    public MachineLearningModel(LinearEquation linearEquation, double score) {
        this.linearEquation = linearEquation;
        this.score = score;
    }

    public LinearEquation getLinearEquation() {
        return linearEquation;
    }

    public double getScore() {
        return score;
    }
}
