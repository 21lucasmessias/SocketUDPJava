package server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.ChatMessageDTO;
import dtos.UserDTO;

import java.time.Instant;

public class ChatMessage {
    @JsonProperty("createdAt")
    private Instant createdAt;
    @JsonProperty("user")
    private UserDTO user;
    @JsonProperty("content")
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(final UserDTO user, final String content) {
        this.user = user;
        this.content = content;

        this.createdAt = Instant.now();
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ChatMessageDTO toDto() {
        return new ChatMessageDTO(this.user, this.content, this.createdAt);
    }
}
