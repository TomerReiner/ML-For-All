package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateMachineLearningModelActivity extends AppCompatActivity {
// TODO - abort process.
    public static final String TAG = "CreateMachineLearningModelActivity"; // The name of the activity.

    public static final String IS_DATA_TO_LARGE_TO_DISPLAY = "tooLargeToDisplay";

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Dialog loginDialog;
    private Dialog signUpDialog;

    private DatabaseHelper db;
    private MenuHelper menuHelper;

    private EditText etFileName;

    private Button btnLoadFile;

    private TextView tvChooseXYColumns;

    private Spinner spinnerChooseXData;
    private Spinner spinnerChooseYData;

    private Button btnStart;

    private TextView tvSlope;
    private TextView tvIntercept;

    private Button btnTestModel;

    private TextView tvScore;

    private Button btnShowModel;

    private Button btnReset;

    private String username = "";

    private HashMap<String, double[]> dataset; // This HashMap will contain the dataset.
    private boolean successfullyLoadedDataset = false;

    /**
     * If the data is too large to display on the graph.
     * @see LinearRegressionHelper#isDataTooLargeForGraph(double[])
     * @see PlottingHelper
     * @see ShowModelActivity
     */
    private boolean isDataTooLargeToDisplay = false;

    private LinearRegression model;

    /**
     * This variable will contain the dataset split to training and testing x and training and testing y.
     * @see LinearRegressionHelper#splitData(double[], double[])
     */
    private ArrayList<double[]> splitData = new ArrayList<>();

    private double[] x;
    private double[] y;

    private double[] xTrain;
    private double[] xTest;
    private double[] yTrain;
    private double[] yTest;

    private LinearEquation equation;

    public static final int REQUEST_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine_learning_model);

        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!

        requestReadExternalStoragePermission(); // Asking for permission to read files from the sd card.

        btnLoadFile.setOnClickListener(v -> {
            username = db.getCurrentLoggedInUsername();
            if (username.equals("")) { // If the user is not logged in then we notify him and terminate the model building process.
                Toast.makeText(CreateMachineLearningModelActivity.this, "You must be logged in to build machine learning models.", Toast.LENGTH_SHORT).show();
                return;
            }

            successfullyLoadedDataset = loadDataset();
            if (successfullyLoadedDataset) { // If the dataset was successfully loaded.
                btnLoadFile.setEnabled(false);
                tvChooseXYColumns.setVisibility(View.VISIBLE);
                spinnerChooseXData.setVisibility(View.VISIBLE);
                setSpinnerItems(spinnerChooseXData);
                spinnerChooseYData.setVisibility(View.VISIBLE); // Setting the spinners to choose X and Y data available.
                setSpinnerItems(spinnerChooseYData);
                btnStart.setVisibility(View.VISIBLE);
            }
        });

        btnStart.setOnClickListener(v -> buildMachineLearningModel());

        btnTestModel.setOnClickListener(v -> {
            username = db.getCurrentLoggedInUsername();
            if (username.equals("")) { // If the user is not logged in then we notify him and terminate the model building process.
                Toast.makeText(CreateMachineLearningModelActivity.this, "You must be logged in to view model's score.", Toast.LENGTH_SHORT).show();
                return;
            }
            tvScore.setVisibility(View.VISIBLE); // Setting the next views to be visible.

            double score = model.score(xTest, yTest); // Getting the score of the model.
            tvScore.setText(tvScore.getText().toString() + " " + score * 100 + "%"); // Showing the score in percentages format.
            btnTestModel.setEnabled(false);
            btnShowModel.setVisibility(View.VISIBLE);

            db.addModel(username, equation, score); // Adding the model to the database.
        });

        btnShowModel.setOnClickListener(v -> {
            username = db.getCurrentLoggedInUsername();
            if (username.equals("")) { // If the user is not logged in then we notify him and terminate the model building process.
                Toast.makeText(CreateMachineLearningModelActivity.this, "You must be logged in to view your model's graph.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intentMoveToShowModelActivity = new Intent(CreateMachineLearningModelActivity.this, ShowModelActivity.class);
            intentMoveToShowModelActivity.putExtra(LinearRegressionHelper.X_TRAIN, xTrain);
            intentMoveToShowModelActivity.putExtra(LinearRegressionHelper.X_TEST, xTest);
            intentMoveToShowModelActivity.putExtra(LinearRegressionHelper.Y_TRAIN, yTrain);
            intentMoveToShowModelActivity.putExtra(LinearRegressionHelper.Y_TEST, yTest);
            intentMoveToShowModelActivity.putExtra(IS_DATA_TO_LARGE_TO_DISPLAY, isDataTooLargeToDisplay);
            intentMoveToShowModelActivity.putExtra("slope", equation.getSlope());
            intentMoveToShowModelActivity.putExtra("intercept", equation.getIntercept());
            startActivity(intentMoveToShowModelActivity);
        });

        btnReset.setOnClickListener(v -> {
            resetProcess(); // Terminating the Machine Learning Model building process.
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) // If the menu items successfully loaded.
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginDialog.dismiss();
        signUpDialog.dismiss();
    }
// TODO-dialog for normalize data
    /**
     * This function initializes the variables.
     */
    private void initializeVariables() {
        drawerLayout = findViewById(R.id.dlCreateMachineLearningModelActivity);
        navigationView = findViewById(R.id.nvCreateMachineLearningModelActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();
        loginDialog = new Dialog(CreateMachineLearningModelActivity.this);
        signUpDialog = new Dialog(CreateMachineLearningModelActivity.this);
        db = new DatabaseHelper(CreateMachineLearningModelActivity.this);
        username = db.getCurrentLoggedInUsername();
        menuHelper = new MenuHelper(CreateMachineLearningModelActivity.this, drawerLayout, navigationView, drawerToggle, actionBar, loginDialog, signUpDialog, db, username);

        dataset = new HashMap<>();

        etFileName = findViewById(R.id.etFileName);

        btnLoadFile = findViewById(R.id.btnLoadFile);

        tvChooseXYColumns = findViewById(R.id.tvChooseXYColumns);

        spinnerChooseXData = findViewById(R.id.spinnerChooseXData);
        spinnerChooseYData = findViewById(R.id.spinnerChooseYData);

        btnStart = findViewById(R.id.btnStart);

        tvSlope = findViewById(R.id.tvSlope);
        tvIntercept = findViewById(R.id.tvIntercept);

        btnTestModel = findViewById(R.id.btnTestModel);

        tvScore = findViewById(R.id.tvScore);

        btnShowModel = findViewById(R.id.btnShowModel);

        btnReset = findViewById(R.id.btnReset);
    }

    /**
     * This function checks if there is a permission to read from external storage.
     * @return <code>true</code> if there is a permission to read from external storage, <code>false</code> if not.
     */
    private boolean canReadExternalStorage() {
        return (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * This function asks permission from the user to read external storage if he didn't grant the permission.
     */
    private void requestReadExternalStoragePermission() {
        if (!canReadExternalStorage())
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
    }

    /**
     * This function loads the dataset from the file that the user wanted.
     * @return <code>true</code> if the file was successfully loaded, <code>false</code> if not.
     */
    private boolean loadDataset() {
        String fileName = etFileName.getText().toString();
        try {
            if (canReadExternalStorage()) {
                File file = new File(Environment.getExternalStorageDirectory() + "/Download/", fileName); // Getting the file from the Downloads directory in the external storage.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                FileHelper helper = new FileHelper(bufferedReader);
                dataset = helper.getDataset(); // Loading the dataset.

                if (dataset == null || dataset.size() == 0) { // If loading the dataset failed for any reason.
                    Toast.makeText(CreateMachineLearningModelActivity.this,
                            "Please Make sure that all the values in the file are filled, there is the same amount of items in each column and that there are at least 10 rows in the dataset and 2 or more numeric columns.",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                else // The the file was successfully loaded.
                    return true;
            }
            else { // The user has not granted the application permission to read files.
                Toast.makeText(CreateMachineLearningModelActivity.this, "Can't read file without permission.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) { // Error loading the file.
            Toast.makeText(CreateMachineLearningModelActivity.this, "Error Loading the file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This function sets the items in <code>spinner</code> to be the {@link #dataset} keys(the columns of the dataset).
     * @param spinner The spinner that we want to set the items in it.
     */
    private void setSpinnerItems(Spinner spinner) {
        String[] columns = dataset.keySet().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateMachineLearningModelActivity.this, android.R.layout.simple_spinner_item, columns);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * This function checks what item was selected in <code>spinner</code>.
     * @param spinner The spinner in which we want to see which item was selected.
     * @return The item that was selected in the spinner.
     */
    private String getSelectedColumn(Spinner spinner) {
        int position = spinner.getSelectedItemPosition();
        return (String) spinner.getItemAtPosition(position);
    }

    /**
     * This function resets the Machine Learning building process.
     * This function will reset the layout to its starting position.
     * The function won't reset the text in {@link #etFileName}.
     * @see R.layout#activity_create_machine_learning_model
     */
    private void resetProcess() {
        btnLoadFile.setEnabled(true);
        tvChooseXYColumns.setVisibility(View.GONE);
        spinnerChooseXData.setClickable(true);
        spinnerChooseXData.setVisibility(View.GONE);
        spinnerChooseYData.setClickable(true);
        spinnerChooseYData.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);
        btnStart.setEnabled(true);
        tvSlope.setText(R.string.slope);
        tvSlope.setVisibility(View.GONE);
        tvIntercept.setText(R.string.intercept);
        tvIntercept.setVisibility(View.GONE);
        btnShowModel.setVisibility(View.GONE);
        btnShowModel.setEnabled(true);
        tvScore.setText(R.string.model_score);
        tvScore.setVisibility(View.GONE);
        btnTestModel.setVisibility(View.GONE);
        btnTestModel.setEnabled(true);
        btnShowModel.setVisibility(View.GONE);
        btnShowModel.setEnabled(true);
    }

    /**
     * This function builds the machine learning model once the user presses
     * {@link #btnStart}.
     */
    private void buildMachineLearningModel() {
        username = db.getCurrentLoggedInUsername();
        if (username.equals("")) { // If the user is not logged in then we notify him and terminate the model building process.
            Toast.makeText(CreateMachineLearningModelActivity.this, "You must be logged in to build machine learning models.", Toast.LENGTH_SHORT).show();
            return;
        }

        String xColumn = getSelectedColumn(spinnerChooseXData);
        String yColumn = getSelectedColumn(spinnerChooseYData);
        spinnerChooseXData.setClickable(false);
        spinnerChooseYData.setClickable(false);
        x = dataset.get(xColumn);
        y = dataset.get(yColumn);

        splitData = LinearRegressionHelper.splitData(x, y);

        xTrain = splitData.get(LinearRegressionHelper.X_TRAIN_INDEX);
        xTest = splitData.get(LinearRegressionHelper.X_TEST_INDEX);
        yTrain = splitData.get(LinearRegressionHelper.Y_TRAIN_INDEX);
        yTest = splitData.get(LinearRegressionHelper.Y_TEST_INDEX); // Getting the data.

        isDataTooLargeToDisplay = LinearRegressionHelper.isDataTooLargeForGraph(x) || LinearRegressionHelper.isDataTooLargeForGraph(y);

        model = new LinearRegression(xTrain, yTrain);
        model.fit(); // Fitting the model to create the best matching linear equation for this data.
        equation = model.getLinearEquation();

        btnTestModel.setVisibility(View.VISIBLE);
        tvSlope.setVisibility(View.VISIBLE);
        tvSlope.setText(tvSlope.getText().toString() + " " + equation.getSlope());
        tvIntercept.setVisibility(View.VISIBLE);
        tvIntercept.setText(tvIntercept.getText().toString() + " " + equation.getIntercept());
        btnStart.setEnabled(false);
    }
}
