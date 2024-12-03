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

public class NameActivity extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        Button saveChangesButton = findViewById(R.id.saveNameChangesButton);
        EditText inputOldName = (EditText) findViewById(R.id.enterCurrentName);
        EditText inputNewName = (EditText) findViewById(R.id.enterNewName);

        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldName = inputOldName.getText().toString().trim();
                String newName = inputNewName.getText().toString().trim();
                launchAccountInfoActivity(userId, oldName, newName);
            }
        });
    }

    private void launchAccountInfoActivity(String userId, String oldName, String newName){
        UserAccount account = accountManager.getUserAccount(userId);

        // Error check: Ensure neither name is blank
        if(oldName.isEmpty() || newName.isEmpty()){
            Toast.makeText(this, "Names cannot be blank", Toast.LENGTH_SHORT).show();
        }
        // Error check: Ensure new name contains only letters
        else if (!newName.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "New name must contain only letters", Toast.LENGTH_SHORT).show();
        }
        // Fetch the current account from the AccountManager
        else if (account == null) {
            Toast.makeText(this, "User account not found", Toast.LENGTH_SHORT).show();
        }
        // All Checks Pass - Changing Name
        else {
            newName = newName.substring(0,1).toUpperCase() + newName.substring(1).toLowerCase();
            accountManager.changeUserName(userId, oldName, newName);
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User's name successfully changed to " + newName + "!", Toast.LENGTH_SHORT).show();
        }
    }
}