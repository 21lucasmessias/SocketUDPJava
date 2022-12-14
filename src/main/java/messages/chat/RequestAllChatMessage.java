package messages.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestAllChatMessage {
    @JsonProperty("requestAllChat")
    private RequestAllChat requestAllChat;

    public RequestAllChatMessage() {
    }

    public RequestAllChatMessage(RequestAllChat requestAllChat) {
        this.requestAllChat = requestAllChat;
    }

    public RequestAllChat getRequestAllChat() {
        return requestAllChat;
    }

    public void setRequestAllChat(RequestAllChat requestAllChat) {
        this.requestAllChat = requestAllChat;
    }
}
