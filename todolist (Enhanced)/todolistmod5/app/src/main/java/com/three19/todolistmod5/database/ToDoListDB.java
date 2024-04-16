package com.three19.todolistmod5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.three19.todolistmod5.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoListDB extends DBConnection {

    // Constructor
    public ToDoListDB(Context context) {
        super(context);
    }

    // Method to add a new task
    public ToDo add(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ToDo toDo = new ToDo();
        toDo.setName(name);

        try {
            ContentValues values = toDo.getContentValuesToAdd();
            long id = db.insertOrThrow("todolist", null, values);
            toDo.setId((int) id);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error or notify user
        } finally {
            db.close();
        }

        return toDo;
    }

    // Method to update an existing task
    public void update(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = toDo.getContentValuesToUpdate();
        db.update("todolist", values, "id = ?", new String[]{String.valueOf(toDo.getId())});
        db.close();
    }

    // Method to remove a task by its ID
    public void remove(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("todolist", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to clear all tasks from the database
    public void clearAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("todolist", null, null); // No need for a where clause
        db.close();
    }

    // Method to retrieve details of a task by its ID
    public ToDo getDetails(int id) {
        String selectQuery = "SELECT * FROM campsites WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        ToDo toDo = new ToDo();
        toDo.parse(cursor);

        db.close();
        return toDo;
    }

    // Method to retrieve a list of all tasks
    public List<ToDo> getList() {
        List<ToDo> toDoList = new ArrayList<>();

        String selectQuery = "SELECT * FROM todolist";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.parse(cursor);
                toDoList.add(toDo);
            } while (cursor.moveToNext());
        }

        db.close();
        return toDoList;
    }
}

