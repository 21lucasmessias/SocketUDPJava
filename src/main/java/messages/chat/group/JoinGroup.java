package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinGroup {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("groupId")
    private String groupId;

    public JoinGroup() {
    }

    public JoinGroup(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
