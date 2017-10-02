package cs2340.theratpack.ratapp;

/** Model for the user class
 * Created by Jamal Paden on 9/28/2017.
 */

public class User {
    private String username;
    private String password;
    private String email;
    private Integer loginAttempts;
    private UserType type;


    public User(String username, String password, String email, UserType type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
        this.loginAttempts = 0;
    }

    /**
     * Method to increment user logins. Used for security
     */
    public void incrementLogins() {
        this.loginAttempts++;
    }

    //public void registerUser() {

    //}

    /**
     * Method to return whether or not a given user is an admin
     * @return whether or not a particular instance is an admin
     */
    public boolean isAdmin() {
        return type.toString().equals("Administraator");
    }



}
