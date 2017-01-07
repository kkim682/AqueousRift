package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import main.MainFXApplication;
import model.UserDataObject;
import classes.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for login page.
 */
public class LoginController implements IController {
    private MainFXApplication mainApplication;
    private UserDataObject userDAO = UserDataObject.getInstance();
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private Hyperlink createAccount;

    @FXML
    private Button cancel;

    @FXML
    private Hyperlink forgotPassword;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    private int tries = 2;
    /**
     * Button handler for login page.
     * Clicking "Login" should check validity of info and count login attempts.
     * Clicking "Cancel" will redirect to main screen.
     *
     * @param event the button user clicks.
     */
    @FXML
     private void handleButtonClicked(ActionEvent event) {
        if (event.getSource() == cancel) {
            mainApplication.showMap();
            mainApplication.showMainScreen();
        } else if (event.getSource() == login) {
            if (checkValid()) {
                mainApplication.showMap();
                mainApplication.showMainScreen();
                mainApplication.updateMenuBar();
                mainApplication.checkAuthority();
            }
        }
    }

    /**
     * Link handler for login page.
     * Clicking "Create an Account" will redirect to register page.
     * Clicking "Forgot My Password" will redirect to find password page.
     *
     * @throws IOException throws an exception when fxml is not found.
     * @param event the button user clicks.
     */
    @FXML
    private void handleLinkClicked(ActionEvent event) throws IOException {
        if (event.getSource() == createAccount) {
            mainApplication.showRegisterScreen();
        } else if (event.getSource() == forgotPassword) {
            mainApplication.showFindPasswordScreen();
        }
    }

    /**
     * Checks if provided input matches stored model.
     * Will display an error message if input doesn't match.
     * This will check the info up to 3 times before user gets blocked.
     *
     * @return boolean true if input matches.
     */
    @FXML
    private boolean checkValid() {
        String user = username.getText();

        if (userDAO.userExists(user)) {
            username.setStyle("-fx-border-width: 0px ;");
            User queriedUser = userDAO.getUser(user);
            String alertMessage;

            Alert wrongPasswordAlert = new Alert(Alert.AlertType.WARNING);
            wrongPasswordAlert.setTitle("Wrong Password");

            if (queriedUser.getIsBanned().equals("true")) {
                Alert bannedAlert = new Alert(Alert.AlertType.WARNING);
                bannedAlert.setTitle("Banned User Login Attempt");
                bannedAlert.setHeaderText("You have been banned "
                    + "due to three consecutive failed login attempts. "
                    + "You will receive an e-mail to change your password.");
                bannedAlert.showAndWait();
                log.error(queriedUser.getName().toString()
                    + " has been banned.");
                return false;
            } else if (queriedUser.getPassword().equals(password.getText())) {
                mainApplication.setCurrentUsername(user);
                mainApplication.setCurrentUser(queriedUser);
                log.info(queriedUser.getName().toString()
                    + " logged in successfully");
                return true;
            } else {
                if (tries > 0) {
                    password.setStyle(
                        "-fx-border-color: red ; -fx-border-width: 2px ;");

                    alertMessage = "The password you entered "
                        + "was wrong.\nYou have " + tries
                        + " out of 3 tries left.";

                    wrongPasswordAlert.setHeaderText(alertMessage);
                    wrongPasswordAlert.showAndWait();
                } else {
                    Alert threeTriesAlert = new Alert(Alert.AlertType.WARNING);
                    threeTriesAlert.setTitle("Three failed login attempts");
                    threeTriesAlert.setHeaderText(
                        "You made 3 failed login attempts."
                        + "\nContact an admin to have your account unblocked.");
                    queriedUser.setIsBanned("true");
                    threeTriesAlert.showAndWait();
                }
                tries--;
                log.error(user + " has attempted" + (3 - tries)
                    + "unsucessful logins");
                return false;
            }
        } else {
            password.setStyle("-fx-border-width: 0px ;");
            username.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");

            Alert doesNotExistAlert = new Alert(Alert.AlertType.WARNING);
            doesNotExistAlert.setTitle("Wrong Username");
            doesNotExistAlert.setHeaderText("This username does not exist.\n"
                + "Please enter a valid username.");

            doesNotExistAlert.showAndWait();
            log.error(user + " is an invalid username.");
            return false;
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
