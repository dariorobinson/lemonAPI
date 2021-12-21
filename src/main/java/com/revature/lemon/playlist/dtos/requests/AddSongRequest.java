package com.revature.lemon.playlist.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * DTO used to add songs into a playlist
 */
public class AddSongRequest {

    @JsonIgnore
    private String playlistId;

    private String songUrl;

    private String name;

    private Duration duration;

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
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
        AddSongRequest that = (AddSongRequest) o;
        return Objects.equals(playlistId, that.playlistId) && Objects.equals(songUrl, that.songUrl) && Objects.equals(name, that.name) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, songUrl, name, duration);
    }

    @Override
    public String
    toString() {
        return "AddSongRequest{" +
                "playlistId='" + playlistId + '\'' +
                ", songUrl='" + songUrl + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
