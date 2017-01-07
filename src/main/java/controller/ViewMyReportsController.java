package controller;

import classes.WaterSourceReport;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.text.Text;
import main.MainFXApplication;
import model.ReportDataObject;
import classes.WaterReport;

public class ViewMyReportsController implements IController {
    private static final StringProperty CURRENT_USERNAME
            = new SimpleStringProperty(null);
    private static final BooleanProperty IS_AUTHORIZED
            = new SimpleBooleanProperty(false);
    private static final BooleanProperty IS_LOGGED_IN
            = new SimpleBooleanProperty(false);
    private static final BooleanProperty SHOW_PURITY_REPORTS
            = new SimpleBooleanProperty(false);
    private MainFXApplication mainApplication;
    @FXML private static StackPane pane;
    @FXML private Button back;
    @FXML private Button submit;
    @FXML private Text header;
    @FXML private TableView<WaterReport> reportView;
    @FXML private Button delete;
    @FXML private Button switchTable;
    private final TableColumn<WaterReport, String> user
            = new TableColumn<>("Username");
    private final TableColumn<WaterReport, String> location
            = new TableColumn<>("Location");
    private final TableColumn<WaterReport, String> date
            = new TableColumn<>("Date");
    private final TableColumn<WaterReport, String> type
            = new TableColumn<>("Type");
    private final TableColumn<WaterReport, String> condition
            = new TableColumn<>("Condition");
    private final TableColumn<WaterReport, String> contamination
            = new TableColumn<>("Contamination (ppm)");
    private final TableColumn<WaterReport, String> virus
            = new TableColumn<>("Virus (ppm)");
    private final TableColumn<WaterReport, String> overallCondition
            = new TableColumn<>("Overall Condition");
    private ReportDataObject reportDAO;
    private final ObservableList<WaterReport> obsList
            = FXCollections.observableArrayList();

    /**
     * Loads report data
     */
    @FXML
    private void initialize() {
        reportDAO = ReportDataObject.getInstance();
        delete.visibleProperty().bind(IS_AUTHORIZED);
        setReportView(false);
        user.setCellValueFactory(
                new PropertyValueFactory<>("reporterId"));
        location.setCellValueFactory(
                new PropertyValueFactory<>("location"));
        date.setCellValueFactory(
                new PropertyValueFactory<>("date"));
        type.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        condition.setCellValueFactory(
                new PropertyValueFactory<>("condition"));
        virus.setCellValueFactory(
                new PropertyValueFactory<>("virusPPM"));
        contamination.setCellValueFactory(
                new PropertyValueFactory<>(
                        "contaminantPPM"));
        overallCondition.setCellValueFactory(
                new PropertyValueFactory<>(
                        "overallCondition"));
        switchViews();
    }

    /**
     * Button handler for view reports page
     *
     * @param event the button user clicks.
     */
    @FXML
    private void handleButtonClicked(ActionEvent event)  {
        if (event.getSource() == back) {
            MapController.setAllPins("All");
            mainApplication.showMap();
            mainApplication.showMainScreen();
        } else if (event.getSource() == submit) {
            if (IS_LOGGED_IN.get()) {
                mainApplication.showMap();
                mainApplication.showReportScreen();
            } else {
                mainApplication.showMap();
                mainApplication.showLoginScreen();
            }
        } else if (event.getSource() == switchTable) {
            if (SHOW_PURITY_REPORTS.get()) {
                setReportView(false);
                header.setText("MY SOURCE REPORTS");
                switchTable.setText("View My Confirmed Reports");
            } else {
                setReportView(true);
                header.setText("MY CONFIRMED REPORTS");
                switchTable.setText("View My Source Reports");
            }
            switchViews();
        } else if (event.getSource() == delete) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete this report");
            alert.setHeaderText(
                    "Are you sure you want to delete this report?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ReportDataObject reportDAO = ReportDataObject.getInstance();
                WaterReport report =
                        reportView.getSelectionModel().getSelectedItem();
                if (report instanceof WaterSourceReport) {
                    reportDAO.deleteSourceReport(report.getId());
                } else {
                    reportDAO.deletePurityReport(report.getId());
                }
                mainApplication.showViewAllReportsScreen();
            }
        }
    }

    /**
     * Updates the columns based on the view mode.
     *
     */
    private void switchViews() {
        reportView.getColumns().clear();
        obsList.clear();
        if (IS_AUTHORIZED.get() && SHOW_PURITY_REPORTS.get()) {
            reportView.getColumns().addAll(user, location, date,
                    overallCondition, contamination, virus);
        } else if (SHOW_PURITY_REPORTS.get()) {
            reportView.getColumns().addAll(user, location, date,
                    overallCondition);
        } else {
            reportView.getColumns().addAll(user, location, date, type,
                    condition);
        }
        obsList.addAll(parseReportList(reportDAO));
        FilteredList<WaterReport> filteredList
                = new FilteredList<>(obsList, p -> true);
        filteredList.setPredicate(username ->
                username.getReporterId().contains(CURRENT_USERNAME.get()));
        reportView.setItems(filteredList);
        reportView.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Parses the reportList from the given Report Data Object
     * @param  reportDAO Report Data Object to parse
     * @return           ArrayList of water reports
     */
    private List<WaterReport> parseReportList(ReportDataObject reportDAO) {
        if (SHOW_PURITY_REPORTS.get()) {
            return new ArrayList<>(
                    reportDAO.getAllPurityReports().values());
        } else {
            return new ArrayList<>(
                    reportDAO.getAllSourceReports().values());
        }
    }

    /**
     * set the visibility of the buttons based on the user type.
     * @param set true if the user is authorized (worker, admin, manager).
     *            false if the user is not logged in or a general user.
     */
    public static void setAuthority(boolean set) {
        IS_AUTHORIZED.set(set);
    }

    /**
     * gets the username of the currently logged in user.
     * @param set true if the user is logged in.
     */
    public static void setUsername(String set) {
        CURRENT_USERNAME.set(set);
    }

    /**
     * set the functionality of the buttons based on the login status.
     * @param set true if the user is logged in.
     */
    public static void setLoggedIn(boolean set) {
        IS_LOGGED_IN.set(set);
    }

    /**
     * switches the view by clicking the button.
     * @param set true if purity reports are shown.
     */
    private static void setReportView(boolean set) {
        SHOW_PURITY_REPORTS.setValue(set);
    }


    /**
     * Gives the controller access to mainApplication.
     *
     * @param mainFXApplication mainFXApplication
     * */
    public void setMainApp(MainFXApplication mainFXApplication) {
        mainApplication = mainFXApplication;
    }
}
