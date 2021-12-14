package com.revature.lemon.playlist.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.lemon.user.User;

public class NewPlaylistRequest {

    @JsonIgnore
    private User creator;

    private String name;
    private String description;
    private String access;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
