package com.revature.lemon.playlist.dtos.requests;

import com.revature.lemon.song.Song;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class AddSongRequest {

    private String playlistId;
    private String songUrl;

    public AddSongRequest() {

    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddSongRequest that = (AddSongRequest) o;
        return Objects.equals(playlistId, that.playlistId) && Objects.equals(songUrl, that.songUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, songUrl);
    }

    @Override
    public String toString() {
        return "AddSongRequest{" +
                "playlistId='" + playlistId + '\'' +
                ", songUrl='" + songUrl + '\'' +
                '}';
    }
}
