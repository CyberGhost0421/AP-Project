package Controllers;

import DBaccess.*;
import Models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.SecureRandom;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {
    private final UserDB userDB;

    public UserController() throws SQLException {
        userDB = new UserDB();
    }

    public void createUser(String id, String firstName, String lastName, String email, String phoneNumberType, String phoneNumber, String password, String country, String city, Date birthday, String socialLink, Date userCreatedAt) throws SQLException {
        User user = new User( id,  firstName,  lastName,  email,  phoneNumberType,  phoneNumber,  password,  country,  city,  birthday,  socialLink,  userCreatedAt );
        user.setToken(generateToken());
        if (isUserExists(id))
            userDB.updateUser(user);
        else
            userDB.saveUser(user);
    }

    public void deleteUser(String id) throws SQLException {
        User user = new User();
        user.setId(id);
        userDB.deleteUser(user);
    }

    public void deleteUsers() throws SQLException {
        userDB.deleteUsers();
    }

    public void updateUser(String id, String firstName, String lastName, String email, String phoneNumberType, String phoneNumber, String password, String country, String city, Date birthday, String socialLink, Date userCreatedAt) throws SQLException {
        User user = new User(id,  firstName,  lastName,  email,  phoneNumberType,  phoneNumber,  password,  country,  city,  birthday,  socialLink,  userCreatedAt);
        userDB.updateUser(user);
    }

    public String getUserById(String id) throws SQLException, JsonProcessingException {
        User user = userDB.getUser(id);
        if (user == null)
            return "No User";
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(user);
        return response;
    }

    public String getUsers() throws SQLException, JsonProcessingException {
        ArrayList<User> users = userDB.getUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(users);
        return response;
    }

    public String getUserByIdAndPass(String id, String pass) throws SQLException, JsonProcessingException {
        User user = userDB.getUser(id, pass);
        if (user == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(user);

        return response;
    }

    public boolean isUserExists(String ID) {
        if (ID == null) return false;
        try {
            return (userDB.getUser(ID) != null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  Generating TOKEN
     */
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 16;

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            token.append(randomChar);
        }

        return token.toString();
    }

}