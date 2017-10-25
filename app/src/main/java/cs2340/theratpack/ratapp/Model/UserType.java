package cs2340.theratpack.ratapp.Model;

/**
 * Created by Jamal Paden on 9/30/2017.
 */

public enum UserType {
    USER("User"),
    ADMIN("Administrator");

    private String value;

    private UserType(String value) {
        this.value = value;
    }

    /**
     * Method to return the string representation of the UserType
     * @return the string representation of the enum
     */
    @Override
    public String toString() {
        return value;
    }
}
