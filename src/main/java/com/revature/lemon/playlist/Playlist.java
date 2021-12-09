package com.revature.lemon.playlist;

import com.revature.lemon.common.model.UserPlaylistRole;
import com.revature.lemon.common.model.SongPlaylistOrder;
import com.revature.lemon.common.util.AccessType;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @Column(name = "playlist_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "access DEFAULT 'PRIVATE' CHECK (access in ('PRIVATE', 'PUBLIC'))")
    private AccessType access;

    @OneToMany(mappedBy = "playlist")
    private List<UserPlaylistRole> playlistRole;

    @OneToMany(mappedBy = "playlist")
    private List<SongPlaylistOrder> songOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccessType getAccess() {
        return access;
    }

    public void setAccess(AccessType access) {
        this.access = access;
    }

    public List<UserPlaylistRole> getPlaylistRole() {
        return playlistRole;
    }

    public void setPlaylistRole(List<UserPlaylistRole> playlistRole) {
        this.playlistRole = playlistRole;
    }

    public List<SongPlaylistOrder> getSongOrder() {
        return songOrder;
    }

    public void setSongOrder(List<SongPlaylistOrder> songOrder) {
        this.songOrder = songOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id) && Objects.equals(name, playlist.name) && Objects.equals(description, playlist.description) && access == playlist.access && Objects.equals(playlistRole, playlist.playlistRole) && Objects.equals(songOrder, playlist.songOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, access, playlistRole, songOrder);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", access=" + access +
                ", playlistRole=" + playlistRole +
                ", songOrder=" + songOrder +
                '}';
    }
}
