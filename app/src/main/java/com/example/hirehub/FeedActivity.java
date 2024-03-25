package com.example.hirehub;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FeedActivity extends Activity implements PostsAdapter.PostClickListener {
    private RecyclerView postsRecyclerView;
    private HireHubDbHelper dbHelper;
    private PostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        dbHelper = new HireHubDbHelper(this);
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String userCity = sharedPref.getString("userCity", null);

        if (userCity != null) {
            loadPostsByCity(userCity);
        } else {
            loadAllPosts();
        }
    }

    @Override
    public void onPostClick(Post post) {
        // Logic to execute when a post is clicked
        Toast.makeText(this, "Post clicked: " + post.getTitle(), Toast.LENGTH_SHORT).show();
    }
    private void loadAllPosts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Post> posts = new ArrayList<>();

        // SQL to join Posts and Users table and select all posts
        String sql = "SELECT Posts.title, Posts.description, Users.email FROM " + HireHubContract.PostsTable.TABLE_NAME +
                " INNER JOIN " + HireHubContract.UsersTable.TABLE_NAME +
                " ON Posts.user_id = Users._id";

        Cursor cursor = db.rawQuery(sql, null); // No selectionArgs needed since we're not filtering

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            posts.add(new Post(userEmail, title, description));
        }
        cursor.close();

        adapter = new PostsAdapter(this, posts, this);
        postsRecyclerView.setAdapter(adapter);

        if (posts.isEmpty()) {
            Log.d("FeedActivity", "No posts found.");
        }
    }

    private void loadPostsByCity(String city) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Post> posts = new ArrayList<>();

        String[] projection = {
                HireHubContract.PostsTable.COLUMN_NAME_TITLE,
                HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION,
                // Include more columns as needed
        };

        String selection = HireHubContract.PostsTable.COLUMN_NAME_CITY + " = ?";
        String[] selectionArgs = { city };

        Cursor cursor = db.query(
                HireHubContract.PostsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.PostsTable.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION));
            // Assuming you want to display the email (or another identifier) from the user who posted,
            // you need to join the Users table and fetch that data.
            posts.add(new Post("UserEmail", title, description)); // Replace "UserEmail" with the actual data
        }
        cursor.close();

        adapter = new PostsAdapter(this, posts, this);
        postsRecyclerView.setAdapter(adapter);

        if (posts.isEmpty()) {
            Log.d("FeedActivity", "No posts found for city: " + city);
        }
    }
/*
    private void loadAllPosts() {
        // Similar to loadPostsByCity but without the city filter.
        // You can combine both methods for brevity by adjusting the selection and selectionArgs based on whether a city is provided.
    }
    private void loadPostsByCity(String city) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Post> posts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT title, description FROM posts WHERE city = ?", new String[]{city});

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            posts.add(new Post("Default User", title, description)); // Use a default username for simplicity
        }
        cursor.close();

        adapter = new PostsAdapter(this, posts, this);
        postsRecyclerView.setAdapter(adapter);
    }
*/
private String getUserCity(int userId) {
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    String[] projection = { HireHubContract.UsersTable.COLUMN_NAME_CITY };
    String selection = HireHubContract.UsersTable.COLUMN_NAME_USER_ID + " = ?";
    String[] selectionArgs = { String.valueOf(userId) };

    Cursor cursor = db.query(
            HireHubContract.UsersTable.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
    );

    String city = null;
    if (cursor.moveToFirst()) {
        city = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.UsersTable.COLUMN_NAME_CITY));
    }
    cursor.close();
    return city;
}

    private int getPostsCountInCity(String city) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { "COUNT(*) AS count" };
        String selection = HireHubContract.PostsTable.COLUMN_NAME_CITY + " = ?";
        String[] selectionArgs = { city };

        Cursor cursor = db.query(
                HireHubContract.PostsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0); // Get the count from the first column
        }
        cursor.close();
        return count;
    }
}
