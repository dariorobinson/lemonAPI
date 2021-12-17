package com.revature.lemon.playlist.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.user.User;

/**
 * DTO used to create a new playlist
 */
public class NewPlaylistRequest {

    @JsonIgnore
    private User creator;

    private String name;
    private String description;
    private AccessType access;

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

    public AccessType getAccess() {
        return access;
    }

    public void setAccess(AccessType access) {
        this.access = access;
    }
}
