package classes;

import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

public abstract class WaterReport {

    private String id = null;
    private Date date;
    private StringProperty reporterId = new SimpleStringProperty();
    private ObjectProperty<Location> location = new SimpleObjectProperty<>();


    /**
     * Instantiates an abstract WaterReport. WaterSourceReport and
     * Water purity report extend this class.
     * @param  date     The date report was created
     * @param  reporterId The username of the report creator.
     * @param  location The location that the water report comes from.
     */
    WaterReport(Date date, String reporterId, Location location) {
        this.date = date;
        this.reporterId.set(reporterId);
        this.location.set(location);
    }

    /**
     * No args constructor for firebase
     */
    WaterReport() {

    }

    /**
     * Gets id of the Report.
     * @return Returns Id of Report.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets Id of water Report.
     * @param id New id of the Report.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the location of the water source.
     *
     * @return the location of the water source
     */
    public Location getLocation() {
        return location.get();
    }

    /**
     * Set the location of the water source.
     *
     * @param location the location of the water source
     */
    public void setLocation(Location location) {
        this.location.set(location);
    }

    /**
     * Get the date when the report is created.
     *
     * @return the date when the report is created
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the created date on the report.
     * @param date Report creation date.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the date as a string when the report is created.
     *
     * @return the date when the report was created as a string.
     */
    public String getDateAsString() {
        SimpleDateFormat dateFormat =
            new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm:ss a zzz");
        return dateFormat.format(this.getDate());
    }

    /**
     * Set the date as a string when the report is created.
     *
     * @param date The date when the report was created.
     */
    public void setDateAsString(String date) {
        SimpleDateFormat dateFormat =
            new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm:ss a zzz");
        this.date = dateFormat.parse(date, new ParsePosition(0));
    }

    /**
     * Get the user ID of the reporter.
     *
     * @return the user ID of the reporter
     */
    public String getReporterId() {
        return reporterId.get();
    }

    /**
     * Sets the reporterId of this reports.
     * @param id The user id of reporter.
     */
    public void setReporterId(String id) {
        reporterId.set(id);
    }
}
