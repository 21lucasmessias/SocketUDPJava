package messages.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetUser {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;

    public SetUser() {
    }

    public SetUser(final String username, final String id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
