//CURRENCY
package com.example.bruh;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class HistoryResponse {

    @SerializedName("base")
    public String base;

    @SerializedName("rates")
    public Map<String, Map<String, Double>> rates;
}
