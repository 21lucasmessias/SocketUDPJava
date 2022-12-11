package server.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import helpers.Mapper;
import org.apache.commons.codec.digest.DigestUtils;
import server.models.User;
import dtos.UserDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Database {
    final private Mapper mapper;

    final private HashMap<String, User> users = new HashMap<>();

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

    private void saveDataToFile() {
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

    public Optional<User> login(String username, String password) {
        return this.users.values().stream().filter(user -> {
            return user.getUsername().equals(username) && user.getPassword().equals(new DigestUtils("SHA3-256").digestAsHex(password));
        }).findFirst();
    }

    public List<UserDTO> getUsers() {
        return users.values().stream().map(User::toDto).toList();
    }
}
