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
 * The MainActivity class serves as the entry point for the app.
 * It initializes the main UI components and handles user interaction with the start button.
 * When the start button is clicked, it navigates to the WelcomeActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * Initializes the UI components and sets up the click listener for the start button.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup start button and its click listener
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity("Click to Start!");
            }
        });
    }

    /**
     * Launches the WelcomeActivity when the start button is clicked.
     *
     * @param startButton The text displayed on the start button.
     */
    private void launchActivity(String startButton){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
