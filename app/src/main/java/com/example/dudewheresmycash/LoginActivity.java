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

public class LoginActivity extends AppCompatActivity {

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

        EditText inputUserID = (EditText) findViewById(R.id.inputUserID);
        EditText inputUserPassword = (EditText) findViewById(R.id.inputPassword);
        Button nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userID = inputUserID.getText().toString();
                String userPassword = inputUserPassword.getText().toString();
                launchActivity("Next", userID, userPassword);
            }
        });
    }

    private void launchActivity(String button, String id, String pass){
        if(!(id.isEmpty()) && !(pass.isEmpty())){
            Intent intent = new Intent(this, OverviewActivity.class);
            intent.putExtra("USER_ID", id);
            intent.putExtra("USER_PASSWORD", pass);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Error: Incorrect Username or Password", Toast.LENGTH_SHORT).show();
        }
    }
}