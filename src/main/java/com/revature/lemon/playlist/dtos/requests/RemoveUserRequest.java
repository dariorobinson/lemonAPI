package com.revature.lemon.playlist.dtos.requests;

import java.util.Objects;

/**
 * DTO used to remove user access from a playlist
 */
public class RemoveUserRequest {

    private String username;

    private String discriminator;

    //private String playlistId; suggestion for if we use this in the UserController class instead

    public RemoveUserRequest() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveUserRequest that = (RemoveUserRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(discriminator, that.discriminator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, discriminator);
    }

    @Override
    public String toString() {
        return "RemoveUserRequest{" +
                "username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                '}';
    }
}
