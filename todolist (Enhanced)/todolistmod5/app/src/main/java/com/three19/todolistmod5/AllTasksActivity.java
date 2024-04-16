package com.three19.todolistmod5;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.three19.todolistmod5.database.ToDoListDB;
import com.three19.todolistmod5.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class AllTasksActivity extends AppCompatActivity {

    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        setTitle("All Tasks");

        // Load tasks asynchronously to avoid blocking the UI thread
        loadTasks();

        Button backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });
    }

    // Method to load tasks from the database asynchronously
    private void loadTasks() {
        new AsyncTask<Void, Void, List<ToDo>>() {
            @Override
            protected List<ToDo> doInBackground(Void... voids) {
                ToDoListDB toDoListDB = new ToDoListDB(AllTasksActivity.this);
                return toDoListDB.getList();
            }

            @Override
            protected void onPostExecute(List<ToDo> tasks) {
                // Update UI with the retrieved tasks
                if (tasks != null) {
                    updateListView(tasks);
                } else {
                    showToast("Failed to load tasks.");
                }
            }
        }.execute();
    }

    // Method to update the ListView with tasks
    private void updateListView(List<ToDo> tasks) {
        adapter = new ToDoListAdapter(this, (ArrayList<ToDo>) tasks);
        ListView listView = findViewById(R.id.lstView);
        listView.setAdapter(adapter);
    }

    // Method to navigate back to MainActivity
    private void navigateToMainActivity() {
        Intent intent = new Intent(AllTasksActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent stacking
    }

    // Method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

