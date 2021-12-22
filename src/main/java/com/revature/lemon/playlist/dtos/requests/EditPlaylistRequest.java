package com.revature.lemon.playlist.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.user.User;

public class EditPlaylistRequest {

    @JsonIgnore
    private String playlistId;

    private String name;
    private String description;
    private AccessType access;

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
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
