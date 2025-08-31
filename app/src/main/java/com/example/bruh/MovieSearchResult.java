package com.example.bruh;

import androidx.annotation.NonNull;

// Add this class inside your MainActivity class with the new name
public   class MovieSearchResult {
    private final int id;
    private final String title;

    public MovieSearchResult(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
