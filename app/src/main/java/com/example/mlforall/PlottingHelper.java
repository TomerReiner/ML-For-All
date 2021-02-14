package com.example.mlforall;

import android.graphics.Color;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Arrays;

/**
 * This class helps setting the models {@link com.jjoe64.graphview.GraphView}.
 */
public class PlottingHelper {

    private double[] xTrain;
    private double[] xTest;
    private double[] yTrain;
    private double[] yTest;
    private double slope;
    private double intercept;

    public PlottingHelper(double[] xTrain, double[] xTest, double[] yTrain, double[] yTest, double slope, double intercept) {
        this.xTrain = xTrain;
        this.xTest = xTest;
        this.yTrain = yTrain;
        this.yTest = yTest;
        this.slope = slope;
        this.intercept = intercept;
    }

    /**
     * This function create the linear line so the user will be able to the the model that was built for {@link #xTrain} an {@link #yTrain}.
     * @return The line.
     */
    public LineGraphSeries<DataPoint> getLine() {
        LineGraphSeries<DataPoint> line = new LineGraphSeries<>();
        double maxXTrain = xTrain[xTrain.length - 1]; // Getting the max item in xTrain(xTrain is sorted so its the last item).
        double maxXTest = xTest[xTest.length - 1];
        int maxXValue = (int) Math.max(maxXTrain, maxXTest);


        for (int i = 0; i < maxXValue; i++) {
            double yPredicted = slope * i + intercept;
            line.appendData(new DataPoint(i, yPredicted), true, maxXValue);
        }
        return line;
    }
    /**
     * This function creates the training points.
     * @return The training points.
     */
    public PointsGraphSeries<DataPoint> getTrainingPoints() {
        PointsGraphSeries<DataPoint> trainPoints = new PointsGraphSeries<>();
        int xTrainLength = xTrain.length;

        for (int i = 0; i < xTrainLength; i++) {
            double currentX = xTrain[i];
            double currentY = yTrain[i];
            trainPoints.appendData(new DataPoint(currentX, currentY), true, xTrainLength);
        }

        trainPoints.setColor(Color.RED);
        return trainPoints;
    }

    /**
     * This function creates the testing points.
     * @return The testing points.
     */
    public PointsGraphSeries<DataPoint> getTestingPoints() {
        PointsGraphSeries<DataPoint> testingPoints = new PointsGraphSeries<>();
        int xTestLength = xTest.length;

        for (int i = 0; i < xTestLength; i++) {
            double currentX = xTest[i];
            double currentY = yTest[i];
            testingPoints.appendData(new DataPoint(currentX, currentY), true, xTestLength);
        }

        testingPoints.setColor(Color.BLUE);
        return testingPoints;
    }
}
