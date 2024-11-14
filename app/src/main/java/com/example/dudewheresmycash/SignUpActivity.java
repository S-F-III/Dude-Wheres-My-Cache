package com.example.dudewheresmycash;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText inputUserName = (EditText) findViewById(R.id.inputUserName);
        EditText inputUserID = (EditText) findViewById(R.id.inputUserID);
        EditText inputPassword = (EditText) findViewById(R.id.inputPassword);
        EditText inputUserBudget = (EditText) findViewById(R.id.inputBudget);
        Button nextButton = findViewById(R.id.next_button);

        accountManager = new AccountManager(this);
        accountManager.initializeAccountList();

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userName = inputUserName.getText().toString();
                String userID = inputUserID.getText().toString();
                String userPassword = inputPassword.getText().toString();
                String userBudget = inputUserBudget.getText().toString();
                UserAccount newAccount = new UserAccount(userID, userPassword, userName, userBudget);
                accountManager.addUserAccount(newAccount);
                accountManager.saveAccountsToFile();
                launchActivity("Next", userName, userID, userPassword, userBudget);
            }
        });
    }

    private void launchActivity(String button, String name, String id, String pass, String budget){
        if(!(name.isEmpty()) && !(id.isEmpty()) && !(pass.isEmpty() && isNumeric(budget))){
            int userBudget = Integer.parseInt(budget);
            if(userBudget > 0){
                Intent intent = new Intent(this, OverviewActivity.class);
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_ID", id);
                intent.putExtra("USER_PASSWORD", pass);
                intent.putExtra("USER_BUDGET", budget);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Invalid Budget: Must be greater than 0", Toast.LENGTH_SHORT).show();
            }
        }
        else if (!isNumeric(budget)) {
            Toast.makeText(this, "Invalid Budget: Enter a number e.g., 1000", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: All fields must be filled out", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}