package com.example.mlforall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;


public class ShowModelActivity extends AppCompatActivity {

    public static final String GRAPH_WARNING = "Your data values are too large.\n" +
            " The data points won't be shown.\n" +
            " If you want that the data points will be shown, you have to set the CSV file values to be between -100 and 100.";

    /**
     * This {@link TextView} shows {@link #GRAPH_WARNING} if
     * <code>{@link #isDataTooLargeToDisplay} == true</code>.
     * The warning warns the user that the data values are too large,
     * and he has to change them to smaller values.
     * The application will not normalize the data to prevent errors that occur with
     * the {@link GraphView}.
     * The view will be {@link View#GONE} until this view is necessary.
     */
    private TextView tvGraphWarning;
    private GraphView graph;

    private Intent intent; // The intent that moved us to this activity.

    /**
     * This params are the values that we got from {@link CreateMachineLearningModelActivity}.
     * If the activity was started from {@link MyModelsActivity},
     * the values will remain <code>null</code>.
     */
    private double[] xTrain = null;
    private double[] xTest = null;
    private double[] yTrain = null;
    private double[] yTest = null;

    /**
     * The slope we got from {@link CreateMachineLearningModelActivity}
     * or {@link MyModelsActivity}.
     */
    private double slope;

    /**
     * The intercept we got from {@link CreateMachineLearningModelActivity}
     * or {@link MyModelsActivity}.
     */
    private double intercept;

    /**
     * The boolean that says if the values are too large to display.
     * The value was taken from {@link CreateMachineLearningModelActivity}.
     */
    private boolean isDataTooLargeToDisplay;

    private PlottingHelper plottingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_model);

        intent = getIntent();
        tvGraphWarning = findViewById(R.id.tvGraphWarning);
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

        if (isDataTooLargeToDisplay) {
            tvGraphWarning.setVisibility(View.VISIBLE);
            tvGraphWarning.setText(GRAPH_WARNING);
        }

        if ((xTrain != null && xTest != null && yTrain != null && yTest != null) && !isDataTooLargeToDisplay) { // If the training and testing points are not null and if the data can be displayed we show the data.
            graph.addSeries(plottingHelper.getTrainingPoints());
            graph.addSeries(plottingHelper.getTestingPoints());
        }
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
}