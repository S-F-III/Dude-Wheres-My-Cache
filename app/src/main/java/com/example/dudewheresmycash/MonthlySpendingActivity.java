package com.example.dudewheresmycash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MonthlySpendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monthly_spending);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView hbMenu = findViewById(R.id.hbMenu);
        hbMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchHBMenu();
            }
        });
    }

    private void launchHBMenu(){
        Intent intent = new Intent(this, HamburgerActivity.class);
        startActivity(intent);
    }
}