package com.revature.lemon.userplaylist;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPlaylistRoleKey implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "playlist_id")
    private String playlistId;

    private UserPlaylistRoleKey() {

    }

    public UserPlaylistRoleKey(String userId, String playlistId) {
        this.userId = userId;
        this.playlistId = playlistId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPlaylistRoleKey that = (UserPlaylistRoleKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(playlistId, that.playlistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, playlistId);
    }
}
