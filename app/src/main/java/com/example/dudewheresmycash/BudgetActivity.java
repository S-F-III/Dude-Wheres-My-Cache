package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

/**
 * Activity class for managing and updating the user's budget.
 * Allows the user to input their current and new budget, validates the input,
 * and updates the budget in the system.
 */
public class BudgetActivity extends AppCompatActivity {

    /**
     * Manages user account data, including retrieval, updates, and validation.
     * Used to interact with and manage user account-related operations.
     */
    private AccountManager accountManager;

    /**
     * Called when the activity is starting.
     * Sets up the layout, initializes UI components, and configures event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget);

        // Apply window insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve user ID from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Initialize UI components
        Button saveChangesButton = findViewById(R.id.saveNameChangesButton);
        EditText inputOldBudget = (EditText) findViewById(R.id.enterCurrentBudget);
        EditText inputNewBudget = (EditText) findViewById(R.id.enterNewBudget);

        // Initialize AccountManager and load account data
        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        // Set click listener for the save changes button
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldBudget = inputOldBudget.getText().toString().trim();
                String newBudget = inputNewBudget.getText().toString().trim();
                launchAccountInfoActivity(userId, oldBudget, newBudget);
            }
        });
    }

    /**
     * Validates user input, updates the user's budget, and navigates back to the account information screen.
     *
     * @param userId    The ID of the user whose budget is being updated.
     * @param oldBudget The current budget entered by the user.
     * @param newBudget The new budget entered by the user.
     */
    private void launchAccountInfoActivity(String userId, String oldBudget, String newBudget){
        UserAccount account = accountManager.getUserAccount(userId);

        // Validate input fields
        // Error check: Ensure neither budget is blank
        if(oldBudget.isEmpty() || newBudget.isEmpty()){
            Toast.makeText(this, "Names cannot be blank", Toast.LENGTH_SHORT).show();
        }
        // Error check: Ensure new budget contains only numbers
        else if (!newBudget.matches("[0-9]+")) {
            Toast.makeText(this, "New budget must contain only numbers", Toast.LENGTH_SHORT).show();
        }
        // Fetch the current account from the AccountManager
        else if (account == null) {
            Toast.makeText(this, "User account not found", Toast.LENGTH_SHORT).show();
        }
        // All validations pass
        // All Checks Pass - Changing Budget
        else {
            accountManager.changeUserBudget(userId, oldBudget, newBudget);
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User's budget successfully changed to " + newBudget + "!", Toast.LENGTH_SHORT).show();
        }
    }
}