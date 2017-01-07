package controller;

import classes.User;
import classes.PasswordRecoveryManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.MainFXApplication;
import model.UserDataObject;

import java.util.Optional;

/**
 * Controller class for finding password page.
 */
public class FindPasswordController implements IController {
    private MainFXApplication mainApplication;
    private UserDataObject userDAO = UserDataObject.getInstance();
    private User user;

    private BooleanProperty showSecurityQuestion = new SimpleBooleanProperty();

    @FXML private Button cancel;
    @FXML private Label usernameLabel;
    @FXML private Label lastNameLabel;
    @FXML private TextField username;
    @FXML private TextField lastName;
    @FXML private Button viaSecurityQuestion;
    @FXML private Button viaText;
    @FXML private GridPane viaQuestionGrid;
    @FXML private Button securityOK;
    @FXML private Button securityCancel;
    @FXML private Text question;
    @FXML private TextField answer;

    private int attempts = 4;


    /**
     * initialize
     **/
    @FXML
    private void initialize() {
        viaQuestionGrid.visibleProperty().bind(showSecurityQuestion);
        usernameLabel.visibleProperty().bind(showSecurityQuestion.not());
        lastNameLabel.visibleProperty().bind(showSecurityQuestion.not());
        username.visibleProperty().bind(showSecurityQuestion.not());
        lastName.visibleProperty().bind(showSecurityQuestion.not());
        cancel.visibleProperty().bind(showSecurityQuestion.not());
        viaText.visibleProperty().bind(showSecurityQuestion.not());
        viaSecurityQuestion.visibleProperty().bind(showSecurityQuestion.not());
    }

    /**
     * Button handler for find password page.
     * Clicking OK button will compare user input with stored model.
     * If the input matches the model, user's password will be [...]
     * Clicking Cancel button will redirect to the welcome page.
     *
     * @param event the button user clicks.
     */

    @FXML
    private void handleButtonClicked(ActionEvent event) {
        username.setStyle(
                "-fx-border-width: 0px ;");
        lastName.setStyle(
                "-fx-border-width: 0px ;");
        if (event.getSource() == cancel) {
            mainApplication.showLoginScreen();
        } else if (userDAO.userExists(username.getText())) {
            username.setStyle(
                "-fx-border-width: 0px ;");
            user = userDAO.getUser(username.getText());
            if (event.getSource() == viaSecurityQuestion) {
                if (user.getName().getLastName().equals(lastName.getText())) {
                    showSecurityQuestion.setValue(true);
                    question.setText(user.getSecurityQuestion());
                } else {
                    lastName.setStyle(
                        "-fx-border-color: red ; -fx-border-width: 2px ;");
                    Alert lastNameAlert = new Alert(Alert.AlertType.WARNING);
                    lastNameAlert.setTitle("Wrong Lat Name");
                    lastNameAlert.setHeaderText("The last name entered"
                        + " is incorrect and does not match the last name given"
                        + " for the user entered.");
                    lastNameAlert.showAndWait();
                }
            } else if (event.getSource() == viaText) {
                if (user.getName().getLastName().equals(lastName.getText())) {
                    PasswordRecoveryManager toSend =
                        new PasswordRecoveryManager(user.getPhoneNum(),
                        user.getPassword());
                    toSend.sendPassword();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Text sent");
                    String temp = "New password has been texted to "
                        + user.getPhoneNum();
                    alert.setHeaderText(temp);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        mainApplication.showLoginScreen();
                    }
                } else {
                    lastName.setStyle(
                        "-fx-border-color: red ; -fx-border-width: 2px ;");
                    Alert lastNameAlert = new Alert(Alert.AlertType.WARNING);
                    lastNameAlert.setTitle("Wrong Last Name");
                    lastNameAlert.setHeaderText("The last name entered"
                        + " is incorrect and does not\n"
                            + "match the last name given"
                        + " for the user entered.");
                    lastNameAlert.showAndWait();
                }
            }
        } else {
            username.setStyle(
                "-fx-border-color: red ; -fx-border-width: 2px ;");
            Alert userNameAlert = new Alert(Alert.AlertType.WARNING);
            userNameAlert.setTitle("Invalid User Name");
            userNameAlert.setHeaderText("The username entered does not exist.");
            userNameAlert.showAndWait();
        }
    }

    /**
     * switches to security question screen
     * @param event clicking button
     **/
    @FXML
    private void handleSecurityButton(ActionEvent event) {
        if (event.getSource() == cancel) {
            mainApplication.showLoginScreen();
        } else if (event.getSource() == securityCancel) {
            showSecurityQuestion.setValue(false);
        } else if (event.getSource() == securityOK) {
            Alert wrongAnswerAlert = new Alert(Alert.AlertType.WARNING);
            wrongAnswerAlert.setTitle("Wrong Answer");
            String alertMessage = "";
            
            if (user.getSecurityAnswer().equals(answer.getText())) {
                Alert changePassword = new Alert(Alert.AlertType.INFORMATION);
                changePassword.setTitle("THIS IS YOUR PASSWORD");
                String passwordInfo = "Your password is:\n\n"
                        + user.getPassword() + "\n\njust as it is shown.";
                changePassword.setHeaderText(passwordInfo);
                Optional<ButtonType> result = changePassword.showAndWait();
                
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    mainApplication.showLoginScreen();
                }
            } else if (attempts > 0) {
                answer.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");

                alertMessage = "The password you entered "
                    + "was wrong.\nYou have " + attempts
                    + " out of 5 tries left.";
                attempts--;
                wrongAnswerAlert.setHeaderText(alertMessage);
                wrongAnswerAlert.showAndWait();
            } else {
                PasswordRecoveryManager toSend =
                    new PasswordRecoveryManager(user.getPhoneNum(),
                    user.getPassword());
                toSend.sendPassword();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Text sent");
                String temp = "New password has been texted to "
                    + user.getPhoneNum();
                alert.setHeaderText(temp);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    mainApplication.showLoginScreen();
                }
            }
        }
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
