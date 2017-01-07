package controller;

import classes.HistoricalReport;
import classes.Location;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import main.MainFXApplication;

import java.util.ArrayList;
import java.util.List;

public class ViewHistoricalGraphController implements IController {
    private MainFXApplication mainApplication;
    @FXML private ComboBox yearList = new ComboBox<>();
    @FXML private RadioButton virusButton;
    @FXML private RadioButton contaminantButton;
    @FXML private Button ok;
    @FXML private Button back;
    @FXML private TextField latitude;
    @FXML private TextField longitude;
    @FXML private TextField radius;
    @FXML private LineChart graph;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    /**
     * Initializes variable bindings and login handler
    */

    @FXML
    private void initialize() {
        yearList.getItems().setAll("2016", "2017");
    }

    /**
     * allow for calling back to the main application code if necessary
     * @param main   the reference to the FX Application instance
     * */
    public void setMainApp(MainFXApplication main) {
        mainApplication = main;
    }

    /**
     * Button handler for graph page.
     * Clicking "Back to Main" will redirect to main screen
     * Clicking "OK" will update the graph on left side
     *
     * @param event the button user clicks.
     */
    @FXML
    private void handleButtonClicked(ActionEvent event) {
        if (event.getSource() == ok) {
            Location radiusCenter = new Location(
                    latitude.getText(), longitude.getText());
            Double radiusSize = Double.parseDouble(radius.getText());
            String type;
            if (contaminantButton.isSelected()) {
                type = "contaminantppm";
            } else {
                type = "virusppm";
            }
            String year = yearList.getValue().toString();
            updateChart(radiusCenter, radiusSize, type, year);
        } else if (event.getSource() == back) {
            mainApplication.showMap();
            mainApplication.showMainScreen();
        }
    }

    /**
     * This updates the chart based on information provided by user.
     *
     * @param  radiusCenter Center of the search for reports
     * @param  radiusSize   Size of the radius to search for reports in
     * @param  type         virusPPM or contaminantPPM
     * @param  year         year to filter the reports on
     */
    @FXML
    private void updateChart(Location radiusCenter,
                             Double radiusSize, String type, String year) {
        HistoricalReport report = new HistoricalReport(
                radiusCenter, radiusSize, type, year);
        ArrayList<Double> list = report.getDataByMonth();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May",
            "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        List<XYChart.Data<String, Double>> seriesData = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            seriesData.add(new XYChart.Data(months[i], list.get(i)));
            System.out.println(months[i] + " " + list.get(i));
        }

        XYChart.Series<String, Double> series
                = new XYChart.Series<>();
        series.getData().addAll(seriesData);
        xAxis.setAnimated(false);
        graph.setData(FXCollections.observableArrayList(series));
        graph.setLegendVisible(false);
        if (type.equals("virusppm")) {
            yAxis.setLabel("Virus PPM");
        } else {
            yAxis.setLabel("Contaminant PPM");
        }

    }
}
