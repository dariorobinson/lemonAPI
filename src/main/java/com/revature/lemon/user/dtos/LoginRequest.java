package com.revature.lemon.user.dtos;

/**
 * DTO used for authentication of user in login
 */
public class LoginRequest {
    private String id;
    private String username;
    private String discriminator;

    public LoginRequest(String id, String username, String discriminator){
        this.id=id;
        this.username=username;
        this.discriminator=discriminator;
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
