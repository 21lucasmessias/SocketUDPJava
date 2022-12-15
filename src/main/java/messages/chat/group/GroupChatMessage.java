package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import messages.Message;
import messages.chat.individual.PrivateChat;

public class GroupChatMessage implements Message {
    @JsonProperty("groupChat")
    private GroupChat groupChat;

    public GroupChatMessage() {
    }

    public GroupChatMessage(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

}
