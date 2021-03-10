package com.example.mlforall;

public class LinearEquation {

    private double slope;
    private double intercept;

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


    public boolean equals(LinearEquation equation) {
        return this.slope == equation.getSlope() && this.intercept == equation.getIntercept();
    }
}
