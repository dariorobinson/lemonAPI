package com.revature.lemon.common.model;

import com.revature.lemon.playlist.Playlist;
import com.revature.lemon.song.Song;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SongPlaylist {

    @EmbeddedId
    private SongPlaylistKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @MapsId("url")
    @JoinColumn(name = "song_url")
    private Song song;

    private int songOrder;

    public SongPlaylist() {
    }

    public SongPlaylist(Song song) {
        this.song = song;
    }

    public SongPlaylist(Song song, Playlist playlist) {
        this.song = song;
        this.playlist = playlist;
        setId(new SongPlaylistKey(song.getUrl(), playlist.getId()));
    }

    public SongPlaylistKey getId() {
        return id;
    }

    public void setId(SongPlaylistKey id) {
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

    public int getSongOrder() {
        return songOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongPlaylist that = (SongPlaylist) o;
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
                ", songOrder=" + songOrder +
                '}';
    }
}
