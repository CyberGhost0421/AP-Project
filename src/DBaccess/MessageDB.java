package DBaccess;

import Models.Message;

import java.sql.*;
import java.util.ArrayList;

public class MessageDB {
    private final Connection connection;

    public MessageDB() throws SQLException {DatabaseMetaData DatabaseConnectionManager = null;
        connection = DatabaseConnectionManager.getConnection();
        createMessageTable();
    }

    public void createMessageTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS messages (id VARCHAR(60) PRIMARY KEY, sender VARCHAR(36), receiver VARCHAR(36), text VARCHAR(300), messageDate bigint)");
        preparedStatement.executeUpdate();
    }

    public void saveMessage(Message message) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (id, sender, receiver, text, messageDate) VALUES (?, ?, ?, ?, ?)");
        preparedStatement.setString(1, message.getId());
        preparedStatement.setString(2, message.getSender());
        preparedStatement.setString(3, message.getReceiver());
        preparedStatement.setString(4, message.getText());
        preparedStatement.setLong(5, message.getMessageDate());
        preparedStatement.executeUpdate();
    }

    public void deleteMessage(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        preparedStatement.setString(1, id);
        preparedStatement.executeUpdate();
    }

    public void deleteAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages");
        preparedStatement.executeUpdate();
    }

    public ArrayList<Message> getMessages(String user1, String user2) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE (sender = ? AND receiver = ?) or (sender = ? AND receiver = ?)");
        statement.setString(1, user1); statement.setString(2, user2);
        statement.setString(3, user2); statement.setString(4, user1);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Message message = new Message();
            message.setId(resultSet.getString("id"));
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setMessageDate(resultSet.getLong("messageDate"));
            messages.add(message);
        }
        return messages;
    }

    public ArrayList<Message> getNotify(String receiver) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE receiver = ?");
        statement.setString(1, receiver);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Message message = new Message();
            message.setId(resultSet.getString("id"));
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setMessageDate(resultSet.getLong("messageDate"));
            messages.add(message);
        }
        return messages;
    }
}