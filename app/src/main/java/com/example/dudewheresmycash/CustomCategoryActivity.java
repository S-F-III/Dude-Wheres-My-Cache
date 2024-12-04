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

public class CustomCategoryActivity extends AppCompatActivity {

    private static final String TAG = "CustomCategoryActivity";
    private CategoryTracker categoryTracker;
    private ExpenseBank expenseBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        Button saveChangesButton = findViewById(R.id.saveNameChangesButton);
        EditText inputCurrentCategory = (EditText) findViewById(R.id.enterCategoryTitle);
        EditText inputCustomCategory = (EditText) findViewById(R.id.enterCustomTitle);

        categoryTracker = new CategoryTracker();
        categoryTracker.initializeInternalStorage(this, "categories.csv", "internal-categories.csv");
        categoryTracker.loadCategoriesFromInternalFile(this, "internal-categories.csv", userId);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldTitle = inputCurrentCategory.getText().toString().trim();
                String newTitle = inputCustomCategory.getText().toString().trim();
                launchAccountInfoActivity(userId, oldTitle, newTitle);
            }
        });
    }

    private void createExpenseList(){
        expenseBank = new ExpenseBank(this);
        expenseBank.initializeExpenseList();
    }

    private void launchAccountInfoActivity(String userId, String oldTitle, String newTitle){

        createExpenseList();

        if (newTitle.isEmpty()) {
            Toast.makeText(this, "New title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(oldTitle.equals("Unspent")){
            Toast.makeText(this, "Cannot change \"Unspent\" category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Capitalize the new title
        String capitalizedNewTitle = newTitle.substring(0, 1).toUpperCase() + newTitle.substring(1);

        boolean titleUpdated = false;

        // Iterate through categories to find and update (optional: must make this optimal as it is O N time complexity
        for (Category category : categoryTracker.getCategories()) {
            if (category.getUserId().equals(userId)) {
                // Check if the old title matches
                if (category.getCategoryName().equalsIgnoreCase(oldTitle)) {
                    category.setCategoryName(capitalizedNewTitle);
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
                expenseBank.saveExpensesToFile(); // Ensure you have this method implemented
            }

            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Category title updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Category with the specified title not found or doesn't belong to the user.", Toast.LENGTH_SHORT).show();
        }
    }
}