package projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import projects.entity.Project; // Import the correct Project class
import projects.dao.DbConnection;

public class ProjectDao {

    // Insert a new project into the database
    public static int insertProject(Project project) {
        String sql = "INSERT INTO projects (projectName, estimatedHours, actualHours, difficulty, notes) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setBigDecimal(2, project.getEstimatedHours());
            preparedStatement.setBigDecimal(3, project.getActualHours());
            preparedStatement.setInt(4, project.getDifficulty());
            preparedStatement.setString(5, project.getNotes());
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int projectId = generatedKeys.getInt(1);
                    project.setId(projectId); // Set the generated ID in the project object
                    return projectId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
        
        return -1; // Insertion failed
    }

    // Retrieve a project by its ID from the database
    public static Project getProjectById(int projectId) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, projectId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToProject(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
        
        return null; // Project not found
    }

    // Retrieve a list of all projects from the database
    public static List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                projects.add(mapResultSetToProject(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
        
        return projects;
    }

    // Helper method to map a ResultSet row to a Project object
    private static Project mapResultSetToProject(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt("id"));
        project.setProjectName(resultSet.getString("projectName"));
        project.setEstimatedHours(resultSet.getBigDecimal("estimatedHours"));
        project.setActualHours(resultSet.getBigDecimal("actualHours"));
        project.setDifficulty(resultSet.getInt("difficulty"));
        project.setNotes(resultSet.getString("notes"));
        return project;
    }
}
