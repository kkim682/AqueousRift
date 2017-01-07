package controller;

import classes.WaterSourceReport;
import classes.WaterPurityReport;
import classes.WaterReport;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import main.MainFXApplication;
import model.ReportDataObject;

import java.util.Optional;

/**
 * Controller for main app screen page, which will be
 * "landing page" for all non-users and users.
 * All guests can view the reports by clicking the pins on map.
 * + Users can submit a report.
 * + Workers can submit a report with virus & contamination
 * level, and good water will show on the map.
 * + Managers can edit and delete a report, and view historical graph.
 * + Admins can ban users and view security logs.
 * Someone please detail this.
 */
public class MainScreenController implements IController {

    private static BooleanProperty isSourceReport
            = new SimpleBooleanProperty(true);
    private static BooleanProperty isAuthorized
            = new SimpleBooleanProperty(false);
    private MainFXApplication mainApplication;
    @FXML private Text longitude;
    @FXML private Text latitude;
    @FXML private Text date;
    @FXML private Text condition;
    @FXML private Text overallCondition;
    @FXML private Text type;
    @FXML private Text virus;
    @FXML private Text contamination;
    @FXML private Label typeLabel;
    @FXML private Label conditionLabel;
    @FXML private Label overallConditionLabel;
    @FXML private Label virusLabel;
    @FXML private Label contaminationLabel;
    @FXML private Button update;
    @FXML private Button delete;
    @FXML private Button report;

    /**
     * Initializes the buttons
     */
    @FXML
    private void initialize() {
        delete.visibleProperty().bind(isAuthorized);
        update.visibleProperty().bind(isAuthorized);
        virus.visibleProperty().bind(isAuthorized.and(isSourceReport.not()));
        virusLabel.visibleProperty().bind(
                isAuthorized.and(isSourceReport.not()));
        contamination.visibleProperty().bind(
                isAuthorized.and(isSourceReport.not()));
        contaminationLabel.visibleProperty().bind(
                isAuthorized.and(isSourceReport.not()));
        overallCondition.visibleProperty().bind(isSourceReport.not());
        overallConditionLabel.visibleProperty().bind(isSourceReport.not());
        type.visibleProperty().bind(isSourceReport);
        typeLabel.visibleProperty().bind(isSourceReport);
        condition.visibleProperty().bind(isSourceReport);
        conditionLabel.visibleProperty().bind(isSourceReport);
    }

    /**
     * set the button based on the user types
     * @param set true if the user is authorized (worker, admin, manager).
     *            false if the user is not logged in or a general user.
     */
    public static void setAuthority(boolean set) {
        isAuthorized.set(set);
    }


    /**
     * Button handler for mainScreenPage.
     * Clicking "Submit" will redirect to water source
     * report if logged in. Login is required.
     *
     * @param event the button user clicks.
     */
    @FXML
    private void handleButtonClicked(ActionEvent event) {
        if (event.getSource() == report) {
            if (mainApplication.getCurrentUser() == null) {
                mainApplication.showLoginScreen();
            } else {
                mainApplication.showReportScreen();
            }
        } else if (event.getSource() == update) {
            if (mainApplication.getCurrentReport() != null) {
                mainApplication.showEditReportScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Click a pin to update a report");
                alert.setHeaderText("Please click on a pin to"
                        + "select a report.");
                alert.showAndWait();
            }
        } else if (event.getSource() == delete) {
            if (mainApplication.getCurrentReport() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete this report");
                alert.setHeaderText(
                        "Are you sure you want to delete this report?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ReportDataObject reportDAO = ReportDataObject.getInstance();
                    WaterReport currentReport =
                            mainApplication.getCurrentReport();
                    if (isSourceReport.get()) {
                        reportDAO.deleteSourceReport(currentReport.getId());
                    } else {
                        reportDAO.deletePurityReport(currentReport.getId());
                    }
                    mainApplication.showMap();
                    mainApplication.showMainScreen();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Click a pin to update a report");
                alert.setHeaderText("Please select a report to delete.");
                alert.showAndWait();
            }

        }
    }

    /**
     * Sets the current report
     * @param report Report to set
     */
    public void setCurrentReport(WaterReport report) {
        if (report instanceof WaterSourceReport) {
            WaterSourceReport sourceReport = (WaterSourceReport) report;
            type.setText(sourceReport.getType().toString());
            condition.setText(sourceReport.getCondition().toString());
            virus.setText(null);
            contamination.setText(null);
            isSourceReport.set(true);
        } else if (report instanceof WaterPurityReport) {
            WaterPurityReport purityReport = (WaterPurityReport) report;
            overallCondition.setText(purityReport.getOverallCondition()
                .toString());
            contamination.setText(Double.toString(purityReport
                .getContaminantPPM()) + " ppm");
            virus.setText(Double.toString(purityReport.getVirusPPM()) + " ppm");
            type.setText(null);
            isSourceReport.set(false);
        }
        date.setText(report.getDateAsString());
        longitude.setText(report.getLocation().getLongitude());
        latitude.setText(report.getLocation().getLatitude());
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
