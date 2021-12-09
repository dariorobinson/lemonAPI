package com.revature.lemon.common.model;

import com.revature.lemon.playlist.Playlist;
import com.revature.lemon.song.Song;

import javax.persistence.*;

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

    public SongPlaylistOrderKey getId() {
        return id;
    }

    public void setId(SongPlaylistOrderKey id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
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

    public int getSongOrder() {
        return songOrder;
    }

    public void setSongOrder(int songOrder) {
        this.songOrder = songOrder;
    }
}
