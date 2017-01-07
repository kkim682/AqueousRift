package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import main.MainFXApplication;
import model.UserDataObject;
import classes.User;
import classes.Name;
import classes.UserType;
import java.util.Optional;

/**
 * Controller class for registration page.
 */
public class RegisterController implements IController {

    private MainFXApplication mainApplication;

    private UserDataObject userDAO = UserDataObject.getInstance();
    
    @FXML
    private Button cancel;

    @FXML
    private Button ok;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private ComboBox<String> prefix;

    @FXML
    private TextField email;

    @FXML
    private TextField pNumber;

    @FXML
    private ComboBox<UserType> userType = new ComboBox<>();

    @FXML
    private ComboBox<String> securityQuestion = new ComboBox<>();

    @FXML
    private TextField securityAnswer;

    /**
     * Initializes item (comboBox)
     */
    @FXML
    private void initialize() {
        userType.getItems().setAll(UserType.values());
        userType.setValue((UserType.GeneralUser));
        prefix.getItems().setAll("Mr.", "Ms.", "Mrs.");
        prefix.setValue("Mr.");
        securityQuestion.getItems().setAll("What city were you born in?",
            "What is your mother's maiden name?",
            "What is the name of your first pet?",
            "What is your father's middle name?",
            "Who are your two favorite teachers from high school?");
    }


    /**
     * Button handler for creating an account page.
     * Clicking OK button will store new user information and create an account.
     * Clicking Cancel button will redirect to the welcome page.
     *
     * @param event the button user clicks.
     */
    @FXML
    private void handleButtonClicked(ActionEvent event) {
        if (event.getSource() == cancel) {
            mainApplication.showLoginScreen();
        } else if (event.getSource() == ok) {
            Boolean emptyFieldsExist = validateUserRegistration();
            if (emptyFieldsExist) {
                Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
                emptyAlert.setTitle("Empty fields");
                emptyAlert.setHeaderText("Please fill out all "
                        + "the required fields.");
                emptyAlert.showAndWait();
            } else if (!password.getText().equals(
                    confirmPassword.getText())) {
                Alert passwordAlert = new Alert(Alert.AlertType.WARNING);

                passwordAlert.setTitle("Password");
                passwordAlert.setHeaderText("Your confirmed password "
                        + "does not match the new password field.");

                firstName.setStyle("-fx-border-width: 0px ;");
                lastName.setStyle("-fx-border-width: 0px ;");
                email.setStyle("-fx-border-width: 0px ;");
                password.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                confirmPassword.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");

                passwordAlert.showAndWait();
            } else if (userDAO.userExists(username.getText())) {
                Alert takenAlert = new Alert(Alert.AlertType.WARNING);
                takenAlert.setTitle("Password");
                takenAlert.setHeaderText("Your chosen username "
                        + "is already taken! Please choose "
                        + "another username.");
                
                password.setStyle("-fx-border-width: 0px ;");
                confirmPassword.setStyle("-fx-border-width: 0px ;");
                firstName.setStyle("-fx-border-width: 0px ;");
                lastName.setStyle("-fx-border-width: 0px ;");
                email.setStyle("-fx-border-width: 0px ;");
                username.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");

                takenAlert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm registration");
                alert.setHeaderText("Are you sure above"
                        + " information is correct?\n"
                        + "Click \"OK\" to confirm.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //TODO check & display error if userID or email
                    // is already in use.
                    String pre = prefix.getValue();
                    if (pre == null) {
                        pre = "";
                    }
                    Name name = new Name(firstName.getText(),
                            lastName.getText(), pre);
                    User testUser = new User(password.getText(),
                            email.getText(),
                            pNumber.getText(), "4", name,
                            securityQuestion.getValue(),
                            userType.getValue().toString());
                    testUser.setSecurityAnswer(securityAnswer.getText());
                    UserDataObject userDAO = UserDataObject.getInstance();
                    userDAO.addSingleUser(testUser, username.getText());
                    mainApplication.showLoginScreen();
                } else {
                    alert.close();
                }
            }
        }
    }

    /**
     * Validates the user Registration inputs.  Simple as of now, just checks
     * that required fields aren't empty.
     * TODO - REGEX matching
     * @return An alert message describing the input errors, empty String
     * if input is valid.
     */
    private Boolean validateUserRegistration() {
        int emptyFields = 0;

        if (username.getText().length() == 0) {
            username.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            username.setStyle("-fx-border-width: 0px ;");
        }

        if (password.getText().length() == 0) {
            confirmPassword.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            password.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            password.setStyle("-fx-border-width: 0px ;");
            confirmPassword.setStyle("-fx-border-width: 0px ;");
        }

        if (firstName.getText().length() == 0) {
            firstName.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            firstName.setStyle("-fx-border-width: 0px ;");
        }

        if (lastName.getText().length() == 0) {
            lastName.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            lastName.setStyle("-fx-border-width: 0px ;");
        }


        if (email.getText().length() == 0) {
            email.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            email.setStyle("-fx-border-width: 0px ;");
        }

        if (pNumber.getText().length() == 0) {
            pNumber.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            pNumber.setStyle("-fx-border-width: 0px ;");
        }

        if (securityAnswer.getText().length() == 0) {
            securityAnswer.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
        } else {
            securityAnswer.setStyle("-fx-border-width: 0px ;");
        }

        return (emptyFields != 0);
    }

    /**
     * Gives the controller access to mainApplication.
     *
     * @param mainFXApplication mainFXApplication
     */
    public void setMainApp(MainFXApplication mainFXApplication) {
        mainApplication = mainFXApplication;
    }

}
