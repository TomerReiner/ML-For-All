package com.example.mlforall;

/**
 * This class represents a simple 2D linear equation of this form:
 * <pre>
 *     {@code y = m * x + b}
 * </pre>
 * where <code>m</code> is the slope and <code>b</code> is the intercept.
 */
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
