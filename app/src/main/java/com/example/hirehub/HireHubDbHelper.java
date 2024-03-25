package com.example.hirehub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;


public class HireHubDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HireHub.db";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + HireHubContract.UsersTable.TABLE_NAME + " (" +
                    HireHubContract.UsersTable.COLUMN_NAME_USER_ID + " INTEGER PRIMARY KEY," +
                    HireHubContract.UsersTable.COLUMN_NAME_EMAIL + " TEXT," +
                    HireHubContract.UsersTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    HireHubContract.UsersTable.COLUMN_NAME_CITY + " TEXT," +
                    HireHubContract.UsersTable.COLUMN_NAME_EMPLOYMENT_STATUS + " TEXT)";

    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + HireHubContract.PostsTable.TABLE_NAME + " (" +
                    HireHubContract.PostsTable.COLUMN_NAME_POST_ID + " INTEGER PRIMARY KEY," +
                    HireHubContract.PostsTable.COLUMN_NAME_USER_ID + " INTEGER," +
                    HireHubContract.PostsTable.COLUMN_NAME_TITLE + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_CATEGORY + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_SECTOR + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_CONTRACT_TYPE + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_CITY + " TEXT," +
                    "FOREIGN KEY (" + HireHubContract.PostsTable.COLUMN_NAME_USER_ID + ") REFERENCES " +
                    HireHubContract.UsersTable.TABLE_NAME + "(" + HireHubContract.UsersTable.COLUMN_NAME_USER_ID + "))";

    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + HireHubContract.UsersTable.TABLE_NAME;

    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + HireHubContract.PostsTable.TABLE_NAME;

    public HireHubDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_POSTS);
    }
    private void insertInitialData(SQLiteDatabase db) {
        // Insert a test user
        ContentValues userValues = new ContentValues();
        userValues.put(HireHubContract.UsersTable.COLUMN_NAME_EMAIL, "testuser@gmail.com");
        userValues.put(HireHubContract.UsersTable.COLUMN_NAME_PASSWORD, "pass123");
        userValues.put(HireHubContract.UsersTable.COLUMN_NAME_CITY, "Settat");
        userValues.put(HireHubContract.UsersTable.COLUMN_NAME_EMPLOYMENT_STATUS, "Candidate");
        long newUserId = db.insert(HireHubContract.UsersTable.TABLE_NAME, null, userValues);

        if (newUserId != -1) {
            // Insert a test post linked to the test user
            ContentValues postValues = new ContentValues();
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_USER_ID, newUserId);
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_TITLE, "Test Job Title");
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_CATEGORY, "UI/UX design");
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_SECTOR, "IT");
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_CONTRACT_TYPE, "Full-time");
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION, "This is a test job description.");
            postValues.put(HireHubContract.PostsTable.COLUMN_NAME_CITY, "Settat");
            db.insert(HireHubContract.PostsTable.TABLE_NAME, null, postValues);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database upgrade policy is to simply discard the data and start over
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
