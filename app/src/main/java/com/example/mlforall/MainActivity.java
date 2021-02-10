package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Dialog loginDialog;
    private Dialog signUpDialog;

    private DatabaseHelper db;
    private MenuHelper menuHelper;

    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!
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
        drawerLayout = findViewById(R.id.dlMainActivity);
        navigationView = findViewById(R.id.nvMainActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();
        loginDialog = new Dialog(MainActivity.this);
        signUpDialog = new Dialog(MainActivity.this);
        db = new DatabaseHelper(MainActivity.this);
        username = db.getCurrentLoggedInUsername();
        menuHelper = new MenuHelper(MainActivity.this, drawerLayout, navigationView, drawerToggle, actionBar, loginDialog, signUpDialog, db, username);
    }
}
