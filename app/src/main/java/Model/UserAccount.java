package Model;

/**
 * Represents a user account in the budget tracking application.
 * Stores user-specific information, such as ID, password, name, and budget.
 */
public class UserAccount {

    /**
     * The unique ID of the user.
     */
    private String userID;

    /**
     * The password associated with the user account.
     */
    private String userPassword;

    /**
     * The name of the user.
     */
    private String userName;

    /**
     * The budget allocated for the user.
     */
    private String userBudget;

    /**
     * Constructs a {@code UserAccount} object with the specified details.
     *
     * @param userID       the unique ID of the user
     * @param userPassword the password associated with the user account
     * @param userName     the name of the user
     * @param userBudget   the budget allocated for the user
     */
    public UserAccount(String userID, String userPassword, String userName, String userBudget){
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userBudget = userBudget;
    }

    /**
     * Retrieves the unique ID of the user.
     *
     * @return the user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Updates the unique ID of the user.
     *
     * @param userID the new user ID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Retrieves the password associated with the user account.
     *
     * @return the user password
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Updates the password associated with the user account.
     *
     * @param userPassword the new user password
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Updates the name of the user.
     *
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves the budget allocated for the user.
     *
     * @return the user budget
     */
    public String getUserBudget() {
        return userBudget;
    }

    /**
     * Updates the budget allocated for the user.
     *
     * @param userBudget the new user budget
     */
    public void setUserBudget(String userBudget) {
        this.userBudget = userBudget;
    }


}
