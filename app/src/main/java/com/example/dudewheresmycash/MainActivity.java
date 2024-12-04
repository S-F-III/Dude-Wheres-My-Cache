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
 * MainActivity serves as the entry point of the application.
 * Provides a start button that navigates the user to the WelcomeActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * Sets up the layout, applies edge-to-edge design, and initializes the start button.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);

        // Set the layout resource for the activity
        setContentView(R.layout.activity_main);

        // Apply window insets for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the start button
        Button startButton = findViewById(R.id.start_button);
        // Set up click listener for the start button to navigate to the WelcomeActivity
        startButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Handles the click event for the start button.
             * Launches the WelcomeActivity when clicked.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view){
                launchActivity("Click to Start!");
            }
        });
    }

    /**
     * Launches the WelcomeActivity when the start button is clicked.
     *
     * @param startButton A string identifying the button (for extensibility, currently unused).
     */
    private void launchActivity(String startButton){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
