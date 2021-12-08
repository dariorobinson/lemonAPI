package com.revature.lemon.playlist;

import com.revature.lemon.song.Song;
import com.revature.lemon.user.User;

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
    @Column(nullable = false, columnDefinition = "VARCHAR DEFAULT 'PRIVATE'")
    private AccessType access;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, columnDefinition = "VARCHAR DEFAULT 'UNACCESSIBLE'")
    private RoleType userRole;

    @ManyToMany
    @JoinTable(
            name = "users_playlists",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<User> userPlaylists;

    @ManyToMany
    @JoinTable(
            name = "songs_playlists",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songPlaylists;

    public enum AccessType {
        PRIVATE, PUBLIC
    }

    public enum RoleType {
        CREATOR, EDITOR, VIEWER
    }

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

    public RoleType getUserRole() {
        return userRole;
    }

    public void setUserRole(RoleType userRole) {
        this.userRole = userRole;
    }

    public List<User> getUserPlaylists() {
        return userPlaylists;
    }

    public void setUserPlaylists(List<User> userPlaylists) {
        this.userPlaylists = userPlaylists;
    }

    public List<Song> getSongPlaylists() {
        return songPlaylists;
    }

    public void setSongPlaylists(List<Song> songPlaylists) {
        this.songPlaylists = songPlaylists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id) && Objects.equals(name, playlist.name) && Objects.equals(description, playlist.description) && access == playlist.access && userRole == playlist.userRole && Objects.equals(userPlaylists, playlist.userPlaylists) && Objects.equals(songPlaylists, playlist.songPlaylists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, access, userRole, userPlaylists, songPlaylists);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", access=" + access +
                ", userRole=" + userRole +
                ", songPlaylists=" + songPlaylists +
                '}';
    }
}
