package com.example.hirehub;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;



public class PostingActivity extends Activity {

    private EditText titleEditText, contractTypeEditText, descriptionEditText;
    private Spinner categorySpinner, sectorSpinner, citySpinner;
    private Button postButton;
    private HireHubDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        // Initialize the database helper
        dbHelper = new HireHubDbHelper(this);

        // Initialize UI components
        titleEditText = findViewById(R.id.titleEditText);
        contractTypeEditText = findViewById(R.id.contractTypeEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        sectorSpinner = findViewById(R.id.sectorSpinner);
        citySpinner = findViewById(R.id.citySpinner);
        postButton = findViewById(R.id.postButton);

        // Setup Spinners (Example for categorySpinner, do similarly for others)
        setupSpinner(categorySpinner, R.array.category_array); // Assume R.array.category_array is your category array resource

        setupSpinner(sectorSpinner, R.array.sector_array);

        setupSpinner(citySpinner, R.array.city_array);

        postButton.setOnClickListener(v -> attemptPostCreation());
    }

    private void setupSpinner(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void attemptPostCreation() {
        String title = titleEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String sector = sectorSpinner.getSelectedItem().toString();
        String contractType = contractTypeEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String city = citySpinner.getSelectedItem().toString();


        // Validation (Simplified for brevity. Add more as needed)
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title and Description are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert new post into database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HireHubContract.PostsTable.COLUMN_NAME_TITLE, title);
        values.put(HireHubContract.PostsTable.COLUMN_NAME_CATEGORY, category);
        values.put(HireHubContract.PostsTable.COLUMN_NAME_SECTOR, sector);
        values.put(HireHubContract.PostsTable.COLUMN_NAME_CONTRACT_TYPE, contractType);
        values.put(HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION, description);
        values.put(HireHubContract.PostsTable.COLUMN_NAME_CITY, city);



        long newRowId = db.insert(HireHubContract.PostsTable.TABLE_NAME, null, values);
        // Log the result of the insert operation
        Log.d("PostingActivity", "Post ID: " + newRowId);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving post.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Post created successfully.", Toast.LENGTH_SHORT).show();
            // Navigate to FeedActivity
            Intent intent = new Intent(PostingActivity.this, FeedActivity.class);
            intent.putExtra("city", city); // Pass the city to the FeedActivity
            startActivity(intent);
            finish();
            // Close this activity after posting
        }
    }
}
