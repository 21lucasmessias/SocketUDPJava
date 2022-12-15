package messages.chat.individual;

import com.fasterxml.jackson.annotation.JsonProperty;
import messages.Message;

public class PrivateChatMessage implements Message {
    @JsonProperty("privateChat")
    private PrivateChat privateChat;

    public PrivateChatMessage() {
    }

    public PrivateChatMessage(PrivateChat privateChat) {
        this.privateChat = privateChat;
    }

    public PrivateChat getPrivateChat() {
        return privateChat;
    }

    public void setPrivateChat(PrivateChat privateChat) {
        this.privateChat = privateChat;
    }

}
