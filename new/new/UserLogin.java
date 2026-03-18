import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLogin {
    private Scene loginScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Stage stage;

    public UserLogin(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));
        Button loginButton = new Button("Sign In");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                validateLogin();
            }
        });
        loginLayout.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton);

        loginScene = new Scene(loginLayout, 600, 600);
        stage.setTitle("User Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Authenticate the user using the authentication service module
        User loggedInUser = AuthenticationService.authenticate(username, password);
        if (loggedInUser != null) {
            if (AuthorizationService.isAdmin(loggedInUser)) {
                AddUser addUserView = new AddUser(stage, loggedInUser);
                addUserView.initializeComponents();
            } else if (AuthorizationService.isDeveloper(loggedInUser)) {
                AddCommand addCommandView = new AddCommand(stage, loggedInUser);
                addCommandView.initializeComponents();
            } else {
                ViewCommands viewCommands = new ViewCommands(stage, loggedInUser);
                viewCommands.initializeComponents();
            }
        } else {
            showAlert("Authentication Failed", "Invalid username or password.");
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
