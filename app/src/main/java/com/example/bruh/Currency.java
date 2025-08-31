
package com.example.bruh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.example.bruh.CurrencyAPIClient;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Currency extends AppCompatActivity {

    EditText amountInput;
    AutoCompleteTextView fromCurrencyDropdown, toCurrencyDropdown;
    Button convertBtn;
    TextView resultText;
    LineChart lineChart;
    ExchangeRateApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        // Views
        amountInput = findViewById(R.id.amountInput);
        fromCurrencyDropdown = findViewById(R.id.fromCurrencyDropdown);
        toCurrencyDropdown = findViewById(R.id.toCurrencyDropdown);
        convertBtn = findViewById(R.id.convertBtn);
        resultText = findViewById(R.id.resultText);
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);


        // API client
        api = CurrencyAPIClient.getClient().create(ExchangeRateApi.class);

        // Load currency list
        loadCurrencyList();

        // Convert button
        convertBtn.setOnClickListener(v -> {
            String from = fromCurrencyDropdown.getText().toString().trim();
            String to = toCurrencyDropdown.getText().toString().trim();
            String amountStr = amountInput.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            convertCurrency(from, to, amount);
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Currency.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    // Load all currencies into dropdowns

    private void loadCurrencyList() {
        api.getSymbols().enqueue(new Callback<SymbolsResponse>() {
            @Override
            public void onResponse(Call<SymbolsResponse> call, Response<SymbolsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<String> currencyCodes = new ArrayList<>();
                    for (List<String> codePair : response.body().codes) {
                        currencyCodes.add(codePair.get(0)); // first element is code
                    }
                    Collections.sort(currencyCodes);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            Currency.this,
                            android.R.layout.simple_dropdown_item_1line,
                            currencyCodes
                    );
                    fromCurrencyDropdown.setAdapter(adapter);
                    toCurrencyDropdown.setAdapter(adapter);

                    fromCurrencyDropdown.setOnClickListener(v -> fromCurrencyDropdown.showDropDown());
                    toCurrencyDropdown.setOnClickListener(v -> toCurrencyDropdown.showDropDown());
                } else {
                    Toast.makeText(Currency.this, "No symbols received", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SymbolsResponse> call, Throwable t) {
                Toast.makeText(Currency.this, "Error loading currencies: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Convert currency
    private void convertCurrency(String from, String to, double amount) {
        api.getLatestRates(from).enqueue(new Callback<LatestResponse>() {
            @Override
            public void onResponse(Call<LatestResponse> call, Response<LatestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Double rate = response.body().rates.get(to);
                    if (rate != null) {
                        double converted = amount * rate;
                        resultText.setText(String.format(Locale.getDefault(), "%.2f %s = %.2f %s", amount, from, converted, to));
                    } else {
                        resultText.setText("Rate not available");
                    }
                } else {
                    resultText.setText("API Error");
                }
            }

            @Override
            public void onFailure(Call<LatestResponse> call, Throwable t) {
                resultText.setText("Error: " + t.getMessage());
            }
        });
    }


}
