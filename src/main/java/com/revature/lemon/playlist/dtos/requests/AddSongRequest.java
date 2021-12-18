package com.revature.lemon.playlist.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

/**
 * DTO used to add songs into a playlist
 */
public class AddSongRequest {

    @JsonIgnore
    private String playlistId;

    private List<String> songList;

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public List<String> getSongList() {
        return songList;
    }

    public void setSongList(List<String> songList) {
        this.songList = songList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddSongRequest that = (AddSongRequest) o;
        return Objects.equals(playlistId, that.playlistId) && Objects.equals(songList, that.songList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, songList);
    }

    @Override
    public String toString() {
        return "AddSongRequest{" +
                "playlistId='" + playlistId + '\'' +
                ", songList=" + songList +
                '}';
    }
}
