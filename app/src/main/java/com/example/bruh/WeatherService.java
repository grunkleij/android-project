package com.example.bruh; // Use your correct package name

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface WeatherService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeatherByCity(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    // ⬇️ ADD THIS NEW METHOD for getting city suggestions as you type ⬇️
    @GET("geo/1.0/direct")
    Call<List<GeoCodingResponse>> getCitySuggestions(
            @Query("q") String cityName,
            @Query("limit") int limit,
            @Query("appid") String apiKey
    );
}