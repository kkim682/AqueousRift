package classes;
import java.util.ArrayList;
import model.ReportDataObject;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;

public class HistoricalReport {
    private ArrayList<Double> dataByMonth;
    private Location radiusCenter;
    private Double radiusSize;
    private Map<String, WaterPurityReport> purityReports;

    /**
     * Constructor for Historical report
     * @param  radiusCenter Center of the search for reports
     * @param  radiusSize   Size of the radius to search for reports in
     * @param  type         virusPPM or contaminantPPM
     * @param  year         year to filter the reports on
     */
    public HistoricalReport(Location radiusCenter, Double radiusSize,
        String type, String year) {
        this(radiusCenter, radiusSize, type, year,
            ReportDataObject.getInstance().getAllPurityReports());
    }

    /**
     * Construct for Historical report (primarily for testing)
     * @param  radiusCenter  Center of the search for reports
     * @param  radiusSize    Size of the radius to search for reports in
     * @param  type          virusPPM or contaminantPPM
     * @param  year          year to filter the reports on
     * @param  purityReports Maps report ids to purity reports
     */
    public HistoricalReport(Location radiusCenter, Double radiusSize,
        String type, String year,
        Map<String, WaterPurityReport> purityReports) {

        if (radiusSize < 0) {
            throw new IllegalArgumentException("Radius can't be less " 
                + "than zero");
        }

        if (year == null || year.length() != 4) {
            throw new IllegalArgumentException("Year is either null or not of "
                + "the right length");
        }
        this.radiusCenter = radiusCenter;
        this.radiusSize = radiusSize;
        this.purityReports = purityReports;

        dataByMonth = findReports(radiusCenter,
                type, year);



    }

    /**
     * Returns the xy pairings for the data
     * @return  xy coordinate pairs
     */
    public ArrayList<Double> getDataByMonth() {
        return dataByMonth;
    }

    /**
     * Getter for center of the search
     * @return radiusCenter
     */
    public Location getCenter() {
        return radiusCenter;
    }

    /**
     * Getter for size of radius
     * @return radiusSize
     */
    public Double getRadiusSize() {
        return radiusSize;
    }

    /**
     * Finds all of the reports within the given area and returns 
     * an ArrayList of xy pairings. The x coordinate is time in long
     * and the y coordinate is the ppm as a double
     * @param radiusCenter Center of the radius to search for
     * @param  type       virusPPM or contaminantPPM
     * @param  year       year to filter the reports on
     * @return  ArrayList arrayList of xy pairings
     */
    private ArrayList<Double> findReports(Location radiusCenter,
                                          String type, String year) {

        ArrayList<Double> retList = new ArrayList<>(12);

        Map<String, Double[]> monthDataMap = new HashMap<>();

        for (WaterPurityReport wpr : purityReports.values()) {

            SimpleDateFormat yearF = new SimpleDateFormat("yyyy");
            SimpleDateFormat month = new SimpleDateFormat("MM");
            if (yearF.format(wpr.getDate()).equals(year)) {
                System.out.println(wpr.getDate());
                Location l = wpr.getLocation();

                if (withinRadius(radiusCenter, l)) {
                    double data = 0;

                    if (type.equalsIgnoreCase("virusppm")) {
                        data = wpr.getVirusPPM();
                    } else if (type.equalsIgnoreCase("contaminantppm")) {
                        data = wpr.getContaminantPPM();
                    }

                    Double[] monthData = monthDataMap.get(month.format(wpr
                        .getDate()));

                    if (monthData == null) {
                        monthData = new Double[]{data, new Double(1)};
                        monthDataMap.put(month.format(wpr.getDate()),
                            monthData);

                    } else {
                        data = ((monthData[0] * monthData[1]) + data)
                            / (monthData[1] + 1);
                        monthData[0] = data;
                        monthData[1] = monthData[1] + 1;
                        monthDataMap.put(month.format(wpr.getDate()),
                            monthData);

                    }
                }
            }
        }
        //Note: This will cause index out of bounds exception.
        //        for (String month : monthDataMap.keySet()) {
        //            double dataToAdd = monthDataMap.get(month) != null
        //                    ? monthDataMap.get(month)[0] : 0;
        //            retList.add(Integer.parseInt(month) - 1, dataToAdd);
        //        }

        String[] monthStr = {"01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12"};
        for (int i = 0; i < 12; i++) {
            if (monthDataMap.get(monthStr[i]) != null) {
                retList.add(i, monthDataMap.get(monthStr[i])[0]);
            } else {
                retList.add(i, 0.0);
            }
        }
        return retList;
    }


    /**
     * Determines if distance between two locations is within the given radius
     * @param  l1 First Location
     * @param  l2 Second Location
     * @return   True if distance between two locations is less than radiusSize
     */
    private boolean withinRadius(Location l1, Location l2) {
        double lat1 = Double.parseDouble(l1.getLatitude());
        double lat2 = Double.parseDouble(l2.getLatitude());
        double lon1 = Double.parseDouble(l1.getLongitude());
        double lon2 = Double.parseDouble(l2.getLongitude());

        final int earthRadius = 6371;
        Double latDistance = Math.toRadians(lat1 - lat2);

        Double lonDistance = Math.toRadians(lon1 - lon2);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c * .621371; //converts km to miles
        return distance <= radiusSize;
    }

}