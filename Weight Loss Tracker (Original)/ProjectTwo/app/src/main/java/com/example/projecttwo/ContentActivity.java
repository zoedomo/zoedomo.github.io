package com.example.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    public void addNewData(View view) {
    }
    public void editRow(View view) {

    }
    public void deleteRow(View view) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        dbHelper = new DatabaseHelper(this);

        // Set welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome to the Weight Loss App!");

        // Set up button to access SMS Permission page
        Button smsPermissionButton = findViewById(R.id.smsPermissionButton);
        smsPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start SMS Permission Activity
                Intent intent = new Intent(ContentActivity.this, SmsPermissionActivity.class);
                startActivity(intent);
            }
        });
    }
}