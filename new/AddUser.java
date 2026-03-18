import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        System.out.println("Add User button clicked");
    }
}