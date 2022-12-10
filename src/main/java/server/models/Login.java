package server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public Login() {
        this.username = "";
        this.password = "";
    }

    public Login(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
