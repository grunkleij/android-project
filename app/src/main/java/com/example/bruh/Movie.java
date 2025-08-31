package com.example.bruh;

public class Movie {
    private  int id;
    private String title;
    private String posterResId;

    public Movie(int id, String title, String posterResId) {
        this.id = id;
        this.title = title;
        this.posterResId = posterResId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterResId() {
        return posterResId;
    }
}
