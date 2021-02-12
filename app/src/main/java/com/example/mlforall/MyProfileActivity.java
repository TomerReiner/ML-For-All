package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MyProfileActivity extends AppCompatActivity {

    public static final String TAG = "MyProfileActivity";

    private DrawerLayout drawerLayout; // The main layout in activity_main.xml.
    private NavigationView navigationView; // The Navigation view in activity_main.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Dialog loginDialog;
    private Dialog signUpDialog;

    private DatabaseHelper db;
    private MenuHelper menuHelper;

    private TextView tvUsername;
    private Button btnChangeUsername;
    private TextView tvPassword;
    private Button btnChangePassword;
    private Button btnRequestMyData;
    private Button btnDeleteData;
    private Button btnDeleteAccount;

    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!

        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = db.getCurrentLoggedInUsername();
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
        drawerLayout = findViewById(R.id.dlMyProfileActivity);
        navigationView = findViewById(R.id.nvMyProfileActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();

        loginDialog = new Dialog(MyProfileActivity.this);
        signUpDialog = new Dialog(MyProfileActivity.this);
        db = new DatabaseHelper(MyProfileActivity.this);
        username = db.getCurrentLoggedInUsername();
        menuHelper = new MenuHelper(MyProfileActivity.this, drawerLayout, navigationView, drawerToggle, actionBar, loginDialog, signUpDialog, db, username);

        tvUsername = findViewById(R.id.tvUsername);
        btnChangeUsername = findViewById(R.id.btnChangeUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnRequestMyData = findViewById(R.id.btnRequestMyData);
        btnDeleteData = findViewById(R.id.btnDeleteData);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
    }
}