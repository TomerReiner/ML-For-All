package com.example.mlforall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.Arrays;

public class ShowModelActivity extends AppCompatActivity {

    private GraphView graph;

    private Intent intent;

    private double[] xTrain;
    private double[] xTest;
    private double[] yTrain;
    private double[] yTest;

    private double slope;
    private double intercept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_model);

        intent = getIntent();
        graph = findViewById(R.id.graph);

        xTrain = intent.getDoubleArrayExtra(LinearRegressionHelper.X_TRAIN);
        xTest = intent.getDoubleArrayExtra(LinearRegressionHelper.X_TEST);
        yTrain = intent.getDoubleArrayExtra(LinearRegressionHelper.Y_TRAIN);
        yTest = intent.getDoubleArrayExtra(LinearRegressionHelper.Y_TEST);
        slope = intent.getDoubleExtra("slope", 0);
        intercept = intent.getDoubleExtra("intercept", 0);

        sortData(xTrain, yTrain);
        sortData(xTest, yTest);

        graph.addSeries(getLine());
        graph.addSeries(getTrainingPoints());
        graph.addSeries(getTestingPoints());
    }

    /**
     * This function create the linear line so the user will be able to the the model that was built for {@link #xTrain} an {@link #yTrain}.
     * @return The line.
     */
    private LineGraphSeries<DataPoint> getLine() {
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
    private PointsGraphSeries<DataPoint> getTrainingPoints() {
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
    private PointsGraphSeries<DataPoint> getTestingPoints() {
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

    /**
     * This function sorts <code>x</code> from minimum value to maximum value and then change <y>so</y> so the y values for each x value will be saved.
     * @param x The x values.
     * @param y The y values.
     */
    private void sortData(double[] x, double[] y) {
        double[] xCopy = x.clone();
        double[] yCopy = y.clone();

        Arrays.sort(x); // Sorting the array.

        int xLength = x.length;

        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < xLength; j++) { // x and y lengths are equal.
                if (xCopy[j] == x[i])
                    y[i] = yCopy[j];
            }
        }
    }
}