package com.revature.lemon.playlist.dtos.responses;

import com.revature.lemon.common.model.SongPlaylist;
import com.revature.lemon.song.Song;

import java.time.Duration;
import java.util.Objects;

/**
 * DTO used for a response that contains information about the song pertaining to the playlist
 */
public class SongsInPlaylistResponse {

    private String url;

    private String name;

    private Duration duration;

    private int songOrder;

    public SongsInPlaylistResponse(SongPlaylist playlist) {
        Song song = playlist.getSong();
        this.url = song.getUrl();
        this.name = song.getName();
        this.duration = song.getDuration();
        this.songOrder = playlist.getSongOrder();
    }

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

    public int getSongOrder() {
        return songOrder;
    }

    public void setSongOrder(int songOrder) {
        this.songOrder = songOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongsInPlaylistResponse that = (SongsInPlaylistResponse) o;
        return Objects.equals(url, that.url) && Objects.equals(name, that.name) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, duration);
    }

    @Override
    public String toString() {
        return "SongsInPlaylistResponse{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
