package com.example.mlforall;

import android.app.Dialog;
import android.content.Context;
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
    private final DatabaseHelper db;

    /**
     * The Login Dialog.
     * @see R.layout#login_dialog
     */
    private final Dialog loginDialog;

    /**
     * The Sign Up Dialog
     * @see R.layout#sign_up_dialog
     */
    private final Dialog signUpDialog;

    public DialogHelper(Context context, DatabaseHelper db, Dialog loginDialog, Dialog signUpDialog) {
        this.context = context;
        this.db = db;
        this.loginDialog = loginDialog;
        this.signUpDialog = signUpDialog;

    }

    /**
     * This function build the Login dialog.
     * @return The name of the user that signed up.
     */
    public String buildLoginDialog() {
        loginDialog.setContentView(R.layout.login_dialog);

        loginDialog.setCancelable(true);

        EditText etLoginUsername = loginDialog.findViewById(R.id.etLoginUsername);
        EditText etLoginPassword = loginDialog.findViewById(R.id.etLoginPassword);
        Button btnLogin = loginDialog.findViewById(R.id.btnLogin);
        Button btnMoveToSignUp = loginDialog.findViewById(R.id.btnMoveToSignUp); // Getting the views in login_dialog.

        final String[] username = {""};

        btnLogin.setOnClickListener(v -> {
            username[0] = etLoginUsername.getText().toString();
            String password = etLoginPassword.getText().toString();
            User user = new User(username[0], password);

            boolean successfullyLoggedInUser = db.login(user);
            if (successfullyLoggedInUser) { // If the user was successfully logged in.
                Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show(); // Greeting the user for signing in.
                loginDialog.dismiss();
            }
            else { // Login Failed.
                Toast.makeText(context, "Incorrect username or password.", Toast.LENGTH_LONG).show();
                etLoginUsername.setText("");
                etLoginPassword.setText(""); // Clearing the fields.
            }
        });
        btnMoveToSignUp.setOnClickListener(v -> {
            loginDialog.dismiss();
            username[0] = createSignUpDialog();
        });
        loginDialog.show();
        return username[0];
    }

    /**
     * This function creates the sign up dialog.
     * @return The name of the username that signed up.
     */
    private String createSignUpDialog() {
        signUpDialog.setContentView(R.layout.sign_up_dialog);
        signUpDialog.setCancelable(true);

        EditText etSignUpUsername = signUpDialog.findViewById(R.id.etSignUpUsername);
        EditText etSignUpPassword = signUpDialog.findViewById(R.id.etSignUpPassword);
        EditText etRetypePassword = signUpDialog.findViewById(R.id.etRetypePassword);
        Button btnSignUp = signUpDialog.findViewById(R.id.btnSignUp);

        final String[] username = {""};
        btnSignUp.setOnClickListener(v -> {
            username[0] = etSignUpUsername.getText().toString();
            String password = etSignUpPassword.getText().toString();
            String retypePassword = etRetypePassword.getText().toString();

            if (!username[0].equals("") && !password.equals("") && password.equals(retypePassword)) { // If all the fields are filled and the passwords are equal.
                User user = new User(username[0], password);
                boolean userSuccessfullySignedUp = db.addUser(user);
                if (userSuccessfullySignedUp) { // If the user was successfully signed up.
                    Toast.makeText(context, "Welcome To My App!", Toast.LENGTH_SHORT).show(); // Greeting the user for signing up.
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
        });
        signUpDialog.show();
        return username[0];
    }
}
