package server.models;

import dtos.GroupDTO;

import java.util.UUID;

public class Group {
    private String id;
    private String name;

    public Group() {
    }

    public Group(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public GroupDTO toDto() {
        return new GroupDTO(this.id, this.name);
    }

    public static Group createGroup(final String name) {
        return new Group(UUID.randomUUID().toString(), name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
