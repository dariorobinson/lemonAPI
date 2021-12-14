package com.revature.lemon.userplaylist;

import com.revature.lemon.playlist.Playlist;
import com.revature.lemon.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserPlaylistRole {

    @EmbeddedId
    private UserPlaylistRoleKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Column()
    private String userRole;

    public UserPlaylistRole() {
    }

    public UserPlaylistRole(User user, Playlist playlist) {
        this.user = user;
        this.playlist = playlist;
        setId(new UserPlaylistRoleKey(user.getId(), playlist.getId()));
    }

    public UserPlaylistRoleKey getId() {
        return id;
    }

    public void setId(UserPlaylistRoleKey id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPlaylistRole that = (UserPlaylistRole) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(playlist, that.playlist) && Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, playlist, userRole);
    }

    @Override
    public String toString() {
        return "UserPlaylistRole{" +
                "id=" + id +
                '}';
    }
}
