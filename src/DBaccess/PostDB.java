package DBaccess;

import Models.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDB {
    private final Connection connection;
    public PostDB() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createPostTable();
    }

    public void createPostTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS posts (id VARCHAR(36) PRIMARY KEY, writer_id VARCHAR(36) NOT NULL, owner_id VARCHAR(36) NOT NULL, text VARCHAR(280) NOT NULL, media_path TEXT[] , likes INTEGER NOT NULL, replies INTEGER NOT NULL, create_at DATE NOT NULL);");
        statement.executeUpdate();
    }

    public void deletePostsDB() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DROP TABLE IF EXISTS posts");
        statement.executeUpdate();
    }

    public void savePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (writer_id, owner_id, text, media_path, likes, replies, create_at, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, post.getWriterId());
        statement.setString(2, post.getOwnerId());
        statement.setString(3, post.getText());
        statement.setArray(4, connection.createArrayOf("text", post.getMediaPaths().toArray()));
        statement.setInt(5, post.getLikes());
        statement.setInt(6, post.getReplies());
        statement.setDate(7, new java.sql.Date(post.getCreatedAt().getTime()));
        statement.setString(8, post.getId());
        statement.executeUpdate();
        System.out.println("OK");

    }

    public void updatePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE posts SET writer_id = ?, owner_id = ?, text = ?, media_path = ?, likes = ?, replies = ?, create_at = ? WHERE id = ?");
        statement.setString(1, post.getWriterId());
        statement.setString(2, post.getOwnerId());
        statement.setString(3, post.getText());
        statement.setArray(4, connection.createArrayOf("text", post.getMediaPaths().toArray()));
        statement.setInt(5, post.getLikes());
        statement.setInt(6, post.getReplies());
        statement.setDate(7, new java.sql.Date(post.getCreatedAt().getTime()));
        statement.setString(8, post.getId());
        statement.executeUpdate();
    }

    public void deletePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE id = ?");
        statement.setString(1, post.getId());
        statement.executeUpdate();
    }

    public void deletePost(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE id = ?");
        statement.setString(1, id);
        statement.executeUpdate();
    }

    public Post getPost(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Post post = new Post();
            post.setId(resultSet.getString("id"));
            post.setWriterId(resultSet.getString("writer_id"));
            post.setOwnerId(resultSet.getString("owner_id"));
            post.setText(resultSet.getString("text"));
            Array x = resultSet.getArray("media_path");
            String[] mediapath = (String[]) x.getArray();
            post.setMediaPaths(mediapath);
            post.setLikes(resultSet.getInt("likes"));
            post.setReplies(resultSet.getInt("replies"));
            post.setCreatedAt(resultSet.getDate("create_at"));
            return post;
        }
        return null;
    }

    public Post getPost(Post post1) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM tweets WHERE id = ?");
        statement.setString(1, post1.getId());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Post post = new Post();
            post.setId(resultSet.getString("id"));
            post.setWriterId(resultSet.getString("writer_id"));
            post.setOwnerId(resultSet.getString("owner_id"));
            post.setText(resultSet.getString("text"));
            Array x = resultSet.getArray("media_path");
            String[] mediapath = (String[]) x.getArray();
            post.setMediaPaths(mediapath);
            post.setLikes(resultSet.getInt("likes"));
            post.setReplies(resultSet.getInt("replies"));
            post.setCreatedAt(resultSet.getDate("create_at"));
            return post;
        }
        return null;
    }

    public Post getPostByWriterId(String writerId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE writer_id = ?");
        statement.setString(1, writerId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Post post = new Post();
            post.setId(resultSet.getString("id"));
            post.setWriterId(resultSet.getString("writer_id"));
            post.setOwnerId(resultSet.getString("owner_id"));
            post.setText(resultSet.getString("text"));
            Array x = resultSet.getArray("media_path");
            String[] mediapath = (String[]) x.getArray();
            post.setMediaPaths(mediapath);
            post.setLikes(resultSet.getInt("likes"));
            post.setReplies(resultSet.getInt("replies"));
            post.setCreatedAt(resultSet.getDate("create_at"));
            return post;
        }
        return null;
    }

    public Post getPostByOwnerId(String ownerId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE owner_id = ?");
        statement.setString(1, ownerId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Post post = new Post();
            post.setId(resultSet.getString("id"));
            post.setWriterId(resultSet.getString("writer_id"));
            post.setOwnerId(resultSet.getString("owner_id"));
            post.setText(resultSet.getString("text"));
            Array x = resultSet.getArray("media_path");
            String[] mediapath = (String[]) x.getArray();
            post.setMediaPaths(mediapath);
            post.setLikes(resultSet.getInt("likes"));
            post.setReplies(resultSet.getInt("replies"));
            post.setCreatedAt(resultSet.getDate("create_at"));
            return post;
        }
        return null;
    }

    public List<Post> getAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM posts");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Post> posts = new ArrayList<>();

        while (resultSet.next()) {
            Post post = new Post();
            post.setId(resultSet.getString("id"));
            post.setWriterId(resultSet.getString("writer_id"));
            post.setOwnerId(resultSet.getString("owner_id"));
            post.setText(resultSet.getString("text"));
            Array x = resultSet.getArray("media_path");
            String[] mediapath = (String[]) x.getArray();
            post.setMediaPaths(mediapath);
            post.setLikes(resultSet.getInt("likes"));
            post.setReplies(resultSet.getInt("replies"));
            post.setCreatedAt(resultSet.getDate("create_at"));
            posts.add(post);
        }
        return posts;
    }
    public int NumberOfPosts() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM posts");
        ResultSet resultSet = preparedStatement.executeQuery();
        int res = 0;
        while (resultSet.next()) {
            res++;
        }
        return res;
    }

    public void deleteAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM posts");
        preparedStatement.executeUpdate();
    }

    public ArrayList<String> getPostsByDate(Date date) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM posts WHERE create_at = ?");
        statement.setDate(1, date);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> posts = new ArrayList<>();
        while (resultSet.next()) {
            posts.add(resultSet.getString("id"));
        }
        return posts;
    }
}