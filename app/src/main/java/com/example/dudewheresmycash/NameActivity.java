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
 * Activity class for updating the user's name.
 * Provides functionality to change the user's name and validates input before saving changes.
 */
public class NameActivity extends AppCompatActivity {

    private AccountManager accountManager;

    /**
     * Called when the activity is starting.
     * Sets up the layout, initializes UI components, and configures the save button to handle name changes.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);
        // Set the layout resource for the activity
        setContentView(R.layout.activity_name);
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
        EditText inputOldName = (EditText) findViewById(R.id.enterCurrentName);
        EditText inputNewName = (EditText) findViewById(R.id.enterNewName);

        // Initialize the AccountManager and load account data
        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        // Set up the save changes button to handle name updates
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event for the save changes button.
             * Validates input and attempts to update the user's name.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                String oldName = inputOldName.getText().toString().trim();
                String newName = inputNewName.getText().toString().trim();
                launchAccountInfoActivity(userId, oldName, newName);
            }
        });
    }

    /**
     * Validates the inputs and updates the user's name in the account manager.
     * Navigates back to the AccountInfoActivity on successful update.
     *
     * @param userId  The ID of the current user.
     * @param oldName The current name of the user.
     * @param newName The new name to update for the user.
     */
    private void launchAccountInfoActivity(String userId, String oldName, String newName){
        UserAccount account = accountManager.getUserAccount(userId);

        // Error check: Ensure neither name is blank
        if(oldName.isEmpty() || newName.isEmpty()){
            Toast.makeText(this, "Names cannot be blank", Toast.LENGTH_SHORT).show();
        }
        // Error check: Ensure new name contains only letters
        else if (!newName.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "New name must contain only letters", Toast.LENGTH_SHORT).show();
        }
        // Fetch the current account from the AccountManager
        else if (account == null) {
            Toast.makeText(this, "User account not found", Toast.LENGTH_SHORT).show();
        }
        // All Checks Pass - Changing Name
        else {
            newName = newName.substring(0,1).toUpperCase() + newName.substring(1).toLowerCase();
            accountManager.changeUserName(userId, oldName, newName);
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User's name successfully changed to " + newName + "!", Toast.LENGTH_SHORT).show();
        }
    }
}