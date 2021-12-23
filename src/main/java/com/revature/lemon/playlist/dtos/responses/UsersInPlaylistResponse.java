package com.revature.lemon.playlist.dtos.responses;

import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.user.User;
import com.revature.lemon.common.model.UserPlaylist;

import java.util.Objects;

/**
 * DTO used for a response with user information in a playlist
 */
public class UsersInPlaylistResponse {

    //might not need id to be sent to UI, because where do we store it? we don't want to show it
    private String id;

    private String username;

    private String discriminator;

    private RoleType userRole;

    public UsersInPlaylistResponse() {

    }

    public UsersInPlaylistResponse(UserPlaylist users) {
        User user = users.getUser();
        this.userRole = users.getUserRole();
        this.id = user.getId();
        this.username = user.getUsername();
        this.discriminator = user.getDiscriminator();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
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
        UsersInPlaylistResponse that = (UsersInPlaylistResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(discriminator, that.discriminator) && userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, discriminator, userRole);
    }

    @Override
    public String toString() {
        return "UsersInPlaylistResponse{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
