package com.revature.lemon.common.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SongPlaylistOrderKey implements Serializable {

    @Column(name = "playlist_id")
    private String playlistId;

    @Column(name = "song_url")
    private String songUrl;

    public SongPlaylistOrderKey() {

    }
    public SongPlaylistOrderKey(String songUrl, String playlistId) {
        this.songUrl = songUrl;
        this.playlistId = playlistId;
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
        SongPlaylistOrderKey that = (SongPlaylistOrderKey) o;
        return Objects.equals(playlistId, that.playlistId) && Objects.equals(songUrl, that.songUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, songUrl);
    }
}
