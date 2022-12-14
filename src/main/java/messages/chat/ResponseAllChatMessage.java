package messages.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.ChatMessageDTO;

import java.util.List;

public class ResponseAllChatMessage {
    @JsonProperty("responseAllChat")
    private List<ChatMessageDTO> responseAllChat;

    public ResponseAllChatMessage() {
    }

    public ResponseAllChatMessage(List<ChatMessageDTO> responseAllChat) {
        this.responseAllChat = responseAllChat;
    }

    public List<ChatMessageDTO> getResponseAllChat() {
        return responseAllChat;
    }

    public void setResponseAllChat(List<ChatMessageDTO> responseAllChat) {
        this.responseAllChat = responseAllChat;
    }
}
