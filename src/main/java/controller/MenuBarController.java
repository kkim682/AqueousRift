package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Optional;
import main.MainFXApplication;
import classes.UserType;
import classes.User;



/**
 * Controller class for main border pane. Will contain file, help, etc
 */
public class MenuBarController implements IController {

    private BooleanProperty userLoggedIn = new SimpleBooleanProperty(false);
    private static BooleanProperty isManager = new SimpleBooleanProperty(false);
    private MainFXApplication mainApplication;
    private StringProperty username = new SimpleStringProperty("Hello, Guest");
    private StringProperty userType
            = new SimpleStringProperty(UserType.GeneralUser.toString());
    private static BooleanProperty isAuthorized =
            new SimpleBooleanProperty(false);

    @FXML private Menu hello;
    @FXML private Menu login;
    @FXML private Menu userOptions;
    @FXML private Menu reports;
    @FXML private MenuItem viewMyReports;
    @FXML private MenuItem historicalGraph;
    @FXML private Menu home;
    @FXML private MenuItem bottled;
    @FXML private MenuItem well;
    @FXML private MenuItem stream;
    @FXML private MenuItem lake;
    @FXML private MenuItem spring;
    @FXML private MenuItem other;
    @FXML private MenuItem waste;
    @FXML private MenuItem treatableClear;
    @FXML private MenuItem treatableMuddy;
    @FXML private MenuItem potable;
    @FXML private MenuItem safe;
    @FXML private MenuItem treatable;
    @FXML private MenuItem unsafe;

    /**
     * Initializes variable bindings and login handler
     */
    @FXML
    private void initialize() {
        userOptions.visibleProperty().bind(userLoggedIn);
        historicalGraph.visibleProperty().bind(isAuthorized.and(isManager));
        reports.setText("Reports");
        login.visibleProperty().bind(userLoggedIn.not());
        viewMyReports.visibleProperty().bind(userLoggedIn);
        hello.textProperty().bind(username);
        userOptions.textProperty().bind(userType);
        Label loginLabel = new Label("Login");
        loginLabel.setOnMouseClicked(event ->
                mainApplication.showLoginScreen());
        login.setGraphic(loginLabel);
        Label homeLabel = new Label("Home");
        homeLabel.setOnMouseClicked(event -> {
            mainApplication.showMap();
            mainApplication.showMainScreen();
        });
        home.setGraphic(homeLabel);

    }

    /**
     * shows menu items based on the user types
     * @param set true if the user is authorized (worker, admin, manager).
     *            false if the user is not logged in or a general user.
     */
    public static void setAuthority(boolean set) {
        isAuthorized.set(set);
    }

    /**
     * checks if the current user is a manager.
     * This will allow only manager to view historical graph.
     * @param set true if the user is a manager).
     *            false if the user is guest, general user, worker, or admin.
     */
    public static void setManager(boolean set) {
        isManager.set(set);
    }

    /**
     * allow for calling back to the main application code if necessary
     * @param main   the reference to the FX Application instance
     * */
    public void setMainApp(MainFXApplication main) {
        mainApplication = main;
    }

    /**
     * Logout the user and show the menu as a guest
     */
    @FXML
    private void handleLogoutMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            mainApplication.showMap();
            mainApplication.showMainScreen();
            mainApplication.setCurrentUser(null);
            mainApplication.setCurrentUsername("");
            username.set("Hello, Guest");
            userLoggedIn.set(false);
            setAuthority(false);
            setManager(false);
            MainScreenController.setAuthority(false);
            CreateReportController.setAuthority(false);
            ViewAllReportsController.setLoggedIn(false);
            ViewAllReportsController.setAuthority(false);
            ViewMyReportsController.setAuthority(false);
            ViewMyReportsController.setLoggedIn(false);
        }
    }

    /**
     * Lets the user edit profile
     */
    @FXML
    private void handleEditMenu() {
        mainApplication.showMap();
        mainApplication.showEditProfileScreen();
    }

    /**
     * filters by water type
     *
     * @param event clicking the water type
     */
    @FXML
    private void handleWaterType(ActionEvent event) {
        MapController.setWaterType("null");
        if (event.getSource() == bottled) {
            MapController.setWaterType("Bottled");
        } else if (event.getSource() == well) {
            MapController.setWaterType("Well");
        } else if (event.getSource() == stream) {
            MapController.setWaterType("Stream");
        } else if (event.getSource() == lake) {
            MapController.setWaterType("Lake");
        } else if (event.getSource() == spring) {
            MapController.setWaterType("Spring");
        } else if (event.getSource() == other) {
            MapController.setWaterType("Other");
        }
        MapController.setWaterCondition("null");
        MapController.setOverallCondition("null");
        MapController.setAllPins("null");
        mainApplication.showMap();
        mainApplication.showMainScreen();
    }

    /**
     * filters by water condition
     *
     * @param event clicking the condition of the water
     */
    @FXML
    private void handleWaterCondition(ActionEvent event) {
        MapController.setWaterCondition("null");
        if (event.getSource() == waste) {
            MapController.setWaterCondition("Waste");
        } else if (event.getSource() == treatableClear) {
            MapController.setWaterCondition("Treatable_Clear");
        } else if (event.getSource() == treatableMuddy) {
            MapController.setWaterCondition("Treatable_Muddy");
        } else if (event.getSource() == potable) {
            MapController.setWaterCondition("Potable");
        }
        MapController.setWaterType("null");
        MapController.setOverallCondition("null");
        MapController.setAllPins("null");
        mainApplication.showMap();
        mainApplication.showMainScreen();
    }

    /**
     * filters by overall water condition of the confirmed reports
     *
     * @param event clicking the menu item
     */
    @FXML
    private void handleOverallCondition(ActionEvent event) {
        MapController.setOverallCondition("null");
        if (event.getSource() == safe) {
            MapController.setOverallCondition("Safe");
        } else if (event.getSource() == treatable) {
            MapController.setOverallCondition("Treatable");
        } else if (event.getSource() == unsafe) {
            MapController.setOverallCondition("Unsafe");
        }
        MapController.setWaterCondition("null");
        MapController.setWaterType("null");
        MapController.setAllPins("null");
        mainApplication.showMap();
        mainApplication.showMainScreen();
    }


    /**
     * Show all water source report pins on the map.
     */
    @FXML
    private void handleViewAllPins() {
        MapController.setAllPins("All");
        MapController.setWaterType("null");
        MapController.setWaterCondition("null");
        MapController.setOverallCondition("null");
        mainApplication.showMap();
        mainApplication.showMainScreen();
    }

    /**
     * Lets the authorized user to view historical graph;
     */
    @FXML
    private void handleViewGraph() {
        mainApplication.showGraph();
    }


    /**
     * Lets the user  view all reports
     */
    @FXML
    private void handleViewAllMenu() {
        mainApplication.showViewAllReportsScreen();
    }

    /**
     * Lets the user view reports user has submitted
     */
    @FXML
    private void handleViewMyReportsMenu() {
        mainApplication.showViewMyReportsScreen();
    }



    /**
     * About menu item event handler.
     * Provide information on our project, version, etc
     */
    @FXML
    private void handleAboutMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aqueous Rift");
        alert.setHeaderText("About");
        alert.setContentText("Update : This is a project for CS2340.");
        alert.showAndWait();
    }

    /**
     * Contact menu item event handler.
     * Provide credit and contact information of developers.
     */
    @FXML
    private void handleContactMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aqueous Rift");
        alert.setHeaderText("Contact");
        alert.setContentText("Created by: "
                + "\n\nGraham McAllister\nAhJin Noh\nJoshua Gaul"
                + "\nKwangHee Kim\nAakanksha Patel");
        alert.showAndWait();
    }

    /**
     * Changes the MenuBar when a user logs in.
     *
     * @param user User that logs into the application
     */
    public void userLogsIn(User user) {
        username.set("Hello, " + mainApplication.getCurrentUsername());
        userLoggedIn.set(true);
        userType.set(user.getUserType());
    }
}
