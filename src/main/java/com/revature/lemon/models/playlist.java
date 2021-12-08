package com.revature.lemon.models;

import javax.persistence.*;

@Entity
@Table(name = "playlists")
public class playlist {

    @Id
    @Column(name = "playlist_id", nullable = false, unique = true)
    private String playlist_id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR CHECK (LENGTH(name) >= 1)")
    private String playlistName;

    @Column(name = "description")
    private String description;

    @Column(name = "access_type", nullable = false)
    private String access_type;

    public playlist(String playlist_id, String playlistName, String description, String access_type) {
        this.playlist_id = playlist_id;
        this.playlistName = playlistName;
        this.description = description;
        this.access_type = access_type;
    }

    // no arg constructor (just in case we need it)
    public playlist() {
        super();
    }

    public String getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(String playlist_id) {
        this.playlist_id = playlist_id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    @Override
    public String toString() {
        return "playlist{" +
                "playlist_id='" + playlist_id + '\'' +
                ", playlistName='" + playlistName + '\'' +
                ", description='" + description + '\'' +
                ", access_type='" + access_type + '\'' +
                '}';
    }
}
