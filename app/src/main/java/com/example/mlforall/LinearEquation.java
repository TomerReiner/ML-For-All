package com.example.mlforall;

public class LinearEquation {

    private final double slope;
    private final double intercept;

    public LinearEquation(double slope, double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }
}
