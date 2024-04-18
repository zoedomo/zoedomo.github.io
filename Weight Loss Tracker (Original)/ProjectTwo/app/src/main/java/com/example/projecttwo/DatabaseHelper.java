package com.example.projecttwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyAppDB";

    // Table names and column names
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade database when needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // CRUD operations (insert, read, update, delete) methods

    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Check if a user with the given username and password exists in the database
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + "=?" + " AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                null, null, null);

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return userExists;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD},
                COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);

            if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1) {
                user = new User();
                user.setId(cursor.getInt(idIndex));
                user.setUsername(cursor.getString(usernameIndex));
                user.setPassword(cursor.getString(passwordIndex));
            }
        }
        cursor.close();
        db.close();
        return user;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());

        return db.update(TABLE_USERS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Method to update a user's weight
    public int updateUserWeight(int userId, String newWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("weight", newWeight);

        // Updating row
        return db.update(TABLE_USERS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)});
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }
}
// COULD NOT FIGURE OUT HOW TO GET THE DATABASE CONNECTED
// AFTER HOURS AND MANY YOUTUBE VIDEOS. I'M VERY SORRY!