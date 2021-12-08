package com.revature.lemon.models;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class appUsers {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String user_id;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR CHECK (LENGTH(username) >= 4)")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR CHECK (LENGTH(password) >= 4)")
    private String password;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR CHECK (email <> '')")
    private String email;

    public appUsers(String user_id, String username, String password, String email) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // no arg constructor (just in case we need it)
    public appUsers() {
        super();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "appUsers{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
