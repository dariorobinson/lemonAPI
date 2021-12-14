package com.revature.lemon.playlist.dtos.requests;

import java.util.Objects;

public class AddUserRequest {

    private String username;

    private String discriminator;

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
        AddUserRequest that = (AddUserRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(discriminator, that.discriminator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, discriminator);
    }

    @Override
    public String toString() {
        return "AddUserRequest{" +
                "username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                '}';
    }
}
