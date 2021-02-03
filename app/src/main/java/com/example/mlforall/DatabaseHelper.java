package com.example.mlforall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final int VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s (%s TEXT PRIMARY KEY, %s TEXT NOT NULL);", USERS_TABLE_NAME, USERNAME, PASSWORD);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * This function add a user to the database.
     * @param user The user that we want to add to the database.
     * @return <code>true</code> if the user was successfully inserted, <code>false</code>.
     */
    public boolean addUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            boolean isUserAlreadyExists = this.userNameAlreadyExists(username);

            if (isUserAlreadyExists) // If the user already exists.
                return false;
            // If we are here then it means the is no user with that username.
            ContentValues cv = new ContentValues();
            cv.put(USERNAME, username);
            cv.put(PASSWORD, password);
            db.insert(USERS_TABLE_NAME, null, cv);
        }
        catch (Exception e) { // If the insertion of the user failed.
            return false;
        }
        db.close();
        return true;
    }

    /**
     * This function logs the user in.
     * @param user The user that we want to login.
     * @return <code>true</code> if the user was successfully logged in, <code>false</code> if not.
     */
    public boolean login(User user) {
        String query = String.format("SELECT %s, %s FROM %s;", USERNAME, PASSWORD, USERS_TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();

        String username = user.getUsername();
        String password = user.getPassword();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String currentUsername = cursor.getString(cursor.getColumnIndex(USERNAME));
                String currentPassword = cursor.getString(cursor.getColumnIndex(PASSWORD));

                if (username.equals(currentUsername) && password.equals(currentPassword)) // If the user was found.
                    return true;
            }
        }
        cursor.close();
        db.close();
        return false; // No matching user was found.
    }

    /**
     * This function checks if a username already exists in {@link #USERNAME} columns.
     * @param username The username that we want to check if he exists in the database.
     * @return <code>true</code> if the username already exists, <code>false</code> if not.
     */
    private boolean userNameAlreadyExists(String username) {
        String query = String.format("SELECT %s FROM %s", USERNAME, USERS_TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String currentUsername = cursor.getString(cursor.getColumnIndex(USERNAME));

                if (username.equals(currentUsername)) // If the username was found.
                    return true;
            }
        }
        cursor.close();
        db.close();
        return false; // No username was found.

    }
}
