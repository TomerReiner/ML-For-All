package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


public class AboutActivity extends AppCompatActivity {

    public static final String TAG = "AboutActivity"; // The name of the activity.

    private DrawerLayout drawerLayout; // The main layout in activity_about.xml.
    private NavigationView navigationView; // The Navigation view in activity_about.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private Dialog loginDialog;
    private Dialog signUpDialog;
    private DatabaseHelper db;
    private MenuHelper menuHelper;

    private TextView tvAbout;

    private Button btnHiddenFeature;

    private String username = "";

    /**
     * The count of times the user clicked on the secret button({@link #btnHiddenFeature}).
     * To open the hidden feature, the user has to click on the button 5 times.
     */
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!

        count = 0;

        btnHiddenFeature.setOnClickListener(v -> {
            count++;
            if (count == 2) { // If the user clicked the button 2 times, we open the hidden feature.
                Intent intent = new Intent(AboutActivity.this, ShowModelActivity.class);
                intent.putExtra("tag", TAG); // Transferring the name of the activity to ShowModelActivity to identify that the user came from this activity and open the hidden feature.
                startActivity(intent);
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
        count = 0;
    }

    /**
     * This function initializes the variables.
     */
    private void initializeVariables() {
        drawerLayout = findViewById(R.id.dlAboutActivity);
        navigationView = findViewById(R.id.nvAboutActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();

        loginDialog = new Dialog(AboutActivity.this);
        signUpDialog = new Dialog(AboutActivity.this);
        db = new DatabaseHelper(AboutActivity.this);
        username = db.getCurrentLoggedInUsername();
        menuHelper = new MenuHelper(AboutActivity.this, drawerLayout, navigationView, drawerToggle, actionBar, loginDialog, signUpDialog, db, username);

        tvAbout = findViewById(R.id.tvAbout);
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
        btnHiddenFeature = findViewById(R.id.btnHiddenFeature);
    }
}
