package com.example.movietask;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("title")
    String title;
    @SerializedName("release_date")
    String date;
    @SerializedName("poster_path")
    String poster;
    @SerializedName("overview")
    String description;

    @NonNull
    @Override
    public String toString() {
        return this.getTitle() +"\n"+ this.getDate() +"\n"+ this.getPoster() +"\n"+ this.getDescription();
    }

    public Movie(String poster, String title, String date, String description) {
        this.poster = poster;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
