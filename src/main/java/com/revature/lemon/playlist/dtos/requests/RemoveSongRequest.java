package com.revature.lemon.playlist.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class RemoveSongRequest {

    @JsonIgnore
    private String playlistId;

    private String songUrl;

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveSongRequest that = (RemoveSongRequest) o;
        return Objects.equals(playlistId, that.playlistId) && Objects.equals(songUrl, that.songUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, songUrl);
    }

    @Override
    public String toString() {
        return "RemoveSongRequest{" +
                "playlistId='" + playlistId + '\'' +
                ", songUrl='" + songUrl + '\'' +
                '}';
    }
}
