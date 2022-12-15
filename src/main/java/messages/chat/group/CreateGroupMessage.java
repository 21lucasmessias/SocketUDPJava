package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import messages.Message;

public class CreateGroupMessage implements Message {
    @JsonProperty("createGroupMessage")
    private CreateGroup createGroupMessage;

    public CreateGroupMessage() {
    }

    public CreateGroupMessage(CreateGroup createGroupMessage) {
        this.createGroupMessage = createGroupMessage;
    }

    public CreateGroup getCreateGroupMessage() {
        return createGroupMessage;
    }

    public void setCreateGroupMessage(CreateGroup createGroupMessage) {
        this.createGroupMessage = createGroupMessage;
    }
}
