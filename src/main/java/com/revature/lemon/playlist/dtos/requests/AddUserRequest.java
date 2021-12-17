package com.revature.lemon.playlist.dtos.requests;

import com.revature.lemon.common.util.RoleType;

import java.util.Objects;

/**
 * DTO used to grant user access to a playlist
 */
public class AddUserRequest {

    private String username;

    private String discriminator;

    private RoleType userRole;

    //private String playlistId; suggestion for if we use this in the UserController class instead

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
        AddUserRequest that = (AddUserRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(discriminator, that.discriminator) && userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, discriminator, userRole);
    }

    @Override
    public String toString() {
        return "AddUserRequest{" +
                "username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
