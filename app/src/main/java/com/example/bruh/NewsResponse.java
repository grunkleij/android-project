package com.example.bruh;

import java.util.List;

public class NewsResponse {
    private String status;
    private List<Article> news;

    public String getStatus() {
        return status;
    }

    public List<Article> getNews() {
        return news;
    }
}
