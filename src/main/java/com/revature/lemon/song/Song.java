package com.revature.lemon.song;

import javax.persistence.*;
import java.time.Duration;
import java.util.Objects;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @Column(name = "song_url", nullable = false, unique = true)
    private String url;

    @Column
    private String name;

    @Column
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
        Song song = (Song) o;
        return Objects.equals(url, song.url) && Objects.equals(name, song.name) && Objects.equals(duration, song.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, duration);
    }

    @Override
    public String toString() {
        return "Song{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
