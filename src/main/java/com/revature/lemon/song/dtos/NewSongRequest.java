package com.revature.lemon.song.dtos;

import java.time.Duration;
import java.util.Objects;

/**
 * DTO used for adding a new song in the database
 */
public class NewSongRequest {
    private String url;
    private String name;
    private Duration duration;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewSongRequest that = (NewSongRequest) o;
        return Objects.equals(url, that.url) && Objects.equals(name, that.name) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, duration);
    }

    @Override
    public String toString() {
        return "NewSongRequest{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
