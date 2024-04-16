package com.three19.todolistmod5.model;

import android.content.ContentValues;
import android.database.Cursor;

public class ToDo {
    private int _id;
    private String _name;

    // Getters and setters for _id and _name
    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }

    // Method to create ContentValues for adding a new task
    public ContentValues getContentValuesToAdd() {
        ContentValues values = new ContentValues();
        values.put("name", _name);
        return values;
    }

    // Method to create ContentValues for updating an existing task
    public ContentValues getContentValuesToUpdate() {
        ContentValues values = new ContentValues();
        values.put("name", _name);
        return values;
    }

    // Method to parse data from a Cursor
    public void parse(Cursor cursor) {
        if (cursor != null) {
            setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
    }
}

