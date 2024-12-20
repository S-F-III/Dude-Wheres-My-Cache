package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Model.Expense;
import Model.ExpenseBank;

/**
 * The MonthlySpendingActivity class displays a graphical representation of the user's
 * monthly spending in the form of a bar chart. It also provides navigation to other
 * activities via a hamburger menu and buttons.
 *
 * This activity retrieves the logged-in user's ID from shared preferences, initializes
 * the ExpenseBank to load user-specific expenses, and dynamically generates a bar chart
 * summarizing monthly spending data.
 */
public class MonthlySpendingActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * Initializes the UI components, sets up the hamburger menu and button click listeners,
     * retrieves the user's ID from shared preferences, and prepares the monthly spending chart.
     *
     * @param savedInstanceState the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_spending);


        ImageView hbMenu = findViewById(R.id.hbMenu);
        ImageView hbMenu2 = findViewById(R.id.hbMenu2);
        TextView overviewButton = findViewById(R.id.overviewButton);
        TextView expenseButton = findViewById(R.id.expenseButton);
        TextView notificationButton = findViewById(R.id.notificationButton);
        TextView accountInfoButton = findViewById(R.id.accountInfoButton);
        TextView settingsButton = findViewById(R.id.settingsButton);
        TextView monthlySpendingButton = findViewById(R.id.monthlySpendingButton);
        TextView signOutButton = findViewById(R.id.signOutButton);

        hbMenu.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.VISIBLE));
        hbMenu2.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.GONE));

        overviewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchOverview();
            }
        });
        expenseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchExpenses();
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchNotfications();
            }
        });
        accountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAccountInfo();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSettings();
            }
        });
        monthlySpendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMonthlySpending();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignOut();
            }
        });

        // Retrieve user ID
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Check if userId is valid
        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize ExpenseBank and load expenses
        ExpenseBank expenseBank = new ExpenseBank(this);
        expenseBank.initializeExpenseList();

        // Dynamically create and add a BarChart
        BarChart barChart = new BarChart(this);
        LinearLayout chartContainer = findViewById(R.id.barChart);
        chartContainer.addView(barChart);

        // Prepare data for the chart
        ArrayList<BarEntry> entries = processMonthlyExpenses(expenseBank, userId);

        // Create a dataset and add it to the chart
        BarDataSet dataSet = new BarDataSet(entries, "Monthly Spending");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Add colors to the bars
        BarData data = new BarData(dataSet);

        // Customize the chart
        barChart.setData(data);
        barChart.setFitBars(true); // Make the bars fit within the chart
        Description description = new Description();
        description.setText("Spending Overview");
        barChart.setDescription(description);
        barChart.animateY(1000); // Animation

        // Get Y-axis (Left Axis) of the chart
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Start from 0
        leftAxis.setAxisMaximum(5000f); // Adjust this to a value higher than the highest bar value
        leftAxis.setGranularity(50f); // Step size for labels

        // Optionally remove the right axis for a cleaner look
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Set the bar width (between 0.1f and 1.0f)
        data.setBarWidth(0.5f); // Adjust for wider bars

        // Dynamically set the chart size to stretch
        barChart.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        barChart.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        barChart.requestLayout();

        barChart.getXAxis().setGranularity(1f); // Ensure labels are evenly spaced
        barChart.getXAxis().setLabelCount(12); // Number of months or data points

    }

    /**
     * Processes the user's monthly expenses by aggregating expense amounts for each month.
     *
     * @param expenseBank The ExpenseBank containing user expenses.
     * @param userId      The ID of the current user.
     * @return A list of BarEntry objects representing monthly spending totals.
     */
    private ArrayList<BarEntry> processMonthlyExpenses(ExpenseBank expenseBank, String userId){
        ArrayList<BarEntry> entries = new ArrayList<>();
        Map<Integer, Double> monthlyTotals = new HashMap<>();

        // Iterate through expenses for the user
        for (Expense expense : expenseBank.getExpenses()) {
            if (expense.getExpenseOwner().equals(userId)) {
                // Extract the month from the expense date
                int month = expense.getDate().getMonthValue();

                // Add the expense amount to the monthly total
                monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + expense.getExpenseAmount());
            }
        }

        // Convert map entries into BarEntry objects
        for (Map.Entry<Integer, Double> entry : monthlyTotals.entrySet()) {
            int month = entry.getKey();
            float totalAmount = entry.getValue().floatValue();
            entries.add(new BarEntry(month, totalAmount));
        }

        // Sort entries by month (optional, for cleaner chart display)
        Collections.sort(entries, (e1, e2) -> Float.compare(e1.getX(), e2.getX()));

        return entries;
    }

    /**
     * Navigates to the OverviewActivity.
     */
    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the ExpenseActivity.
     */
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the NotificationActivity.
     */
    private void launchNotfications() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the AccountInfoActivity.
     */
    private void launchAccountInfo() {
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the SettingsActivity.
     */
    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Navigates to the MonthlySpendingActivity.
     * (Redundant here since it's already in MonthlySpendingActivity.)
     */
    private void launchMonthlySpending() {
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }

    /**
     * Signs the user out by clearing shared preferences and returning to the MainActivity.
     */
    private void launchSignOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER_ID").apply();
        editor.clear(); // Clear all stored data
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
