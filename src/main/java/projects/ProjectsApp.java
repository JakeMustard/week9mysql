package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project; // Import the Project class
import projects.service.ProjectService;

public class ProjectsApp {
    private List<String> operations = List.of("1) Add Project");
    private Scanner scanner = new Scanner(System.in);
    private ProjectService projectService = new ProjectService(null); // Initialize with a valid ProjectService

    public static void main(String[] args) {
        ProjectsApp app = new ProjectsApp();
        app.processUserSelections();
    }

    private void processUserSelections() {
        boolean done = false;
        while (!done) {
            // Display available operations
            printOperations();
            try {
                // Get the user's menu selection
                int selection = getUserSelection();
                switch (selection) {
                    case -1:
                        // Handle the case when selection is -1 (e.g., user chooses to exit)
                        done = exitMenu();
                        break;
                    case 1:
                        // Handle the case for adding a project
                        addProject();
                        break;
                    default:
                        // Display an error message for invalid selections
                        System.out.println("\nError: " + selection + " is not a valid selection. Try again.");
                        break;
                }
            } catch (Exception e) {
                // Display an error message if an exception occurs
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printOperations() {
        System.out.println("These are the available selections. Press Enter key to quit:");
        for (String operation : operations) {
            System.out.println(operation);
        }
    }

    private int getUserSelection() {
        Integer input = getIntInput("Enter a menu selection");
        return Objects.isNull(input) ? -1 : input;
    }

    private Integer getIntInput(String prompt) {
        String input = getStringInput(prompt);

        if (Objects.isNull(input)) {
            return null;
        }

        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(input + " is not a valid number. Try again.");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();

        if (input.isBlank()) {
            return null;
        } else {
            return input.trim();
        }
    }

    private boolean exitMenu() {
        System.out.println("Exiting the program.");
        // Perform any necessary cleanup or closing operations here
        return true;
    }

    private void addProject() {
        // Collect project details from the user
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
        String notes = getStringInput("Enter the project notes");

        // Create a Project object with the collected details
        Project project = new Project();
        project.setProjectName(projectName);
        project.setEstimatedHours(estimatedHours);
        project.setActualHours(actualHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);

        // Add the project using the service class
        Project addedProject = projectService.addProject(project);

        // Display the added project details
        System.out.println("You have successfully created project:");
        System.out.println("ID: " + addedProject.getId());
        System.out.println("Name: " + addedProject.getProjectName());
        System.out.println("Estimated Hours: " + addedProject.getEstimatedHours());
        System.out.println("Actual Hours: " + addedProject.getActualHours());
        System.out.println("Difficulty: " + addedProject.getDifficulty());
        System.out.println("Notes: " + addedProject.getNotes());
    }

    private BigDecimal getDecimalInput(String prompt) {
        String input = getStringInput(prompt);

        if (Objects.isNull(input)) {
            return null;
        }

        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(input + " is not a valid decimal number. Try again.");
        }
    }
}
