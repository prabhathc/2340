package cs2340.theratpack.ratapp.model;


public enum UserType {
    USER("User"),
    ADMIN("Administrator");

    private final String value;

    UserType(String value) {
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
