package com.example.mlforall;

public class PointDistance {
    private Point p;
    private double distance;

    public PointDistance(Point p, double distance) {
        this.p = p;
        this.distance = distance;
    }

    public Point getP() {
        return p;
    }

    public void setP(Point p) {
        this.p = p;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
