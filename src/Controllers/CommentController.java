package Controllers;

import DBaccess.CommentDB;
import Models.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Models.Comment;
import java.sql.SQLException;
import java.util.List;

public class CommentController {
    private final CommentDB commentDB;
    public CommentController() throws SQLException {
        commentDB = new CommentDB();
    }

    public void createCommentTable() throws SQLException {
        commentDB.createCommentTable();
    }

    public void saveComment(String commenter, String commentPost) throws SQLException {
        Comment comment = new Comment(commenter, commentPost);
        commentDB.saveComment(comment);
    }

    public void deleteComment(String commenter, String commentPost) throws SQLException {
        Comment comment = new Comment(commenter, commentPost);
        commentDB.deleteComment(comment);
    }

    public void deleteAll() throws SQLException {
        commentDB.deleteAll();
    }

    public String getComments(String Id) throws SQLException, JsonProcessingException {
        List<Comment> comments = commentDB.getComments(Id);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(comments);
    }

    public String getCommenters(String Id) throws SQLException, JsonProcessingException {
        List<Comment> comments = commentDB.getCommenters(Id);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(comments);
    }

    public String getAll() throws SQLException, JsonProcessingException {
        List<Comment> comments = commentDB.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(comments);
    }
}