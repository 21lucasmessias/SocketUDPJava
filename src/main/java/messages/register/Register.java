package messages.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Register {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public Register() {
        this.username = "";
        this.password = "";
    }

    public Register(final String username, final String password) {
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
