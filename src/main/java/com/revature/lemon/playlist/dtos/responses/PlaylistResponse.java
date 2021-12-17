package com.revature.lemon.playlist.dtos.responses;

import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.playlist.Playlist;

import java.util.Objects;

/**
 * DTO used for showing list of playlists
 */
public class PlaylistResponse {

    private String id;

    private String name;

    private String description;

    private AccessType access;

    public PlaylistResponse() {

    }

    public PlaylistResponse(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.description = playlist.getDescription();
        this.access = playlist.getAccess();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistResponse that = (PlaylistResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(access, that.access);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, access);
    }

    @Override
    public String toString() {
        return "PlaylistResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}
