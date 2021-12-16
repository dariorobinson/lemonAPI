package com.revature.lemon.playlist.dtos.requests;

import com.revature.lemon.song.Song;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class AddSongRequest {

    private String playlistId;
    private List<Song> songList;

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }


    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
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
