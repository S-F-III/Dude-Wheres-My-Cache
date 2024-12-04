package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity class for displaying and managing user account information.
 * Provides options for viewing account creation details, updating account settings, and navigating to other sections of the app.
 */
public class AccountInfoActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * Sets up the layout, initializes UI components, and configures navigation and action buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);
        // Set the layout resource for the activity
        setContentView(R.layout.activity_account_info);
        // Apply window insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        ImageView hbMenu = findViewById(R.id.hbMenu);
        ImageView hbMenu2 = findViewById(R.id.hbMenu2);
        TextView overviewButton = findViewById(R.id.overviewButton);
        TextView expenseButton = findViewById(R.id.expenseButton);
        TextView notificationButton = findViewById(R.id.notificationButton);
        TextView accountInfoButton = findViewById(R.id.accountInfoButton);
        TextView settingsButton = findViewById(R.id.settingsButton);
        TextView monthlySpendingButton = findViewById(R.id.monthlySpendingButton);
        TextView signOutButton = findViewById(R.id.signOutButton);
        // Configure hamburger menu toggle
        hbMenu.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.VISIBLE));
        hbMenu2.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.GONE));
        overviewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchOverview();
            }
        });
        expenseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchExpenses();
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchNotfications();
            }
        });
        accountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAccountInfo();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSettings();
            }
        });
        monthlySpendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMonthlySpending();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignOut();
            }
        });

        // Load shared preferences and display account information
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Retrieve and display the account creation date using the existing sharedPreferences
        String creationDate = sharedPreferences.getString("CREATION_DATE", "Not Available");
        TextView creationDateTextView = findViewById(R.id.accountCreatedText); // Corrected ID
        creationDateTextView.setText("Account Created: " + creationDate);

        // Configure account modification buttons
        Button changeNameButton = findViewById(R.id.changeNameButton);
        Button changePasswordButton = findViewById(R.id.changePasswordButton);
        Button changeBudgetButton = findViewById(R.id.changeBudgetButton);

        TextView displayUserID = findViewById(R.id.showCurrentUser);
        displayUserID.setText("Current User: " + userId);

        changeNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchNameActivity();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchPasswordActivity();
            }
        });

        changeBudgetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchBudgetActivity();
            }
        });

    }

    /**
     * Launches the NameActivity to update the user's name.
     */
    private void launchNameActivity(){
        Intent intent = new Intent(this, NameActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the PasswordActivity to update the user's password.
     */
    private void launchPasswordActivity(){
        Intent intent = new Intent(this, PasswordActivity.class);
        startActivity(intent);
    }


    /**
     * Launches the BudgetActivity to update the user's budget.
     */
    private void launchBudgetActivity(){
        Intent intent = new Intent(this, BudgetActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the OverviewActivity.
     */
    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the ExpenseActivity.
     */
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the NotificationActivity.
     */
    private void launchNotfications() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the AccountInfoActivity.
     * This method is included to conform with the navigation pattern.
     */
    private void launchAccountInfo() {
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the SettingsActivity.
     */
    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the MonthlySpendingActivity.
     */
    private void launchMonthlySpending() {
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }

    /**
     * Logs out the current user, clears user preferences, and returns to the MainActivity.
     */
    private void launchSignOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER_ID").apply();
        editor.clear(); // Clear all stored data
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
