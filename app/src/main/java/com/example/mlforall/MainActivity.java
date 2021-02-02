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

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Dialog dialog;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        setMainMenu(); // Initialize the main menu-DO NOT REMOVE!
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
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * This function initializes the variables.
     */
    private void initializeVariables() { // TODO-complete
        drawerLayout = findViewById(R.id.dlMainActivity);
        navigationView = findViewById(R.id.nvMainActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();

        db = new DatabaseHelper(MainActivity.this);
    }

    /**
     * This function initializes the main menu.
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemGoHome})</code> because we are in home activity.
     * @see CreateMachineLearningModelActivity
     * @see MyModelsActivity
     * @see MyProfileActivity
     * @see AboutActivity
     */
    private void setMainMenu() {
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemCreateMachineLearningModel) { // If the user wants to go to CreateMachineLearningModelActivity.
                    Intent intentMoveToCreateMachineLearningActivity = new Intent(MainActivity.this, CreateMachineLearningModelActivity.class);
                    startActivity(intentMoveToCreateMachineLearningActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                    Intent intentMoveToMyProfileActivity = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(intentMoveToMyProfileActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                    Intent intentMoveToMyModelsActivity = new Intent(MainActivity.this, MyModelsActivity.class);
                    startActivity(intentMoveToMyModelsActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemLoginOrLogout) {
                    DialogHelper helper = new DialogHelper(MainActivity.this, db);
                    dialog = helper.buildDialog();
                    dialog.show();
                }
                else if (item.getItemId() == R.id.itemAbout) { // If the user wants to move to AboutActivity.
                    Intent intentMoveToAboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intentMoveToAboutActivity);
                    return true;
                }
                return false;
            }
        });
    }
}
