package com.example.bruh;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ExchangeRateApi {
    // latest rates
    // Latest rates
    @GET("latest/{base}")
    Call<LatestResponse> getLatestRates(@Path("base") String base);

    // Optional: fetch supported codes
    @GET("codes")
    Call<SymbolsResponse> getSymbols();
}
