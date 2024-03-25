package com.example.hirehub;


import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox keepConnectedCheckBox;
    private Button loginButton;
    private TextView textViewForgotPassword;
    private TextView signUpTextView;
    private HireHubDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the database helper
        dbHelper = new HireHubDbHelper(this);

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        keepConnectedCheckBox = findViewById(R.id.keepConnectedCheckBox);
        loginButton = findViewById(R.id.loginButton);
        textViewForgotPassword = findViewById(R.id.forgotPasswordTextView);
        signUpTextView = findViewById(R.id.signUpTextView);

        loginButton.setOnClickListener(v -> attemptLogin());

        textViewForgotPassword.setOnClickListener(v -> {
            // Navigate to ForgotPasswordActivity or similar
            Toast.makeText(LoginActivity.this, "Forgot Password functionality not implemented.", Toast.LENGTH_SHORT).show();
        });

        signUpTextView.setOnClickListener(v -> {
            // Navigate to SignUpActivity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Query the database for the email
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                HireHubContract.UsersTable.COLUMN_NAME_EMAIL,
                HireHubContract.UsersTable.COLUMN_NAME_PASSWORD
        };
        String selection = HireHubContract.UsersTable.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                HireHubContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String dbPassword = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.UsersTable.COLUMN_NAME_PASSWORD));
            if (dbPassword.equals(password)) {
                // Password matches
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("IsLoggedIn", true);
                editor.apply();
                // Navigate to PostingActivity
                Intent intent = new Intent(LoginActivity.this, PostingActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Password does not match
                Toast.makeText(this, "Password or username is incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Email not found
            Toast.makeText(this, "Password or username is incorrect", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
