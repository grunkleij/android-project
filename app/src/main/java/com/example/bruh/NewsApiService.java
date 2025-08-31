package com.example.bruh;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("latest-news")
    Call<NewsResponse> getLatestNews(
            @Query("apiKey") String apiKey
    );
}
