package com.revature.lemon.user.dtos;

import com.revature.lemon.user.User;

/**
 * DTO used for authentication of user in login
 */
public class LoginRequest {
    private String id;
    private String username;
    private String discriminator;
    private String token;   //todo delete

    public LoginRequest(String id, String username, String discriminator){
        this.id=id;
        this.username=username;
        this.discriminator=discriminator;
    }

    public LoginRequest(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.discriminator = user.getDiscriminator();
    }

    //todo delete
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "New User Information{"+
                "id='" + id + '\'' +
                "username='" + username+ '\'' +
                "discriminator='" + discriminator + '\'' +
                "}";
    }

    //____________________________________Getter Section____________________________________
    public String getId() {return id;}
    public String getUsername() {return username;}
    public String getDiscriminator() {return discriminator;}
    //____________________________________Setter Section____________________________________
    public void setId(String id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setDiscriminator(String discriminator) {this.discriminator = discriminator;}


}
