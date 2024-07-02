package DBaccess;
import Models.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDB {
    private final Connection connection;
    public CommentDB() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createCommentTable();
    }

    public void createCommentTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS comments (commenter VARCHAR(36), commentpost VARCHAR(36), PRIMARY KEY (commenter, commentpost))");
        preparedStatement.executeUpdate();
    }

    public void saveComment(Comment comment) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO comments (commenter, commentpost) VALUES (?, ?)");
        preparedStatement.setString(1, comment.getCommenter());
        preparedStatement.setString(2, comment.getCommentPost());
        preparedStatement.executeUpdate();
    }

    public void deleteComment(Comment comment) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM comments WHERE commenter = ? AND commentpost = ?");
        preparedStatement.setString(1, comment.getCommenter());
        preparedStatement.setString(2, comment.getCommentPost());
        preparedStatement.executeUpdate();
    }

    public void deleteAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM comments");
        preparedStatement.executeUpdate();
    }

    /**
     * @param Id
     * @return id has Commented what
     * @throws SQLException
     */
    public List<Comment> getComments(String Id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE commenter = ?");
        preparedStatement.setString(1, Id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setCommenter(resultSet.getString("commenter"));
            comment.setCommentPost(resultSet.getString("commentpost"));
            comments.add(comment);
        }
        return comments;
    }

    /**
     * @param Id
     * @return what has commented id
     * @throws SQLException
     */
    public List<Comment> getCommenters(String Id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE commenter = ?");
        preparedStatement.setString(1, Id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setCommenter(resultSet.getString("commenter"));
            comment.setCommentPost(resultSet.getString("commentpost"));
            comments.add(comment);
        }
        return comments;
    }

    public List<Comment> getAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comments");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setCommenter(resultSet.getString("commenter"));
            comment.setCommentPost(resultSet.getString("commentpost"));
            comments.add(comment);
        }
        return comments;
    }

    public boolean isCommenting(String commentId, String commentpostId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE commenter = ? AND commentpost = ?");
        preparedStatement.setString(1, commentId);
        preparedStatement.setString(2, commentpostId);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isCommenting = resultSet.next();
        return isCommenting;
    }
}