package Controllers;

import DBaccess.*;
import Models.Skill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class SkillController {

    private final SkillDB skillDB;

    public SkillController() throws SQLException {
        skillDB = new SkillDB();
    }

    public void createSkill(String id, String skillTitle, String skillDetail) throws SQLException {
        Skill skill = new Skill(id, skillTitle, skillDetail);

        if (isSkillExists(id,skillTitle))
            return;
        else
            skillDB.saveSkill(skill);
    }

    public void deleteSkill(String id,String skillTitle) throws SQLException {
        Skill skill = new Skill();
        skill.setId(id);
        skill.setSkillTitle(skillTitle);
        skillDB.deleteSkill(skill);
    }

    public void updateSkill(String id, String skillTitle, String skillDetail,String previousTitle) throws SQLException {
        Skill skill = new Skill(id, skillTitle, skillDetail);
        skillDB.updateSkill(skill,previousTitle);
    }

    public String getSkillsById(String id) throws SQLException, JsonProcessingException {

        if (skillDB.getSkills(id) == null)
            return "No Skill";
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(skillDB.getSkills(id));
        return response;
    }
    public String getSkillById(String id,String title) throws SQLException, JsonProcessingException {

        if (skillDB.getSkill(id,title) == null)
            return "No Skill";
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(skillDB.getSkill(id,title));
        return response;
    }

    public boolean isSkillExists(String ID,String title) {
        if (ID == null) return false;
        try {
            return (skillDB.getSkill(ID,title) != null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
