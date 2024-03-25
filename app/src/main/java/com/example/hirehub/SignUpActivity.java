package com.example.hirehub;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Spinner citySpinner;
    private RadioGroup employmentStatusRadioGroup;
    private Button createAccountButton;
    private TextView termsTextView;
    private HireHubDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize the database helper
        dbHelper = new HireHubDbHelper(this);

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        citySpinner = findViewById(R.id.citySpinner);
        employmentStatusRadioGroup = findViewById(R.id.employmentStatusRadioGroup);
        createAccountButton = findViewById(R.id.createAccountButton);
        termsTextView = findViewById(R.id.termsTextView);

        // Setup city spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item); // city_array is a placeholder, replace with your actual array resource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        // Create account button click listener
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAccountCreation();
            }
        });
    }

    private void attemptAccountCreation() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String city = citySpinner.getSelectedItem().toString();
        int selectedId = employmentStatusRadioGroup.getCheckedRadioButtonId();
        RadioButton employmentStatusButton = findViewById(selectedId);
        String employmentStatus = employmentStatusButton.getText().toString();

        // Validate input fields
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 5) {
            Toast.makeText(this, "Password must be at least 5 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert new user into database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HireHubContract.UsersTable.COLUMN_NAME_EMAIL, email);
        values.put(HireHubContract.UsersTable.COLUMN_NAME_PASSWORD, password);
        values.put(HireHubContract.UsersTable.COLUMN_NAME_CITY, city);
        values.put(HireHubContract.UsersTable.COLUMN_NAME_EMPLOYMENT_STATUS, employmentStatus);

        long newRowId = db.insert(HireHubContract.UsersTable.TABLE_NAME, null, values);
        if (newRowId == -1) {
            // If the row ID is -1, there was an error with insertion.
            Toast.makeText(this, "Error with saving user.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User created successfully.", Toast.LENGTH_SHORT).show();
            // Navigate to PostingActivity or LoginActivity as appropriate
            finish(); // Close this activity
        }
    }
}
