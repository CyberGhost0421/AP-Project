package Controllers;

import DBaccess.*;
import Models.Education;
import Models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducationController {
    private final EducationDB educationDB;

    public EducationController() throws SQLException {
        educationDB = new EducationDB();
    }

    public void createEducation(String id, String institute, String study, Date startedDate, Date finishedDate, String grade, String activities, String description) throws SQLException {
        Education education = new Education(id, institute, study, startedDate, finishedDate, grade, activities, description);

        if (isEducationExists(id))
            educationDB.updateEducation(education);
        else
            educationDB.saveEducation(education);
    }

    public void deleteEducation(String id) throws SQLException {
        Education education = new Education();
        education.setId(id);
        educationDB.deleteEducation(education);
    }

    public void updateEducation(String id, String institute, String study,Date startedDate, Date finishedDate, String grade, String activities, String description) throws SQLException {
        Education education = new Education(id, institute, study, startedDate, finishedDate, grade, activities, description);
        educationDB.updateEducation(education);
    }

    public String getEducationById(String id) throws SQLException, JsonProcessingException {

        if (educationDB.getEducation(id) == null)
            return "No Education";
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(educationDB.getEducation(id));
        return response;
    }

    public boolean isEducationExists(String ID) {
        if (ID == null) return false;
        try {
            return (educationDB.getEducation(ID) != null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}