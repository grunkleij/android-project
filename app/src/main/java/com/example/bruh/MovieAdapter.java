package com.example.bruh;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_comp, parent, false); // your card layout
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieTitle.setText(movie.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(movie.getPosterResId())   // load URL instead of drawable
                .placeholder(R.drawable.ic_launcher_background) // fallback image
                .into(holder.moviePoster);

        holder.itemView.setOnClickListener(v -> {
            // Get the movie that was clicked
            Movie clickedMovie = movieList.get(holder.getAdapterPosition());
            int movieId = clickedMovie.getId();

            Intent intent = new Intent(v.getContext(), MovieDetailsPage.class);
            intent.putExtra("MOVIE_ID",movieId);
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
