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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Activity class for handling user login.
 * Allows users to enter their credentials to access their account or navigate back to the welcome screen.
 */
public class LoginActivity extends AppCompatActivity {

    private AccountManager accountManager;

    /**
     * Called when the activity is starting.
     * Sets up the layout, initializes the account manager, and configures login and cancel button actions.
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
        setContentView(R.layout.activity_login);

        // Apply window insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize AccountManager
        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        // Initialize UI components
        EditText inputUserID = (EditText) findViewById(R.id.inputUserID);
        EditText inputUserPassword = (EditText) findViewById(R.id.inputPassword);
        Button logInButton = findViewById(R.id.log_in_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        // Set up cancel button to return to welcome screen
        cancelButton.setOnClickListener(v -> launchWelcome());

        // Set up login button to validate credentials and launch the next activity
        logInButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Handles the click event for the login button.
             * Retrieves the user ID and password from the input fields and initiates the login process.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view){
                String userID = inputUserID.getText().toString();
                String userPassword = inputUserPassword.getText().toString();
                launchActivity("Next", userID, userPassword);
            }
        });
    }

    /**
     * Validates user credentials and launches the appropriate activity if login is successful.
     * If login fails, displays an appropriate error message.
     *
     * @param button The name of the button triggering this action (for extensibility).
     * @param id     The user ID entered by the user.
     * @param pass   The password entered by the user.
     */
    private void launchActivity(String button, String id, String pass){
        UserAccount currAccount = accountManager.getUserAccount(id);

        if(currAccount != null) {
            if (!(id.equals(currAccount.getUserID()))) {
                Toast.makeText(this, "Error: No existing username found", Toast.LENGTH_SHORT).show();
            } else if (id.equals(currAccount.getUserID()) && !(pass.equals(currAccount.getUserPassword()))) {
                Toast.makeText(this, "Error: Incorrect Password", Toast.LENGTH_SHORT).show();
            } else {
                // Save user ID to SharedPreferences before launching the next activity
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_ID", id); // Store the user ID here
                editor.apply();

                //launch the OverviewActivity
                Intent intent = new Intent(this, OverviewActivity.class);
                intent.putExtra("USER_ID", id);
                intent.putExtra("USER_PASSWORD", pass);
                intent.putExtra("USER_NAME", currAccount.getUserName());
                intent.putExtra("USER_BUDGET", currAccount.getUserBudget());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(this, "current account not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launches the WelcomeActivity to return to the welcome screen.
     */
    private void launchWelcome() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

}