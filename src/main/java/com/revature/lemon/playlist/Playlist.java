package com.revature.lemon.playlist;

import com.revature.lemon.common.model.SongPlaylistKey;
import com.revature.lemon.common.util.AccessType;
import com.revature.lemon.common.util.RoleType;
import com.revature.lemon.playlist.dtos.requests.NewPlaylistRequest;
import com.revature.lemon.common.model.UserPlaylist;
import com.revature.lemon.common.model.SongPlaylist;
import com.revature.lemon.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @Column(name = "playlist_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar DEFAULT 'PRIVATE' CHECK (access in ('PRIVATE', 'PUBLIC'))")
    private AccessType access;

    //CascadeType is used to let hibernate know that it needs to persist/remove/merge etc. cascaded tables when playlist table gets updated
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPlaylist> userRoleList;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongPlaylist> songOrderList;

    public Playlist() {
        super();
    }

    public Playlist(NewPlaylistRequest playlistRequest) {
       this.name = playlistRequest.getName();
       this.description = playlistRequest.getDescription();
       this.access = playlistRequest.getAccess();
       addCreator(playlistRequest.getCreator());
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

    public void setUserRoleList(List<UserPlaylist> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public List<UserPlaylist> getUserRoleList() {
        return userRoleList;
    }

    public List<SongPlaylist> getSongOrderList() {
        return songOrderList;
    }

    public void setSongOrderList(List<SongPlaylist> songOrderList) {
        if(!songOrderList.isEmpty()) {
            this.songOrderList.clear();
        } else if (this.songOrderList == null) {
            this.songOrderList = new LinkedList<SongPlaylist>();
        }
        this.songOrderList.addAll(songOrderList);
    }

    /**
     * Used to create UserPlaylistRole with given session user and currently selected playlist.
     * Assigns the "CREATOR" role and creates a row for Playlist in PlaylistRole
     * @param user current session user
     */
    public void addCreator(User user) {
        UserPlaylist newUser = new UserPlaylist(user, this);
        List<UserPlaylist> newPlaylistRole = new ArrayList<>();
        setUserRoleList(newPlaylistRole);
        newUser.setUserRole(RoleType.CREATOR);
        userRoleList.add(newUser);
    }

    /**
     * Add user into the current list of users
     * @param user is the user to be granted access to playlist
     * @param role is the role to be granted to user
     */
    public void addUser(User user, RoleType role) {
        UserPlaylist newUser = new UserPlaylist(user, this);
        newUser.setUserRole(role);
        userRoleList.add(newUser);
    }

    public void removeUser(UserPlaylist user) {
        userRoleList.remove(user);
    }

    public void addSong(SongPlaylist songPlaylist) {
        songPlaylist.setPlaylist(this);
        songPlaylist.setId(new SongPlaylistKey(songPlaylist.getSong().getUrl(), getId()));
        songOrderList.add(songPlaylist);
    }

    public void removeSong(SongPlaylist songPlaylist) {
        songOrderList.remove(songPlaylist);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id) && Objects.equals(name, playlist.name) && Objects.equals(description, playlist.description) && access == playlist.access && Objects.equals(userRoleList, playlist.userRoleList) && Objects.equals(songOrderList, playlist.songOrderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, access, userRoleList, songOrderList);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", access=" + access +
                ", playlistRole=" + userRoleList +
                ", songOrder=" + songOrderList +
                '}';
    }
}
