package com.example.dudewheresmycash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity class for displaying a hamburger menu.
 * Provides navigation to various sections of the application such as Overview, Expenses, Notifications, Account Info, Settings, and Monthly Spending.
 * Includes functionality for signing out.
 */
public class HamburgerActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * Sets up the layout, applies edge-to-edge design, and initializes navigation buttons with listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout design
        EdgeToEdge.enable(this);

        // Set the layout resource for the activity
        setContentView(R.layout.activity_hamburger);

        // Apply window insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize navigation buttons
        Button overview = findViewById(R.id.overviewButton);
        Button expense = findViewById(R.id.expenseButton);
        Button notification = findViewById(R.id.notificationButton);
        Button accountInfo = findViewById(R.id.accountInfoButton);
        Button settings = findViewById(R.id.settingsButton);
        Button monthlySpending = findViewById(R.id.monthlySpendingButton);
        Button signOut = findViewById(R.id.signOutButton);

        // Set up click listeners for navigation buttons
        overview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchOverview();
            }
        });
        expense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchExpenses();
            }
        });
        notification.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchNotfications();
            }
        });
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAccountInfo();
            }
        });
        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSettings();
            }
        });
        monthlySpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMonthlySpending();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignOut();
            }
        });
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
     * Signs out the user and clears the shared preferences.
     * Redirects to the MainActivity.
     */
    private void launchSignOut() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}