package com.example.mlforall;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * This class create Naive Bayes Classifier Model based on Bayes's formula.
 * @see <a href="https://en.wikipedia.org/wiki/Naive_Bayes_classifier">Naive Bayes classifier</a>
 * @author Tomer Reiner
 */
public class KNearestNeighbors {

    private ArrayList<Point> points; // The points for the model.
    private ArrayList<Integer> labels; // The label(class) for each label.

    private ArrayList<Integer> unique;

    private HashMap<Integer, ArrayList<Point>> splitData;

    public KNearestNeighbors(ArrayList<Point> points, ArrayList<Integer> labels)  throws IllegalArgumentException {
        this.points = points;
        this.labels = labels;
        this.unique = getUnique();
        this.splitData = spiltData();

        if (this.points.size() != this.labels.size()) {
            throw new IllegalArgumentException(String.format("The length of the arrays don't match: %d, %d", this.points.size(), this.labels.size()));
        }
    }


    /**
     * This function finds the unique values in {@link #labels}.
     * @return {@link ArrayList} with all the unique values in {@link #labels}.
     */
    private ArrayList<Integer> getUnique() {
        ArrayList<Integer> unique = new ArrayList<Integer>();

        int labelLength = this.labels.size();

        for (int i = 0; i < labelLength; i++) {

            int currentY = this.labels.get(i);

            if (!unique.contains(currentY)) // If currentY is not in unique.
                unique.add(currentY);
        }
        return unique;
    }

    private HashMap<Integer, ArrayList<Point>> spiltData() {
        HashMap<Integer, ArrayList<Point>> splitData = new HashMap<Integer, ArrayList<Point>>();

        int uniqueSize = this.unique.size();

        for (int i = 0; i < uniqueSize; i++) // Initialize the HashMap.
            splitData.put(unique.get(i), new ArrayList<Point>());

        int pointsSize = this.points.size();

        for (int i = 0; i < pointsSize; i++) { // Splitting the data to labels.
            Point currentPoint = this.points.get(i);
            int currentLabel = this.labels.get(i);

            splitData.get(currentLabel).add(currentPoint);
        }
        return splitData;
    }

    /**
     * This function computes the Euclidean Distance between <code>p</code> and <code>p2</code>.
     * Formula:
     * <pre>
     * sqrt((x1 - x2)^2 + (y1 - y2)^2)
     * </pre>
     * @param p1 The point that we want to calculate its Euclidean Distance from <code>p2</code>.
     * @param p2
     * @return The Euclidean Distance between <code>p</code> and <code>p2</code>.
     */
    private double euclideanDistance(Point p1, Point p2) {
        double distance = 0;
        distance += Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2);
        return Math.sqrt(distance);
    }

    private ArrayList<Point> getNeighbors(Point testPoint) {
        int pointsSize = this.points.size();
        int uniqueSize = this.unique.size();

        ArrayList<Double> distances = new ArrayList<Double>();
        ArrayList<PointDistance> pointDistances = new ArrayList<PointDistance>();
        ArrayList<Point> neighbors = new ArrayList<Point>();

        for (int i = 0; i < pointsSize; i++) {
            double distance = this.euclideanDistance(this.points.get(i), testPoint);
            distances.add(distance);
            pointDistances.add(new PointDistance(this.points.get(i), distance));
        }

        pointDistances.sort(new Comparator<PointDistance>() {
            @Override
            public int compare(PointDistance o1, PointDistance o2) {
                double distanceO1 = o1.getDistance();
                double distanceO2 = o2.getDistance();

                if (distanceO1 > distanceO2)
                    return  -1;
                return distanceO1 == distanceO2 ? 0 : 1;
            }
        });
        for (int i = 0; i < uniqueSize; i++)
            neighbors.add(pointDistances.get(i).getP());
        return neighbors;
    }

    public int predictClassification(Point testPoint) {
        ArrayList<Point> points = this.getNeighbors(testPoint);
        return 0;
    }

}
