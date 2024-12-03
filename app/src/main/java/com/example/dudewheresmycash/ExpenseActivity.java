package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import java.time.format.DateTimeFormatter;

import Model.Expense;
import Model.ExpenseBank;

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



        hbMenu.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.VISIBLE));
        hbMenu2.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.GONE));

        addExpenseButton.setOnClickListener(v -> {
            // Show the overlay
            findViewById(R.id.addExpenseOverlay).setVisibility(View.VISIBLE);
            populateAddExpenseOverlay(userId);
        });

        cancelAddExpenseButton.setOnClickListener(v -> {
            // Clear input fields
            EditText amountInput = findViewById(R.id.inputExpenseAmount);
            EditText categoryInput = findViewById(R.id.inputExpenseCategory);
            EditText descriptionInput = findViewById(R.id.inputExpenseDescription);
            EditText dateInput = findViewById(R.id.inputExpenseDate);
            CheckBox recurringCheckBox = findViewById(R.id.isRecurringAdd);

            amountInput.setText("");
            categoryInput.setText("");
            descriptionInput.setText("");
            dateInput.setText("");
            recurringCheckBox.setChecked(false);

            // Hide the overlay
            findViewById(R.id.addExpenseOverlay).setVisibility(View.GONE);
        });

        removeExpenseButton.setOnClickListener(v -> {
            findViewById(R.id.removeExpenseOverlay).setVisibility(View.VISIBLE);
            populateRemoveExpenseList(userId);
        });

        cancelRemoveExpenseButton.setOnClickListener(v -> {
            findViewById(R.id.removeExpenseOverlay).setVisibility(View.GONE);
            dynamicExpenseSetup(userId);
        });

        modifyExpenseButton.setOnClickListener(v ->{
            findViewById(R.id.modifyExpenseOverlay).setVisibility(View.VISIBLE);
            populateModifyExpenseList(userId);
        });

        cancelModifyExpenseButton.setOnClickListener(v -> {
            findViewById(R.id.modifyExpenseOverlay).setVisibility(View.GONE);
            dynamicExpenseSetup(userId);
        });

        cancelSelectedModifyExpenseButton.setOnClickListener(v -> {
            // Clear input fields
            EditText amountInput = findViewById(R.id.inputNewExpenseAmount);
            EditText categoryInput = findViewById(R.id.inputNewExpenseCategory);
            EditText descriptionInput = findViewById(R.id.inputNewExpenseDescription);
            EditText dateInput = findViewById(R.id.inputNewExpenseDate);
            CheckBox recurringCheckBox = findViewById(R.id.isRecurringModify);

            amountInput.setText("");
            categoryInput.setText("");
            descriptionInput.setText("");
            dateInput.setText("");
            recurringCheckBox.setChecked(false);

            findViewById(R.id.modifySelectedExpenseOverlay).setVisibility(View.GONE);
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
                launchNotifications();
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

    //gets the next highest available ID to assign to a new expense
    private int getNextAvailableID(String userAcc) {
        int maxID = 0; // Start with 0; you can start with 1 if IDs should be 1-based.
        if (expenseBank != null) {
            for (Expense expense : expenseBank.getExpenses()) {
                if (expense.getExpenseOwner().equals(userAcc)) {
                    maxID = Math.max(maxID, expense.getExpenseID());
                }
            }
        }
        return maxID + 1; // Return the next ID after the highest existing one.
    }

    //Populates the list of expenses in the main Expense page
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
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // Create the TextView for expense amount and description
                    TextView expenseDescr = new TextView(this);
                    String expenseInfo = String.format("$ %.2f - %s", expense.getExpenseAmount(),    String.valueOf(expense.getExpenseDescription()));

                    expenseDescr.setText(expenseInfo);
                    expenseDescr.setTypeface(null, Typeface.BOLD);
                    expenseDescr.setTextSize(24);
                    expenseDescr.setTextColor(Color.BLACK);
                    expenseDescr.setPadding(16, 16, 16, 8);
                    expenseDescr.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // Create the TextView for the date
                    TextView expenseDate = new TextView(this);
                    expenseDate.setText(String.valueOf(expense.getDate()));
                    expenseDate.setTypeface(null, Typeface.ITALIC);
                    expenseDate.setTextSize(20);
                    expenseDate.setTextColor(getResources().getColor(R. color. green));
                    expenseDate.setPadding(16, 0, 16, 16);
                    expenseDate.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

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

    //Creates the overlay where you add expenses
    private void populateAddExpenseOverlay(String userId){

        Button doneButton = findViewById(R.id.doneAddExpenseButton); // Button in overlay
        doneButton.setOnClickListener(doneView -> {
            // Retrieve input values
            EditText amountInput = findViewById(R.id.inputExpenseAmount);
            EditText categoryInput = findViewById(R.id.inputExpenseCategory);
            EditText descriptionInput = findViewById(R.id.inputExpenseDescription);
            EditText dateInput = findViewById(R.id.inputExpenseDate);
            CheckBox recurringCheckBox = findViewById(R.id.isRecurringAdd);

            // Validate inputs
            String amountText = amountInput.getText().toString().trim();
            String category = categoryInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String dateText = dateInput.getText().toString().trim();
            boolean isRecurring = recurringCheckBox.isChecked();

            if (amountText.isEmpty() || category.isEmpty() || description.isEmpty() || dateText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Parse inputs
                double amount = Double.parseDouble(amountText);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateText, formatter);

                // Create the Expense object
                Expense newExpense = new Expense(
                        getNextAvailableID(userId), // Assume ID is auto-incremented or managed elsewhere
                        userId, // Current user's ID from SharedPreferences
                        amount,
                        category,
                        description,
                        date,
                        isRecurring
                );

                // Save to CSV
                expenseBank.addUserExpense(newExpense);
                expenseBank.saveExpensesToFile();

                // Clear input fields and hide overlay
                amountInput.setText("");
                categoryInput.setText("");
                descriptionInput.setText("");
                dateInput.setText("");
                recurringCheckBox.setChecked(false);
                findViewById(R.id.addExpenseOverlay).setVisibility(View.GONE);
                dynamicExpenseSetup(userId);

                Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input format.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Creates the overlay with the list of expenses for the user to remove
    private void populateRemoveExpenseList(String userAcc) {
        LinearLayout expenseListContainer = findViewById(R.id.removeExpense_layout);
        Button removeSelectedExpenseButton = findViewById(R.id.removeSelectedExpenseButton);
        expenseListContainer.removeAllViews(); // Clear existing views

        // Fetch the user's expenses
        if (expenseBank != null) {
            Log.d("populateExpenseList", "Total Expenses: " + expenseBank.getExpenses().size());
            for(Expense expense : expenseBank.getExpenses()) {
                Log.d("populateExpenseList", "Owner: " + expense.getExpenseOwner());
                if (expense.getExpenseOwner().equals(userAcc)) {
                    Log.d("populateExpenseList", "Matching Expense: " + expense.getExpenseDescription());

                    Button expenseButton = new Button(this);
                    expenseButton.setText(expense.getExpenseDescription() + " - $" + expense.getExpenseAmount());
                    expenseButton.setTag(expense); // Store the expense object in the tag
                    expenseButton.setOnClickListener(v -> {
                        // Highlight the selected button
                        clearSelections(expenseListContainer);
                        expenseButton.setBackgroundColor(Color.LTGRAY);
                        removeSelectedExpenseButton.setVisibility(View.VISIBLE);

                        // Set the expense to be removed
                        removeSelectedExpenseButton.setOnClickListener(removeView -> {
                            removeExpense((Expense) expenseButton.getTag(), userAcc);
                        });
                    });

                    expenseListContainer.addView(expenseButton);
                }
            }
        }
        else {
            Log.e("populateExpenseList", "ExpenseBank is null!");
            Toast.makeText(this, "No expenses found for this user.", Toast.LENGTH_SHORT).show();
        }
    }

    //Used to unhighlight the unselected expenses in populateRemoveExpenseList
    private void clearSelections(LinearLayout container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    //Used to remove the expense in populateRemoveExpenseList
    private void removeExpense(Expense expense, String userAcc) {
        // Remove the expense from the ExpenseBank
        boolean removed = expenseBank.removeExpense(expense);

        if (removed) {
            Toast.makeText(this, "Expense removed successfully!", Toast.LENGTH_SHORT).show();

            // Update the CSV file
            expenseBank.saveExpensesToFile();

            // Refresh the expense list
            populateRemoveExpenseList(userAcc);
        } else {
            Toast.makeText(this, "Failed to remove the expense.", Toast.LENGTH_SHORT).show();
        }
    }

    //Creates overlay with the list of current expenses to modify
    private void populateModifyExpenseList (String userAcc){
        LinearLayout expenseListContainer = findViewById(R.id.modifyExpense_layout);
        Button modifySelectedExpenseButton = findViewById(R.id.modifySelectedExpenseButton);
        expenseListContainer.removeAllViews(); // Clear existing views

        final String[] selectedExpenseID = {null}; // Need expenseID to modify exact expense in AVD memory

        // Fetch the user's expenses
        if (expenseBank != null) {
            Log.d("populateExpenseList", "Total Expenses: " + expenseBank.getExpenses().size());
            for(Expense expense : expenseBank.getExpenses()) {
                Log.d("populateExpenseList", "Owner: " + expense.getExpenseOwner());
                if (expense.getExpenseOwner().equals(userAcc)) {
                    Log.d("populateExpenseList", "Matching Expense: " + expense.getExpenseDescription());

                    Button expenseButton = new Button(this);
                    expenseButton.setText(expense.getExpenseDescription() + " - $" + expense.getExpenseAmount());
                    expenseButton.setTag(expense); // Store the expense object in the tag
                    expenseButton.setOnClickListener(v -> {
                        clearSelections(expenseListContainer);
                        expenseButton.setBackgroundColor(Color.LTGRAY);
                        selectedExpenseID[0] = String.valueOf(expense.getExpenseID()); //Grab the selected expenseID
                        modifySelectedExpenseButton.setVisibility(View.VISIBLE);
                    });

                    expenseListContainer.addView(expenseButton);
                }
            }
            modifySelectedExpenseButton.setOnClickListener(v -> {
                View overlay = findViewById(R.id.modifySelectedExpenseOverlay);
                overlay.setVisibility(View.VISIBLE);
                populateModifyExpenseOverlay(userAcc, selectedExpenseID[0]);
                Log.d("ModifyExpense", "Overlay visibility set to VISIBLE");
            });
        }
        else {
            Log.e("populateExpenseList", "ExpenseBank is null!");
            Toast.makeText(this, "No expenses found for this user.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateModifyExpenseOverlay (String userAcc, String expenseID){
        // Find the expense to modify using the expenseID
        Expense expenseToModify = expenseBank.getExpenseByID(expenseID, userAcc); // Implement getExpenseByID in ExpenseBank

        if (expenseToModify == null) {
            Toast.makeText(this, "Expense not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pre-fill fields with existing data
        EditText amountInput = findViewById(R.id.inputNewExpenseAmount);
        EditText categoryInput = findViewById(R.id.inputNewExpenseCategory);
        EditText descriptionInput = findViewById(R.id.inputNewExpenseDescription);
        EditText dateInput = findViewById(R.id.inputNewExpenseDate);
        CheckBox recurringCheckBox = findViewById(R.id.isRecurringModify);

        amountInput.setText(String.valueOf(expenseToModify.getExpenseAmount()));
        categoryInput.setText(expenseToModify.getExpenseCategory());
        descriptionInput.setText(expenseToModify.getExpenseDescription());
        dateInput.setText(expenseToModify.getDate().toString());
        recurringCheckBox.setChecked(expenseToModify.isRecurring());

        // Save changes when done
        Button doneButton = findViewById(R.id.doneModifyExpenseButton);
        doneButton.setOnClickListener(doneView -> {

            // Retrieve updated input values
            String amountText = amountInput.getText().toString().trim();
            String category = categoryInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String dateText = dateInput.getText().toString().trim();
            boolean isRecurring = recurringCheckBox.isChecked();

            if (amountText.isEmpty() || category.isEmpty() || description.isEmpty() || dateText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Parse inputs
                double amount = Double.parseDouble(amountText);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateText, formatter);

                // Modify the expense with the given ID
                expenseToModify.setExpenseAmount(amount);
                expenseToModify.setExpenseCategory(category);
                expenseToModify.setExpenseDescription(description);
                expenseToModify.setDate(date);
                expenseToModify.setRecurring(isRecurring);

                // Save to CSV
                expenseBank.saveExpensesToFile();

                // Clear input fields and hide overlay
                amountInput.setText("");
                categoryInput.setText("");
                descriptionInput.setText("");
                dateInput.setText("");
                recurringCheckBox.setChecked(false);
                findViewById(R.id.modifySelectedExpenseOverlay).setVisibility(View.GONE);

                // Reload the expenses
                dynamicExpenseSetup(userAcc);
                populateModifyExpenseList(userAcc);

                Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input format.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }
    private void launchNotifications() {
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
