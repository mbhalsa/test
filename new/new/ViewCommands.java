import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewCommands {
    private Stage stage;
    private User currentUser;

    public ViewCommands(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public void initializeComponents() {
        if (currentUser == null) {
            showAlert("Access Denied", "You must be logged in to view commands.");
            return;
        }

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label titleLabel = new Label("Git Commands");
        TextArea commandsArea = new TextArea();
        commandsArea.setEditable(false);
        commandsArea.setPrefHeight(500);

        loadCommands(commandsArea);

        layout.getChildren().addAll(titleLabel, commandsArea);

        Scene scene = new Scene(layout, 700, 600);
        stage.setTitle("View Commands");
        stage.setScene(scene);
        stage.show();
    }

    private void loadCommands(TextArea commandsArea) {
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM commands";

        try {
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            StringBuilder output = new StringBuilder();
            output.append(String.format("%-5s %-30s %-80s%n", "ID", "Command", "Description"));
            output.append("--------------------------------------------------------------------------------------------------------------\n");

            while (rs.next()) {
                output.append(String.format(
                        "%-5d %-30s %-80s%n",
                        rs.getInt("id"),
                        rs.getString("command"),
                        rs.getString("description")
                ));
            }

            commandsArea.setText(output.toString());
            DBUtils.closeConnection(con, statement);
        } catch (Exception e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}