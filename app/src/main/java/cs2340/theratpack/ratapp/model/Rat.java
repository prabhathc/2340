package cs2340.theratpack.ratapp.model;
import com.google.firebase.database.DataSnapshot;


public class Rat {
    // --Commented out by Inspection (11/10/2017 9:48 PM):private static final String TAG = "Rat";
    private String uniqueKey;
    private long createdDate;
    private String locationType;
    private String incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private double latitude;
    private double longitude;

    /**
     * Call this when you're pulling from firebase
     * @param ratData this is the object from firebase if youre pulling rat data down
     */
    public Rat(DataSnapshot ratData) {
        this(ratData.getKey(), Long.parseLong((String)ratData.child("Created Date").getValue()),
                (String)ratData.child("Location Type").getValue(), (String)ratData.child("Incident Zip").getValue(),
                (String)ratData.child("Incident Address").getValue(), (String)ratData.child("City").getValue(),
                (String)ratData.child("Borough").getValue(), Double.parseDouble((String)ratData.child("Longitude").getValue()),
                Double.parseDouble((String)ratData.child("Latitude").getValue()));
        //DataSnapshot ratData1 = ratData;
    }

    /**
     * Call this explicitly if you're creating a rat sighting. Follow it up with pushing to the database
     * @param uniqueKey key in firebase
     * @param createdDate date the sighting ocurred
     * @param locationType the kind of location the incident was at
     * @param incidentZip zip code
     * @param incidentAddress address of the incident
     * @param city city of the incident
     * @param borough borough of ...
     * @param longitude longitude for Google Maps
     * @param latitude latitute for Google Maps
     */
    public Rat(String uniqueKey, long createdDate, String locationType, String incidentZip,
               String incidentAddress, String city, String borough, double longitude,
               double latitude) {
        this.uniqueKey = uniqueKey;
        this.createdDate = createdDate;
        this.locationType = locationType;
        this.incidentZip = incidentZip;
        this.incidentAddress = incidentAddress;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*public void setUniqueKey(String newKey) {
        uniqueKey = newKey;
    }

    public void setCreatedDate(Long newDate) {
        createdDate = newDate;
    }

    public void setLocationType(String newLocType) {
        locationType = newLocType;
    }

    public void setIncidentZip(String newZip) {
        incidentZip = newZip;
    }

    public void setIncidentAddress(String newAddress) {
        incidentAddress = newAddress;
    }

    public void setCity(String newCity) {
        city = newCity;
    }

    public void setBorough(String newBorough) {
        borough = newBorough;
    }

    public void setLatitude(double newLat) {
        latitude = newLat;
    }

    public void setLongitude(double newLong) {
        longitude = newLong;
    }
    */

    public String getUniqueKey() {
        return uniqueKey;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getIncidentZip() {
        return incidentZip;
    }

    public String getIncidentAddress() {
        return incidentAddress;
    }

    public String getCity() {
        return city;
    }

    public String getBorough() {
        return borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String toString() { return uniqueKey; }

}
