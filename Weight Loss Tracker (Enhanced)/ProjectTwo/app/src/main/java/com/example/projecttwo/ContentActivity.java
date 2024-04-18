package com.example.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity {
        private DatabaseHelper dbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_main);
            dbHelper = new DatabaseHelper(this);

            // Set up button to add new data
            Button addNewDataButton = findViewById(R.id.addNewDataButton);
            addNewDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get weight and date from EditText fields
                    EditText editTextWeight = findViewById(R.id.editTextWeight);
                    EditText editTextDate = findViewById(R.id.editTextDate);
                    String weight = editTextWeight.getText().toString().trim();
                    String date = editTextDate.getText().toString().trim();

                    // Validate input
                    if (weight.isEmpty() || date.isEmpty()) {
                        Toast.makeText(ContentActivity.this, "Please enter both weight and date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Add the data to the database
                    dbHelper.addWeightEntry(1, weight, date); // Assuming user ID is 1

                    // Show a confirmation message
                    Toast.makeText(ContentActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();

                    // Clear EditText fields
                    editTextWeight.setText("");
                    editTextDate.setText("");
                }
            });

            Button viewEntriesButton = findViewById(R.id.viewEntriesButton);
            viewEntriesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start WeightEntriesActivity when the button is clicked
                    Intent intent = new Intent(ContentActivity.this, WeightEntriesActivity.class);
                    startActivity(intent);
                }
            });

            // Set up button to manage SMS permissions
            Button manageSmsPermissionsButton = findViewById(R.id.smsPermissionButton);
            manageSmsPermissionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start SmsPermissionActivity when the button is clicked
                    Intent intent = new Intent(ContentActivity.this, SmsPermissionActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

