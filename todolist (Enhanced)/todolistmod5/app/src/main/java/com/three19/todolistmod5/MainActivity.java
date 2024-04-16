package com.three19.todolistmod5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.three19.todolistmod5.database.ToDoListDB;
import com.three19.todolistmod5.model.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// MainActivity class responsible for handling the main interface of the app
public class MainActivity extends AppCompatActivity {

    // Variables declaration
    ToDoListDB toDoListDB; // Database handler
    List<ToDo> arrayList; // List of tasks
    ToDoListAdapter adapter; // Adapter for the ListView
    ToDo selectedToDo; // Selected task
    int selectedPosition; // Position of selected task
    EditText txtName; // EditText for task name
    Button addBtn; // Button for adding/updating task

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout

        // Initialize UI components
        txtName = (EditText) findViewById(R.id.txtName);
        addBtn = (Button) findViewById(R.id.btnAdd);

        // Initialize database handler and retrieve list of tasks
        toDoListDB = new ToDoListDB(this);
        arrayList = toDoListDB.getList();

        // Initialize adapter with the list of tasks
        adapter = new ToDoListAdapter(this, (ArrayList<ToDo>) arrayList);

        // Set adapter to ListView to display tasks
        ListView listView = (ListView) findViewById(R.id.lstView);
        listView.setAdapter(adapter);

        // Long press listener for deleting tasks
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                removeItemFromList(position); // Call method to remove task
                return true;
            }
        });

        // Click listener for updating task details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedToDo = arrayList.get(position); // Get selected task
                selectedPosition = position;
                txtName.setText(selectedToDo.getName()); // Set task name in EditText

                // Change button text to "Update" and set click listener to update task
                addBtn.setText("Update");
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get task name from EditText and update database
                        String name = txtName.getText().toString();
                        selectedToDo.setName(name);
                        toDoListDB.update(selectedToDo);
                        // Update list and reset UI
                        arrayList.set(selectedPosition, selectedToDo);
                        adapter.notifyDataSetChanged();
                        reset();
                    }
                });

            }
        });

        // Click listener for adding new task
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = txtName.getText().toString();

                // Check if a task is selected or not
                if (selectedToDo == null) {
                    // If no task selected, add new task to database
                    ToDo newToDo = new ToDo();
                    newToDo.setName(name);
                    toDoListDB.add(name);
                    // Update list and notify adapter
                    arrayList.add(newToDo);
                    adapter.notifyDataSetChanged();
                } else {
                    // If task selected, update task in database
                    selectedToDo.setName(name);
                    toDoListDB.update(selectedToDo);
                    // Update list and notify adapter
                    arrayList.set(selectedPosition, selectedToDo);
                    adapter.notifyDataSetChanged();
                    reset();
                }
                // Clear the text in EditText after adding the task
                txtName.setText("");
            }
        });

        // Click listener for clearing all tasks
        Button clearBtn = (Button) findViewById(R.id.btnClear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Clear all tasks from the database
                toDoListDB.clearAllTasks();
                // Clear the list and update the adapter
                arrayList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        // Click listener for navigating to AllTasksActivity
        Button allBtn = (Button) findViewById(R.id.btnAll);
        allBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AllTasksActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to remove a task from the list and database
    protected void removeItemFromList(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        // Alert dialog to confirm task deletion
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get selected task and remove from list
                ToDo toDo = arrayList.get(position);
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
                // Remove task from database and reset UI
                toDoListDB.remove(toDo.getId());
                reset();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss dialog
            }
        });

        alert.show(); // Show alert dialog
    }

    // Method to reset UI components
    protected void reset() {
        txtName.setText(""); // Clear EditText
        addBtn.setText("Add"); // Reset button text
        selectedToDo = null; // Reset selected task
        selectedPosition = -1; // Reset selected position
    }
}

// Custom adapter class for displaying tasks in ListView
class ToDoListAdapter extends ArrayAdapter<ToDo> {

    public ToDoListAdapter(Context context, ArrayList<ToDo> toDoList) {
        super(context, 0, toDoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDo toDo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(android.R.id.text1);
        name.setText(toDo.getName()); // Set task name in TextView


        return convertView;
    }
}