package Controllers;

import DBaccess.MessageDB;
import Models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageController {
    private final MessageDB messageDB;

    public MessageController() throws SQLException {
        messageDB = new MessageDB();
    }

    public void addMessage(String id, String sender, String receiver, String text) throws SQLException {
        Message message = new Message(id, sender, receiver, text);
        messageDB.saveMessage(message);
    }

    public String getMessages(String user1, String user2) throws SQLException, JsonProcessingException {
        ArrayList<Message> messages = messageDB.getMessages(user1, user2);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(messages);
        return response;
    }

    public void deleteMessage(String id) throws SQLException {
        messageDB.deleteMessage(id);
    }

    public void deleteAll() throws SQLException {
        messageDB.deleteAll();
    }

    public String getNotify(String receiver, int count) throws SQLException, JsonProcessingException {
        ArrayList<Message> messages = messageDB.getNotify(receiver);
        count = Integer.min(count, messages.size());
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(messages.subList(messages.size() - count, messages.size()));
        return response;
    }
}