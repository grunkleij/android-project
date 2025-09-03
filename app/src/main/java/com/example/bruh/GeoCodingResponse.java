package com.example.bruh;

public class GeoCodingResponse {
    private String name;
    private String country;
    private String state;

    // Getters
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getState() { return state; }

    // This is important! It defines how the text will appear in the suggestion dropdown.
    @Override
    public String toString() {
        if (state != null && !state.isEmpty()) {
            return name + ", " + state + ", " + country;
        }
        return name + ", " + country;
    }
}