package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {

    private final ObjectMapper mapper;

    public Mapper() {
        this.mapper = new ObjectMapper();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
