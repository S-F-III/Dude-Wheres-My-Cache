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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LoginActivity extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        EditText inputUserID = (EditText) findViewById(R.id.inputUserID);
        EditText inputUserPassword = (EditText) findViewById(R.id.inputPassword);
        Button logInButton = findViewById(R.id.log_in_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> launchWelcome());


        logInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userID = inputUserID.getText().toString();
                String userPassword = inputUserPassword.getText().toString();
                launchActivity("Next", userID, userPassword);
            }
        });
    }

    private void launchActivity(String button, String id, String pass){
        UserAccount currAccount = accountManager.getUserAccount(id);

        if(currAccount != null) {
            if (!(id.equals(currAccount.getUserID()))) {
                Toast.makeText(this, "Error: No existing username found", Toast.LENGTH_SHORT).show();
            } else if (id.equals(currAccount.getUserID()) && !(pass.equals(currAccount.getUserPassword()))) {
                Toast.makeText(this, "Error: Incorrect Password", Toast.LENGTH_SHORT).show();
            } else {
                // Save user ID to SharedPreferences before launching the next activity
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_ID", id); // Store the user ID here
                editor.apply();

                //launch the OverviewActivity
                Intent intent = new Intent(this, OverviewActivity.class);
                intent.putExtra("USER_ID", id);
                intent.putExtra("USER_PASSWORD", pass);
                intent.putExtra("USER_NAME", currAccount.getUserName());
                intent.putExtra("USER_BUDGET", currAccount.getUserBudget());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(this, "current account not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchWelcome() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

}