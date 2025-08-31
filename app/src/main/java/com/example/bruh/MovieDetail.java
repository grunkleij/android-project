package com.example.bruh;

import java.util.List;

public class MovieDetail {
    private final String title;
    private final String overview;
    private final String posterPath;
    private final String backdropPath;
    private final String releaseDate;
    private final int runtime;
    private final double voteAverage;
    private final String tagline;
    private final List<String> genres;

    public MovieDetail(String title, String overview, String posterPath, String backdropPath,
                       String releaseDate, int runtime, double voteAverage, String tagline, List<String> genres) {
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.voteAverage = voteAverage;
        this.tagline = tagline;
        this.genres = genres;
    }

    // --- Getters for all fields ---
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String getPosterPath() { return "https://image.tmdb.org/t/p/w500" + posterPath; }
    public String getBackdropPath() { return "https://image.tmdb.org/t/p/w1280" + backdropPath; }
    public String getReleaseDate() { return releaseDate; }
    public int getRuntime() { return runtime; }
    public double getVoteAverage() { return voteAverage; }
    public String getTagline() { return tagline; }
    public List<String> getGenres() { return genres; }
}