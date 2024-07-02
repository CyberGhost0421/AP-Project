package DBaccess;

import Models.Education;
import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EducationDB {

    private final Connection connection;

    public EducationDB() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createEducationTable();
    }

    public void createEducationTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS educations (id VARCHAR(36) PRIMARY KEY, institute VARCHAR(255), study VARCHAR(255), startedDate DATE,finishedDate DATE, grade VARCHAR(36) , activities VARCHAR(500), description VARCHAR(1000))");
        statement.executeUpdate();
    }

    public void saveEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO educations (id , institute, study , startedDate,finishedDate, grade, activities, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, education.getId());
        statement.setString(2, education.getInstitute());
        statement.setString(3, education.getStudy());
        statement.setDate(4, new java.sql.Date(education.getStartedDate().getTime()));
        statement.setDate(5, new java.sql.Date(education.getFinishedDate().getTime()));
        statement.setString(6, education.getGrade());
        statement.setString(7, education.getActivities());
        statement.setString(8, education.getDescription());

        statement.executeUpdate();

    }

    public Education getEducation(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Education education = new Education();
            education.setId(resultSet.getString("id"));
            education.setInstitute(resultSet.getString("institute"));
            education.setStudy(resultSet.getString("study"));
            education.setStartedDate(resultSet.getDate("startedDate"));
            education.setFinishedDate(resultSet.getDate("finishedDate"));
            education.setGrade(resultSet.getString("grade"));
            education.setActivities(resultSet.getString("activities"));
            education.setDescription(resultSet.getString("description"));

            return education;
        }

        return null; // User not found
    }

    public void updateEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE educations SET id = ?, institute = ?, study = ?,startedDate = ?, finishedDate = ? ,  grade = ?, activities = ? , description = ?  WHERE id = ?");
        statement.setString(1, education.getId());
        statement.setString(2, education.getInstitute());
        statement.setString(3, education.getStudy());
        statement.setDate(4, new java.sql.Date(education.getStartedDate().getTime()));
        statement.setDate(5, new java.sql.Date(education.getFinishedDate().getTime()));
        statement.setString(6, education.getGrade());
        statement.setString(7, education.getActivities());
        statement.setString(8, education.getDescription());
        statement.setString(9, education.getId());

        statement.executeUpdate();
    }

    public void deleteEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM educations WHERE id = ?");
        statement.setString(1, education.getId());
        ResultSet resultSet = statement.executeQuery();
    }
}
