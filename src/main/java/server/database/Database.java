package server.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import dtos.ChatMessageDTO;
import dtos.GroupDTO;
import dtos.UserDTO;
import helpers.Mapper;
import org.apache.commons.codec.digest.DigestUtils;
import server.models.ChatMessage;
import server.models.Group;
import server.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Database {
    final private Mapper mapper;
    final private HashMap<String, User> users = new HashMap<>();
    final private HashMap<String, Group> groups = new HashMap<>();
    final private HashMap<String, List<ChatMessage>> messages = new HashMap<>();

    public Database() {
        this.mapper = new Mapper();
        this.loadDataFromFile();
    }

    private void loadDataFromFile() {
        try {
            Path path = Paths.get("src/main/java/server/database/users.txt");
            File myObj = new File(String.valueOf(path));
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                final User user = this.mapper.getMapper().readValue(data, User.class);
                this.users.put(user.getId(), user);
                this.messages.put(user.getId(), new ArrayList<>());
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private synchronized void saveDataToFile() {
        try {
            Path path = Paths.get("src/main/java/server/database/users.txt");
            FileWriter myWriter = new FileWriter(String.valueOf(path));

            users.forEach((id, user) -> {
                try {
                    myWriter.write(mapper.getMapper().writeValueAsString(user) + '\n');
                } catch (IOException e) {
                    System.out.println("Problem to deserialize user: " + user.toString());
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public synchronized Optional<User> login(String username, String password) {
        return this.users.values().stream().filter(user -> {
            return user.getUsername().equals(username) && user.getPassword().equals(new DigestUtils("SHA3-256").digestAsHex(password));
        }).findFirst();
    }

    public synchronized boolean register(String username, String password) {
        final Optional<User> foundUser = this.users.values().stream().filter(user -> {
            return user.getUsername().equals(username);
        }).findFirst();

        final boolean loginAlreadyExists = foundUser.isPresent();

        if (!loginAlreadyExists) {
            final var newUser = User.from(username, new DigestUtils("SHA3-256").digestAsHex(password));
            this.users.put(newUser.getId(), newUser);

            this.saveDataToFile();

            return true;
        }

        return false;
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }

    public List<UserDTO> getOnlineUsersList() {
        return users.values().stream().filter(user -> user.getSocket() != null && !user.getSocket().isClosed()).map(User::toDto).toList();
    }

    public List<GroupDTO> getGroupsList() {
        return groups.values().stream().map(Group::toDto).toList();
    }

    public HashMap<String, Group> getGroups() {
        return this.groups;
    }

    public HashMap<String, List<ChatMessage>> getHashMessages() {
        return messages;
    }

    public List<ChatMessageDTO> getPrivateHistoryMessages(String from, String to) {
        final var listOfMessages = messages.get(from);
        if (listOfMessages != null) {
            return listOfMessages
                    .stream().filter(chatMessage -> chatMessage.getUser().getId().equals(to))
                    .map(ChatMessage::toDto)
                    .toList();
        }

        return List.of();
    }
}
