package DBaccess;
import Models.Education;
import Models.Skill;
import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public class SkillDB {

    private final Connection connection;

    public SkillDB() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createSkillTable();
    }

    public void createSkillTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS skills (id VARCHAR(36), skillTitle VARCHAR(255), skillDetail VARCHAR(255))");
        statement.executeUpdate();
    }

    public void saveSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO skills (id , skillTitle, skillDetail) VALUES (?, ?, ?)");
        statement.setString(1, skill.getId());
        statement.setString(2, skill.getSkillTitle());
        statement.setString(3, skill.getSkillDetail());
        statement.executeUpdate();

    }

    public Skill getSkill(String userId,String title) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills WHERE id = ? AND skillTitle = ?");
        statement.setString(1, userId);
        statement.setString(2, title);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Skill skill = new Skill();
            skill.setId(resultSet.getString("id"));
            skill.setSkillTitle(resultSet.getString("skillTitle"));
            skill.setSkillDetail(resultSet.getString("skillDetail"));

            return skill;
        }

        return null; // Skill not found
    }

    public void updateSkill(Skill skill,String previousTitle) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE skills SET id = ?, skillTitle = ?, skillDetail = ?  WHERE id = ? AND skillTitle = ?");
        statement.setString(1, skill.getId());
        statement.setString(2, skill.getSkillTitle());
        statement.setString(3, skill.getSkillDetail());
        statement.setString(4, skill.getId());
        statement.setString(5, previousTitle);


        statement.executeUpdate();
    }

    public void deleteSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE id = ? AND skillTitle = ?");
        statement.setString(1, skill.getId());
        statement.setString(2, skill.getSkillTitle());
        statement.executeUpdate();
    }

    public ArrayList<Skill> getSkills(String userId) throws SQLException {
        ArrayList<Skill> skills = new ArrayList<Skill>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM skills WHERE id = ?");
        statement.setString(1,userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Skill skill = new Skill();
            skill.setId(resultSet.getString("id"));
            skill.setSkillTitle(resultSet.getString("skillTitle"));
            skill.setSkillDetail(resultSet.getString("skillDetail"));

            skills.add(skill);
        }

        return skills;
    }

}
