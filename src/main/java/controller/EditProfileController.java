package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import main.MainFXApplication;
import model.UserDataObject;
import classes.User;
import classes.Name;
import java.util.Optional;

/**
 * Controller class for editing profile screen.
 */
public class EditProfileController implements IController {

    private MainFXApplication mainApplication;

    @FXML
    private Button cancel;

    @FXML
    private Button ok;

    @FXML
    private Text username;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private ComboBox prefix;

    @FXML
    private TextField email;

    @FXML
    private TextField pNumber;


    /**
     * Initializes choices in comboBoxes
     */
    @FXML
    private void initialize() {
        //noinspection unchecked
        prefix.getItems().setAll("Mr", "Ms", "Mrs");
    }

    /**
     * Button handler for editing profile page.
     * Clicking OK button will ask for confirmation,
     * then update the information.
     * Clicking Cancel button will close the alert.
     *
     * @param event the button user clicks.
     */
    @FXML
    private void handleButtonClicked(ActionEvent event) {
        if (event.getSource() == cancel) {
            mainApplication.showMainScreen();
        } else if (event.getSource() == ok) {
            Boolean emptyFieldsExist = validateEditProfile();
            if (emptyFieldsExist) {
                Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
                emptyAlert.setTitle("Empty fields");
                emptyAlert.setHeaderText("Please fill out all "
                        + "the required fields.");
                emptyAlert.showAndWait();
            } else if (!newPassword.getText().equals(
                    confirmPassword.getText())) {
                Alert passAlert = new Alert(Alert.AlertType.WARNING);
                passAlert.setTitle("Password");
                passAlert.setHeaderText("Your confirmed password "
                        + "doesn't match the new password field!!");

                firstName.setStyle("-fx-border-width: 0px ;");
                lastName.setStyle("-fx-border-width: 0px ;");
                email.setStyle("-fx-border-width: 0px ;");
                newPassword.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                confirmPassword.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");

                passAlert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Profile Update");
                alert.setHeaderText("Are you sure you want to"
                        + " update above information?\n"
                        + "Click \"OK\" to confirm.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    UserDataObject userDAO = UserDataObject.getInstance();
                    User prevUserInfo = mainApplication.getCurrentUser();
                    String uId = prevUserInfo.getUserId();
                    String userType = prevUserInfo.getUserType();
                    Name name = new Name(firstName.getText(),
                        lastName.getText(), prefix.getValue().toString());
                    User editedUser = new User(newPassword.getText(),
                            email.getText(), pNumber.getText(),
                            uId, name, prevUserInfo.getSecurityQuestion(),
                            userType);
                    editedUser.setSecurityAnswer(
                            prevUserInfo.getSecurityAnswer());
                    userDAO.editSingleUser(editedUser, username.getText());
                    mainApplication.showMainScreen();
                } else {
                    alert.close();
                }
            }
        }
    }

    /**
     * Validates the edit profile inputs.  Simple as of now, just checks
     * that required fields aren't empty.
     * @return An alert message describing the input errors, empty String
     * if input is valid.
     */
    private Boolean validateEditProfile() {
        int emptyFields = 0;
        
        if (newPassword.getText().length() == 0) {
            newPassword.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            emptyFields++;
            confirmPassword.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
        } else {
            newPassword.setStyle("-fx-border-width: 0px ;");
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

        return (emptyFields != 0);
    }


    /**
     * Helper method to dynamically populate the User's profile.
     * @param  user User logged in
     * @param  username username of the User logged in
     */
    public void populateUserInformation(User user, String username) {
        this.username.setText(username);

        //noinspection unchecked
        prefix.setValue(user.getName().getPrefix());
        firstName.setText(user.getName().getFirstName());
        lastName.setText(user.getName().getLastName());
        email.setText(user.getEmail());
        pNumber.setText(user.getPhoneNum());
        newPassword.setText(user.getPassword());
        confirmPassword.setText(user.getPassword());
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
