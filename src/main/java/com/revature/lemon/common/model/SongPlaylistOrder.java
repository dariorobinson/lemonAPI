package com.revature.lemon.common.model;

import com.revature.lemon.playlist.Playlist;
import com.revature.lemon.song.Song;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SongPlaylistOrder {

    @EmbeddedId
    private SongPlaylistOrderKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @MapsId("url")
    @JoinColumn(name = "song_url")
    private Song song;

    private int songOrder;

    public SongPlaylistOrder() {
    }

    public SongPlaylistOrder(Song song) {
        this.song = song;
    }

    public SongPlaylistOrder(Song song, Playlist playlist) {
        this.song = song;
        this.playlist = playlist;
        setId(new SongPlaylistOrderKey(song.getUrl(), playlist.getId()));
    }

    public SongPlaylistOrderKey getId() {
        return id;
    }

    public void setId(SongPlaylistOrderKey id) {
        this.id = id;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setSongOrder(int songOrder) {
        this.songOrder = songOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongPlaylistOrder that = (SongPlaylistOrder) o;
        return songOrder == that.songOrder && Objects.equals(id, that.id) && Objects.equals(playlist, that.playlist) && Objects.equals(song, that.song);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playlist, song, songOrder);
    }

    @Override
    public String toString() {
        return "SongPlaylistOrder{" +
                "id=" + id +
                ", song=" + song +
                '}';
    }
}
