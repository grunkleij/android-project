package com.example.bruh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;

    private int currentPage = 1;
    private boolean isLoading = false;
    private String type = "popular";

    // --- NEW FOR SEARCH ---
    private AutoCompleteTextView etSearchMovies;
//    private ArrayAdapter<String> searchAdapter;

    private ArrayAdapter<MovieSearchResult> searchAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    // --- END NEW ---


    public MainActivity() throws MalformedURLException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // You can keep this if you like
        setContentView(R.layout.activity_main);

//        BottomManager btmmg = new BottomManager();
//        btmmg.setit(this, R.id.item_1);

        // --- NEW ---
        etSearchMovies = findViewById(R.id.etSearchMovies);
        setupSearch();
        // --- END NEW ---

        recyclerView = findViewById(R.id.recyclerViewMovies);
        movieList = new ArrayList<>(); // Initialize the list *before* using it
        adapter = new MovieAdapter(movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && (layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount() && layoutManager.findFirstVisibleItemPosition() >= 0) {
                    currentPage++;
                    fetchMovies(currentPage, type);
                }
            }
        });

        fetchMovies(1, "popular");

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPage = 1;
                movieList.clear();
                adapter.notifyDataSetChanged();
                type = (tab.getPosition() == 0) ? "popular" : "top_rated";
                fetchMovies(currentPage, type);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // --- NEW ---
    private void setupSearch() {
        // The adapter now holds SearchResult objects
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        etSearchMovies.setAdapter(searchAdapter);

        // ... (the TextWatcher part remains the same) ...
        etSearchMovies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    searchRunnable = () -> searchMovies(s.toString());
                    handler.postDelayed(searchRunnable, 500);
                }
            }
        });


        // --- UPDATE THE CLICK LISTENER ---
        etSearchMovies.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected SearchResult object
            MovieSearchResult selectedResult = (MovieSearchResult) parent.getItemAtPosition(position);
            if (selectedResult == null) return;

            // Extract the ID and Title
            int movieId = selectedResult.getId();
            String movieTitle = selectedResult.getTitle();

//            Toast.makeText(MainActivity.this, "Selected: " + movieTitle, Toast.LENGTH_SHORT).show();

            // Create the intent and pass the movie ID
            Intent intent = new Intent(MainActivity.this, MovieDetailsPage.class);
            intent.putExtra(MovieDetailsPage.MOVIE_ID, movieId);
            startActivity(intent);
        });
    }

        // Handle when a user clicks an item in the dropdown



    // --- NEW ---
    // This method fetches results for the search dropdown
    private void searchMovies(String query) {
        new Thread(() -> {
            try {
                String encodedQuery = URLEncoder.encode(query, "UTF-8");
                URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=582913cbc1255e68ef241e0956a7ae7c&query=" + encodedQuery);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray results = jsonObject.getJSONArray("results");

                // --- CHANGE THIS BLOCK ---
                List<MovieSearchResult> searchResults = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObj = results.getJSONObject(i);
                    int id = movieObj.getInt("id");
                    String title = movieObj.getString("title");
                    searchResults.add(new MovieSearchResult(id, title)); // Create SearchResult objects
                }
                // --- END CHANGE ---

                runOnUiThread(() -> {
                    searchAdapter.clear();
                    searchAdapter.addAll(searchResults); // Add the new list of objects
                    searchAdapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchMovies(int page, String type) {
        isLoading = true;
        new Thread(() -> {
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + type + "?api_key=582913cbc1255e68ef241e0956a7ae7c&language=en-US&page=" + page);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray results = jsonObject.getJSONArray("results");

                List<Movie> fetchedMovies = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObj = results.getJSONObject(i);
                    int  id = movieObj.getInt("id");
                    String title = movieObj.getString("original_title");
                    String posterPath = movieObj.getString("poster_path");
                    String fullPosterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                    fetchedMovies.add(new Movie(id, title, fullPosterUrl));
                }

                runOnUiThread(() -> {
                    movieList.addAll(fetchedMovies);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                });

            } catch (Exception e) {
                e.printStackTrace();
                isLoading = false;
            }
        }).start();
    }
}