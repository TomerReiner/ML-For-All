package com.example.mlforall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MyProfileActivity extends AppCompatActivity {

    public static final String TAG = "MyProfileActivity";

    private DrawerLayout drawerLayout; // The main layout in activity_my_profile.xml.
    private NavigationView navigationView; // The Navigation view in activity_my_profile.xml.
    private ActionBarDrawerToggle drawerToggle;
    private ActionBar actionBar;

    private ConstraintLayout constraintLayout;

    private Dialog loginDialog;
    private Dialog signUpDialog;
    private Dialog changeUsernameDialog;
    private Dialog changePasswordDialog;
    private Dialog deleteMLModelsDialog;
    private Dialog deleteAccountDialog;

    private DatabaseHelper db;
    private MenuHelper menuHelper;

    private TextView tvUsername;
    private Button btnChangeUsername;
    private TextView tvPassword;
    private Button btnChangePassword;
    private Button btnDeleteMLModels;
    private Button btnDeleteAccount;

    private TextView tvLoginNote;

    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initializeVariables(); // Initialize all the variables-DO NOT REMOVE!
        menuHelper.setMainMenu(TAG); // Initialize the main menu-DO NOT REMOVE!

        updateUsernameAndPasswordTextViews();

        btnChangeUsername.setOnClickListener(v -> {
            createChangeUsernameDialog();
            updateUsernameAndPasswordTextViews();
        });

        btnChangePassword.setOnClickListener(v -> {
            createChangePasswordDialog();
            updateUsernameAndPasswordTextViews();
        });


        btnDeleteMLModels.setOnClickListener(v -> {
            updateUsernameAndPasswordTextViews();
            createDeleteMLModelsDialog();
        });

        btnDeleteAccount.setOnClickListener(v -> createDeleteAccountDialog());

        // These listeners are used to update the text views' text on this activity.
        constraintLayout.setOnClickListener(v -> updateUsernameAndPasswordTextViews());

        drawerToggle.setToolbarNavigationClickListener(v -> updateUsernameAndPasswordTextViews());

        constraintLayout.setOnTouchListener((View v, MotionEvent event) -> {
            updateUsernameAndPasswordTextViews();
            return false;
        });

        navigationView.setOnTouchListener((v, event) -> {
            updateUsernameAndPasswordTextViews();
            return false;
        });

        drawerLayout.setOnTouchListener((v, event) -> {
            updateUsernameAndPasswordTextViews();
            return false;
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
        changeUsernameDialog.dismiss();
        deleteMLModelsDialog.dismiss();
        deleteAccountDialog.dismiss();
    }

    /**
     * This function initializes the variables.
     */
    private void initializeVariables() {
        drawerLayout = findViewById(R.id.dlMyProfileActivity);
        navigationView = findViewById(R.id.nvMyProfileActivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBar = getSupportActionBar();

        constraintLayout = findViewById(R.id.constrainLayout);

        loginDialog = new Dialog(MyProfileActivity.this);
        signUpDialog = new Dialog(MyProfileActivity.this);
        changeUsernameDialog = new Dialog(MyProfileActivity.this);
        changePasswordDialog = new Dialog(MyProfileActivity.this);
        deleteMLModelsDialog = new Dialog(MyProfileActivity.this);
        deleteAccountDialog = new Dialog(MyProfileActivity.this);
        db = new DatabaseHelper(MyProfileActivity.this);
        username = db.getCurrentLoggedInUsername();
        menuHelper = new MenuHelper(MyProfileActivity.this, drawerLayout, navigationView, drawerToggle, actionBar, loginDialog, signUpDialog, db, username);

        tvUsername = findViewById(R.id.tvUsername);
        btnChangeUsername = findViewById(R.id.btnChangeUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteMLModels = findViewById(R.id.btnDeleteMLModels);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        tvLoginNote = findViewById(R.id.tvLoginNote);
    }

    /**
     * This function creates the change username dialog.
     * The dialog will enable the username to change his username.
     * If the new username already exists, the user will be asked to choose other username.
     */
    private void createChangeUsernameDialog() {
        username = db.getCurrentLoggedInUsername();
        if (username.equals("")) { // If there is no username logged in, we terminate the process.
            Toast.makeText(MyProfileActivity.this, "You must be logged in to change your username.", Toast.LENGTH_LONG).show();
            return;
        }

        changeUsernameDialog.setContentView(R.layout.change_username_dialog);
        changeUsernameDialog.setCancelable(true);

        EditText etChangeUsernameNewUsername = changeUsernameDialog.findViewById(R.id.etChangeUsernameNewUsername);
        EditText etChangeUsernameRetypeUsername = changeUsernameDialog.findViewById(R.id.etChangeUsernameRetypeUsername);
        EditText etChangeUsernameVerifyPassword = changeUsernameDialog.findViewById(R.id.etChangeUsernameVerifyPassword);

        Button btnChangeUsernameConfirm = changeUsernameDialog.findViewById(R.id.btnChangeUsernameConfirm);

        btnChangeUsernameConfirm.setOnClickListener(v -> {
            String newUsername = etChangeUsernameNewUsername.getText().toString();
            String retypeUsername = etChangeUsernameRetypeUsername.getText().toString();
            String insertedPassword = etChangeUsernameVerifyPassword.getText().toString(); // The values that the user typed.
            String realPassword = db.getPasswordForUsername(username); // The real password of the user.

            /*
            If the new username filed and the retype username field are equal,
            and if the password that the user has entered is his reaL Password.
             */
            if (newUsername.equals(retypeUsername) && insertedPassword.equals(realPassword)) {
                boolean successfullyChangesUsername = db.changeUsername(username, newUsername);

                if (successfullyChangesUsername) { // If the username was successfully changed.
                    Toast.makeText(MyProfileActivity.this, "Username was successfully changes!", Toast.LENGTH_LONG).show();
                    username = newUsername; // Updating the username.
                    changeUsernameDialog.dismiss();
                    updateUsernameAndPasswordTextViews();
                }

                else { // Changing the username failed.
                    Toast.makeText(MyProfileActivity.this, "Error encountered. Please make sure that all the fields are filled." +
                            " If this doesn't work, try other username.", Toast.LENGTH_LONG).show();
                    clearAllFields(etChangeUsernameNewUsername, etChangeUsernameRetypeUsername, etChangeUsernameVerifyPassword);
                }
            }
            else {
                Toast.makeText(MyProfileActivity.this, "Please make sure that the usernames are equal, and that the password is correct", Toast.LENGTH_LONG).show();
                clearAllFields(etChangeUsernameNewUsername, etChangeUsernameRetypeUsername, etChangeUsernameVerifyPassword);
            }
        });
        changeUsernameDialog.show();
    }

    /**
     * This function creates the change username dialog.
     * The dialog will enable the username to change his username.
     * If the new username already exists, the user will be asked to choose other username.
     */
    private void createChangePasswordDialog() {
        username = db.getCurrentLoggedInUsername();
        if (username.equals("")) { // If there is no username logged in, we terminate the process.
            Toast.makeText(MyProfileActivity.this, "You must be logged in to change your password.", Toast.LENGTH_LONG).show();
            return;
        }

        changePasswordDialog.setContentView(R.layout.change_password_dialog);
        changePasswordDialog.setCancelable(true);

        EditText etChangePasswordNewPassword = changePasswordDialog.findViewById(R.id.etChangePasswordNewPassword);
        EditText etChangePasswordRetypePassword = changePasswordDialog.findViewById(R.id.etChangePasswordRetypePassword);
        EditText etChangePasswordOldPassword = changePasswordDialog.findViewById(R.id.etChangePasswordOldPassword);

        Button btnChangePasswordConfirm = changePasswordDialog.findViewById(R.id.btnChangePasswordConfirm);

        btnChangePasswordConfirm.setOnClickListener(v -> {
            String newPassword = etChangePasswordNewPassword.getText().toString();
            String retypePassword = etChangePasswordRetypePassword.getText().toString();
            String insertedPassword = etChangePasswordOldPassword.getText().toString(); // The values that the user typed.
            String realPassword =  db.getPasswordForUsername(username); // The real password of the user.

            /*
            If the new password filed and the retype password field are equal,
            and if the password that the user has entered is his real password.
             */
            if (newPassword.equals(retypePassword) && insertedPassword.equals(realPassword)) {
                boolean successfullyChangesPassword = db.changePassword(username, newPassword);

                if (successfullyChangesPassword) { // If the password was successfully changed.
                    Toast.makeText(MyProfileActivity.this, "Password was successfully changed!", Toast.LENGTH_LONG).show();
                    changePasswordDialog.dismiss();
                    updateUsernameAndPasswordTextViews();
                }
                else {
                    Toast.makeText(MyProfileActivity.this, "Error encountered. Please make sure that all the fields are filled.", Toast.LENGTH_LONG).show();
                    clearAllFields(etChangePasswordNewPassword,  etChangePasswordRetypePassword, etChangePasswordOldPassword);
                }
            }
            else {
                Toast.makeText(MyProfileActivity.this, "Please make sure that new passwords' fields are equal and that the old password is correct.", Toast.LENGTH_LONG).show();
                clearAllFields(etChangePasswordNewPassword,  etChangePasswordRetypePassword, etChangePasswordOldPassword);
            }
        });
        changePasswordDialog.show();
    }

    /**
     * This function creates the dialog to delete all the user's ML Models.
     * If the passwords that the user has inserted are equal and they are the real password,
     * the user ML Model table will be emptied.
     * @see DatabaseHelper#deleteUserData(String)
     */
    private void createDeleteMLModelsDialog() {
        username = db.getCurrentLoggedInUsername();
        if (username.equals("")) { // If there is no username logged in, we terminate the process.
            Toast.makeText(MyProfileActivity.this, "You must be logged in to delete your ML Models.", Toast.LENGTH_LONG).show();
            return;
        }

        deleteMLModelsDialog.setContentView(R.layout.delete_ml_models_dialog);
        deleteMLModelsDialog.setCancelable(true);

        EditText etDeleteMLModelsPassword = deleteMLModelsDialog.findViewById(R.id.etDeleteMLModelsPassword);
        EditText etDeleteMLModelsRetypePassword = deleteMLModelsDialog.findViewById(R.id.etDeleteMLModelsRetypePassword);
        Button btnDeleteMLModelsConfirm = deleteMLModelsDialog.findViewById(R.id.btnDeleteMLModelsConfirm);

        btnDeleteMLModelsConfirm.setOnClickListener(v -> {
            String insertedPassword = etDeleteMLModelsPassword.getText().toString();
            String retypePassword = etDeleteMLModelsRetypePassword.getText().toString();
            String realPassword = db.getPasswordForUsername(username); // The real password of the user.

            if (realPassword.equals(insertedPassword) && realPassword.equals(retypePassword)) { // If the passwords are equal, and if both the inserted passwords are equal then we will delete the user's ML Models.
                db.deleteUserData(username);
                Toast.makeText(MyProfileActivity.this, "Your ML Models were deleted :(", Toast.LENGTH_LONG).show();
                deleteMLModelsDialog.dismiss();
            }
            else {
                Toast.makeText(MyProfileActivity.this, "Incorrect password.", Toast.LENGTH_LONG).show();
                clearAllFields(etDeleteMLModelsPassword, etDeleteMLModelsRetypePassword);
            }
        });
        deleteMLModelsDialog.show();
    }

    /**
     * This function deletes the account of the currently logged in user.
     * If the passwords that the user has inserted are equal and they are the real password,
     * the account will be deleted.
     * @see DatabaseHelper#deleteUser(String)
     */
    private void createDeleteAccountDialog() {
        username = db.getCurrentLoggedInUsername();
        if (username.equals("")) { // If there is no username logged in, we terminate the process.
            Toast.makeText(MyProfileActivity.this, "You must be logged in to delete your account.", Toast.LENGTH_LONG).show();
            return;
        }

        deleteAccountDialog.setContentView(R.layout.delete_account_dialog);
        deleteAccountDialog.setCancelable(true);

        EditText etDeleteAccountPassword = deleteAccountDialog.findViewById(R.id.etDeleteAccountPassword);
        EditText etDeleteAccountRetypePassword = deleteAccountDialog.findViewById(R.id.etDeleteAccountRetypePassword);
        Button btnDeleteAccountConfirm = deleteAccountDialog.findViewById(R.id.btnDeleteAccountConfirm);

        btnDeleteAccountConfirm.setOnClickListener(v -> {
            String insertedPassword = etDeleteAccountPassword.getText().toString();
            String retypePassword = etDeleteAccountRetypePassword.getText().toString();
            String realPassword = db.getPasswordForUsername(username); // The real password of the user.

            if (realPassword.equals(insertedPassword) && realPassword.equals(retypePassword)) { // If the passwords are equal, and if both the inserted passwords are equal then we will delete user's account.
                db.deleteUser(username);
                username = db.getCurrentLoggedInUsername();
                updateUsernameAndPasswordTextViews();
                Toast.makeText(MyProfileActivity.this, "Your account was deleted :(", Toast.LENGTH_LONG).show();
                deleteAccountDialog.dismiss();
            }
            else {
                Toast.makeText(MyProfileActivity.this, "Incorrect password.", Toast.LENGTH_LONG).show();
                clearAllFields(etDeleteAccountPassword, etDeleteAccountRetypePassword);
            }
        });

        deleteAccountDialog.show();
    }


    /**
     * This function clears all the {@link EditText} in <code>args</code>
     * @param args The {@link EditText} that we want to clear their values.
     */
    private void clearAllFields(EditText ... args) {
        for (EditText et : args)
            et.setText("");
    }

    /**
     * This function updates the text in {@link #tvUsername} and {@link #tvPassword}
     * to be the username and the password of the currently logged in user.
     * If there is no user logged in, the fields will contain the texts:
     * <pre>
     * {@link #tvUsername} : {@link R.string#username}
     * {@link #tvPassword} : {@link R.string#password}
     * </pre>
     */
    private void updateUsernameAndPasswordTextViews() {
        username = db.getCurrentLoggedInUsername();

        if (username.equals("")) { // If the username is empty, which means there is no user logged in.
            tvUsername.setText(R.string.username);
            tvPassword.setText(R.string.password);
            tvLoginNote.setVisibility(View.VISIBLE);
        }

        else {
            tvUsername.setText(R.string.username);
            tvPassword.setText(R.string.password); // Resetting the fields.
            tvUsername.setText(tvUsername.getText().toString() + " " +  username);
            int passwordLength = db.getPasswordForUsername(username).length();
            String passwordAsterisk = passwordFromAsterisks(passwordLength);
            tvPassword.setText(tvPassword.getText().toString() + " " + passwordAsterisk); // Updating the fields.
            tvLoginNote.setVisibility(View.GONE); // There is a user logged in so we don't need this note.
        }
    }

    /**
     * This function "hides" the password using asterisks.
     * <pre>
     * {@code
     * passwordFromAsterisks(8) -> "********"
     * }
     * </pre>
     * @param passwordLength The length of the password.
     * @return Password from asterisks.
     */
    private String passwordFromAsterisks(int passwordLength) {
        StringBuilder passwordAsterisk = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            passwordAsterisk.append("*");
        }
        return passwordAsterisk.toString();
    }
}