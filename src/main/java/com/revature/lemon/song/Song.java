package com.revature.lemon.song;

import com.revature.lemon.common.model.SongPlaylistOrder;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @Column(name = "song_url", nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Duration duration;

    @OneToMany(mappedBy = "song")
    List<SongPlaylistOrder> songOrder;

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

    public List<SongPlaylistOrder> getSongOrder() {
        return songOrder;
    }

    public void setSongOrder(List<SongPlaylistOrder> songOrder) {
        this.songOrder = songOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(url, song.url) && Objects.equals(name, song.name) && Objects.equals(duration, song.duration) && Objects.equals(songOrder, song.songOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, duration, songOrder);
    }

    @Override
    public String toString() {
        return "Song{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", songOrder=" + songOrder +
                '}';
    }
}
