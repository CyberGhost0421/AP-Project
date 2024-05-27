package DBaccess;

import Models.User;

import java.sql.*;
import java.util.ArrayList;
public class UserDB {
    private final Connection connection;
    public UserDB() throws SQLException {
        DatabaseMetaData DatabaseConnectionManager = null;
        connection = DatabaseConnectionManager.getConnection();
        createUserTable();
    }


    public void createUserTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id VARCHAR(36) PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255), email VARCHAR(255), phoneNumberType VARCHAR(255) , phone_number VARCHAR(255), password VARCHAR(255), country VARCHAR(255) , city VARCHAR(255) , birthday DATE , socialLink VARCHAR(255) , created_at DATE)");
        statement.executeUpdate();
    }

    public void saveUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, first_name, last_name, email, phoneNumberType ,phone_number, password, country,city, birthday,socialLink, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, user.getId());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getPhoneNumberType());
        statement.setString(6, user.getPhoneNumber());
        statement.setString(7, user.getPassword());
        statement.setString(8, user.getCountry());
        statement.setString(9, user.getCity());
        statement.setDate(10, new java.sql.Date(user.getBirthday().getTime()));
        statement.setString(11, user.getSocialLink());
        statement.setDate(12, new java.sql.Date(user.getUserCreatedAt().getTime()));

        statement.executeUpdate();
    }

    public void deleteUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        statement.setString(1, user.getId());
        statement.executeUpdate();
    }

    public void deleteUsers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM users");
        statement.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ?, email = ?,phoneNumberType = ?, phone_number = ? ,  password = ?, country = ? , city = ? , birthday = ? , socialLink = ? WHERE id = ?");
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPhoneNumberType());
        statement.setString(5, user.getPhoneNumber());
        statement.setString(6, user.getPassword());
        statement.setString(7, user.getCountry());
        statement.setString(8, user.getCity());
        statement.setDate(9, new java.sql.Date(user.getBirthday().getTime()));
        statement.setString(10, user.getSocialLink());
        statement.setString(11, user.getId());

        statement.executeUpdate();
    }

    public User getUser(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setPassword(resultSet.getString("password"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setSocialLink(resultSet.getString("socialLink"));
            user.setPhoneNumberType(resultSet.getString("phoneNumberType"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setUserCreatedAt(resultSet.getDate("created_at"));
            return user;
        }

        return null; // User not found
    }

    public User getUser(String userId, String userPass) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ? AND password = ?");
        statement.setString(1, userId);
        statement.setString(2, userPass);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setPassword(resultSet.getString("password"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setSocialLink(resultSet.getString("socialLink"));
            user.setPhoneNumberType(resultSet.getString("phoneNumberType"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setUserCreatedAt(resultSet.getDate("created_at"));

            return user;
        }

        return null; // User not found
    }

    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setPassword(resultSet.getString("password"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setSocialLink(resultSet.getString("socialLink"));
            user.setPhoneNumberType(resultSet.getString("phoneNumberType"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setUserCreatedAt(resultSet.getDate("created_at"));
            users.add(user);
        }

        return users;
    }
}
