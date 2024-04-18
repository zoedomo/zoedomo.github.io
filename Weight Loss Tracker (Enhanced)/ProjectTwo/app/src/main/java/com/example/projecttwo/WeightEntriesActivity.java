package com.example.projecttwo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WeightEntriesActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView weightEntriesListView;
    private SimpleCursorAdapter adapter;
    private Button btnEdit;
    private Button btnDelete;
    private long selectedEntryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_entries);
        dbHelper = new DatabaseHelper(this);

        // Initialize ListView
        weightEntriesListView = findViewById(R.id.weightEntriesListView);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Load weight entries from the database
        loadWeightEntries();

        // Set up the onItemClick listener for the ListView
        weightEntriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the clicked item's ID
                Cursor cursor = (Cursor) adapter.getItem(position);
                selectedEntryId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

                // Check if an action (edit or delete) is selected
                if (btnEdit.getVisibility() == View.VISIBLE) {
                    editWeightEntry(selectedEntryId);
                } else if (btnDelete.getVisibility() == View.VISIBLE) {
                    showDeleteConfirmationDialog(selectedEntryId);
                }
            }
        });
    }

    // Method to load weight entries from the database and display them in the ListView
    private void loadWeightEntries() {
        Cursor cursor = dbHelper.getAllWeightEntries();
        if (cursor != null) {
            // Setup adapter
            String[] fromColumns = {DatabaseHelper.COLUMN_WEIGHT, DatabaseHelper.COLUMN_DATE};
            int[] toViews = {android.R.id.text1, android.R.id.text2}; // Android's built-in layout for simple_list_item_2
            adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, fromColumns, toViews, 0);

            // Bind adapter to ListView
            weightEntriesListView.setAdapter(adapter);
        }
    }

    // Method to show a confirmation dialog before deleting a weight entry
    private void showDeleteConfirmationDialog(final long entryId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call the delete method in DatabaseHelper to delete the entry
                dbHelper.deleteWeightEntry(entryId);

                // Refresh the ListView
                loadWeightEntries();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    // Method to handle editing a weight entry
    private void editWeightEntry(long entryId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Entry");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newWeight = input.getText().toString().trim();
                if (!newWeight.isEmpty()) {
                    // Update the entry with the new weight
                    dbHelper.updateWeightEntry(entryId, Double.parseDouble(newWeight));

                    // Refresh the ListView
                    loadWeightEntries();
                } else {
                    Toast.makeText(WeightEntriesActivity.this, "Please enter a valid weight.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Method to handle click event of the Edit button
    public void editButtonClicked(View view) {
        // Show edit confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Edit");
        builder.setMessage("Are you sure you want to edit this entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Proceed with editing
                editWeightEntry(selectedEntryId);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    // Method to handle click event of the Delete button
    public void deleteButtonClicked(View view) {
        // Show delete button and hide edit button
        btnDelete.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
    }
}
