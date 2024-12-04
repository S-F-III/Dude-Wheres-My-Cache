package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import Model.Expense;
import Model.ExpenseBank;
import Model.Notification;
import Model.NotificationBank;

/**
 * The NotificationActivity class handles the management and display of notifications.
 * It allows users to add, remove, and view notifications specific to their account.
 * The notifications are displayed dynamically, and the user can interact with them through the app's interface.
 */
public class NotificationActivity extends AppCompatActivity {

    private NotificationBank notificationBank;

    /**
     * Called when the activity is created.
     * Initializes the UI components, handles system bar adjustments, sets up notification management,
     * and configures button click listeners for adding, removing, and navigating through notifications.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        dynamicNotificationSetup(userId);

        ImageView hbMenu = findViewById(R.id.hbMenu);
        ImageView hbMenu2 = findViewById(R.id.hbMenu2);
        Button addNotificationButton = findViewById(R.id.addNotificationButton);
        TextView cancelAddButton = findViewById(R.id.cancelAddButton);
        Button removeNotificationButton = findViewById(R.id.removeNotificationButton);
        TextView cancelRemoveButton = findViewById(R.id.cancelRemoveButton);

        TextView overviewButton = findViewById(R.id.overviewButton);
        TextView expenseButton = findViewById(R.id.expenseButton);
        TextView notificationButton = findViewById(R.id.notificationButton);
        TextView accountInfoButton = findViewById(R.id.accountInfoButton);
        TextView settingsButton = findViewById(R.id.settingsButton);
        TextView monthlySpendingButton = findViewById(R.id.monthlySpendingButton);
        TextView signOutButton = findViewById(R.id.signOutButton);

        //For the overlays
        hbMenu.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.VISIBLE));
        hbMenu2.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.GONE));

        TextView submitAddButton = findViewById(R.id.submitAddButton);
        addNotificationButton.setOnClickListener(v -> {
            findViewById(R.id.notificationAddBox).setVisibility(View.VISIBLE);

            submitAddButton.setOnClickListener(doneView -> {
                EditText titleInput = findViewById(R.id.inputNotificationTitle);
                EditText dateInput = findViewById(R.id.inputNotificationDate);

                String title = titleInput.getText().toString().trim();
                String dateText = dateInput.getText().toString().trim();
                if (dateText.isEmpty() || title.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(dateText, formatter);

                    Notification newNotification = new Notification(title, date, userId);

                    notificationBank.addUserNotification(newNotification);
                    notificationBank.saveNotificationToFile();

                    titleInput.setText("");
                    dateInput.setText("");
                    dynamicNotificationSetup(userId);
                    findViewById(R.id.notificationAddBox).setVisibility(View.GONE);


                    Toast.makeText(this, "Notification added successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid input format.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        cancelAddButton.setOnClickListener(v ->
        {
            findViewById(R.id.notificationAddBox).setVisibility(View.GONE);

            EditText titleInput = findViewById(R.id.inputNotificationTitle);
            EditText dateInput = findViewById(R.id.inputNotificationDate);

            titleInput.setText("");
            dateInput.setText("");

            findViewById(R.id.notificationAddBox).setVisibility(View.GONE);
        });

        removeNotificationButton.setOnClickListener(v -> {
                    findViewById(R.id.notificationRemoveBox).setVisibility(View.VISIBLE);
            populateNotificationList(userId);
            Toast.makeText(this, "Select a notification to remove", Toast.LENGTH_SHORT).show();
        });
        cancelRemoveButton.setOnClickListener(v -> {
                findViewById(R.id.notificationRemoveBox).setVisibility(View.GONE);
            dynamicNotificationSetup(userId);
        });

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
    }

    /**
     * Dynamically displays user's notifications
     *
     * @param userAcc The current user's ID to manage personalized notifications.
     */
    private void dynamicNotificationSetup(String userAcc){

        createNotificationList();

        if(notificationBank != null){
            LinearLayout notificationLayoutMain = findViewById(R.id.notification_layout);
            notificationLayoutMain.removeAllViews(); // Clear any previous data before adding new views

            for(Notification x : notificationBank.getNotifications()){
                Log.e("NotificationActivity", "Owner: " + x.getOwner());
                if(x.getOwner().equals(userAcc)) {
                    Log.d("NotificationActivity", "Adding notification: " + x.getTitle() );


                    LinearLayout notificationLayout = new LinearLayout(this);
                    notificationLayout.setOrientation(LinearLayout.VERTICAL);
                    notificationLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    notificationLayout.setGravity(Gravity.CENTER);


                    TextView notificationDescr = new TextView(this);
                    String notificationInfo = x.getTitle();
                    notificationDescr.setText(notificationInfo);
                    notificationDescr.setTextSize(48);
                    notificationDescr.setGravity(Gravity.CENTER);
                    notificationDescr.setTextColor(Color.BLACK);
                    notificationDescr.setTypeface(null, Typeface.BOLD); // Make text bold
                    notificationDescr.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    notificationDescr.setPadding(0, 0, 0, 8); // Optional padding to separate description from date


                    TextView notificationDate = new TextView(this);
                    notificationDate.setText(String.valueOf(x.getDate()));
                    notificationDate.setTextSize(24);
                    notificationDate.setTextColor(Color.BLACK);
                    notificationDate.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                     notificationDate.setPadding(0, 0, 0, 16); // Optional padding to separate bottom of entry to top of next

                    YearMonth currentMonthYear = YearMonth.now();

                    if (YearMonth.from(x.getDate()).equals(currentMonthYear)) {
                        notificationLayout.addView(notificationDescr);
                        notificationLayout.addView(notificationDate);
                    }


                    notificationLayoutMain.addView(notificationLayout);
                }
            }
        }
        else{
            Toast.makeText(this, "No notifications found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes the list of notifications from the internal-notifications csv file
     */
    private void createNotificationList(){
        notificationBank = new NotificationBank(this);
        notificationBank.initializeNotificationList();
    }

    /**
     * Populates the notification list for removal, showing notifications associated with the current user.
     *
     * @param userAcc The current user's ID to filter notifications.
     */
    private void populateNotificationList(String userAcc) {
        LinearLayout notificationListContainer = findViewById(R.id.removeNotification_layout);
        TextView removeSelectedNotificationButton = findViewById(R.id.submitRemoveButton);
        notificationListContainer.removeAllViews(); // Clear existing views

        if (notificationBank != null) {
            Log.d("populateNotificationList", "Total Notifications: " + notificationBank.getNotifications().size());
            for(Notification x : notificationBank.getNotifications()) {
                Log.d("populateNotificationList", "Owner: " + x.getOwner());
                if (x.getOwner().equals(userAcc)) {
                    Log.d("populateNotificationList", "Matching Notifications ");

                    Button notificationButton = new Button(this);
                    notificationButton.setText(x.getTitle());
                    notificationButton.setTag(x);
                    notificationButton.setOnClickListener(v -> {
                        // Highlight the selected button
                        clearSelections(notificationListContainer);
                        notificationButton.setBackgroundColor(Color.LTGRAY);
                        removeSelectedNotificationButton.setVisibility(View.VISIBLE);

                        removeSelectedNotificationButton.setOnClickListener(removeView -> {
                            removeNotification((Notification) notificationButton.getTag(), userAcc);
                        });
                    });

                    notificationListContainer.addView(notificationButton);
                }
            }
        }
        else {
            Log.e("populateExpenseList", "ExpenseBank is null!");
            Toast.makeText(this, "No expenses found for this user.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Removes a notification from the list and updates the data file.
     *
     * @param notification The notification to be removed.
     * @param userAcc The current user's ID to ensure correct removal.
     */
    private void removeNotification(Notification notification, String userAcc) {
        // Remove the expense from the ExpenseBank
        boolean removed = notificationBank.removeNotification(notification);

        if (removed) {
            Toast.makeText(this, "Notification removed successfully!", Toast.LENGTH_SHORT).show();

            // Update the CSV file
            notificationBank.saveNotificationToFile();

            populateNotificationList(userAcc);
        } else {
            Toast.makeText(this, "Failed to remove the expense.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears any background selections in the notification list container.
     *
     * @param container The container holding the notification list buttons.
     */
    private void clearSelections(LinearLayout container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * Navigates to the OverviewActivity.
     */
    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the ExpenseActivity.
     */
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the NotificationActivity.
     */
    private void launchNotfications() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the AccountInfoActivity.
     */
    private void launchAccountInfo() {
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the SettingsActivity.
     */
    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the MonthlySpendingActivity.
     * (Redundant here since it's already in MonthlySpendingActivity.)
     */
    private void launchMonthlySpending() {
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }

    /**
     * Signs the user out by clearing shared preferences and returning to the MainActivity.
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