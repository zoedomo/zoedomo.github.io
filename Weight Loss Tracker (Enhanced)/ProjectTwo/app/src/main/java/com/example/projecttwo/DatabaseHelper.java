package com.example.projecttwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "WeightLossDB";

    // Table names and column names for users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Table names and column names for weight entries
    public static final String TABLE_WEIGHT_ENTRIES = "weight_entries";
    private static final String COLUMN_WEIGHT_ENTRY_ID = "entry_id";
    private static final String COLUMN_USER_ID_FK = "user_id_fk"; // Foreign key linking to users table
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create weight entries table
        String CREATE_WEIGHT_ENTRIES_TABLE = "CREATE TABLE " + TABLE_WEIGHT_ENTRIES + "("
                + COLUMN_WEIGHT_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID_FK + " INTEGER,"
                + COLUMN_WEIGHT + " REAL,"
                + COLUMN_DATE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" + ")";
        db.execSQL(CREATE_WEIGHT_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Add a new weight entry to the weight entries table
    public void addWeightEntry(int userId, String weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_DATE, date);
        db.insert(TABLE_WEIGHT_ENTRIES, null, values);
        db.close();
    }

    // Get all weight entries
    public Cursor getAllWeightEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT entry_id AS _id, weight, date FROM " + TABLE_WEIGHT_ENTRIES, null);
    }

    // Check if a user with the given username and password exists
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return userExists;
    }
    // Add a new user to the users table
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    // Method to update a weight entry in the database
    public void updateWeightEntry(long entryId, double newWeight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, newWeight);

        // Updating row
        db.update(TABLE_WEIGHT_ENTRIES, values, COLUMN_WEIGHT_ENTRY_ID + " = ?", new String[]{String.valueOf(entryId)});
        db.close();
    }
    public void deleteWeightEntry(long entryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEIGHT_ENTRIES, COLUMN_WEIGHT_ENTRY_ID + "=?", new String[]{String.valueOf(entryId)});
        db.close();
    }

}
