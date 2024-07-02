package Controllers;

import DBaccess.LikeDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Models.Like;
import java.sql.SQLException;
import java.util.List;

public class LikeController {
    private final LikeDB likeDB;
    public LikeController() throws SQLException {
        likeDB = new LikeDB();
    }

    public void createLikeTable() throws SQLException {
        likeDB.createLikeTable();
    }

    public void saveLike(String liker, String likedPost) throws SQLException {
        Like like = new Like(liker, likedPost);
        likeDB.saveLike(like);
    }

    public void deleteLike(String liker, String likedPost) throws SQLException {
        Like like = new Like(liker, likedPost);
        likeDB.deleteLike(like);
    }

    public void deleteAll() throws SQLException {
        likeDB.deleteAll();
    }

    public String getLikes(String Id) throws SQLException, JsonProcessingException {
        List<Like> likes = likeDB.getLikes(Id);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public String getLikers(String Id) throws SQLException, JsonProcessingException {
        List<Like> likes = likeDB.getLikers(Id);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public String getAll() throws SQLException, JsonProcessingException {
        List<Like> likes = likeDB.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }
}