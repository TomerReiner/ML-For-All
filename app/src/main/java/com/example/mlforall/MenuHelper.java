package com.example.mlforall;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * This class helps setting the main menu in all of the app's activities.
 * @see MainActivity
 * @see CreateMachineLearningModelActivity
 * @see MyModelsActivity
 * @see MyProfileActivity
 * @see AboutActivity
 * @see R.menu#main_menu
 */
public class MenuHelper {

    private Context context;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;
    private Dialog loginDialog;
    private Dialog signUpDialog;
    private DatabaseHelper db;
    private DialogHelper dialogHelper;
    private String username;

    public MenuHelper(Context context, DrawerLayout drawerLayout, NavigationView navigationView, ActionBarDrawerToggle drawerToggle, ActionBar actionBar, Dialog loginDialog, Dialog signUpDialog, DatabaseHelper db, String username) {
        this.context = context;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
        this.drawerToggle = drawerToggle;
        this.actionBar = actionBar;
        this.loginDialog = loginDialog;
        this.signUpDialog = signUpDialog;
        this.db = db;
        this.username = username;
        this.dialogHelper = new DialogHelper(context, db, loginDialog, signUpDialog);
    }

    /**
     * This function sets the main menu for each activity.
     * If you are in one of the activities and the press the item on the main menu that moves the user to this activity, nothing will happen.
     * For example, if you are in {@link CreateMachineLearningModelActivity} and you press on {@link R.id#itemCreateMachineLearningModel}, nothing will happen.
     * @param tag The name of the activity that we are in.
     */
    public void setMainMenu(String tag) {
        switch (tag) {
            case MainActivity.TAG:
                setMainMenuForMainActivity();
                break;
            case CreateMachineLearningModelActivity.TAG:
                setMainMenuForCreateMachineLearningModelActivity();
                break;
            case MyModelsActivity.TAG:
                setMainMenuForMyModelsActivity();
                break;
            case MyProfileActivity.TAG:
                setMainMenuForMyProfileActivity();
                break;
            case AboutActivity.TAG:
                setMainMenuForAboutActivity();
                break;
        }
    }

    /**
     * This function sets the main menu for {@link MainActivity}.
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemGoHome})</code> because we are in home activity.
     * @see CreateMachineLearningModelActivity
     * @see MyModelsActivity
     * @see MyProfileActivity
     * @see AboutActivity
     */
    private void setMainMenuForMainActivity() {
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.itemCreateMachineLearningModel) { // If the user wants to go to CreateMachineLearningModelActivity.
                Intent intentMoveToCreateMachineLearningActivity = new Intent(context, CreateMachineLearningModelActivity.class);
                context.startActivity(intentMoveToCreateMachineLearningActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                Intent intentMoveToMyProfileActivity = new Intent(context, MyProfileActivity.class);
                context.startActivity(intentMoveToMyProfileActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                Intent intentMoveToMyModelsActivity = new Intent(context, MyModelsActivity.class);
                context.startActivity(intentMoveToMyModelsActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemLoginOrLogout) {
                username = db.getCurrentLoggedInUsername();
                if (username.equals("")) { // If there isn't a logged in user and the user pressed on itemLogInOrLogout then it means that the user wants to log out.
                    username = dialogHelper.buildLoginDialog();
                }
                else {
                    db.logOut(username);
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                    username = "";
                }
            }
            else if (item.getItemId() == R.id.itemAbout) { // If the user wants to move to AboutActivity.
                Intent intentMoveToAboutActivity = new Intent(context, AboutActivity.class);
                context.startActivity(intentMoveToAboutActivity);
                return true;
            }
            return false;
        });
    }

    /**
     * This function sets the main menu for {@link CreateMachineLearningModelActivity}
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemCreateMachineLearningModel})</code> because we are in {@link CreateMachineLearningModelActivity}.
     * @see MainActivity
     * @see AboutActivity
     * @see MyModelsActivity
     * @see MyProfileActivity
     */
    private void setMainMenuForCreateMachineLearningModelActivity() {
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.itemGoHome) { // If the user wants to go to MainActivity.
                Intent intentMoveToMainActivity = new Intent(context, MainActivity.class);
                context.startActivity(intentMoveToMainActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                Intent intentMoveToMyModelsActivity = new Intent(context, MyModelsActivity.class);
                context.startActivity(intentMoveToMyModelsActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                Intent intentMoveToMyProfileActivity = new Intent(context, MyProfileActivity.class);
                context.startActivity(intentMoveToMyProfileActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemLoginOrLogout) {
                username = db.getCurrentLoggedInUsername();
                if (username.equals("")) { // If there isn't a logged in user and the user pressed on itemLogInOrLogout then it means that the user wants to log out.
                    username = dialogHelper.buildLoginDialog();
                }
                else {
                    db.logOut(username);
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                    username = "";
                }
            }
            else if (item.getItemId() == R.id.itemAbout) { // If the user wants to move to AboutActivity.
                Intent intentMoveToAboutActivity = new Intent(context, AboutActivity.class);
                context.startActivity(intentMoveToAboutActivity);
                return true;
            }
            return false;
        });
    }

    /**
     * This function sets the main menu for {@link MyModelsActivity}
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemMyModels})</code> because we are in {@link MyModelsActivity}.
     * We also don't implement login and logout in the activity menu because this will harm the activities function.
     * @see CreateMachineLearningModelActivity
     * @see MainActivity
     * @see MyProfileActivity
     * @see AboutActivity
     */
    private void setMainMenuForMyModelsActivity() {
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.itemGoHome) { // If the user wants to go to MainActivity.
                Intent intentMoveToMainActivity = new Intent(context, MainActivity.class);
                context.startActivity(intentMoveToMainActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemCreateMachineLearningModel) { // If the user wants to go to CreateMachineLearningModelActivity.
                Intent intentMoveToCreateMachineLearningActivity = new Intent(context, CreateMachineLearningModelActivity.class);
                context.startActivity(intentMoveToCreateMachineLearningActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                Intent intentMoveToMyProfileActivity = new Intent(context, MyProfileActivity.class);
                context.startActivity(intentMoveToMyProfileActivity);
                return true;
            }
//                else if (item.getItemId() == R.id.itemLoginOrLogout) {
//                    username = db.getCurrentLoggedInUsername();
//                    if (username.equals("")) { // If there isn't a logged in user and the user pressed on itemLogInOrLogout then it means that the user wants to log out.
//                        username = dialogHelper.buildLoginDialog();
//                    }
//                    else {
//                        db.logOut(username);
//                        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
//                        username = "";
//                    }
//                }
            else if (item.getItemId() == R.id.itemAbout) { // If the user wants to move to AboutActivity.
                Intent intentMoveToAboutActivity = new Intent(context, AboutActivity.class);
                context.startActivity(intentMoveToAboutActivity);
                return true;
            }
            return false;
        });
    }

    /**
     * This function sets the main menu for {@link MyProfileActivity}
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemMyProfile})</code> because we are in {@link MyProfileActivity}.
     * @see CreateMachineLearningModelActivity
     * @see MainActivity
     * @see MyModelsActivity
     * @see AboutActivity
     */
    private void setMainMenuForMyProfileActivity() {
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.itemGoHome) { // If the user wants to go to MainActivity.
                Intent intentMoveToMainActivity = new Intent(context, MainActivity.class);
                context.startActivity(intentMoveToMainActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemCreateMachineLearningModel) { // If the user wants to go to CreateMachineLearningModelActivity.
                Intent intentMoveToCreateMachineLearningActivity = new Intent(context, CreateMachineLearningModelActivity.class);
                context.startActivity(intentMoveToCreateMachineLearningActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                Intent intentMoveToMyModelsActivity = new Intent(context, MyModelsActivity.class);
                context.startActivity(intentMoveToMyModelsActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemLoginOrLogout) {
                username = db.getCurrentLoggedInUsername();
                if (username.equals("")) { // If there isn't a logged in user and the user pressed on itemLogInOrLogout then it means that the user wants to log out.
                    username = dialogHelper.buildLoginDialog();
                }
                else {
                    db.logOut(username);
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                    username = "";
                }
            }
            else if (item.getItemId() == R.id.itemAbout) { // If the user wants to move to AboutActivity.
                Intent intentMoveToAboutActivity = new Intent(context, AboutActivity.class);
                context.startActivity(intentMoveToAboutActivity);
                return true;
            }
            return false;
        });
    }

    /**
     * This function sets the main menu for {@link AboutActivity}
     * @implNote We don't implement <code>if (item.getItemId == {@link R.id#itemAbout})</code> because we are in {@link AboutActivity}.
     * @see MainActivity
     * @see CreateMachineLearningModelActivity
     * @see MyModelsActivity
     * @see MyProfileActivity
     */
    private void setMainMenuForAboutActivity() {
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true); // Set the menu icon available.
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.itemGoHome) { // If the user wants to go to MainActivity.
                Intent intentMoveToMainActivity = new Intent(context, MainActivity.class);
                context.startActivity(intentMoveToMainActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemCreateMachineLearningModel) { // If the user wants to go to AboutActivity.
                Intent intentMoveToCreateMachineLearningActivity = new Intent(context, CreateMachineLearningModelActivity.class);
                context.startActivity(intentMoveToCreateMachineLearningActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemMyModels) { // If the user wants to go to MyModelsActivity.
                Intent intentMoveToMyModelsActivity = new Intent(context, MyModelsActivity.class);
                context.startActivity(intentMoveToMyModelsActivity);
                return true;
            }
            else if (item.getItemId() == R.id.itemLoginOrLogout) {
                username = db.getCurrentLoggedInUsername();
                if (username.equals("")) { // If there isn't a logged in user and the user pressed on itemLogInOrLogout then it means that the user wants to log out.
                    username = dialogHelper.buildLoginDialog();
                }
                else {
                    db.logOut(username);
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                    username = "";
                }
            }
            else if (item.getItemId() == R.id.itemMyProfile) { // If the user wants to go to MyProfileActivity.
                Intent intentMoveToMyProfileActivity = new Intent(context, MyProfileActivity.class);
                context.startActivity(intentMoveToMyProfileActivity);
                return true;
            }
            return false;
        });
    }
}
