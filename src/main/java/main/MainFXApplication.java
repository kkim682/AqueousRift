package main;
import controller.IController;
import controller.ViewAllReportsController;
import controller.CreateReportController;
import controller.EditReportController;
import controller.EditProfileController;
import controller.MainScreenController;
import controller.MenuBarController;
import controller.MapController;
import controller.ViewMyReportsController;
import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import model.ReportDataObject;
import model.DataManager;
import model.UserDataObject;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import classes.User;
import classes.WaterReport;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * Runs the main application and routes the views.
 **/
public class MainFXApplication extends Application {
    private static final Logger LOGGER = Logger.getLogger("MainFXApplication");
    private static Timeline timeLine;
    private static User currentUser;
    private static String currentUsername;
    private static WaterReport currentReport;
    private static LatLong mapCenter;
    private static MenuBarController menuBarControl;
    private static MainScreenController mainScreenControl;
    private static EditReportController editReportControl;
    private static CreateReportController createReportControl;
    private static MapController mapControl;


    //the main container for the application window
    private Stage mainScreen;

    //the main layout for the main window
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        mainScreen = primaryStage;
        showSplashPage();
    }

    /**
     * initialize the main screen which will also have other pages.
     *
     * @param mainScreen screen to be displayed.
     */
    private void initRootLayout(Stage mainScreen) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFXApplication.class.
                    getClassLoader().getResource("view/MenuBar.fxml"));
            rootLayout = loader.load();
            menuBarControl = loader.getController();
            menuBarControl.setMainApp(this);
            mainScreen.setTitle("Aqueous Rift");
            Scene scene = new Scene(rootLayout);
            mainScreen.setScene(scene);
            mainScreen.show();
            mainScreen.setResizable(false);
            mainScreen.sizeToScene();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to find "
                    + "the fxml file for MenuBar!!");
            e.printStackTrace();
        }
    }

    /**
     * Helper method to show screen from given fxmlfilepath
     * @param fxmlFilePath relative path of fxml file
     * @param screenType   Type of app screen that the fxml file represents
     * @param location   where this screen will be displayed
     */
    private void showScreen(String fxmlFilePath,
                            String screenType, String location) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFXApplication.class.
                    getClassLoader().getResource(fxmlFilePath));
            Pane showPage = loader.load();
            animate(showPage);
            switch (location) {
            case "LEFT":
                rootLayout.setLeft(showPage);
                break;
            case "CENTER":
                rootLayout.setCenter(showPage);
                break;
            case "RIGHT":
                rootLayout.setRight(showPage);
                break;
            default:
                rootLayout.setRight(showPage);
                break;

            }
            IController controller = loader.getController();
            controller.setMainApp(this);
            if (controller instanceof EditProfileController) {
                EditProfileController c = (EditProfileController) (controller);
                c.populateUserInformation(currentUser, currentUsername);
            } else if (controller instanceof MainScreenController) {
                mainScreenControl = (MainScreenController) (controller);
            } else if (controller instanceof EditReportController) {
                editReportControl = (EditReportController) (controller);
                editReportControl.populateReportInformation(currentReport);
            } else if (controller instanceof CreateReportController) {
                createReportControl = (CreateReportController) (controller);
                createReportControl.populateLocation(getMapCenter());
            } else if (controller instanceof MapController) {
                mapControl = (MapController) (controller);
                mapCenter = mapControl.getCenter();
                if (createReportControl != null) {
                    createReportControl.populateLocation(mapCenter);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to find "
                + "the fxml file for " + screenType);
            e.printStackTrace();
        }
    }

    /**
     * animated initial page
     *
     */
    private void showSplashPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainFXApplication.class.
                    getClassLoader().getResource("view/SplashScreen.fxml"));
            Pane showPage = loader.load();
            Scene scene = new Scene(showPage);
            mainScreen.setScene(scene);
            mainScreen.show();
            mainScreen.setResizable(false);
            mainScreen.sizeToScene();

            FadeTransition transition = new FadeTransition(
                    Duration.seconds(2.9), showPage);
            transition.setFromValue(100);
            transition.setToValue(0);
            transition.setCycleCount(1);
            transition.setOnFinished(event -> {
                initRootLayout(mainScreen);
                showMap();
                showMainScreen();
            });
            transition.play();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to find "
                    + "the fxml file for Splash Screen");
            e.printStackTrace();
        }
    }

    /**
     * sets the screen to login page
     */
    public void showLoginScreen() {
        LOGGER.entering("MainFXApplication", "showLoginScreen");
        showScreen("view/Login.fxml", "LoginScreen", "RIGHT");
    }

    /**
     * sets the screen to registration page
     */
    public void showRegisterScreen() {
        LOGGER.entering("MainFXApplication", "showRegisterScreen");
        showScreen("view/Register.fxml", "RegisterScreen", "RIGHT");
    }

    /**
     * sets the screen to find password page
     */
    public void showFindPasswordScreen() {
        showScreen("view/FindPassword.fxml", "PasswordScreen", "RIGHT");
    }

    /**
     * sets the screen to welcome screen
     */
    public void showMainScreen() {
        showScreen("view/MainScreen.fxml", "MainScreen", "RIGHT");
    }

    /**
     * sets the screen to editing profile page
     */
    public void showEditProfileScreen() {
        showScreen("view/EditProfile.fxml", "EditProfileScreen", "RIGHT");
    }


    /**
     * sets the screen to reporting water source page
     */
    public void showReportScreen() {
        showScreen("view/CreateReport.fxml",
                "ReportWaterSourceScreen", "RIGHT");
    }

    /**
     * sets the screen to view all recent reports
     */
    public void showViewAllReportsScreen() {
        showScreen("view/ViewAllReports.fxml",
                "ViewAllReports", "LEFT");
    }

    /**
     * sets the screen to view all recent reports
     */
    public void showViewMyReportsScreen() {
        showScreen("view/ViewMyReports.fxml",
                "ViewMyReports", "LEFT");
    }


    /**
     * sets the screen to editing an existing reports
     */
    public void showEditReportScreen() {
        showScreen("view/EditReport.fxml",
                "EditReport", "RIGHT");
    }


    /**
     * sets the screen to view the map
     */
    public void showMap() {
        showScreen("view/ViewMap.fxml",
                "Map", "LEFT");
        if (timeLine != null) {
            timeLine.stop();
        }
        timeLine = new Timeline(new KeyFrame(Duration.seconds(10),
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //update map
                    if (mapControl != null) {
                        mapControl.refreshMap();
                    }
                }
            }));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * sets the screen to view all recents reports
     */
    public void showGraph() {
        showScreen("view/ViewHistoricalGraph.fxml",
                "ViewHistoricalGraph", "LEFT");
    }

    /**
     * Passes login information from LoginController to MenuBarController.
     * Main Application calls a controller, not ideal but only solution I
     * could think of.  And this prevents controllers from calling each other
     * which would be even worse.
     */
    public void updateMenuBar() {
        menuBarControl.userLogsIn(currentUser);
    }

    /**
     * update the buttons for main screen based on the user type & login status.
     */
    public void checkAuthority() {
        if (currentUser.getUserType().equals("Manager")
                || currentUser.getUserType().equals("Worker")
                || currentUser.getUserType().equals("Admin")) {
            MainScreenController.setAuthority(true);
            CreateReportController.setAuthority(true);
            ViewAllReportsController.setAuthority(true);
            MenuBarController.setAuthority(true);
            ViewMyReportsController.setAuthority(true);
            if (currentUser.getUserType().equals("Manager")) {
                MenuBarController.setManager(true);
            }
        } else {
            MainScreenController.setAuthority(false);
            CreateReportController.setAuthority(false);
            ViewAllReportsController.setAuthority(false);
            MenuBarController.setAuthority(false);
            ViewMyReportsController.setAuthority(false);
        }
        ViewAllReportsController.setLoggedIn(true);
        ViewMyReportsController.setUsername(currentUsername);
        ViewMyReportsController.setLoggedIn(true);
    }

    /**
     * gets the current userinfo
     * @return current User using the app
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * sets the current user when logged in
     * @param u user currently using the app
     */
    public void setCurrentUser(User u) {
        currentUser = u;
    }

    /**
     * Gets the username of the user logged into the application.
     * @return current username
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Sets the username of the user logged into the application.
     * @param username username who is currently using the app
     */
    public void setCurrentUsername(String username) {
        currentUsername = username;
    }

    /**
     * Gets the current center of map in the application.
     * @return Current center of map
     */
    public LatLong getMapCenter() {
        return mapCenter;
    }

    /**
     * Sets the current center of map in the application.
     * @param center Current center of map current
     */
    public void setMapCenter(LatLong center) {
        mapCenter = center;
        if (createReportControl != null) {
            createReportControl.populateLocation(center);
        }
        if (editReportControl != null) {
            editReportControl.populateLocation(center);
        }
    }

    /**
     * Updates the LatLong of the application by only changing latitude.
     * @param newLat New Latitude of the map.
     */
    public void changeCenterLatitude(Double newLat) {
        if (newLat != null && mapCenter != null) {
            mapCenter = new LatLong(newLat, mapCenter.getLongitude());
            createReportControl.populateLocation(mapCenter);
            mapControl.changeCenterView(mapCenter);
        }
    }

    /**
     * Updates the LatLong of the application by only changing longitude.
     * @param newLong New Longitude of the map.
     */
    public void changeCenterLongitude(Double newLong) {
        if (newLong != null && mapCenter != null) {
            mapCenter = new LatLong(mapCenter.getLatitude(), newLong);
            createReportControl.populateLocation(mapCenter);
            mapControl.changeCenterView(mapCenter);
        }
    }

    /**
     * Gets the report that is currently being viewed in the application.
     * @return currentReport
     */
    public WaterReport getCurrentReport() {
        return currentReport;
    }

    /**
     * Sets the report that is being viewed in the application.
     * @param report Active Report.
     */
    public void setCurrentReport(WaterReport report) {
        currentReport = report;
        mainScreenControl.setCurrentReport(report);
        if (editReportControl != null) {
            editReportControl.populateReportInformation(currentReport);
        }
    }

    /**
     * @return border pane of the main screen
     */
    public BorderPane getRootLayout() {
        return rootLayout;
    }

    /**
     * Slide in the workbench as screen is switched.
     * @param pane pane to be animated
     */
    private void animate(Pane pane) {
        if (pane instanceof StackPane || pane instanceof AnchorPane) {
            TranslateTransition transition
                    = new TranslateTransition(Duration.seconds(0.3), pane);
            transition.setFromX(400);
            transition.setToX(0);
            transition.play();
        }
    }

    /**
     * runs the program.
     * @param args runs the program.
     */
    public static void main(String[] args) {
        //Instantiate/Initialize singletons and static classes
        DataManager.initializeFireBase();
        UserDataObject userDAO = UserDataObject.getInstance();
        ReportDataObject reportDAO = ReportDataObject.getInstance();
        launch(args);
    }
}
