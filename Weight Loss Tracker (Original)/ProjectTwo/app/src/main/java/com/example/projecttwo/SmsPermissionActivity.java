package com.example.projecttwo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

public class SmsPermissionActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 123;

    public void requestSmsPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);
    }

    private boolean isSmsPermissionGranted() {
        // Check if SMS permission is granted
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    // Method for handling denial of permission
    public void denyPermission(View view) {
        navigateToContentActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS permission granted
                Toast.makeText(this, "Successful! Now you will receive messages about weight goals.", Toast.LENGTH_LONG).show();
            } else {
                // SMS permission denied
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
            navigateToContentActivity();
        }
    }

    private void navigateToContentActivity() {
        // Navigate to ContentActivity
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
        finish(); // Close this activity
    }
}