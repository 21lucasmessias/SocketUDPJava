package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinGroupMessage {
    @JsonProperty("joinGroup")
    private JoinGroup joinGroup;

    public JoinGroupMessage() {
    }

    public JoinGroupMessage(JoinGroup joinGroup) {
        this.joinGroup = joinGroup;
    }

    public JoinGroup getJoinGroup() {
        return joinGroup;
    }

    public void setJoinGroup(JoinGroup joinGroup) {
        this.joinGroup = joinGroup;
    }
}
