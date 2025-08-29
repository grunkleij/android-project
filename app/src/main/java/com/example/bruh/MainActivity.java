package com.example.bruh;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;

    private int currentPage = 1;
    private boolean isLoading = false;

    URL url = new URL("https://api.themoviedb.org/3/movie/popular?api_key=582913cbc1255e68ef241e0956a7ae7c&language=en-US&page=1");

    public MainActivity() throws MalformedURLException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BottomManager btmmg = new BottomManager();
        btmmg.setit(this, R.id.item_1);

        recyclerView = findViewById(R.id.recyclerViewMovies);







        adapter = new MovieAdapter(movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    // Load next page when reaching bottom
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        currentPage++;
                        fetchMovies(currentPage,"popular");
                    }
                }
            }
        });


        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        fetchMovies(1,"popular");


        TabLayout tabLayout = findViewById(R.id.tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                currentPage = 1; // reset page when switching tabs
                movieList.clear(); // clear old movies
                adapter.notifyDataSetChanged();

                if (position == 0) {
                    // Popular selected
                    fetchMovies(currentPage, "popular");
                } else if (position == 1) {
                    // Top rated selected
                    fetchMovies(currentPage, "top_rated");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: handle reselection
            }
        });

    }




    private void fetchMovies(int page, String type) {
        isLoading = true;

        new Thread(() -> {
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/"+type+"?api_key=582913cbc1255e68ef241e0956a7ae7c&language=en-US&page=" + page);
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
                    String title = movieObj.getString("original_title");
                    String posterPath = movieObj.getString("poster_path");
                    String fullPosterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

                    fetchedMovies.add(new Movie(title, fullPosterUrl));
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
