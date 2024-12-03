package Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {

    private ArrayList<UserAccount> userAccounts;
    private final Activity activity;
    private static final String FILENAME = "account-list.csv";
    private static final String TAG = "AccountManager";


    public AccountManager(Activity activity){
        this.activity = activity;
        userAccounts = new ArrayList<UserAccount>();
    }

    public ArrayList<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(ArrayList<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    public UserAccount getUserAccount(String userID){
        for (UserAccount account : userAccounts){
            if(account.getUserID().equals(userID)){
                return account;
            }
        }
        return null;
    }

    // Method to initialize or create the account list file if it doesn't exist
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

    // Load accounts from the file
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

    // Method to add a new user account
    public void addUserAccount(UserAccount userAccount) {
        if (userAccounts == null) {
            userAccounts = new ArrayList<>();
        }
        if (userAccount != null) {
            userAccounts.add(userAccount);
        }
    }


    // Save all user accounts to the file
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
}
