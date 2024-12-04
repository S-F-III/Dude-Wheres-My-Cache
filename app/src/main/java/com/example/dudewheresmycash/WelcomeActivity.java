package com.example.dudewheresmycash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * The WelcomeActivity is the first screen shown to the user upon opening the app.
 * It presents two buttons: "Log In" and "Sign Up", which allow the user to
 * either log into an existing account or create a new one by launching
 * the corresponding activities.
 */

public class WelcomeActivity extends AppCompatActivity {

    /**
     * Initializes the WelcomeActivity by setting up the layout, adjusting
     * system bar padding, and handling button click events for Log In
     * and Sign Up actions.
     *
     * @param savedInstanceState The saved instance state (if any) of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Adjusts the padding to accommodate system bars (e.g., status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the buttons
        Button logInButton = findViewById(R.id.log_in_button);
        Button signUpButton = findViewById(R.id.sign_up_button);

        // Set click listeners for the buttons
        logInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            // Launch the LoginActivity when the "Log In" button is clicked
            public void onClick(View view){
                launchActivity("Log In");
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            // Launch the SignUpActivity when the "Sign Up" button is clicked
            public void onClick(View view){
                launchActivity("Sign Up");
            }
        });
    }

    /**
     * Launches either the LoginActivity or SignUpActivity depending on which button was clicked.
     *
     * @param button The name of the button clicked, either "Log In" or "Sign Up".
     */
    private void launchActivity(String button){
        if(button.equals("Log In")){
            // Launch LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            // Launch SignUpActivity
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }
}