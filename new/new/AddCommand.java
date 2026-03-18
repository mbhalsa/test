import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCommand {
    private Stage stage;
    private User currentUser;

    private TextField idField = new TextField();
    private TextField commandField = new TextField();
    private TextArea descriptionField = new TextArea();

    public AddCommand(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void initializeComponents() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button addCommandButton = new Button("Add Command");
        Button viewCommandsButton = new Button("View Commands");

        addCommandButton.setOnAction(e -> addCommand());

        layout.getChildren().addAll(
                new Label("ID:"), idField,
                new Label("Command:"), commandField,
                new Label("Description:"), descriptionField,
                addCommandButton,
                viewCommandsButton
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setTitle("Add Command");
        stage.setScene(scene);
        stage.show();
    }

    private void addCommand() {
        if (!AuthorizationService.isDeveloper(currentUser)) {
            showAlert("Access Denied", "Only developers can add commands.");
            return;
        }

        String idText = idField.getText();
        String command = commandField.getText();
        String description = descriptionField.getText();

        if (idText.isEmpty() || command.isEmpty() || description.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            Connection con = DBUtils.establishConnection();
            String query = "INSERT INTO commands (id, command, description) VALUES (?, ?, ?)";

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, command);
            statement.setString(3, description);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                showInfo("Success", "Command added successfully.");
                clearFields();
            } else {
                showAlert("Error", "Command was not added.");
            }

            DBUtils.closeConnection(con, statement);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "ID must be a number.");
        } catch (Exception e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void clearFields() {
        idField.clear();
        commandField.clear();
        descriptionField.clear();
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