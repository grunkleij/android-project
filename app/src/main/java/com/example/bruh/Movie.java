package com.example.bruh;

public class Movie {
    private String title;
    private String posterResId;

    public Movie(String title, String posterResId) {
        this.title = title;
        this.posterResId = posterResId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterResId() {
        return posterResId;
    }
}
