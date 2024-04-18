package com.example.projecttwo;
import com.example.projecttwo.DatabaseHelper;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // "Create Account" button here
        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button loginButton = findViewById(R.id.loginButton);

        // Set click listeners for buttons
        loginButton.setOnClickListener(view -> handleLoginButtonClick());
    }

    private void handleLoginButtonClick() {
        // Handle login button click
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isAuthenticated = authenticateUser(username, password);

        if (isAuthenticated) {
            // Authentication successful, navigate to the content screen
            Intent intent = new Intent(this, ContentActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Authentication failed, display an error message
            Toast.makeText(this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCreateAccountButtonClick() {

    }

    private boolean authenticateUser(String username, String password) {
        // ADMIN PASSWORD AND USERNAME
        String validUsername = "user123";
        String validPassword = "password123";

        return username.equals(validUsername) && password.equals(validPassword);
    }
}
