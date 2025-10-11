package com.example.bruh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // Existing variables
    TextView greetingText;
    RecyclerView carouselRecyclerView;

    // Variables for the new Highlights Card
    TextView highlightWeatherText, highlightNewsText, highlightCurrencyText, highlightMovieText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find existing views
        greetingText = findViewById(R.id.greeting_text);
        carouselRecyclerView = findViewById(R.id.carousel_recycler_view);

        // Find the new Highlights views
        highlightWeatherText = findViewById(R.id.highlight_weather_text);
        highlightNewsText = findViewById(R.id.highlight_news_text);
        highlightCurrencyText = findViewById(R.id.highlight_currency_text);
        highlightMovieText = findViewById(R.id.highlight_movie_text);

        setDynamicGreeting();
        setupCarousel();
        updateHighlights(); // Call method to populate the new card
    }

    // New method to populate the Highlights card
    private void updateHighlights() {
        // In a real app, you would get this data from APIs
        highlightWeatherText.setText("Kochi: 30°C, Partly Cloudy");
        highlightNewsText.setText("Top Headline: ISRO launches new GSLV rocket...");
        highlightCurrencyText.setText("USD → INR: ₹83.52");
        highlightMovieText.setText("Recommendation: 'Dune: Part Two'");
    }

    // This method remains unchanged
    private void setupCarousel() {
        List<CarouselItem> carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(R.drawable.movie_banner, "Movies"));
        carouselItems.add(new CarouselItem(R.drawable.weather_banner, "Weather"));
        carouselItems.add(new CarouselItem(R.drawable.currency_banner, "Currency"));
        carouselItems.add(new CarouselItem(R.drawable.news_banner, "News"));

        CarouselAdapter adapter = new CarouselAdapter(carouselItems, item -> {
            Intent intent = null;
            String title = item.getTitle();
            if ("Movies".equals(title)) {
                intent = new Intent(HomeActivity.this, MainActivity.class);
            } else if ("Weather".equals(title)) {
                intent = new Intent(HomeActivity.this, activity_weather.class);
            } else if ("Currency".equals(title)) {
                intent = new Intent(HomeActivity.this, Currency.class);
            } else if ("News".equals(title)) {
                intent = new Intent(HomeActivity.this, NewsMain.class);
            }

            if (intent != null) {
                startActivity(intent);
            }
        });

        CarouselLayoutManager layoutManager = new CarouselLayoutManager();
        layoutManager.setCarouselStrategy(new MultiBrowseCarouselStrategy());

        carouselRecyclerView.setLayoutManager(layoutManager);
        carouselRecyclerView.setAdapter(adapter);
    }

    // This method remains unchanged
    private void setDynamicGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour < 12) {
            greetingText.setText("Good Morning!");
        } else if (hour < 17) {
            greetingText.setText("Good Afternoon!"); // This is active now
        } else {
            greetingText.setText("Good Evening!");
        }
    }
}