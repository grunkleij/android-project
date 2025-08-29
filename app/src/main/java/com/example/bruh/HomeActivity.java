package com.example.bruh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    Button movie;
    Button news;
    Button currency;
    Button weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        movie = findViewById(R.id.movie_btn);
        news = findViewById(R.id.news_btn);
        weather = findViewById(R.id.weather_btn);
        currency = findViewById(R.id.currency_btn);


        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Currency.class);
                startActivity(intent);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewsMain.class);
                startActivity(intent);
            }
        });

    }
}