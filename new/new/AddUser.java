import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddUser {
    private Stage stage;
    private User currentUser;

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private TextField roleField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();

    public AddUser(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void initializeComponents() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(e -> addUser());

        layout.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Role:"), roleField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                addUserButton
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setTitle("Add User");
        stage.setScene(scene);
        stage.show();
    }

    private void addUser() {
        if (!AuthorizationService.isAdmin(currentUser)) {
            showAlert("Access Denied", "Only admins can add users.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()
                || firstName.isEmpty() || lastName.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Connection con = DBUtils.establishConnection();
        String query = "INSERT INTO users (username, password, role, firstname, lastname) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, role);
            statement.setString(4, firstName);
            statement.setString(5, lastName);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                showInfo("Success", "User added successfully.");
                clearFields();
            } else {
                showAlert("Error", "User was not added.");
            }

            DBUtils.closeConnection(con, statement);
        } catch (Exception e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
        firstNameField.clear();
        lastNameField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}