package Controllers;

import java.sql.Date;
import java.sql.SQLException;

import DBaccess.*;
import Models.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

public class PostController {
    private final PostDB postDB;

    public PostController() throws SQLException {
        postDB = new PostDB();
    }

    public String createPost(String writerId, String ownerId, String text, String[] mediaPaths, int likes, int replies) throws SQLException {
        Post post = new Post();

        post.setId(String.format("%d", postDB.NumberOfPosts()));
        Post.IncTotalNumOfTweets();
        post.setWriterId(writerId);
        post.setOwnerId(ownerId);
        post.setText(text);
        post.setMediaPaths(mediaPaths);
        post.setLikes(likes);
        post.setReplies(replies);
        post.setCreatedAt(Date.valueOf(LocalDate.now()));

        postDB.savePost(post);
        return post.getId();
    }

    public void updatePost(String writerId, String ownerId, String text, String[] mediaPaths, int likes, int replies) throws SQLException {
        Post post = new Post();

        post.setWriterId(writerId);
        post.setOwnerId(ownerId);
        post.setText(text);
        post.setMediaPaths(mediaPaths);
        post.setLikes(likes);
        post.setReplies(replies);

        postDB.updatePost(post);
    }

    public void deletePost(String id) throws SQLException {
        postDB.deletePost(id);
    }

    public void getPostByWriterId(String writerId) throws SQLException {
        postDB.getPostByWriterId(writerId);
    }

    public void getPostByOwnerId(String ownerId) throws SQLException {
        postDB.getPostByOwnerId(ownerId);
    }

    public String getPostById(String id) throws SQLException, JsonProcessingException {
        Post post = getPost(id);
        if (post == null)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(post);
        return response;
    }

    public Post getPost(String id) throws SQLException {
        return postDB.getPost(id);
    }

    public String getAll() throws SQLException, JsonProcessingException {
        List<Post> posts = postDB.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(posts);
    }
    public void deleteAll() throws SQLException {
        postDB.deleteAll();
    }
}