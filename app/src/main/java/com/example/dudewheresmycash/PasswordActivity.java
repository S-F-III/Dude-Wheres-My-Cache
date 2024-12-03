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

public class PasswordActivity extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        Button saveChangesButton = findViewById(R.id.saveNameChangesButton);
        EditText inputOldPass = (EditText) findViewById(R.id.enterCurrentPassword);
        EditText inputNewPass = (EditText) findViewById(R.id.enterNewPassword);

        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = inputOldPass.getText().toString().trim();
                String newPass = inputNewPass.getText().toString().trim();
                launchAccountInfoActivity(userId, oldPass, newPass);
            }
        });
    }

    private void launchAccountInfoActivity(String userId, String oldPass, String newPass){
        UserAccount account = accountManager.getUserAccount(userId);

        // Error check: Ensure neither password is blank
        if(oldPass.isEmpty() || newPass.isEmpty()){
            Toast.makeText(this, "Passwords cannot be blank", Toast.LENGTH_SHORT).show();
        }
        // Error check: Ensure old password is correct before changing to new password
        else if (!(account.getUserPassword().equals(oldPass))) {
            Toast.makeText(this, "Incorrect current password. Please Re-enter", Toast.LENGTH_SHORT).show();
        }
        // All Checks Pass - Changing Password
        else {
            accountManager.changeUserPassword(userId, oldPass, newPass);
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
        }
    }
}