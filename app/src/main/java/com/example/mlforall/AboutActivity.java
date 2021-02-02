package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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
        drawerLayout = findViewById(R.id.dlAboutActivity);
        navigationView = findViewById(R.id.nvAboutActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();

        tvAbout = findViewById(R.id.tvAbout);
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * This function initializes the main menu.
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemAbout})</code> because we are in {@link AboutActivity}.
     * @see MainActivity
     * @see CreateMachineLearningModelActivity
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
                    Intent intentMoveToMainActivity = new Intent(AboutActivity.this, MainActivity.class);
                    startActivity(intentMoveToMainActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemCreateMachineLearningModel) { // If the user wants to go to AboutActivity.
                    Intent intentMoveToCreateMachineLearningActivity = new Intent(AboutActivity.this, CreateMachineLearningModelActivity.class);
                    startActivity(intentMoveToCreateMachineLearningActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                    Intent intentMoveToMyModelsActivity = new Intent(AboutActivity.this, MyModelsActivity.class);
                    startActivity(intentMoveToMyModelsActivity);
                    return true;
                }
                else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                    Intent intentMoveToMyProfileActivity = new Intent(AboutActivity.this, MyProfileActivity.class);
                    startActivity(intentMoveToMyProfileActivity);
                    return true;
                }
                //TODO-Login
                return false;
            }
        });
    }
}
