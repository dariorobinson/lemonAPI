package com.revature.lemon.user;

import com.revature.lemon.common.model.UserPlaylist;

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

    @Column(nullable = false, columnDefinition = "VARCHAR CHECK (username <> '')")
    private String username;

    @Column(nullable = false)
    private String discriminator;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPlaylist> playlistRole;

    public User() {
        super();
    }

    public User(String id, String username, String discriminator){
        this.id=id;
        this.username=username;
        this.discriminator=discriminator;
    }

    public User(UserPlaylist users) {
        User user = users.getUser();
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

    public List<UserPlaylist> getPlaylistRole() {
        return playlistRole;
    }

    public void setPlaylistRole(List<UserPlaylist> playlistRole) {
        this.playlistRole = playlistRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(discriminator, user.discriminator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, discriminator, playlistRole);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                '}';
    }
}
