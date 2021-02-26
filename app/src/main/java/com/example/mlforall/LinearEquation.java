package com.example.mlforall;

import java.util.Objects;

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

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public void setIntercept(double intercept) {
        this.intercept = intercept;
    }

    public boolean equals(LinearEquation equation) {
        return this.slope == equation.getSlope() && this.intercept == equation.getIntercept();
    }

    @Override
    public int hashCode() {
        return Objects.hash(slope, intercept);
    }

    @Override
    public String toString() {
        return "LinearEquation{" +
                "slope=" + slope +
                ", intercept=" + intercept +
                '}';
    }
}
