package classes;

import java.util.Date;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class WaterPurityReport extends WaterReport {

    private DoubleProperty virusPPM = new SimpleDoubleProperty();
    private DoubleProperty contaminantPPM = new SimpleDoubleProperty();
    private ObjectProperty<OverallCondition> overallCondition =
        new SimpleObjectProperty<>();

    /**
     * Creates a WaterPurityReportObject
     * @param  reporterId Username of the person creating the report
     * @param  date       Date report was created
     * @param  location   Location of the water
     * @param overallCondition Safe/Treatable/Unsafe
     * @param virusPPM The parts per million of viruses in the water.
     * @param  contaminantPPM The parts per million
     *                        of contaminants in the water.
     */
    public WaterPurityReport(String reporterId, Date date, Location location,
        OverallCondition overallCondition, double virusPPM,
            double contaminantPPM) {
        super(date, reporterId, location);
        this.virusPPM.set(virusPPM);
        this.contaminantPPM.set(contaminantPPM);
        this.overallCondition.set(overallCondition);
    }

    /**
     * Sets virusPPM
     * @param virusPPM Virus parts per million to be set
     */
    public void setVirusPPM(double virusPPM) {
        this.virusPPM.set(virusPPM);
    }

    /**
     * Gets this report's virusPPM
     * @return Virus data in parts per million
     */
    public double getVirusPPM() {
        return virusPPM.get();
    }

    /**
     * Sets this report's contaminant parts per million.
     * @param contaminantPPM contaminant parts per million to be set.
     */
    public void setContaminantPPM(double contaminantPPM) {
        this.contaminantPPM.set(contaminantPPM);
    }

    /**
     * Gets this report's contaminant parts per million.
     * @return Contaminant data in parts per million.
     */
    public double getContaminantPPM() {
        return contaminantPPM.get();
    }

    /**
     * Sets condition of the water
     * @param condition OverallCondition of the water to be set.
     */
    public void setOverallCondition(OverallCondition condition) {
        this.overallCondition.set(condition);
    }

    /**
     * Gets OverallCondition
     * @return Returns overall condition of water
     */
    public OverallCondition getOverallCondition() {
        return this.overallCondition.get();
    }

    /**
     * For FireBase
     */
    private WaterPurityReport() {
        super();
    }
}
