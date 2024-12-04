package Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages a collection of {@code UserAccount} objects in budget tracker application.
 * Handles account creation, retrieval, modification, and persistence.
 */
public class AccountManager {

    /**
     * List of all user accounts managed by this instance.
     */
    private ArrayList<UserAccount> userAccounts;

    /**
     * Reference to the Android {@link Activity} that owns this manager.
     */
    private final Activity activity;

    /**
     * Name of the file used to persist account data.
     */
    private static final String FILENAME = "account-list.csv";

    /**
     * Tag used for logging in this class.
     */
    private static final String TAG = "AccountManager";

    /**
     * Constructs an {@code AccountManager} instance.
     * Initializes the account list as an empty {@link ArrayList}.
     *
     * @param activity the Android activity associated with this manager
     */
    public AccountManager(Activity activity){
        this.activity = activity;
        userAccounts = new ArrayList<UserAccount>();
    }

    /**
     * Retrieves the list of user accounts.
     *
     * @return an {@link ArrayList} of {@link UserAccount} objects
     */
    public ArrayList<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    /**
     * Updates the list of user accounts.
     *
     * @param userAccounts the new list of user accounts
     */
    public void setUserAccounts(ArrayList<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    /**
     * Retrieves a user account by its user ID.
     *
     * @param userID the ID of the user account to retrieve
     * @return the matching {@link UserAccount}, or {@code null} if not found
     */
    public UserAccount getUserAccount(String userID){
        for (UserAccount account : userAccounts){
            if(account.getUserID().equals(userID)){
                return account;
            }
        }
        return null;
    }

    /**
     * Initializes the account list file. If the file exists, loads the accounts.
     * Otherwise, creates a new file.
     */
    public void initializeAccountList() {
        try {
            // Attempt to read the file
            InputStream in = activity.openFileInput(FILENAME);
            Log.i(TAG, "File found, loading account data...");
            loadUserAccounts(in);
        } catch (FileNotFoundException e) {
            // If file does not exist, create it
            Log.e(TAG, "File not found. Creating new account list file: " + FILENAME);
            try {
                OutputStream out = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                Log.i(TAG, "File created successfully.");
                out.close();
            } catch (FileNotFoundException e2) {
                Log.e(TAG, "Failed to create file: " + FILENAME);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Loads user accounts from an input stream.
     *
     * @param in the input stream containing user account data
     */
    private void loadUserAccounts(InputStream in) {
        if (in != null) {
            Scanner scan = new Scanner(in);
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");
                if (tokens.length == 4) {
                    addUserAccount(new UserAccount(tokens[0].trim(), tokens[1].trim(), tokens[2].trim(), tokens[3].trim()));
                }
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new user account to the manager.
     *
     * @param userAccount the {@link UserAccount} to add
     */
    public void addUserAccount(UserAccount userAccount) {
        if (userAccounts == null) {
            userAccounts = new ArrayList<>();
        }
        if (userAccount != null) {
            userAccounts.add(userAccount);
        }
    }


    /**
     * Saves all user accounts to the account list file.
     */
    public void saveAccountsToFile() {
        try {
            OutputStream out = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (UserAccount account : userAccounts) {
                String accountData = account.getUserID() + "," + account.getUserPassword() + "," + account.getUserName() + "," + account.getUserBudget() + "\n";
                out.write(accountData.getBytes(StandardCharsets.UTF_8));
            }
            out.close();
            Log.i(TAG, "Account data saved successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to save account data.");
            e.printStackTrace();
        }
    }

    /**
     * Updates a user's name and saves changes to the file.
     *
     * @param userId      the ID of the user
     * @param currentName the current name of the user
     * @param newName     the new name of the user
     */
    public void changeUserName(String userId, String currentName, String newName){
        Log.d(TAG, "Attempting to update " + userId + "'s name. Current Name: " + currentName);
        UserAccount account = getUserAccount(userId);
        if(account != null) {
            account.setUserName(newName);
            saveAccountsToFile();
            Log.i(TAG, "User's name has been changed from " + currentName + " to " + newName);
        }
        else{
            Log.e(TAG, "Account cannot be found for " + userId);
        }
    }

    /**
     * Updates a user's password and saves changes to the file.
     *
     * @param userId          the ID of the user
     * @param currentPassword the current password of the user
     * @param newPassword     the new password of the user
     */
    public void changeUserPassword(String userId, String currentPassword, String newPassword){
        Log.d(TAG, "Attempting to update " + userId + "'s password. Current Password: " + currentPassword);
        UserAccount account = getUserAccount(userId);
        if(account != null) {
            account.setUserPassword(newPassword);
            saveAccountsToFile();
            Log.i(TAG, "User's password has been changed from " + currentPassword + " to " + newPassword);
        }
        else{
            Log.e(TAG, "Account cannot be found for " + userId);
        }
    }

    /**
     * Updates a user's budget and saves changes to the file.
     *
     * @param userId       the ID of the user
     * @param currentBudget the current budget of the user
     * @param newBudget    the new budget of the user
     */
    public void changeUserBudget(String userId, String currentBudget, String newBudget){
        Log.d(TAG, "Attempting to update " + userId + "'s budget. Current Budget: " + currentBudget);
        UserAccount account = getUserAccount(userId);
        if(account != null) {
            account.setUserBudget(newBudget);
            saveAccountsToFile();
            Log.i(TAG, "User's budget has been changed from " + currentBudget + " to " + newBudget);
        }
        else{
            Log.e(TAG, "Account cannot be found for " + userId);
        }
    }

    /**
     * Removes a user account from the manager.
     *
     * @param account the {@link UserAccount} to remove
     * @return {@code true} if the account was removed, {@code false} otherwise
     */
    public boolean removeUserAccount(UserAccount account) {
        return userAccounts.remove(account); // Remove from the ArrayList
    }
}
