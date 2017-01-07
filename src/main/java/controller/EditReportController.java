package controller;

import java.util.Date;
import classes.User;
import classes.WaterCondition;
import classes.WaterType;
import classes.Location;
import classes.WaterSourceReport;
import classes.WaterPurityReport;
import classes.WaterReport;
import classes.OverallCondition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import main.MainFXApplication;
import java.util.Optional;
import model.ReportDataObject;
import model.UserDataObject;
import com.lynden.gmapsfx.javascript.object.LatLong;


/**
 * Controller class for reporting a water source
 */
public class EditReportController implements IController {

    private BooleanProperty isSourceReport
            = new SimpleBooleanProperty(true);
    private static BooleanProperty showConfirm
            = new SimpleBooleanProperty(false);
    private MainFXApplication mainApplication;

    @FXML private ComboBox<WaterType> waterType = new ComboBox<>();
    @FXML private ComboBox<WaterCondition> waterCondition = new ComboBox<>();
    @FXML private ComboBox<OverallCondition> overallCondition =
        new ComboBox<>();
    @FXML private TextField longitude;
    @FXML private TextField latitude;
    @FXML private TextField virus;
    @FXML private TextField contamination;
    @FXML private Text ppm1;
    @FXML private Text ppm2;
    @FXML private Label virusLabel;
    @FXML private Label contaminationLabel;
    @FXML private Label typeLabel;
    @FXML private Label conditionLabel;
    @FXML private Label overallConditionLabel;
    @FXML private Button confirmButton;
    @FXML private Button submit;
    @FXML private Button cancel;

    /**
     * Initializes items (comboBox's)
     */
    @FXML
    private void initialize() {
        waterType.getItems().setAll(WaterType.values());
        waterType.visibleProperty().bind(showConfirm.not());
        waterCondition.getItems().setAll(WaterCondition.values());
        waterCondition.visibleProperty().bind(showConfirm.not());
        overallCondition.getItems().setAll(OverallCondition.values());
        ppm1.visibleProperty().bind(isSourceReport.not().or(showConfirm));
        ppm2.visibleProperty().bind(isSourceReport.not().or(showConfirm));
        virusLabel.visibleProperty().bind(isSourceReport.not().or(showConfirm));
        contaminationLabel.visibleProperty().bind(isSourceReport.not()
            .or(showConfirm));
        virus.visibleProperty().bind(isSourceReport.not().or(showConfirm));
        contamination.visibleProperty().bind(isSourceReport.not()
            .or(showConfirm));
        overallCondition.visibleProperty().bind(isSourceReport.not()
            .or(showConfirm));
        overallConditionLabel.visibleProperty().bind(isSourceReport.not()
            .or(showConfirm));
        waterType.visibleProperty().bind(
                isSourceReport.and(showConfirm.not()));
        waterCondition.visibleProperty().bind(
                isSourceReport.and(showConfirm.not()));
        typeLabel.visibleProperty().bind(
                isSourceReport.and(showConfirm.not()));
        conditionLabel.visibleProperty().bind(
                isSourceReport.and(showConfirm.not()));
        confirmButton.visibleProperty().bind(isSourceReport);
        longitude.focusedProperty()
            .addListener((observable, oldVal, newVal) ->
                    handleLongitudeChange(observable));
        latitude.focusedProperty()
            .addListener((observable, oldVal, newVal) ->
                    handleLatitudeChange(observable));
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
     * Button handler for editing report page.
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
            if (showConfirm.get()) {
                //create resource report
                confirmButton.setId("button-confirm");
                showConfirm.setValue(false);
                confirmButton.setText("Confirm This Report");
            } else {
                //creating purity report
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

            UserDataObject userDAO = UserDataObject.getInstance();
            User user = userDAO.getUser(mainApplication.getCurrentUsername());
            
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Report Update");
                alert.setHeaderText("Are you sure you want to"
                        + " update above information?\n"
                        + "Click \"OK\" to confirm.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ReportDataObject reportDAO = ReportDataObject.getInstance();
                    WaterReport prevReportInfo =
                            mainApplication.getCurrentReport();
                    String reporterId = prevReportInfo.getReporterId();
                    Location loc = new Location("0", "0");
                    try {
                        loc.setLongitude(longitude.getText());
                        loc.setLatitude(latitude.getText());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    Date date = new Date();
                    WaterType type = waterType.getValue();
                    WaterCondition condition = waterCondition.getValue();
                    OverallCondition ovrCond = overallCondition.getValue();
                    if (!showConfirm.get() && isSourceReport.get()) {
                        //update source report to source report
                        System.out.println("source to source");
                        WaterSourceReport report = new WaterSourceReport(
                                reporterId, loc, type,
                                condition, date);
                        reportDAO.editSourceReport(report,
                                prevReportInfo.getId());
                    } else if (showConfirm.get() && isSourceReport.get()) {
                        //update source report to purity report
                        System.out.println("source to purity");
                        WaterPurityReport report = new WaterPurityReport(
                                reporterId, date, loc, ovrCond,
                                Double.parseDouble(virus.getText()),
                                Double.parseDouble(contamination.getText()));
                        reportDAO.confirmPurityReport(report,
                                prevReportInfo.getId());
                    } else if (!showConfirm.get() && !isSourceReport.get()) {
                        //update purity report to purity report
                        System.out.println("purity to purity");
                        WaterPurityReport report = new WaterPurityReport(
                                reporterId, date, loc, ovrCond,
                                Double.parseDouble(virus.getText()),
                                Double.parseDouble(contamination.getText()));
                        reportDAO.editPurityReport(report,
                                prevReportInfo.getId());
                    }

                    mainApplication.showMap();
                    mainApplication.showMainScreen();
                }
            }
        }
    }

    /**
     * Checks to see if there are any empty fields.
     *
     * @return An alert message describing the input errors.
     */
    private boolean emptyFieldsExist() {
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
        if (showConfirm.not().get() && isSourceReport.get()) {
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

        return (count != 0);
    }

    /**
     * Checks if all the fields are of the correct data type.
     *
     * @return whether it is true
     */
    private boolean correctDataType() {

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
     * @return true if it's numeric
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
     * populates the report data from the pin
     * @param  report Report object that contains information for UI.
     *
     */
    public void populateReportInformation(WaterReport report) {
        String latitude = report.getLocation().getLatitude();
        String longitude = report.getLocation().getLongitude();
        WaterType watertype = null;
        Double virus = null;
        Double contamination = null;
        WaterCondition condition = null;
        OverallCondition ovrCondition = null;
        if (report instanceof WaterSourceReport) {
            WaterSourceReport sourceReport = (WaterSourceReport) (report);
            watertype = sourceReport.getType();
            condition = sourceReport.getCondition();
            isSourceReport.set(true);
        } else {
            WaterPurityReport purityReport = (WaterPurityReport) (report);
            virus = purityReport.getVirusPPM();
            contamination = purityReport.getContaminantPPM();
            ovrCondition = purityReport.getOverallCondition();
            isSourceReport.set(false);
        }
        this.longitude.setText(longitude);
        this.latitude.setText(latitude);
        if (waterType != null) {
            this.waterType.setValue(watertype);
        }
        if (condition != null) {
            this.waterCondition.setValue(condition);
        }
        if (virus == null) {
            this.virus.setText("");
        } else {
            this.virus.setText(Double.toString(virus));
        }
        if (contamination == null) {
            this.contamination.setText("");
        } else {
            this.contamination.setText(Double.toString(contamination));
        }
        if (overallCondition != null) {
            this.overallCondition.setValue(ovrCondition);
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
