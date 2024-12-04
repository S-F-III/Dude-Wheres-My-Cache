package com.example.dudewheresmycash;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Model.AccountManager;
import Model.Expense;
import Model.Notification;
import Model.NotificationBank;
import Model.ExpenseBank;
import Model.UserAccount;
import Model.Category;
import Model.CategoryTracker;

/**
 * This activity handles user settings, including account management, data export, budget clearing,
 * and navigation to other app features.
 *
 * <p>Main features include:
 * <ul>
 *     <li>Clearing user budget data</li>
 *     <li>Deleting the user account and associated data</li>
 *     <li>Navigation to various parts of the application</li>
 *     <li>Exporting user data</li>
 * </ul>
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Manages user expenses.
     */
    private ExpenseBank expenseBank;

    /**
     * Manages user notifications.
     */

    private NotificationBank notificationBank;
    /**
     * Manages user accounts.
     */
    private AccountManager accountManager;

    /**
     * Initializes the activity and sets up UI components and functionality for user settings.
     *
     * @param savedInstanceState the saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // Adjust UI for edge-to-edge compatibility
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the current user's ID
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Initializing UI elements
        ImageView hbMenu = findViewById(R.id.hbMenu);
        ImageView hbMenu2 = findViewById(R.id.hbMenu2);
        TextView overviewButton = findViewById(R.id.overviewButton);
        TextView expenseButton = findViewById(R.id.expenseButton);
        TextView notificationButton = findViewById(R.id.notificationButton);
        TextView accountInfoButton = findViewById(R.id.accountInfoButton);
        TextView settingsButton = findViewById(R.id.settingsButton);
        TextView monthlySpendingButton = findViewById(R.id.monthlySpendingButton);
        TextView signOutButton = findViewById(R.id.signOutButton);

        Button clearBudgetButton = findViewById(R.id.clearBudgetDataButton);
        Button exportDataButton = findViewById(R.id.exportDataButton);
        Button noButton = findViewById(R.id.noButton);
        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);
        Button renameCategoryButton = findViewById(R.id.addCustomCategoryButton);

        // Setting listeners for each button and menu item
        exportDataButton.setOnClickListener(v -> findViewById(R.id.clearBudgetBox).setVisibility(View.VISIBLE));
        clearBudgetButton.setOnClickListener(v -> {
            findViewById(R.id.clearBudgetBox).setVisibility(View.VISIBLE);
            Button yesButton = findViewById(R.id.yesButton);
            yesButton.setOnClickListener(v1 -> {
                clearBudgetData(userId);
                findViewById(R.id.clearBudgetBox).setVisibility(View.GONE);
            });
        });
        deleteAccountButton.setOnClickListener(v -> {
            findViewById(R.id.clearBudgetBox).setVisibility(View.VISIBLE);
            Button yesButton = findViewById(R.id.yesButton);
            yesButton.setOnClickListener(v1 -> {
                deleteAccount(userId);
                launchSignOut();

            });

        });


        noButton.setOnClickListener(v -> findViewById(R.id.clearBudgetBox).setVisibility(View.GONE));
        hbMenu.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.VISIBLE));
        hbMenu2.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.GONE));

        // Setting onClick listeners for navigation buttons
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
        renameCategoryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                launchCustomCategoryActivity();
            }
        });
    }

    /**
     * Clears all budget data associated with the current user.
     *
     // @param userId the ID of the current user
     */
    private void clearBudgetData(String userID){
        createExpenseList();
        ArrayList<Expense> expenseRemoval = new ArrayList<>();

        for(Expense x : expenseBank.getExpenses()){
            if(x.getExpenseOwner().equals(userID)){
                expenseRemoval.add(x);
            }
        }

        for (Expense x : expenseRemoval) { removeExpense(x, userID); }
        Toast.makeText(this, "Budget data cleared!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Deletes the user's account and all associated data.
     *
     // @param userId the ID of the current user
     */
    private void deleteAccount(String userID) {
        AccountManager accountManager = new AccountManager(this);
        CategoryTracker categoryTracker = new CategoryTracker();


       // categoryTracker.saveCategoriesToInternalFile(this, "categories.csv");
        categoryTracker.loadCategoriesFromInternalFile(this, "internal-categories.csv", userID);
        createExpenseList();
        accountManager.initializeAccountList();
        createNotificationList();

        // Remove categories related to the user
        ArrayList<Category> categoryRemoval = new ArrayList<>();
        for(Category x : categoryTracker.getCategories()){
            if(x.getUserId().equals(userID)){
                categoryRemoval.add(x);
                Log.i("LoadingCategory", "Category Found");
            }

        }
        for (Category x : categoryRemoval) { removeUserCategories(x, categoryTracker); }

        // Remove expenses related to the user
        ArrayList<Expense> expenseRemoval = new ArrayList<>();
        for(Expense x : expenseBank.getExpenses()){
            if(x.getExpenseOwner().equals(userID)){
                expenseRemoval.add(x);
            }
        }
        for (Expense x : expenseRemoval) { removeExpense(x, userID); }

        // Remove notifications related to the user
        ArrayList<Notification> notificationRemoval = new ArrayList<>();
        for(Notification x: notificationBank.getNotifications()){
            if(x.getOwner().equals(userID)){
                notificationRemoval.add(x);

            }
        }
        for (Notification x : notificationRemoval) { removeNotification(x, userID); }
        // Remove the user account
        ArrayList<UserAccount> accountRemoval = new ArrayList<>();
        for(UserAccount x : accountManager.getUserAccounts()){
            if(x.getUserID().equals(userID)){
                accountRemoval.add(x);

            }
        }
        for (UserAccount x : accountRemoval) { removeAccount(x, userID, accountManager); }

    }

    /**
     * Removes a category from the category tracker and updates the internal categories file.
     *
     * @param category The category to be removed.
     * @param categoryTracker The instance of the CategoryTracker that holds the user's categories.
     */
    public void removeUserCategories(Category category, CategoryTracker categoryTracker) {
        boolean removed = categoryTracker.removeCategory(category);

        if(removed) {
            categoryTracker.saveCategoriesToInternalFile(this, "internal-categories.csv");
        }
    }

    /**
     * Removes a notification from the notification bank and updates the notifications file.
     *
     * @param notification The notification to be removed.
     * @param userAcc The user account associated with the notification to be removed.
     */
    private void removeNotification(Notification notification, String userAcc) {
        // Remove the expense from the ExpenseBank
        boolean removed = notificationBank.removeNotification(notification);

        if (removed) {
           // Update the CSV file
            notificationBank.saveNotificationToFile();
        }
    }
    /**
     * Removes a user account from the account manager and updates the account file.
     *
     * @param account The user account to be removed.
     * @param userAcc The user account identifier.
     * @param accountManager The instance of AccountManager handling user accounts.
     */
    private void removeAccount(UserAccount account, String userAcc, AccountManager accountManager) {
        boolean removed = accountManager.removeUserAccount(account);

        if (removed) {
            Toast.makeText(this, "Account removed successfully!", Toast.LENGTH_SHORT).show();

            accountManager.saveAccountsToFile();
        }
    }

    /**
     * Removes an expense from the expense bank and updates the expenses file.
     *
     * @param expense The expense to be removed.
     * @param userAcc The user account identifier associated with the expense.
     */
    private void removeExpense(Expense expense, String userAcc) {
        // Remove the expense from the ExpenseBank
        boolean removed = expenseBank.removeExpense(expense);

        if (removed) {
            // Update the CSV file
            expenseBank.saveExpensesToFile();
        }
    }
    /**
     * Creates a list of expenses by initializing the ExpenseBank.
     */
    private void createExpenseList(){
        expenseBank = new ExpenseBank(this);
        expenseBank.initializeExpenseList();
    }

    /**
     * Creates a list of notifications by initializing the NotificationBank.
     */
    private void createNotificationList(){
        notificationBank = new NotificationBank(this);
        notificationBank.initializeNotificationList();
    }

    /**
     * Launches the CustomCategoryActivity to allow the user to create or modify custom categories.
     */
    private void launchCustomCategoryActivity(){
        Intent intent = new Intent(this, CustomCategoryActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the OverviewActivity to show the user an overview of their data.
     */
    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the ExpenseActivity to allow the user to view and manage their expenses.
     */
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the NotificationActivity to allow the user to view and manage their notifications.
     */
    private void launchNotfications() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the AccountInfoActivity to allow the user to view and update their account information.
     */
    private void launchAccountInfo() {
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the SettingsActivity for the user to adjust their settings.
     */
    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the MonthlySpendingActivity to show the user their monthly spending details.
     */
    private void launchMonthlySpending() {
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }

    /**
     * Signs out the user by clearing the stored user ID and other preferences, then redirects them to the MainActivity.
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