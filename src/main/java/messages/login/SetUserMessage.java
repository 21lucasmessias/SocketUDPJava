package messages.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import messages.Message;

public class SetUserMessage implements Message {
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
