package com.revature.lemon.user;

import com.revature.lemon.playlist.Playlist;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "unique_user_discriminator", columnNames = {"username", "discriminator"})
})
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR CHECK (username <> '')")
    private String username;

    @Column(nullable = false)
    private String discriminator;

    @ManyToMany
    @JoinTable(
        name = "users_playlists",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<Playlist> userPlaylists;

    public User() {
        super();
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

    public List<Playlist> getUserPlaylists() {
        return userPlaylists;
    }

    public void setUserPlaylists(List<Playlist> userPlaylists) {
        this.userPlaylists = userPlaylists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(discriminator, user.discriminator) && Objects.equals(userPlaylists, user.userPlaylists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, discriminator, userPlaylists);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", userPlaylists=" + userPlaylists +
                '}';
    }
}
