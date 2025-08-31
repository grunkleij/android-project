package com.example.bruh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieDetailsPage extends AppCompatActivity {
    public static final String MOVIE_ID = "MOVIE_ID";

    // UI elements
    private ImageView movieBackdrop, moviePoster;
    private TextView movieTitle, movieTagline, movieRating, movieRuntime, movieReleaseDate, movieGenres, movieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_page);

        // Initialize all views
        initializeViews();

        // Get movie ID from intent
        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID, -1);

        if (movieId != -1) {
            fetchMovieDetails(movieId);
        }
    }

    private void initializeViews() {
        movieBackdrop = findViewById(R.id.movieBackdrop);
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieTagline = findViewById(R.id.movieTagline);
        movieRating = findViewById(R.id.movieRating);
        movieRuntime = findViewById(R.id.movieRuntime);
        movieReleaseDate = findViewById(R.id.movieReleaseDate);
        movieGenres = findViewById(R.id.movieGenres);
        movieOverview = findViewById(R.id.movieOverview);
    }

    private void fetchMovieDetails(int movieId) {
        new Thread(() -> {
            try {
                // Use your API key here
                URL url = new URL("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=582913cbc1255e68ef241e0956a7ae7c&language=en-US");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                // Parse the JSON response
                JSONObject movieJson = new JSONObject(content.toString());
                MovieDetail movieDetail = parseJsonToMovieDetail(movieJson);

                // Update UI on the main thread
                runOnUiThread(() -> updateUi(movieDetail));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private MovieDetail parseJsonToMovieDetail(JSONObject jsonObject) throws Exception {
        String title = jsonObject.getString("title");
        String overview = jsonObject.getString("overview");
        String posterPath = jsonObject.getString("poster_path");
        String backdropPath = jsonObject.getString("backdrop_path");
        String releaseDate = jsonObject.getString("release_date");
        int runtime = jsonObject.getInt("runtime");
        double voteAverage = jsonObject.getDouble("vote_average");
        String tagline = jsonObject.optString("tagline", ""); // Use optString for optional fields

        List<String> genres = new ArrayList<>();
        JSONArray genresArray = jsonObject.getJSONArray("genres");
        for (int i = 0; i < genresArray.length(); i++) {
            genres.add(genresArray.getJSONObject(i).getString("name"));
        }

        return new MovieDetail(title, overview, posterPath, backdropPath, releaseDate, runtime, voteAverage, tagline, genres);
    }

    private void updateUi(MovieDetail movie) {
        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        movieReleaseDate.setText(movie.getReleaseDate());

        // Handle tagline visibility
        if (movie.getTagline().isEmpty()) {
            movieTagline.setVisibility(View.GONE);
        } else {
            movieTagline.setVisibility(View.VISIBLE);
            movieTagline.setText(movie.getTagline());
        }

        // Format strings for display
        movieRating.setText(String.format(Locale.US, "%.1f / 10", movie.getVoteAverage()));
        movieRuntime.setText(String.format(Locale.US, "%d min", movie.getRuntime()));
        movieGenres.setText(TextUtils.join(", ", movie.getGenres()));

        // Load images using Glide
        Glide.with(this).load(movie.getBackdropPath()).into(movieBackdrop);
        Glide.with(this).load(movie.getPosterPath()).into(moviePoster);
    }
}