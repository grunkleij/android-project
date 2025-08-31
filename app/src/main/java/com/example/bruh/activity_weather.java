package com.example.bruh; // Make sure this is your correct package name

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_weather extends AppCompatActivity {

    private static final String API_KEY = "e540de4b9d31d482b34c093ab7cf2326"; // ⬇️ PASTE YOUR API KEY HERE ⬇️
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String TAG = "activity_weather";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewDate, textViewCity, textViewTemperature, textViewDescription;
   // private ImageView imageViewWeatherIcon;
    private LottieAnimationView lottieAnimationView;
    private LinearLayout weeklyForecastContainer;

    private TextView[] hourlyTimes = new TextView[4];
    private ImageView[] hourlyIcons = new ImageView[4];
    private TextView[] hourlyTemps = new TextView[4];

    private static class ForecastDay {
        String dayOfWeek, date, description, condition;
        double temperature, windSpeed;
        int humidity;

        public ForecastDay(String dayOfWeek, String date, String description, double temperature, String condition, double windSpeed, int humidity) {
            this.dayOfWeek = dayOfWeek;
            this.date = date;
            this.description = description;
            this.temperature = temperature;
            this.condition = condition;
            this.windSpeed = windSpeed;
            this.humidity = humidity;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initializeUI();
        BottomNavigationView btm = findViewById(R.id.bottom_navigation);
        BottomManager btmmg = new BottomManager();
        btmmg.setit(this, R.id.item_2);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationAndFetchWeather();
    }

    private void initializeUI() {
        textViewDate = findViewById(R.id.textViewDate);
        textViewCity = findViewById(R.id.textViewCity);
       // imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewDescription = findViewById(R.id.textViewDescription);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        weeklyForecastContainer = findViewById(R.id.weekly_forecast_container);

        hourlyTimes[0] = findViewById(R.id.hourly_time_1);
        hourlyIcons[0] = findViewById(R.id.hourly_icon_1);
        hourlyTemps[0] = findViewById(R.id.hourly_temp_1);

        hourlyTimes[1] = findViewById(R.id.hourly_time_2);
        hourlyIcons[1] = findViewById(R.id.hourly_icon_2);
        hourlyTemps[1] = findViewById(R.id.hourly_temp_2);

        hourlyTimes[2] = findViewById(R.id.hourly_time_3);
        hourlyIcons[2] = findViewById(R.id.hourly_icon_3);
        hourlyTemps[2] = findViewById(R.id.hourly_temp_3);

        hourlyTimes[3] = findViewById(R.id.hourly_time_4);
        hourlyIcons[3] = findViewById(R.id.hourly_icon_4);
        hourlyTemps[3] = findViewById(R.id.hourly_temp_4);
    }

    private void requestLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                fetchWeatherData(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "Location not found. Using default.", Toast.LENGTH_SHORT).show();
                fetchWeatherData(11.2588, 75.7804); // Default to Kozhikode
            }
        });
    }

    private void fetchWeatherData(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeather(latitude, longitude, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Log.e(TAG, "API Error: " + response.code());
                    Toast.makeText(activity_weather.this, "Failed to get weather. Check API Key.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Failure: " + t.getMessage(), t);
                Toast.makeText(activity_weather.this, "Network error.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationAndFetchWeather();
            } else {
                Toast.makeText(this, "Permission denied. Using default location.", Toast.LENGTH_LONG).show();
                fetchWeatherData(11.2588, 75.7804); // Default to Kozhikode
            }
        }
    }

    private void updateUI(WeatherResponse weatherResponse) {
        textViewDate.setText(getCurrentDateFormatted("EEEE, d MMMM yyyy"));
        textViewCity.setText(weatherResponse.getName());
        textViewTemperature.setText(String.format(Locale.getDefault(), "%.0f°", weatherResponse.getMain().getTemp()));

        String mainCondition = weatherResponse.getWeather().get(0).getMain();
        String description = weatherResponse.getWeather().get(0).getDescription();
        textViewDescription.setText(description.substring(0, 1).toUpperCase() + description.substring(1));

        //setWeatherIcon(mainCondition, imageViewWeatherIcon);
        updateWeatherAnimation(mainCondition);
        //Animation fadeInSlide = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide);
       // imageViewWeatherIcon.startAnimation(fadeInSlide);

        populateHourlyForecast(weatherResponse);
        populateWeeklyForecast(weatherResponse);
    }

    private void populateHourlyForecast(WeatherResponse data) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat hourFormat = new SimpleDateFormat("h a", Locale.getDefault());
        for (int i = 0; i < 4; i++) {
            Calendar futureCal = (Calendar) cal.clone();
            futureCal.add(Calendar.HOUR_OF_DAY, i);
            hourlyTimes[i].setText(hourFormat.format(futureCal.getTime()));
            hourlyTemps[i].setText(String.format(Locale.getDefault(), "%.0f°", data.getMain().getTemp() - (i * 0.5)));
            setWeatherIcon(data.getWeather().get(0).getMain(), hourlyIcons[i]);
        }
    }

    private void populateWeeklyForecast(WeatherResponse data) {
        List<ForecastDay> weekForecast = generateDummyForecast(data);
        weeklyForecastContainer.removeAllViews();
        for (ForecastDay day : weekForecast) {
            View weeklyItemView = getLayoutInflater().inflate(R.layout.item_weekly_forecast, weeklyForecastContainer, false);
            TextView dayText = weeklyItemView.findViewById(R.id.weekly_day_text);
            ImageView icon = weeklyItemView.findViewById(R.id.weekly_icon);
            TextView tempText = weeklyItemView.findViewById(R.id.weekly_temp_text);

            dayText.setText(day.dayOfWeek);
            tempText.setText(String.format(Locale.getDefault(), "%.0f°", day.temperature));
            setWeatherIcon(day.condition, icon);
            weeklyItemView.setOnClickListener(v -> showWeatherDialog(day));
            weeklyForecastContainer.addView(weeklyItemView);
        }
    }

    private List<ForecastDay> generateDummyForecast(WeatherResponse data) {
        List<ForecastDay> weekForecast = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Random random = new Random();
        String[] conditions = {"Clear", "Clouds", "Rain", "Thunderstorm"};

        weekForecast.add(new ForecastDay("Today", getCurrentDateFormatted("d MMMM"), data.getWeather().get(0).getDescription(), data.getMain().getTemp(), data.getWeather().get(0).getMain(), data.getWind().getSpeed(), data.getMain().getHumidity()));

        for (int i = 1; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
            String date = new SimpleDateFormat("d MMMM", Locale.getDefault()).format(calendar.getTime());
            String randomCondition = conditions[random.nextInt(conditions.length)];
            double randomTemp = data.getMain().getTemp() + (random.nextInt(6) - 3);
            double randomWind = 5 + random.nextInt(10);
            int randomHumidity = 60 + random.nextInt(25);
            weekForecast.add(new ForecastDay(dayOfWeek, date, randomCondition, randomTemp, randomCondition, randomWind, randomHumidity));
        }
        return weekForecast;
    }

    private void updateWeatherAnimation(String condition) {
        int animationResId;
        switch (condition.toLowerCase()) {
            case "clear": animationResId = R.raw.anim_sunny; break;
            case "rain": case "drizzle": case "thunderstorm": animationResId = R.raw.anim_rain; break;
            default: animationResId = R.raw.anim_clouds; break;
        }
        lottieAnimationView.setAnimation(animationResId);
        lottieAnimationView.playAnimation();
    }

    private void setWeatherIcon(String condition, ImageView imageView) {
        int iconResId;
        switch (condition.toLowerCase()) {
            case "clear": iconResId = R.drawable.ic_sunny; break;
            case "clouds": iconResId = R.drawable.ic_cloudy; break;
            case "rain": case "drizzle": iconResId = R.drawable.ic_rainy; break;
            case "thunderstorm": iconResId = R.drawable.ic_thunderstorm; break;
            default: iconResId = R.drawable.ic_cloudy; break;
        }
        imageView.setImageResource(iconResId);
    }

    private void showWeatherDialog(ForecastDay day) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_weather_details);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView dialogDay = dialog.findViewById(R.id.dialog_day_text);
        TextView dialogDesc = dialog.findViewById(R.id.dialog_description_text);
        TextView dialogTemp = dialog.findViewById(R.id.dialog_temp_text);
        TextView dialogHumidity = dialog.findViewById(R.id.dialog_humidity_text);
        TextView dialogWind = dialog.findViewById(R.id.dialog_wind_text);

        dialogDay.setText(day.dayOfWeek + ", " + day.date);
        dialogDesc.setText("Forecast: " + day.description.substring(0, 1).toUpperCase() + day.description.substring(1));
        dialogTemp.setText(String.format(Locale.getDefault(), "Temperature: %.0f°C", day.temperature));
        dialogHumidity.setText(String.format(Locale.getDefault(), "Humidity: %d%%", day.humidity));
        dialogWind.setText(String.format(Locale.getDefault(), "Wind Speed: %.1f km/h", day.windSpeed * 3.6)); // Convert m/s to km/h
        dialog.show();
    }

    private String getCurrentDateFormatted(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
    }
}