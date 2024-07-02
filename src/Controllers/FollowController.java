package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import DBaccess.FollowDB;
import Models.Follow;
import java.sql.SQLException;
import java.util.List;

public class FollowController {
    private final FollowDB followDB;
    public FollowController() throws SQLException {
        followDB = new FollowDB();
    }

    public void createFollowTable() throws SQLException {
        followDB.createFollowTable();
    }

    public void saveFollow(String follower, String followed) throws SQLException {
        Follow follow = new Follow(follower, followed);
        followDB.saveFollow(follow);
    }

    public void deleteFollow(String follower, String followed) throws SQLException {
        Follow follow = new Follow(follower, followed);
        followDB.deleteFollow(follow);
    }

    public void deleteAll() throws SQLException {
        followDB.deleteAll();
    }

    public String getFollows(String userId) throws SQLException, JsonProcessingException {
        List<Follow> follows = followDB.getFollows(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getFollowers(String userId) throws SQLException, JsonProcessingException {
        List<Follow> follows = followDB.getFollowers(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getAll() throws SQLException, JsonProcessingException {
        List<Follow> follows = followDB.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }
}