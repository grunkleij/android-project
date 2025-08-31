//CURRENCY
package com.example.bruh;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SymbolsResponse {
        @SerializedName("supported_codes")
        public List<List<String>> codes; // [["USD", "United States Dollar"], ...]


}

