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

import javax.net.ssl.X509TrustManager;

public class ShowModelActivity extends AppCompatActivity {

    private GraphView graph;

    private Intent intent;

    private double[] xTrain;
    private double[] xTest;
    private double[] yTrain;
    private double[] yTest;

    private double slope;
    private double intercept;

    private boolean isDataTooLargeToDisplay;

    private PlottingHelper plottingHelper;
// TODO - graph point error
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
        isDataTooLargeToDisplay = intent.getBooleanExtra(CreateMachineLearningModelActivity.IS_DATA_TO_LARGE_TO_DISPLAY, true);

        plottingHelper = new PlottingHelper(xTrain, xTest, yTrain,yTest, slope, intercept, isDataTooLargeToDisplay);

        graph.addSeries(plottingHelper.getLine());

        if ((xTrain != null && xTest != null && yTrain != null && yTest != null) && !isDataTooLargeToDisplay) { // If the training and testing points are not null and if the data can be displayed we show the data.
            graph.addSeries(plottingHelper.getTrainingPoints());
            graph.addSeries(plottingHelper.getTestingPoints());
        }
    }
}