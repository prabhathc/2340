package cs2340.theratpack.ratapp;
import java.*;

/**
 * Created by prabhath on 10/11/2017.
 */

public class Rat {
    private int uniqueKey;
    private String createdDate;
    private String locationType;
    private int incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private double latitude;
    private double longitude;

    public Rat(int uniqueKey, String createdDate, String locationType, int incidentZip, String incidentAddress, String city, String borough, double longitude, double latitude) {
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

    public void setUniqueKey(int newKey) {
        uniqueKey = newKey;
    }

    public void setCreatedDate(String newDate) {
        createdDate = newDate;
    }

    public void setLocationType(String newLocType) {
        locationType = newLocType;
    }

    public void setIncidentZip(int newZip) {
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

    public int getUniqueKey() {
        return uniqueKey;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLocationType() {
        return locationType;
    }

    public int getIncidentZip() {
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

}
