package com.revature.lemon.song;

import com.revature.lemon.playlist.Playlist;

import javax.persistence.*;
import java.sql.Time;
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

    @Column(name = "song_order")
    private int playlistOrder;

    @ManyToMany
    @JoinTable(
            name = "songs_playlists",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Playlist> songPlaylists;

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

    public int getPlaylistOrder() {
        return playlistOrder;
    }

    public void setPlaylistOrder(int playlistOrder) {
        this.playlistOrder = playlistOrder;
    }

    public List<Playlist> getSongPlaylists() {
        return songPlaylists;
    }

    public void setSongPlaylists(List<Playlist> songPlaylists) {
        this.songPlaylists = songPlaylists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return playlistOrder == song.playlistOrder && Objects.equals(url, song.url) && Objects.equals(name, song.name) && Objects.equals(duration, song.duration) && Objects.equals(songPlaylists, song.songPlaylists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, duration, playlistOrder, songPlaylists);
    }

    @Override
    public String toString() {
        return "Song{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", playlistOrder=" + playlistOrder +
                '}';
    }
}
