package controller;

import classes.WaterPurityReport;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.event.MapStateEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import main.MainFXApplication;
import model.ReportDataObject;
import classes.WaterSourceReport;
import netscape.javascript.JSObject;
import java.util.ArrayList;
import java.util.List;
//import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapController implements IController,
        MapComponentInitializedListener {

    private static StringProperty filterType = new SimpleStringProperty();
    private static StringProperty filterCondition = new SimpleStringProperty();
    private static StringProperty filterOverallCondition
            = new SimpleStringProperty();
    private static StringProperty filterAll = new SimpleStringProperty();
    private LatLong center;
    private InfoWindow opened = null;
    private GoogleMap map;
    private MainFXApplication mainApplication;
    @FXML private GoogleMapView mapView;
    private static final List<Marker> MARKERS = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        mapView.addMapInializedListener(this);
    }

    @Override
    public void mapInitialized() {

        MapOptions mapOptions = new MapOptions();
        center = new LatLong(35, 120);
        mainApplication.setMapCenter(center);
        mapOptions.center(center)
                //.mapType(MapTypeIdEnum.TERRAIN)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(3);

        map = mapView.createMap(mapOptions);
        map.addUIEventHandler(map, UIEventType.click, (JSObject obj) -> {
            if (opened != null) {
                opened.close();
            }
            opened = null;
        });
        map.addStateEventHandler(MapStateEventType.drag, () -> {
            center = map.getCenter();
            mainApplication.setMapCenter(center);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        ReportDataObject reportDAO = ReportDataObject.getInstance();
        putAllPins(reportDAO);
    }

    /**
     * Helper method for delegating pin creating task based on reports
     * @param reportDAO Data object to retrieve data for placing pins
     */
    private void putAllPins(ReportDataObject reportDAO) {
        for (WaterSourceReport report
                : reportDAO.getAllSourceReports().values()) {
            String type = report.getType().toString();
            String condition = report.getCondition().toString();
            if (type.equals(filterType.get())) {
                putSourcePins(report);
            } else if (condition.equals(filterCondition.get())) {
                putSourcePins(report);
            } else if ((filterCondition.get() == null
                    && filterType.get() == null
                    && (filterOverallCondition.get() == null))
                    || filterAll.get().equals("All")) {
                putSourcePins(report);
            }
        }
        for (WaterPurityReport report
                : reportDAO.getAllPurityReports().values()) {
            String overallCondition = report.getOverallCondition().toString();
            if (overallCondition.equals(filterOverallCondition.get())) {
                putPurePins(report);
            } else if ((filterCondition.get() == null
                    && filterType.get() == null
                    && filterOverallCondition.get() == null)
                    || filterAll.get().equals("All")) {
                putPurePins(report);
            }
        }
        filterAll.set(null);
        filterOverallCondition.set(null);
        filterType.set(null);
        filterCondition.set(null);
    }

    /**
     * place the pins for water purity reports.
     *
     * @param report water purity report
     */
    private void putPurePins(WaterPurityReport report) {
        double lat = Double.parseDouble(report.getLocation().getLatitude());
        double lng = Double.parseDouble(report.getLocation()
                .getLongitude());
        LatLong location = new LatLong(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location)
            .icon("http://maps.google.com/mapfiles/ms/icons/green-dot.png");
        Marker marker = new Marker(markerOptions);
        MARKERS.add(marker);
        map.addMarker(marker);
        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("Overall Condition: "
                + report.getOverallCondition());
        InfoWindow window = new InfoWindow(infoWindowOptions);

        map.addUIEventHandler(marker,
            UIEventType.click,
            (JSObject obj) -> {
                mainApplication.setCurrentReport(report);
                if (opened != null) {
                    opened.close();
                }
                window.open(map, marker);
                opened = window;
            }
        );
    }

    /**
     * place the pins for water source reports.
     *
     * @param report water source report
     */
    private void putSourcePins(WaterSourceReport report) {
        double lat = Double.parseDouble(report.getLocation().getLatitude());
        double lng = Double.parseDouble(report.getLocation()
                .getLongitude());
        LatLong location = new LatLong(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        Marker marker = new Marker(markerOptions);
        MARKERS.add(marker);
        map.addMarker(marker);
        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("Condition: "
                + report.getCondition() + "<br>Type: "
                + report.getType());
        InfoWindow window = new InfoWindow(infoWindowOptions);

        map.addUIEventHandler(marker,
            UIEventType.click,
            (JSObject obj) -> {
                mainApplication.setCurrentReport(report);
                if (opened != null) {
                    opened.close();
                }
                window.open(map, marker);
                opened = window;
            }
        );
    }

    /**
     * Refreshes the pin placements on the map.
     */
    public void refreshMap() {
    //        for (Iterator<Marker> iter
    //              = MARKERS.iterator(); iter.hasNext();) {
    //            Marker m = iter.next();
    //            map.removeMarker(m);
    //            iter.remove();
    //        }
    //        ReportDataObject reportDAO = ReportDataObject.getInstance();
    //        putAllPins(reportDAO);
    //        log.info("Report markers refreshed!");
    }

    /**
     * Returns the center coordinates of the map.
     * @return LatLong center object.
     */
    public LatLong getCenter() {
        return center;
    }

    /**
     * Updates the map with new center locations
     * @param latlng New center coordinates from main Application.
     */
    public void changeCenterView(LatLong latlng) {
        map.setCenter(latlng);
        center = latlng;
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
     * Sets the water type of the report for filtering the pins.
     *
     * @param inputType type of the water
     */
    public static void setWaterType(String inputType) {
        filterType.set(inputType);
    }

    /**
     * Sets the water type of the report for filtering the pins.
     *
     * @param inputType condition of the water
     */
    public static void setWaterCondition(String inputType) {
        filterCondition.set(inputType);
    }

    /**
     * Sets the overall condition of the report for filtering the pins.
     *
     * @param inputType overall condition of the water
     */
    public static void setOverallCondition(String inputType) {
        filterOverallCondition.set(inputType);
    }

    /**
     * Sets the water type of the report for filtering the pins.
     *
     * @param inputType sets the pins to show all.
     */
    public static void setAllPins(String inputType) {
        filterAll.set(inputType);
    }
}
