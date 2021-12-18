package com.revature.lemon.userplaylist;

import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.playlist.Playlist;
import com.revature.lemon.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserPlaylist {

    @EmbeddedId
    private UserPlaylistKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Enumerated(EnumType.STRING)
    @Column()
    private RoleType userRole;

    public UserPlaylist() {
    }

    public UserPlaylist(User user, Playlist playlist) {
        this.user = user;
        this.playlist = playlist;
        setId(new UserPlaylistKey(user.getId(), playlist.getId()));
    }

    public UserPlaylistKey getId() {
        return id;
    }

    public void setId(UserPlaylistKey id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public RoleType getUserRole() {
        return userRole;
    }

    public void setUserRole(RoleType userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPlaylist that = (UserPlaylist) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(playlist, that.playlist) && Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, playlist, userRole);
    }

    @Override
    public String toString() {
        return "UserPlaylist{" +
                "id=" + id +
                ", user=" + user +
                ", userRole=" + userRole +
                '}';
    }
}
