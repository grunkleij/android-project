//CURRENCY
package com.example.bruh;
import com.google.gson.annotations.SerializedName;
import java.util.Map;
public class LatestResponse {
    @SerializedName("base_code")
    public String base;

    @SerializedName("conversion_rates")
    public Map<String, Double> rates;
}

