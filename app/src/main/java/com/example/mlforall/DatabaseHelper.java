package com.example.mlforall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String USERS_TABLE_NAME = "users";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CURRENTLY_LOGGED_IN = "currentlyLoggedIn";
    public static final String SLOPE = "slope";
    public static final String INTERCEPT = "intercept";
    public static final String SCORE = "score";
    public static final int VERSION = 1;
    public static final int LOGGED_IN = 1;
    public static final int LOGGED_OUT = 0;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s (%s TEXT PRIMARY KEY, %s TEXT NOT NULL, %s INTEGER NOT NULL);", USERS_TABLE_NAME, USERNAME, PASSWORD, CURRENTLY_LOGGED_IN);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * This function add a user to the database and create machine learning models table for him.
     * @param user The user that we want to add to the database.
     * @return <code>true</code> if the user was successfully inserted, <code>false</code>.
     */
    public boolean addUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            boolean isUserAlreadyExists = this.userNameAlreadyExists(username);
            boolean isUsernameValid = this.isUsernameValid(username);
            if (isUserAlreadyExists && !isUsernameValid)  // If the user already exists or not valid.
                return false;

            // If we are here then it means the is no user with that username.
            ContentValues cv = new ContentValues();
            cv.put(USERNAME, username);
            cv.put(PASSWORD, password);
            cv.put(CURRENTLY_LOGGED_IN, LOGGED_IN);
            db.insert(USERS_TABLE_NAME, null, cv);

            String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s REAL NOT NULL, %s REAL NOT NULL, %s REAL NOT NULL);", username, SLOPE, INTERCEPT, SCORE); // Creating data table for each user.
            db.execSQL(query);

            db.close();
        }
        catch (Exception e) { // If the insertion of the user failed.
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This function logs the user in.
     * @param user The user that we want to login.
     * @return <code>true</code> if the user was successfully logged in, <code>false</code> if not.
     */
    public boolean login(User user) {
        String query = String.format("SELECT %s, %s FROM %s;", USERNAME, PASSWORD, USERS_TABLE_NAME);
        SQLiteDatabase dbToRead = this.getReadableDatabase();
        SQLiteDatabase dbToWrite = this.getWritableDatabase();

        String username = user.getUsername();
        String password = user.getPassword();

        Cursor cursor = dbToRead.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String currentUsername = cursor.getString(cursor.getColumnIndex(USERNAME));
                String currentPassword = cursor.getString(cursor.getColumnIndex(PASSWORD));

                if (username.equals(currentUsername) && password.equals(currentPassword)) { // If the user was found.
                    ContentValues cv = new ContentValues();
                    cv.put(CURRENTLY_LOGGED_IN, LOGGED_IN);
                    dbToWrite.update(USERS_TABLE_NAME, cv, USERNAME + " = ?", new String[]{username}); // Updating the logged in status.

                    cursor.close();
                    dbToRead.close();
                    dbToWrite.close();
                    return true;
                }
            }
        }
        return false; // No matching user was found.
    }

    /**
     * This function checks if a username already exists in {@link #USERNAME} columns.
     * @param username The username that we want to check if he exists in the database.
     * @return <code>true</code> if the username already exists, <code>false</code> if not.
     */
    private boolean userNameAlreadyExists(String username) {
        String query = String.format("SELECT %s FROM %s;", USERNAME, USERS_TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String currentUsername = cursor.getString(cursor.getColumnIndex(USERNAME));
                if (username.equals(currentUsername)) { // If the username was found.
                    cursor.close();
                    db.close();
                    return true;
                }
            }
        }
        return false; // No username was found.
    }

    /**
     * This function check if a username is valid(contains only uppercase and lowercase english letters, numbers and underscore).
     * Valid username must begin with a letter or underscore.
     * <pre>
     * {@code
     * isUsernameValid("_Tomer") -> true
     * isUsernameValid("12345") -> false
     * isUsernameValid("Tomer123") -> true
     * }
     * </pre>
     * @param username The username that we want to check if its valid.
     * @return <code>true</code> if the username is valid, <code>false</code> if not.
     */
    private boolean isUsernameValid(String username) {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "1234567890";
        String underscore = "_";
        String validCharacters = uppercaseLetters + lowercaseLetters + numbers + underscore;

        String usernameFirstLetter = "" + username.charAt(0);

        if (numbers.contains(usernameFirstLetter)) // If the username begins with numbers.
            return false;

        int usernameLength = username.length();

        for (int i = 0; i < usernameLength; i++) {
            if (!validCharacters.contains("" + username.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * This function deletes the user from {@link #USERS_TABLE_NAME}.
     * @param username The username of the user that we want to delete.
     * @return <code>true</code> if the user was successfully deleted, <code>false</code> if not.
     * @see #onCreate(SQLiteDatabase)
     */
    private boolean deleteUserFromUsers(String username) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(USERS_TABLE_NAME, USERNAME + " = ?", new String[]{username});
            db.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This function deletes the user's data table.
     * @param username The username of the user that we want to delete his data.
     * @return <code>true</code> if the user's data was successfully deleted, <code>false</code> if not.
     * @see #addUser(User)
     */
    public boolean deleteUserTable(String username) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = String.format("DROP TABLE %s;", username);
            db.execSQL(query);
            db.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This function deletes a user from the database.
     * @param username The username of the user that we want to delete.
     * @return <code>true</code> if the user was successfully deleted form the database, <code>false</code> if not.
     * @see #deleteUser(String)
     * @see #deleteUserFromUsers(String)
     */
    public boolean deleteUser(String username) {
        return this.deleteUserFromUsers(username) && this.deleteUserTable(username);
    }

    /**
     * This function logs the user out.
     * @param username The name of the user that we want to log out.
     * @return <code>true</code> if the user was successfully logged out, <code>false</code> if not.
     */
    public boolean logOut(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(CURRENTLY_LOGGED_IN, LOGGED_OUT);
            db.update(USERS_TABLE_NAME, cv, USERNAME + " = ?", new String[]{username});
            db.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * This function returns the username that is currently logged in.
     * @return the username of the user that is currently logged in, <code>""</code> if there is no user logged in.
     */
    public String getCurrentLoggedInUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s;", USERS_TABLE_NAME);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String currentUsername = cursor.getString(cursor.getColumnIndex(USERNAME));
                int currentLoggedInStatus = cursor.getInt(cursor.getColumnIndex(CURRENTLY_LOGGED_IN));
                if (currentLoggedInStatus == LOGGED_IN) // If the current logged in user was found.
                    return currentUsername;
            }
        }
        return ""; // If there is no user logged in it means the table is empty.
    }

    /**
     * This function deletes all the data from the user's data table(the table that has the user's username).
     * @param username The username of the user that we want to delete his data.
     * @return <code>true</code> if the user's data table was successfully deleted, <code>false</code> if not.
     */
    public boolean deleteUserData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM %s;", username);
        try {
            db.execSQL(query);
            db.close();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This function adds a model to the user's data table.
     * @param username The username of the user that we want to add data for his table.
     * @param equation The linear equation that was built for the data the user has loaded.
     */
    public void addModel(String username, LinearEquation equation, double score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SLOPE, equation.getSlope());
        cv.put(INTERCEPT, equation.getIntercept());
        cv.put(SCORE, score);
        db.insert(username, null, cv);
        db.close();
    }

    /**
     * This function
     * @param username
     * @return
     */
    public String requestData(String username) {
        return null; // TODO-complete
    }
}
