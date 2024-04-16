package com.three19.todolistmod5.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 5;
    // Database name
    private static final String DATABASE_NAME = "data";

    // Constructor
    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method called when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "todolist" table with columns for task IDs and names
        db.execSQL("CREATE TABLE todolist (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)");
    }

    // Method called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing "todolist" table if it exists
        db.execSQL("DROP TABLE IF EXISTS todolist");
        // Recreate the "todolist" table
        onCreate(db);
    }
}
