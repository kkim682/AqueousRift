package classes;

import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class WaterSourceReport extends WaterReport {

    private ObjectProperty<WaterType> type = new SimpleObjectProperty<>();
    private ObjectProperty<WaterCondition> condition =
        new SimpleObjectProperty<>();

    /**
     * Constructor used specifically for the editing call chain in
     * ReportDataObject
     *
     * @param   reporterId The username of the user who created the report
     * @param   location The location of the report
     * @param   type The type of water found
     * @param   condition The condition of water found
     * @param   date The date the report was submitted
     */
    public WaterSourceReport(String reporterId, Location location,
            WaterType type, WaterCondition condition, Date date) {
        super(date, reporterId, location);
        this.type.set(type);
        this.condition.set(condition);
    }

    /**
     * Get the type of the water source.
     *
     * @return the type of the water source
     */
    public WaterType getType() {
        return type.get();
    }

    /**
     * Set the type of the water source.
     *
     * @param type the type of the water source
     */
    public void setType(WaterType type) {
        this.type.set(type);
    }

    /**
     * Sets condition of the water
     * @param condition WaterCondition of the water to be set.
     */
    public void setCondition(WaterCondition condition) {
        this.condition.set(condition);
    }

    /**
     * Gets WaterCondition
     * @return Returns WaterCondition of water
     */
    public WaterCondition getCondition() {
        return this.condition.get();
    }

    /**
     * For FireBase
     */
    private WaterSourceReport() {
        super();
    }
}
