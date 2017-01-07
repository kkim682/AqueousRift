package controller;

import java.util.Date;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import classes.User;
import classes.WaterCondition;
import classes.WaterType;
import classes.UserType;
import classes.Location;
import classes.WaterSourceReport;
import classes.WaterPurityReport;
import classes.OverallCondition;
import javafx.util.Duration;
import main.MainFXApplication;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import model.ReportDataObject;
import model.UserDataObject;
import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * Controller class for reporting a water source
 */
public class CreateReportController implements IController {
    private BooleanProperty animationStopped
            = new SimpleBooleanProperty(true);
    private BooleanProperty purityReport
            = new SimpleBooleanProperty(false);
    private static BooleanProperty isAuthorized
            = new SimpleBooleanProperty(false);
    private static BooleanProperty showConfirm
            = new SimpleBooleanProperty(false);
    private ObjectProperty<UserType> userType = new SimpleObjectProperty<>();
    private MainFXApplication mainApplication;

    @FXML private ComboBox<WaterType> waterType = new ComboBox<>();
    @FXML private ComboBox<WaterCondition> waterCondition = new ComboBox<>();
    @FXML private ComboBox<OverallCondition> overallCondition =
        new ComboBox<>();
    @FXML private Label locationLabel;
    @FXML private TextField longitude;
    @FXML private TextField latitude;
    @FXML private Label typeLabel;
    @FXML private Label conditionLabel;
    @FXML private TextField virus;
    @FXML private TextField contamination;
    @FXML private Text ppm1;
    @FXML private Text ppm2;
    @FXML private Label virusLabel;
    @FXML private Label contaminationLabel;
    @FXML private Button confirmButton;
    @FXML private Button submit;
    @FXML private Button cancel;
    @FXML private Label overallConditionLabel;
    @FXML private ImageView img;

    /**
     * Initializes items (comboBox's) and bindings.
     */
    @FXML
    private void initialize() {
        //Populate static comboBox data
        animationStopped.setValue(true);
        img.visibleProperty().bind(animationStopped.not());
        locationLabel.visibleProperty().bind(animationStopped);
        latitude.visibleProperty().bind(animationStopped);
        longitude.visibleProperty().bind(animationStopped);
        waterType.getItems().setAll(WaterType.values());
        waterType.visibleProperty().bind(
                showConfirm.not().and(animationStopped));
        typeLabel.visibleProperty().bind(
                showConfirm.not().and(animationStopped));
        waterCondition.getItems().setAll(WaterCondition.values());
        waterCondition.visibleProperty().bind(
                showConfirm.not().and(animationStopped));
        conditionLabel.visibleProperty().bind(
                showConfirm.not().and(animationStopped));
        //Bind event handler and set binding variables
        userType.set(UserType.GeneralUser);
        ppm1.visibleProperty().bind(isAuthorized.and(
                showConfirm).and(animationStopped));
        ppm2.visibleProperty().bind(isAuthorized.and(
                showConfirm).and(animationStopped));
        virusLabel.visibleProperty().bind(isAuthorized.and(
                showConfirm).and(animationStopped));
        contaminationLabel.visibleProperty().bind(
                isAuthorized.and(showConfirm).and(
                        animationStopped));
        virus.visibleProperty().bind(isAuthorized.and(
                showConfirm).and(animationStopped));
        contamination.visibleProperty().bind(isAuthorized.and(
                showConfirm).and(animationStopped));
        overallCondition.getItems().setAll(OverallCondition.values());
        overallCondition.visibleProperty().bind(isAuthorized.and(
                showConfirm).and(animationStopped));
        overallConditionLabel.visibleProperty().bind(
                isAuthorized.and(showConfirm).and(animationStopped));
        confirmButton.visibleProperty().bind(isAuthorized);
        showConfirm.setValue(false);
        longitude.focusedProperty()
            .addListener((observable, oldVal, newVal) ->
                    handleLongitudeChange(observable));
        latitude.focusedProperty()
            .addListener((observable, oldVal, newVal) ->
                    handleLatitudeChange(observable));
    }

    /**
     * set the visibility of the text fields based on the user type.
     * @param set true if the user is authorized (worker, admin, manager).
     *            false if the user is not logged in or a general user.
     */
    public static void setAuthority(boolean set) {
        isAuthorized.set(set);
    }

    /**
     * Endpoint for mainApplication to update the report's center location text.
     * @param center latitude and longitude of the center.
     */
    public void populateLocation(LatLong center) {
        if (center != null) {
            latitude.setText(String.valueOf(center.getLatitude()));
            longitude.setText(String.valueOf(center.getLongitude()));
        }
    }

    /**
     * Event listener for latitude text box change.
     * @param focused if the text field is focused or not.
     */
    @FXML
    private void handleLatitudeChange(ObservableValue focused) {
        Boolean isFocused = (Boolean) (focused.getValue());
        if (!isFocused && latitude.getText().length() > 0) {
            Double newLat = Double.parseDouble(latitude.getText());
            mainApplication.changeCenterLatitude(newLat);
        }
    }

    /**
     * Event listener for longitude text box change.
     * @param focused if the text field is focused or not.
     */
    @FXML
    private void handleLongitudeChange(ObservableValue focused) {
        Boolean isFocused = (Boolean) (focused.getValue());
        if (!isFocused && longitude.getText().length() > 0) {
            Double newLong = Double.parseDouble(longitude.getText());
            mainApplication.changeCenterLongitude(newLong);
        }
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
        } else if (event.getSource() == confirmButton) {
            System.out.println(showConfirm.get());
            if (showConfirm.get()) {
                //create resource report
                purityReport.setValue(false);
                confirmButton.setId("button-confirm");
                showConfirm.setValue(false);
                confirmButton.setText("Confirm This Report");
            } else {
                //creating purity report
                purityReport.setValue(true);
                confirmButton.setId("button-delete");
                showConfirm.setValue(true);
                confirmButton.setText("Back to Source Report");
            }

        } else if (event.getSource() == submit) {
            latitude.setStyle(
                "-fx-border-width: 0px ;");
            longitude.setStyle(
                "-fx-border-width: 0px ;");
            waterType.setStyle(
                "-fx-border-width: 0px ;");
            waterCondition.setStyle(
                "-fx-border-width: 0px ;");
            virus.setStyle(
                "-fx-border-width: 0px ;");
            contamination.setStyle(
                "-fx-border-width: 0px ;");
            overallCondition.setStyle(
                "-fx-border-width: 0px ;");

            ReportDataObject reportDAO = ReportDataObject.getInstance();
            String reporterId = mainApplication.getCurrentUsername();
            UserDataObject userDAO = UserDataObject.getInstance();
            User user = userDAO.getUser(reporterId);
            Location loc = new Location("0", "0");

            if (user.getBlocked().equals("true")) {
                Alert blockedAlert = new Alert(Alert.AlertType.WARNING);
                blockedAlert.setTitle("Blocked User Report Attempt");
                blockedAlert.setHeaderText("You have been blocked "
                    + "as a penalty for submitting a faulty report."
                    + "\nYou may request an admin to unblock you.");
                blockedAlert.showAndWait();
                mainApplication.showMap();
                mainApplication.showMainScreen();
            } else if (emptyFieldsExist()) {
                Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
                emptyAlert.setTitle("Empty fields");
                emptyAlert.setHeaderText("Please fill out all "
                        + "the required fields.");
                emptyAlert.showAndWait();
            } else if (!correctDataType()) {
                Alert wrongTypeAlert = new Alert(Alert.AlertType.WARNING);
                wrongTypeAlert.setTitle("Invalid data");
                wrongTypeAlert.setHeaderText("Please enter valid data "
                        + " in the required fields.");
                wrongTypeAlert.showAndWait();
            } else {
                waterCondition.setStyle("-fx-border-width: 0px ;");
                waterType.setStyle("-fx-border-width: 0px ;");
                virus.setStyle("-fx-border-width: 0px ;");
                contamination.setStyle("-fx-border-width: 0px ;");

                if (validateLatAndLon(latitude.getText(),
                    longitude.getText())) {
                    try {
                        loc.setLongitude(longitude.getText());
                        loc.setLatitude(latitude.getText());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    Date date = new Date();
                    WaterType type = waterType.getValue();
                    WaterCondition condition = waterCondition.getValue();
                    OverallCondition overallcondition =
                            overallCondition.getValue();
                    if (!purityReport.get()) {
                        WaterSourceReport report
                            = new WaterSourceReport(reporterId,
                            loc, type, condition, date);
                        reportDAO.addSourceReport(report);
                    } else {
                        WaterPurityReport report
                            = new WaterPurityReport(reporterId,
                            date, loc, overallcondition,
                            Double.parseDouble(virus.getText()),
                            Double.parseDouble(contamination.getText()));
                        reportDAO.addPurityReport(report);
                    }

                    animationStopped.setValue(false);
                    FadeTransition transition = new FadeTransition(
                            Duration.seconds(2), img);
                    transition.setFromValue(0);
                    transition.setToValue(100);
                    transition.setOnFinished(event1 -> {
                        mainApplication.showMap();
                        mainApplication.showMainScreen();
                        animationStopped.setValue(true);
                    });
                    transition.play();
                }
            }
        }
    }

    /**
     * checks all fields for null values
     *
     * @return A boolean value showing if there are null values.
     */
    private Boolean emptyFieldsExist() {
        int count = 0;

        if (longitude.getText().length() == 0) {
            longitude.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            count++;
        }
        if (latitude.getText().length() == 0) {
            latitude.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            count++;
        }
        if (showConfirm.not().get()) {
            if (waterCondition.getValue() == null) {
                waterCondition.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }
            if (waterType.getValue() == null) {
                waterType.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }
        } else {
            if (overallCondition.getValue() == null) {
                overallCondition.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }
            if (virus.getText() == null) {
                virus.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }
            if (contamination.getText() == null) {
                contamination.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }
        }
        System.out.println(count);
        return (count != 0);
    }

    /**
     * Checks if all the fields are of the correct data type.
     *
     * @return whether it is true
     */
    private boolean correctDataType() {
        int count = 0;

        if (!isNumeric(longitude.getText())) {
            longitude.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            count++;
        }

        if (!isNumeric(latitude.getText())) {
            latitude.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            count++;
        }
        if (showConfirm.get()) {
            if (!isNumeric(virus.getText())) {
                virus.setStyle(
                        "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }

            if (!isNumeric(contamination.getText())) {
                contamination.setStyle(
                        "-fx-border-color: red ; -fx-border-width: 2px ;");
                count++;
            }
        }

        return (count == 0);
    }

    /**
     * helper method to see if a certain field
     * is populated with a numeric string
     *
     * @param str string to be examined
     * @return true if it is numeric
     */
    private static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Gives the controller access to mainApplication.
     *
     * @param mainFXApplication mainFXApplication
     */
    public void setMainApp(MainFXApplication mainFXApplication) {
        mainApplication = mainFXApplication;
    }

    /**
     * Validates latitude and longitude values
     * to make sure they are in range.
     *
     * @param lat String value for latitude
     * @param lon String value for longitude
     * @return boolean value representing if valid data was given.
     */
    private Boolean validateLatAndLon(String lat, String lon) {
        double latVal = Double.parseDouble(lat);
        double lonVal = Double.parseDouble(lon);

        if ((latVal >= -90) && (latVal <= 90)
            && (lonVal >= -180) && (lonVal <= 180)) {
            latitude.setStyle("-fx-border-width: 0px ;");
            longitude.setStyle("-fx-border-width: 0px ;");

            return true;

        } else {
            if ((latVal < -90) || (latVal > 90)) {
                latitude.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if ((lonVal < -180) || (lonVal > 180)) {
                longitude.setStyle(
                    "-fx-border-color: red ; -fx-border-width: 2px ;");
            }

            return false;
        }
    }
}
