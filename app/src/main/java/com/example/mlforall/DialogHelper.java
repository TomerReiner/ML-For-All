package com.example.mlforall;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class helps building the dialog and helps managing the login and logout.
 * @see R.layout#login_dialog
 * @see R.layout#sign_up_dialog
 */
public class DialogHelper {

    private final Context context;
    private DatabaseHelper db;

    /**
     * The Login Dialog.
     * @see R.layout#login_dialog
     */
    private Dialog loginDialog;

    /**
     * The Sign Up Dialog
     * @see R.layout#sign_up_dialog
     */
    private Dialog signUpDialog;

    private SharedPreferences sharedPreferences; // We will use the SharedPreferences to write the current user that is logged in to the app.
    private SharedPreferences.Editor editor;

    public DialogHelper(Context context, DatabaseHelper db, Dialog loginDialog, Dialog signUpDialog, SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.context = context;
        this.db = db;
        this.loginDialog = loginDialog;
        this.signUpDialog = signUpDialog;
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    /**
     * This function build the Login dialog.
     */
    public void buildLoginDialog() {
        loginDialog.setContentView(R.layout.login_dialog);
        loginDialog.setCancelable(true);

        EditText etLoginUsername = loginDialog.findViewById(R.id.etLoginUsername);
        EditText etLoginPassword = loginDialog.findViewById(R.id.etLoginPassword);
        Button btnLogin = loginDialog.findViewById(R.id.btnLogin);
        Button btnMoveToSignUp = loginDialog.findViewById(R.id.btnMoveToSignUp); // Getting the views in login_dialog.

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                User user = new User(username, password, null);
                boolean successfullyLoggedInUser = db.login(user);
                if (successfullyLoggedInUser) { // If the user was successfully logged in.
                    editor.putString(context.getString(R.string.current_user_logged_in), username);
                    editor.apply();
                    loginDialog.dismiss();
                }
                else { // Login Failed.
                    Toast.makeText(context, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                    etLoginUsername.setText("");
                    etLoginPassword.setText(""); // Clearing the fields.
                }
            }
        });

        btnMoveToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                createSignUpDialog();
            }
        });
        loginDialog.show();
    }

    /**
     * This function creates the sign up dialog.
     */
    private void createSignUpDialog() {
        signUpDialog.setContentView(R.layout.sign_up_dialog);
        signUpDialog.setCancelable(true);

        EditText etSignUpUsername = signUpDialog.findViewById(R.id.etSignUpUsername);
        EditText etSignUpPassword = signUpDialog.findViewById(R.id.etSignUpPassword);
        EditText etRetypePassword = signUpDialog.findViewById(R.id.etRetypePassword);
        Button btnSignUp = signUpDialog.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etSignUpUsername.getText().toString();
                String password = etSignUpPassword.getText().toString();
                String retypePassword = etRetypePassword.getText().toString();

                if (!username.equals("") && !password.equals("") && password.equals(retypePassword)) { // If all the fields are filled and the passwords are equal.
                    User user = new User(username, password, null);
                    boolean userSuccessfullySignedUp = db.addUser(user);
                    if (userSuccessfullySignedUp) { // If the user was successfully signed up.
                        editor.putString(context.getString(R.string.current_user_logged_in), username);
                        editor.apply();
                        signUpDialog.dismiss();
                        loginDialog.dismiss();
                    }
                    else {
                        Toast.makeText(context, "Error signing up. Please try another username.", Toast.LENGTH_LONG).show();
                        etSignUpUsername.setText("");
                        etSignUpPassword.setText("");
                        etRetypePassword.setText(""); // Clearing the fields.
                    }
                }
                else { // If not all of the fields are filled.
                    Toast.makeText(context, "Please make sure that all the fields are filled and that the passwords are equal.", Toast.LENGTH_LONG).show();
                    etSignUpUsername.setText("");
                    etSignUpPassword.setText("");
                    etRetypePassword.setText(""); // Clearing the fields.
                }
            }
        });
        signUpDialog.show();
    }
}
