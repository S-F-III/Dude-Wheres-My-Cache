package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Model.AccountManager;
import Model.UserAccount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Activity responsible for handling the user sign-up process. This includes collecting user information,
 * validating inputs, creating a new user account, and navigating to the next screen (OverviewActivity).
 */

public class SignUpActivity extends AppCompatActivity {

    private AccountManager accountManager;

    /**
     * Initializes the sign-up screen by setting up the UI components and handling the creation of new user accounts.
     *
     * @param savedInstanceState The saved instance state (if any) of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Adjusts the padding to accommodate system bars (e.g., status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        EditText inputUserName = (EditText) findViewById(R.id.inputUserName);
        EditText inputUserID = (EditText) findViewById(R.id.inputUserID);
        EditText inputPassword = (EditText) findViewById(R.id.inputPassword);
        EditText inputUserBudget = (EditText) findViewById(R.id.inputBudget);
        Button createAcountButton = findViewById(R.id.create_account_button);

        // Initialize AccountManager
        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        // Handle account creation button click
        createAcountButton.setOnClickListener(view -> {
            String userName = inputUserName.getText().toString().trim();
            String userID = inputUserID.getText().toString().trim();
            String userPassword = inputPassword.getText().toString().trim();
            String userBudget = inputUserBudget.getText().toString().trim();

            // Perform input validation
            if (!validateInputs(userName, userID, userPassword, userBudget)) {
                return; // Stop if validation fails
            }

            // Check if the account already exists
            if (accountManager.getUserAccount(userID) != null) {
                Toast.makeText(SignUpActivity.this, "Error: Account already exists", Toast.LENGTH_SHORT).show();
                return; // Stop if user ID already exists
            }

            // Create and save the new account
            UserAccount newAccount = new UserAccount(userID, userPassword, userName, userBudget);
            accountManager.addUserAccount(newAccount);
            accountManager.saveAccountsToFile();

            // Launch the next activity
            launchActivity(userName, userID, userPassword, userBudget);
        });
    }

    /**
     * Validates user inputs for the sign-up form. Checks if all fields are filled and if the budget is valid.
     *
     * @param name The user's name.
     * @param id The user's ID.
     * @param password The user's password.
     * @param budget The user's budget.
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInputs(String name, String id, String password, String budget) {
        // Check if any input field is empty
        if (name.isEmpty() || id.isEmpty() || password.isEmpty() || budget.isEmpty()) {
            Toast.makeText(this, "Error: All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate that the budget is a number
        if (!isNumeric(budget)) {
            Toast.makeText(this, "Invalid Budget: Enter a number e.g., 1000", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate that the budget is greater than 0
        int userBudget = Integer.parseInt(budget);
        if (userBudget <= 0) {
            Toast.makeText(this, "Invalid Budget: Must be greater than 0", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All inputs are valid
    }

    /**
     * Launches the OverviewActivity after successfully creating a new user account.
     * Saves the user's ID and account creation date to SharedPreferences.
     *
     * @param name The user's name.
     * @param id The user's ID.
     * @param password The user's password.
     * @param budget The user's budget.
     */
    private void launchActivity(String name, String id, String password, String budget) {
        // Save user ID and creation date to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_ID", id);
        if (!sharedPreferences.contains("CREATION_DATE")) {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            editor.putString("CREATION_DATE", currentDate);
        }
        editor.apply();

        // Launch the OverviewActivity
        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra("USER_NAME", name);
        intent.putExtra("USER_ID", id);
        intent.putExtra("USER_PASSWORD", password);
        intent.putExtra("USER_BUDGET", budget);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Checks if a string can be parsed as a valid numeric value.
     *
     * @param strNum The string to check.
     * @return true if the string can be parsed as a numeric value, false otherwise.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum); // Try parsing the number
        } catch (NumberFormatException nfe) {
            return false; // Return false if it cannot be parsed
        }
        return true; //Return true if parsing was successful
    }
}