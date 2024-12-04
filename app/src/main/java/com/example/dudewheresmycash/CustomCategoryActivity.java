package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Model.Category;
import Model.CategoryTracker;
import Model.Expense;
import Model.ExpenseBank;

/**
 * Activity class for creating and managing custom categories.
 * Allows users to update existing category titles and descriptions, with validations to ensure data integrity.
 * Updates associated expenses with the new category information.
 */
public class CustomCategoryActivity extends AppCompatActivity {

    /**
     * Tag used for logging purposes in the CustomCategoryActivity.
     * Helps identify log messages specific to this activity.
     */
    private static final String TAG = "CustomCategoryActivity";

    /**
     * Tracks and manages categories for the current user.
     * Handles loading, saving, and updating category data.
     */
    private CategoryTracker categoryTracker;

    /**
     * Manages the collection of expenses for the current user.
     * Handles expense-related operations such as retrieval, addition, and deletion.
     */
    private ExpenseBank expenseBank;

    /**
     * Called when the activity is starting.
     * Sets up the layout, applies edge-to-edge design, and initializes category tracking.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_category);

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
        EditText inputCurrentCategory = (EditText) findViewById(R.id.enterCategoryTitle);
        EditText inputCustomCategory = (EditText) findViewById(R.id.enterCustomTitle);
        EditText inputNewDescription = (EditText) findViewById(R.id.enterNewDescription);

        // Initialize CategoryTracker and load categories
        categoryTracker = new CategoryTracker();
        categoryTracker.initializeInternalStorage(this, "categories.csv", "internal-categories.csv");
        categoryTracker.loadCategoriesFromInternalFile(this, "internal-categories.csv", userId);


        // Set listener for saving changes to a category
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldTitle = inputCurrentCategory.getText().toString().trim();
                String newTitle = inputCustomCategory.getText().toString().trim();
                String newDescription = inputNewDescription.getText().toString().trim();
                launchAccountInfoActivity(userId, oldTitle, newTitle, newDescription);
            }
        });
    }

    /**
     * Initializes the expense list for managing associated expenses with categories.
     */
    private void createExpenseList(){
        expenseBank = new ExpenseBank(this);
        expenseBank.initializeExpenseList();
    }


    /**
     * Updates a category's title and description, validates inputs, and updates associated expenses.
     * Navigates back to the account information activity on successful update.
     *
     * @param userId         The ID of the user making the update.
     * @param oldTitle       The current title of the category to be updated.
     * @param newTitle       The new title for the category.
     * @param newDescription The new description for the category.
     */
    private void launchAccountInfoActivity(String userId, String oldTitle, String newTitle, String newDescription){

        createExpenseList();

        // Validate inputs
        if (newTitle.isEmpty()) {
            Toast.makeText(this, "New title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(newDescription.isEmpty()){
            Toast.makeText(this, "New description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(oldTitle.equals("Unspent")){
            Toast.makeText(this, "Cannot change \"Unspent\" category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Capitalize the new title
        String capitalizedNewTitle = newTitle.substring(0, 1).toUpperCase() + newTitle.substring(1);

        boolean titleUpdated = false;

        // Update the category if it belongs to the user
        // Iterate through categories to find and update (optional: must make this optimal as it is O(N) time complexity
        for (Category category : categoryTracker.getCategories()) {
            if (category.getUserId().equals(userId)) {
                // Check if the old title matches
                if (category.getCategoryName().equalsIgnoreCase(oldTitle)) {
                    category.setCategoryName(capitalizedNewTitle);
                    category.setCategoryDescription(newDescription);
                    titleUpdated = true;
                    break; // Exit loop after finding and updating the matching category
                }
            }
        }

        if (titleUpdated) {
            // Save the updated categories back to the file
            categoryTracker.saveCategoriesToInternalFile(this, "internal-categories.csv");

            // Update expenses associated with the old title
            boolean expenseUpdated = false;
            for (Expense expense : expenseBank.getExpenses()) {
                if (expense.getExpenseOwner().equals(userId) && (expense.getExpenseCategory().equalsIgnoreCase(oldTitle))) {
                    expense.setExpenseCategory(capitalizedNewTitle);
                    expenseUpdated = true;
                    Log.i(TAG, "Updated expense: " + expense.getExpenseID() + " to category: " + capitalizedNewTitle);
                }
            }

            // Optionally save the updated expenses back to persistent storage (if required)
            if (expenseUpdated) {
                expenseBank.saveExpensesToFile();
            }

            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Category title updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Category with the specified title not found or doesn't belong to the user.", Toast.LENGTH_SHORT).show();
        }
    }
}