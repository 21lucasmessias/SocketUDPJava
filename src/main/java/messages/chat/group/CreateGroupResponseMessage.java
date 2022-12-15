package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import messages.Message;

public class CreateGroupResponseMessage implements Message {
    @JsonProperty("createGroupResponse")
    private CreateGroupResponse createGroupResponse;

    public CreateGroupResponseMessage() {
    }

    public CreateGroupResponseMessage(CreateGroupResponse createGroupResponse) {
        this.createGroupResponse = createGroupResponse;
    }

    public CreateGroupResponse getCreateGroupResponse() {
        return createGroupResponse;
    }

    public void setCreateGroupResponse(CreateGroupResponse createGroupResponse) {
        this.createGroupResponse = createGroupResponse;
    }
}
