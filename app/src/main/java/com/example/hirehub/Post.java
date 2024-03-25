package com.example.hirehub;

public class Post {
    private String username;
    private String title;
    private String description;

    private String userEmail; // Add this line

    // Update your constructor
    public Post(String userEmail, String title, String description) {
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
    }

    // Add a getter for userEmail
    public String getUserEmail() { return userEmail; }
    // Getters
    public String getUsername() { return username; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
