package com.example.mlforall;

import android.graphics.Color;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

/**
 * This class helps setting the models {@link com.jjoe64.graphview.GraphView}.
 */
public class PlottingHelper {

    private final double[] xTrain;
    private final double[] xTest;
    private final double[] yTrain;
    private final double[] yTest;
    private final double slope;
    private final double intercept;
    /**
     * If the data values are too large,
     * which means that one of the values in
     * {@link #xTrain}, {@link #xTest}, {@link #yTrain} or {@link #yTest}
     * is less than -100 or more than 100.
     * This is used to prevent {@link OutOfMemoryError},
     * which occurs when the data values are large and the graph can't display values in a large range.
     */
    private final boolean isDataTooLargeToDisplay;

    public PlottingHelper(double[] xTrain, double[] xTest, double[] yTrain, double[] yTest, double slope, double intercept, boolean isDataTooLargeToDisplay) {
        this.xTrain = xTrain;
        this.xTest = xTest;
        this.yTrain = yTrain;
        this.yTest = yTest;
        this.slope = slope;
        this.intercept = intercept;
        this.isDataTooLargeToDisplay = isDataTooLargeToDisplay;
    }

    /**
     * This function creates the linear equation so that the user will be able to view the model that was built for the data.
     * If <code>{@link #isDataTooLargeToDisplay} == true</code>, a line will be shown from x=0 to x=100.
     * @return The line.
     */
    public LineGraphSeries<DataPoint> getLine() {
        if ((xTrain != null && xTest != null && yTrain != null && yTest != null) && !isDataTooLargeToDisplay) // If the training points are not null then we plot them and the data is not too large.
            return plotLinearEquationWithTrainTestValues();
        return plotLinearEquationWithoutTrainTestData();
    }
    /**
     * This function creates the training points.
     * If <code>xTrain.length > 10</code> then only the 10 first points in the dataset will be shown.
     * This is used to prevent too many points on the graph.
     * @return The training points.
     */
    public PointsGraphSeries<DataPoint> getTrainingPoints() {
        PointsGraphSeries<DataPoint> trainPoints = new PointsGraphSeries<>();
        int xTrainLength = xTrain.length;

        if (xTrainLength > 10)
            xTrainLength = 10;

        for (int i = 0; i < xTrainLength; i++) {
            double currentX = xTrain[i];
            double currentY = yTrain[i];
            trainPoints.appendData(new DataPoint(currentX, currentY), true, xTrainLength);
        }
        trainPoints.setTitle("Training Points");
        trainPoints.setColor(Color.RED);
        return trainPoints;
    }

    /**
     * This function creates the testing points.
     * If <code>xTest.length > 10</code> then only the 10 first points in the dataset will be shown.
     * This is used to prevent the appearance of too many points on the graph.
     * @return The testing points.
     */
    public PointsGraphSeries<DataPoint> getTestingPoints() {
        PointsGraphSeries<DataPoint> testingPoints = new PointsGraphSeries<>();
        int xTestLength = xTest.length;

        if (xTestLength > 10)
            xTestLength = 10;

        for (int i = 0; i < xTestLength; i++) {
            double currentX = xTest[i];
            double currentY = yTest[i];
            testingPoints.appendData(new DataPoint(currentX, currentY), true, xTestLength);

        }
        testingPoints.setTitle("Testing Points");
        testingPoints.setColor(Color.BLUE);
        return testingPoints;
    }

    /**
     * This function plots the linear equation if {@link #xTrain}, {@link #xTest}, {@link #yTrain} and {@link #yTest} are not null.
     * @return The line.
     */
    private LineGraphSeries<DataPoint> plotLinearEquationWithTrainTestValues() {
        LineGraphSeries<DataPoint> line = new LineGraphSeries<>();
        double maxXTrain = xTrain[xTrain.length - 1]; // Getting the max item in xTrain(xTrain is sorted so its the last item).
        double maxXTest = xTest[xTest.length - 1];
        double minXTrain = xTrain[0];
        double minXTest = xTest[0];
        int maxXValue = (int) Math.max(maxXTrain, maxXTest);
        int minXValue = (int) (Math.min(minXTrain, minXTest));

        if (minXValue == maxXValue) { // If the min and max values are equal we plot the line from x = 0 to x = 1
            for (int i = 0; i <= 1; i++) {
                double yPredicted = slope * i + intercept;
                line.appendData(new DataPoint(i, yPredicted), true, 2);
            }
            return line;
        }

        for (int i = minXValue; i < maxXValue + 1; i++) {
            double yPredicted = slope * i + intercept;
            line.appendData(new DataPoint(i, yPredicted), true, maxXValue);
        }
        line.setTitle("Linear Equation");
        line.setColor(Color.GREEN);
        return line;
    }

    /**
     * This function plots the linear equation if {@link #xTrain}, {@link #xTest}, {@link #yTrain} and {@link #yTest} are null.
     * @return The line.
     */
    private LineGraphSeries<DataPoint> plotLinearEquationWithoutTrainTestData() {
        LineGraphSeries<DataPoint> line = new LineGraphSeries<>();
        for (int i = 0; i < 100; i++) {
            double yPredicted = slope * i + intercept;
            line.appendData(new DataPoint(i, yPredicted), true, 100);
        }
        line.setTitle("Linear Equation");
        line.setColor(Color.GREEN);
        return line;
    }

    /**
     * This function creates the hidden feature.
     * @return The hidden feature.
     */
    public LineGraphSeries<DataPoint> plotSecretFeature() {
        LineGraphSeries<DataPoint> plot = new LineGraphSeries<>();
        for (int i = -100; i <= 100; i++)
            plot.appendData(new DataPoint(i, 0.0037 * i * i), true, 400);
        return plot;
    }

    /**
     * This function creates the hidden feature.
     * @param x The x value.
     * @return The hidden feature.
     */
    public LineGraphSeries<DataPoint> lineSecretFeature(double x) {
        LineGraphSeries<DataPoint> line = new LineGraphSeries<>();
        for (int i = 50; i <= 65; i++) {
            line.appendData(new DataPoint(x, i), true, 51);
            x += 0.01;
        }
        return line;
    }
}
