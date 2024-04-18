package com.example.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

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
                handleRegisterButtonClick();
            }
        });
    }

    private void handleRegisterButtonClick() {
        // Handle registration button click
        String username = registrationUsernameEditText.getText().toString();
        String password = registrationPasswordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            // Display a message if username or password is empty
            Toast.makeText(RegistrationActivity.this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
        } else {
            // Save the registration data to the database using dbHelper
            dbHelper.addUser(username, password);

            // Display a message to indicate successful registration
            Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Navigate back to the login screen (MainActivity)
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the registration screen
        }
    }
}
