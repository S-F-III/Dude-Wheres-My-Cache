package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    private ExpenseBank expenseBank;
    private Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        dynamicExpenseSetup(userId);

        ImageView hbMenu = findViewById(R.id.hbMenu);
        ImageView hbMenu2 = findViewById(R.id.hbMenu2);
        TextView overviewButton = findViewById(R.id.overviewButton);
        TextView expenseButton = findViewById(R.id.expenseButton);
        TextView notificationButton = findViewById(R.id.notificationButton);
        TextView accountInfoButton = findViewById(R.id.accountInfoButton);
        TextView settingsButton = findViewById(R.id.settingsButton);
        TextView monthlySpendingButton = findViewById(R.id.monthlySpendingButton);
        TextView signOutButton = findViewById(R.id.signOutButton);

        //overlays for adding/modifying/removing expenses
        Button addExpenseButton = findViewById(R.id.addExpense_button);
        TextView cancelAddExpenseButton = findViewById(R.id.cancelAddExpenseButton);
        Button modifyExpenseButton = findViewById(R.id.modifyExpense_button);
        TextView cancelModifyExpenseButton = findViewById(R.id.cancelModifyExpenseButton);
        TextView modifySelectedExpenseButton = findViewById(R.id.modifySelectedExpenseButton);
        Button removeExpenseButton = findViewById(R.id.removeExpense_button);
        TextView cancelRemoveExpenseButton = findViewById(R.id.cancelRemoveExpenseButton);
        TextView cancelSelectedModifyExpenseButton = findViewById(R.id.cancelSelectedModifyExpenseButton);

        addExpenseButton.setOnClickListener(v -> findViewById(R.id.addExpenseOverlay).setVisibility(View.VISIBLE));
        cancelAddExpenseButton.setOnClickListener(v -> findViewById(R.id.addExpenseOverlay).setVisibility(View.GONE));
        modifyExpenseButton.setOnClickListener(v -> findViewById(R.id.modifyExpenseOverlay).setVisibility(View.VISIBLE));
        cancelModifyExpenseButton.setOnClickListener(v -> findViewById(R.id.modifyExpenseOverlay).setVisibility(View.GONE));
        modifySelectedExpenseButton.setOnClickListener(v -> findViewById(R.id.modifySelectedExpenseOverlay).setVisibility(View.VISIBLE));
        cancelSelectedModifyExpenseButton.setOnClickListener(v -> findViewById(R.id.modifySelectedExpenseOverlay).setVisibility(View.GONE));
        removeExpenseButton.setOnClickListener(v -> findViewById(R.id.removeExpenseOverlay).setVisibility(View.VISIBLE));
        cancelRemoveExpenseButton.setOnClickListener(v -> findViewById(R.id.removeExpenseOverlay).setVisibility(View.GONE));

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
    }



    //creates list of expenses
    private void createExpenseList(){
        expenseBank = new ExpenseBank(this);
        expenseBank.initializeExpenseList();
    }

    //method to return total amount of money spent on all user expenses (not finished)
    private double calculateTotalUserExpenses(ArrayList<Expense> expenses, String expenseOwner){
        double totalExpenses = 0.00;
        return totalExpenses;
    }

    private void dynamicExpenseSetup(String userAcc){
        //initialize list of expenses
        createExpenseList();

        if(expenseBank != null){
            LinearLayout expenseLayoutMain = findViewById(R.id.expense_layout);
            expenseLayoutMain.removeAllViews(); // Clear any previous data before adding new views

            for(Expense expense : expenseBank.getExpenses()){
                if(expense.getExpenseOwner().equals(userAcc)) {
                    Log.d("ExpenseActivity", "Adding expense: " + expense.getExpenseAmount() + " " + expense.getExpenseDescription());

                    // Create a vertical LinearLayout for each expense
                    LinearLayout expenseLayout = new LinearLayout(this);
                    expenseLayout.setOrientation(LinearLayout.VERTICAL);
                    expenseLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    // Create the TextView for expense amount and description
                    TextView expenseDescr = new TextView(this);
                    expenseDescr.setText(String.valueOf(expense.getExpenseAmount()) + " " + String.valueOf(expense.getExpenseDescription()));
                    expenseDescr.setTypeface(null, Typeface.BOLD); // Make text bold
                    expenseDescr.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    expenseDescr.setPadding(0, 0, 0, 8); // Optional padding to separate description from date

                    // Create the TextView for date expense was made
                    TextView expenseDate = new TextView(this);
                    expenseDate.setText(String.valueOf(expense.getDate()));
                    expenseDate.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    expenseDate.setPadding(0, 0, 0, 16); // Optional padding to separate bottom of entry to top of next

                    // Add both TextViews to the vertical layout
                    expenseLayout.addView(expenseDescr);
                    expenseLayout.addView(expenseDate);

                    // Add the vertical layout to the main expense layout
                    expenseLayoutMain.addView(expenseLayout);
                }
            }
        }
        else{
            Toast.makeText(this, "No expenses found", Toast.LENGTH_SHORT).show();
        }
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
        sharedPreferences.edit().remove("USER_ID").apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
