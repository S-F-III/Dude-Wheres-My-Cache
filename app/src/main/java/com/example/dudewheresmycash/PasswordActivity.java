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
 * This activity allows the user to change their password by verifying the old password and saving the new one.
 *
 * <p>Features:
 * <ul>
 *   <li>Integrates with {@link AccountManager} to manage user accounts.</li>
 *   <li>Implements secure checks to validate the current password before applying changes.</li>
 *   <li>Utilizes shared preferences to retrieve the user ID.</li>
 *   <li>Handles edge-to-edge UI compatibility using {@link EdgeToEdge} utilities.</li>
 * </ul>
 */

public class PasswordActivity extends AppCompatActivity {

    /**
     * Manages user accounts and handles password changes.
     */
    private AccountManager accountManager;

    /**
     * Initializes the activity, configures UI elements, and sets up the password change functionality.
     *
     * @param savedInstanceState the saved state of the activity, used for restoring previous configurations
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge UI and set the layout
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);

        // Adjust padding for edge-to-edge compatibility
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve user ID from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Initialize UI elements
        Button saveChangesButton = findViewById(R.id.saveNameChangesButton);
        EditText inputOldPass = (EditText) findViewById(R.id.enterCurrentPassword);
        EditText inputNewPass = (EditText) findViewById(R.id.enterNewPassword);

        // Initialize AccountManager and load user account data
        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        // Set up click listener for the Save Changes button
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = inputOldPass.getText().toString().trim();
                String newPass = inputNewPass.getText().toString().trim();
                launchAccountInfoActivity(userId, oldPass, newPass);
            }
        });
    }

    /**
     * Launches the Account Info Activity after validating and updating the user's password.
     *
     * <p>Validates:
     * <ul>
     *   <li>Passwords are not blank.</li>
     *   <li>The old password matches the stored password.</li>
     * </ul>
     * If validation succeeds, the password is updated, and the user is redirected to the Account Info Activity.
     *
     * @param userId  the ID of the current user
     * @param oldPass the current password entered by the user
     * @param newPass the new password entered by the user
     */
    private void launchAccountInfoActivity(String userId, String oldPass, String newPass){
        // Retrieve user account for validation
        UserAccount account = accountManager.getUserAccount(userId);

        // Error check: Ensure neither password is blank
        if(oldPass.isEmpty() || newPass.isEmpty()){
            Toast.makeText(this, "Passwords cannot be blank", Toast.LENGTH_SHORT).show();
        }
        // Error check: Ensure old password is correct before changing to new password
        else if (!(account.getUserPassword().equals(oldPass))) {
            Toast.makeText(this, "Incorrect current password. Please Re-enter", Toast.LENGTH_SHORT).show();
        }
        // All Checks Pass - Changing Password
        else {
            accountManager.changeUserPassword(userId, oldPass, newPass);
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
        }
    }
}