package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CreateMachineLearningModelActivity extends AppCompatActivity {

    public static final String TAG = "CreateMachineLearningModelActivity"; // The name of the activity.

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

    private String username = "";

    private HashMap<String, double[]> dataset; // This HashMap will contain the dataset.
    private boolean successfullyLoadedDataset = false;


    public static final int REQUEST_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine_learning_model);

        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!

        btnLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xColumn = getSelectedColumn(spinnerChooseXData);
                String yColumn = getSelectedColumn(spinnerChooseYData);
                double[] x = dataset.get(xColumn);
                double[] y = dataset.get(yColumn);
            }
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
    }

    /**
     * This function checks if there is a permission to read from external storage.
     * @return <code>true</code> if there is a permission to read from external storage, <code>false</code> if not.
     */
    private boolean canReadExternalStorage() {
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function asks permission from the user to read external storage if he didn't grant the permission.
     */
    private void requestReadExternalStoragePermission() {
        if (!canReadExternalStorage())
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
    }

    /**
     * This function loads the dataset from the file that the user wanted.
     * @return <code>true</code> if the file was successfully loaded, <code>false</code> if not.
     */
    private boolean loadDataset() {
        String fileName = etFileName.getText().toString();
        requestReadExternalStoragePermission();
        try {
            if (canReadExternalStorage()) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName); // Getting the file from the Downloads directory in the external storage.
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                FileHelper helper = new FileHelper(bufferedReader);
                dataset = helper.getDataset(); // Loading the dataset.

                if (dataset == null || dataset.size() == 0) { // If loading the dataset failed for any reason.
                    Toast.makeText(CreateMachineLearningModelActivity.this, "Please Make sure that all the values in the file are filled, there is the same amount of items in each column and that there are at least 10 rows in the dataset and 2 or more numeric columns.", Toast.LENGTH_LONG).show();
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
        Set datasetColumns = dataset.keySet();
        String[] columns = new String[datasetColumns.size()];
        columns = (String[]) datasetColumns.toArray(columns);

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
}
