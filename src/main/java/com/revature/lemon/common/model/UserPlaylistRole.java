package com.revature.lemon.common.model;

import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.playlist.Playlist;
import com.revature.lemon.user.User;

import javax.persistence.*;

@Entity
public class UserPlaylistRole {

    @EmbeddedId
    private UserPlaylistRoleKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ROLE")
    private RoleType userRole;

    public UserPlaylistRole() {
    }

    public UserPlaylistRoleKey getId() {
        return id;
    }

    public void setId(UserPlaylistRoleKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
