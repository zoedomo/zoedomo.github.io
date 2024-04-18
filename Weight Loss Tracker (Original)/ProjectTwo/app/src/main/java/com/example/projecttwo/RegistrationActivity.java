package com.example.projecttwo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

public class RegistrationActivity extends AppCompatActivity {

    private EditText registrationUsernameEditText, registrationPasswordEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        registrationUsernameEditText = findViewById(R.id.registrationUsernameEditText);
        registrationPasswordEditText = findViewById(R.id.registrationPasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);

        // Set click listener for the registration button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle registration button click
                String username = registrationUsernameEditText.getText().toString();
                String password = registrationPasswordEditText.getText().toString();

                // Save the registration data to the database using dbHelper
                dbHelper.addUser(username, password);

                // Display a message to indicate successful registration
                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                // Navigate back to the login screen (MainActivity)
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the registration screen
            }
        });
    }
}
