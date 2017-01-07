package classes;

public class Location {

    private String latitude;
    private String longitude;

    /**
     * Constructor for GPS coordinates.
     *
     * @param latitude The latitude GPS coordinate.
     * @param longitude The longitude GPS coordinate.
     */
    public Location(String latitude, String longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * For FireBase
     */
    public Location() {

    }

    /**
     * Get the latitude of the location.
     *
     * @return the latitude of the location
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Set's latitude instance variable to given latitude.
     * @param latitude The latitude to set as a double.
     * @throws IllegalArgumentException Thrown if latitude isn't
     * between -90 and 90.
     */
    public void setLatitude(String latitude) {
        try {
            if (Math.abs(Double.parseDouble(latitude)) > 90) {
                throw new IllegalArgumentException("Latitude must be within"
                    + " the range of -90 to 90. Given latitude = "
                    + latitude);
            }
            this.latitude = latitude;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Given latitude"
                + "can't be converted to a double");
        }
    }

    /**
     * Get the longitude of the location.
     *
     * @return the longitude of the location
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Set's longitude instance variable to given longitude.
     * @param longitude The longitude to set given as a double.
     * @throws IllegalArgumentException Thrown if longitude isn't between
     * -180 and 180.
     */
    public void setLongitude(String longitude) {
        try {
            if (Math.abs(Double.parseDouble(longitude)) > 180) {
                throw new IllegalArgumentException("Longitude must be within"
                    + " the range of -180 to 180. Given longitude = "
                    + longitude);
            }
            this.longitude = longitude;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Given longitude "
                + "can't be converted to a double");
        }
    }

    @Override
    public String toString() {
        return "lat: " + latitude + ", lng: " + longitude;
    }
}
