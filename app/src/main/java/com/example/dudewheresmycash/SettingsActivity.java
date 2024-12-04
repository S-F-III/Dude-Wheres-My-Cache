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

public class SettingsActivity extends AppCompatActivity {

    private ExpenseBank expenseBank;
    private NotificationBank notificationBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

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

    private void deleteAccount(String userID) {
        AccountManager accountManager = new AccountManager(this);
        CategoryTracker categoryTracker = new CategoryTracker();


       // categoryTracker.saveCategoriesToInternalFile(this, "categories.csv");
        categoryTracker.loadCategoriesFromInternalFile(this, "internal-categories.csv", userID);
        createExpenseList();
        accountManager.initializeAccountList();
        createNotificationList();

        ArrayList<Category> categoryRemoval = new ArrayList<>();
        for(Category x : categoryTracker.getCategories()){
            if(x.getUserId().equals(userID)){
                categoryRemoval.add(x);
                Log.i("LoadingCategory", "Category Found");
            }

        }
        for (Category x : categoryRemoval) { removeUserCategories(x, categoryTracker); }
        ArrayList<Expense> expenseRemoval = new ArrayList<>();
        for(Expense x : expenseBank.getExpenses()){
            if(x.getExpenseOwner().equals(userID)){
                expenseRemoval.add(x);
            }
        }
        for (Expense x : expenseRemoval) { removeExpense(x, userID); }
        ArrayList<Notification> notificationRemoval = new ArrayList<>();
        for(Notification x: notificationBank.getNotifications()){
            if(x.getOwner().equals(userID)){
                notificationRemoval.add(x);

            }
        }
        for (Notification x : notificationRemoval) { removeNotification(x, userID); }
        ArrayList<UserAccount> accountRemoval = new ArrayList<>();
        for(UserAccount x : accountManager.getUserAccounts()){
            if(x.getUserID().equals(userID)){
                accountRemoval.add(x);

            }
        }
        for (UserAccount x : accountRemoval) { removeAccount(x, userID, accountManager); }

    }

    public void removeUserCategories(Category category, CategoryTracker categoryTracker) {
        boolean removed = categoryTracker.removeCategory(category);

        if(removed) {
            categoryTracker.saveCategoriesToInternalFile(this, "internal-categories.csv");
        }
    }
    private void removeNotification(Notification notification, String userAcc) {
        // Remove the expense from the ExpenseBank
        boolean removed = notificationBank.removeNotification(notification);

        if (removed) {
           // Update the CSV file
            notificationBank.saveNotificationToFile();
        }
    }

    private void removeAccount(UserAccount account, String userAcc, AccountManager accountManager) {
        boolean removed = accountManager.removeUserAccount(account);

        if (removed) {
            Toast.makeText(this, "Account removed successfully!", Toast.LENGTH_SHORT).show();

            accountManager.saveAccountsToFile();
        }
    }

    private void removeExpense(Expense expense, String userAcc) {
        // Remove the expense from the ExpenseBank
        boolean removed = expenseBank.removeExpense(expense);

        if (removed) {
            // Update the CSV file
            expenseBank.saveExpensesToFile();
        }
    }

    private void createExpenseList(){
        expenseBank = new ExpenseBank(this);
        expenseBank.initializeExpenseList();
    }
    private void createNotificationList(){
        notificationBank = new NotificationBank(this);
        notificationBank.initializeNotificationList();
    }

    private void launchCustomCategoryActivity(){
        Intent intent = new Intent(this, CustomCategoryActivity.class);
        startActivity(intent);
    }

    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }
    private void launchNotfications() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
    private void launchAccountInfo() {
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }
    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    private void launchMonthlySpending() {
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }
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