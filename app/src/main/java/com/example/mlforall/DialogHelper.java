package com.example.mlforall;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class helps building the dialog.
 * @see R.layout#login_dialog
 * @see R.layout#sign_up_dialog
 */
public class DialogHelper {

    private Context context;
    private DatabaseHelper db;

    public DialogHelper(Context context, DatabaseHelper db) {
        this.context = context;
        this.db = db;
    }

    /**
     * This function build the Login/Sign Up dialog.
     * @return {@link Dialog} the Login/Sign Up dialog.
     */
    public Dialog buildDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.login_dialog);
        dialog.setCancelable(true);

        EditText etLoginUsername = dialog.findViewById(R.id.etLoginUsername);
        EditText etLoginPassword = dialog.findViewById(R.id.etLoginPassword);
        Button btnLogin = dialog.findViewById(R.id.btnLogin);
        Button btnMoveToSignUp = dialog.findViewById(R.id.btnMoveToSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                //TODO-Login
            }
        });

        btnMoveToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                createSignUpDialog();
            }
        });
        return dialog;
    }

    /**
     * This function creates the sign up dialog.
     */
    private void createSignUpDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.sign_up_dialog);
        dialog.setCancelable(true);

        EditText etSignUpUsername = dialog.findViewById(R.id.etSignUpUsername);
        EditText etSignUpPassword = dialog.findViewById(R.id.etSignUpPassword);
        EditText etRetypePassword = dialog.findViewById(R.id.etRetypePassword);
        Button btnSignUp = dialog.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etSignUpUsername.getText().toString();
                String password = etSignUpPassword.getText().toString();
                String retypePassword = etRetypePassword.getText().toString();

                if (password.equals(retypePassword)) { // If the passwords are equal then we can proceed in the sign up.
                    User user = new User(username, password, null);
                    boolean hasUserSuccessfullyInserted = db.addUser(user);
                    if (hasUserSuccessfullyInserted) // The username was inserted successfully.
                        dialog.dismiss();

                    else { // The username was not inserted successfully.
                        Toast.makeText(context, "Error signing up. Please make sure that all the fields are filled and maybe try another username.", Toast.LENGTH_LONG).show();
                        etSignUpUsername.setText("");
                        etSignUpPassword.setText("");
                        etRetypePassword.setText(""); // Clearing the fields.
                    }
                }
                else {
                    Toast.makeText(context, "The passwords are not equal. In order to sign up the passwords must be equal", Toast.LENGTH_LONG).show();
                    etSignUpUsername.setText("");
                    etSignUpPassword.setText("");
                    etRetypePassword.setText(""); // Clearing the fields.
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
