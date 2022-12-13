package messages.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetUserMessage {
    @JsonProperty("setUser")
    private SetUser setUser;

    public SetUserMessage() {
    }

    public SetUserMessage(SetUser login) {
        this.setUser = login;
    }

    public SetUser getSetUser() {
        return setUser;
    }

    public void setSetUser(SetUser setUser) {
        this.setUser = setUser;
    }
}
