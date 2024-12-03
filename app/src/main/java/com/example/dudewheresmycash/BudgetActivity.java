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

public class BudgetActivity extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        Button saveChangesButton = findViewById(R.id.saveNameChangesButton);
        EditText inputOldBudget = (EditText) findViewById(R.id.enterCurrentBudget);
        EditText inputNewBudget = (EditText) findViewById(R.id.enterNewBudget);

        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldBudget = inputOldBudget.getText().toString().trim();
                String newBudget = inputNewBudget.getText().toString().trim();
                launchAccountInfoActivity(userId, oldBudget, newBudget);
            }
        });
    }

    private void launchAccountInfoActivity(String userId, String oldBudget, String newBudget){
        UserAccount account = accountManager.getUserAccount(userId);

        // Error check: Ensure neither budget is blank
        if(oldBudget.isEmpty() || newBudget.isEmpty()){
            Toast.makeText(this, "Names cannot be blank", Toast.LENGTH_SHORT).show();
        }
        // Error check: Ensure new budget contains only numbers
        else if (!newBudget.matches("[0-9]+")) {
            Toast.makeText(this, "New budget must contain only numbers", Toast.LENGTH_SHORT).show();
        }
        // Fetch the current account from the AccountManager
        else if (account == null) {
            Toast.makeText(this, "User account not found", Toast.LENGTH_SHORT).show();
        }
        // All Checks Pass - Changing Budget
        else {
            accountManager.changeUserBudget(userId, oldBudget, newBudget);
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User's budget successfully changed to " + newBudget + "!", Toast.LENGTH_SHORT).show();
        }
    }
}