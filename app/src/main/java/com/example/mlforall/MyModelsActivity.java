package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyModelsActivity extends AppCompatActivity {

    public static final String TAG = "MyModelsActivity";

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Dialog loginDialog;
    private Dialog signUpDialog;

    private DatabaseHelper db;
    private MenuHelper menuHelper;

    private String username = "";

    private TextView tvLoginWarning;

    private ListView lvMyModels;
    private ArrayList<MachineLearningModel> models;
    private MyModelsListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_models);
        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!
        username = db.getCurrentLoggedInUsername();

        if (username.equals(""))  // If there is no user logged in.
            tvLoginWarning.setVisibility(View.VISIBLE);
        else {
            lvMyModels.setVisibility(View.VISIBLE);
            loadModels();
        }

        // If the user wants to show one of his models.
        lvMyModels.setOnItemClickListener((parent, view, position, id) -> {
            LinearEquation currentLinearEquation = models.get(position).getLinearEquation();
            Intent intentMoveToShowModelActivity = new Intent(MyModelsActivity.this, ShowModelActivity.class);
            intentMoveToShowModelActivity.putExtra("slope", currentLinearEquation.getSlope());
            intentMoveToShowModelActivity.putExtra("intercept", currentLinearEquation.getIntercept());
            startActivity(intentMoveToShowModelActivity);
        });

        lvMyModels.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            db.deleteModel(username, position); // Deleting the model.
            loadModels(); // Reloading the models.
            return true;
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
        drawerLayout = findViewById(R.id.dlMyModelsActivity);
        navigationView = findViewById(R.id.nvMyModelsActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();
        loginDialog = new Dialog(MyModelsActivity.this);
        signUpDialog = new Dialog(MyModelsActivity.this);
        db = new DatabaseHelper(MyModelsActivity.this);
        username = db.getCurrentLoggedInUsername();
        menuHelper = new MenuHelper(MyModelsActivity.this, drawerLayout, navigationView, drawerToggle, actionBar, loginDialog, signUpDialog, db, username);
        lvMyModels = findViewById(R.id.lvMyModels);
        tvLoginWarning = findViewById(R.id.tvLoginWarning);
        models = new ArrayList<>();
    }

    /**
     * This function sets {@link #lvMyModels} adapter.
     */
    private void loadModels() {
        if (username.equals(""))
            return;

        models = db.getAllUsersModel(username);
        adapter = new MyModelsListViewAdapter(MyModelsActivity.this, models);
        lvMyModels.setAdapter(adapter);
    }
}