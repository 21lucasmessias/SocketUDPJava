package messages.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrivateChatMessage {
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
