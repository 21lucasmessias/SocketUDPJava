package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ChatMessageDTO {
    @JsonProperty("createdAt")
    private Instant createdAt;
    @JsonProperty("user")
    private UserDTO user;
    @JsonProperty("content")
    private String content;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(final UserDTO user, final String content, final Instant createdAt) {
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
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
}
