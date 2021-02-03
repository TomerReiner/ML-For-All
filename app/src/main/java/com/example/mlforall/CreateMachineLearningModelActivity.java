package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

public class CreateMachineLearningModelActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Button btnUploadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine_learning_model);

        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        setMainMenu(); // Initialize the main menu-DO NOT REMOVE!
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) // If the menu items successfully loaded.
            return true;
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function initializes the variables.
     */
    private void initializeVariables() { // TODO-complete
        drawerLayout = findViewById(R.id.dlCreateMachineLearningModelActivity);
        navigationView = findViewById(R.id.nvCreateMachineLearningModelActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();

        btnUploadFile = findViewById(R.id.btnUploadFile);
    }

    /**
     * This function initializes the main menu.
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemCreateMachineLearningModel})</code> because we are in {@link CreateMachineLearningModelActivity}.
     * @see MainActivity
     * @see AboutActivity
     * @see MyModelsActivity
     * @see MyProfileActivity
     */
    private void setMainMenu() {
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemGoHome) { // If the user wants to go to MainActivity.
                    Intent intentMoveToMainActivity = new Intent(CreateMachineLearningModelActivity.this, MainActivity.class);
                    startActivity(intentMoveToMainActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                    Intent intentMoveToMyModelsActivity = new Intent(CreateMachineLearningModelActivity.this, MyModelsActivity.class);
                    startActivity(intentMoveToMyModelsActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                    Intent intentMoveToMyProfileActivity = new Intent(CreateMachineLearningModelActivity.this, MyProfileActivity.class);
                    startActivity(intentMoveToMyProfileActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemAbout) { // If the user wants to move to AboutActivity.
                    Intent intentMoveToAboutActivity = new Intent(CreateMachineLearningModelActivity.this, AboutActivity.class);
                    startActivity(intentMoveToAboutActivity);
                    return true;
                }
                //TODO-Login
                return false;
            }
        });
    }
}
