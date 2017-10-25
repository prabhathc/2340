package cs2340.theratpack.ratapp.Model;
import com.google.firebase.database.DataSnapshot;


/**
 * Created by prabhath on 10/11/2017.
 */

public class Rat {
    private static final String TAG = "Rat";
    private String uniqueKey;
    private String createdDate;
    private String locationType;
    private String incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private double latitude;
    private double longitude;
    private DataSnapshot ratData;

    /**
     * Call this when you're pulling from firebase
     * @param ratData this is the object from firebase if youre pulling rat data down
     */
    public Rat(DataSnapshot ratData) {
        this(ratData.getKey(), (String)ratData.child("Created Date").getValue(),
                (String)ratData.child("Location Type").getValue(), (String)ratData.child("Incident Zip").getValue(),
                (String)ratData.child("Incident Address").getValue(), (String)ratData.child("City").getValue(),
                (String)ratData.child("Borough").getValue(), Double.parseDouble((String)ratData.child("Longitude").getValue()),
                Double.parseDouble((String)ratData.child("Latitude").getValue()));
        this.ratData = ratData;
    }

    /**
     * Call this explicitly if you're creating a rat sighting. Follow it up with pushing to the database
     * @param uniqueKey key in firebase
     * @param createdDate date the sighting ocurred
     * @param locationType
     * @param incidentZip
     * @param incidentAddress
     * @param city
     * @param borough
     * @param longitude
     * @param latitude
     */
    public Rat(String uniqueKey, String createdDate, String locationType, String incidentZip,
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

    public void setUniqueKey(String newKey) {
        uniqueKey = newKey;
    }

    public void setCreatedDate(String newDate) {
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public String getCreatedDate() {
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
