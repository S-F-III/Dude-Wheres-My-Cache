package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.*;
import java.io.*;

public class OverviewActivity extends AppCompatActivity {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private AccountManager accountManager;
    private UserAccount userAccount;
    private CategoryTracker categoryTracker;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pieChart = findViewById(R.id.pieChart);
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

        // Retrieve user ID from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        if (userId != null) {
            accountManager = new AccountManager(this);
            accountManager.initializeAccountList();
            userAccount = accountManager.getUserAccount(userId);
        }

        if (userAccount != null) {
            String userName = userAccount.getUserName();
            String userBudget = userAccount.getUserBudget();

            Calendar now = Calendar.getInstance();
            //displays a good morning message or good afternoon message with the user's name
            if (now.get(Calendar.HOUR_OF_DAY) < 12) {
                TextView overviewMessage = findViewById(R.id.overview_message);
                overviewMessage.setText("Good Morning, " + "\n" + userName);
            } else {
                TextView overviewMessage = findViewById(R.id.overview_message);
                overviewMessage.setText("Good Afternoon, " + "\n" + userName);
            }

            //displays the budget
            TextView user_spent_amt = findViewById(R.id.budget_spent_amt);
            user_spent_amt.setText("Current Budget: " + "\n$" + userBudget);
        }
        else{
            Toast.makeText(this, "User's account is null", Toast.LENGTH_SHORT).show();
        }

        dynamicCategorySetup();
        createPieChart(this,Double.parseDouble(userAccount.getUserBudget()), pieChart, userAccount.getUserID());

    }

    private void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }
    private void launchExpenses() {
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }
    private void launchNotfications() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
    private void launchAccountInfo() {
        Intent intent = new Intent(this, AccountInfoActivity.class);
        startActivity(intent);
    }
    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    private void launchMonthlySpending() {
        Intent intent = new Intent(this, MonthlySpendingActivity.class);
        startActivity(intent);
    }
    private void launchSignOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        sharedPreferences.edit().remove("USER_ID").apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createCategoryList(){
        categoryTracker = new CategoryTracker();
        categoryTracker.loadCategories(this);
    }

    private void dynamicCategorySetup(){
        /*
         *This code serves as a dynamic way to load all categories, including custom ones
         * It will make a scrollable view that will have a picture of the color to the left
         * And the name of the category to the right
         * We load a list of categories from a file
         * and iterate through them to populate the category list
         * still need to modify this to pull data based on user
         * and exception/debugging statements
         */
        // Initialize the list of categories
        createCategoryList();

        LinearLayout categoryLayoutMain = findViewById(R.id.category_layout);
        categoryLayoutMain.removeAllViews(); // Clear any previous data before adding new views
        for(Category category : categoryTracker.getCategories()){
            Log.d("OverviewActivity", "Adding category: " + category.getCategoryName());

            // Create a horizontal LinearLayout for each category
            LinearLayout categoryLayout = new LinearLayout(this);
            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
            categoryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Create the ImageView (this will be a picture of the color representing the category)
            ImageView categoryImage = new ImageView(this);
            String imageName = category.getCategoryColor().toLowerCase(); // Ensure this matches your image naming
            int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
            categoryImage.setImageResource(imageResource);
            categoryImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100)); // Set size for the image
            categoryImage.setAdjustViewBounds(true); // Maintain aspect ratio

            // Create the TextView
            TextView categoryName = new TextView(this);
            categoryName.setText(category.getCategoryName());
            categoryName.setTypeface(null, Typeface.BOLD); // Make text bold
            int colorResource = getResources().getIdentifier(category.getCategoryColor(), "color", getPackageName());
            categoryName.setTextColor(ContextCompat.getColor(this, colorResource)); // OPTIONAL: Set the color to match category color
            categoryName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    100));
            categoryName.setPadding(16, 0, 0, 0); // Optional padding to separate text from image

            // Add ImageView and TextView to the horizontal layout
            categoryLayout.addView(categoryImage);
            categoryLayout.addView(categoryName);
            categoryLayout.setPadding(0,0,0,12);

            // Add the horizontal layout to the main category layout
            categoryLayoutMain.addView(categoryLayout);
        }
    }

    private void createPieChart(OverviewActivity activity, double total, PieChart pieChart, String userID){
        ArrayList<Expense> loading = new ArrayList<>();
        ArrayList<Expense> totaled = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();
        String fileName = "expense-list.csv";
        double startBudget = total; //need this to show correct color for amount spent at the bottom of this method
        // Access the internal storage file
        File expenses = new File(activity.getFilesDir(), fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(expenses))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String owner = values[1];
                String category = values[3];
                double value = Double.parseDouble(values[2]);
                if (owner.equals(userID)) {
                    loading.add(new Expense(owner, value, category));
                    total = total - value;
                }
            }
            double temp = 0;
            for(Expense x : loading) { if(x.getExpenseCategory().equals("Housing")) { temp = temp + x.getExpenseAmount(); } }
            totaled.add(new Expense (userID, temp, "Housing"));
            temp = 0;
            for(Expense x : loading) { if(x.getExpenseCategory().equals("Transportation")) { temp = temp + x.getExpenseAmount(); } }
            totaled.add(new Expense(userID, temp, "Transportation"));
            temp = 0;
            for(Expense x : loading) { if (x.getExpenseCategory().equals("Food")) { temp = temp + x.getExpenseAmount(); } }
            totaled.add(new Expense (userID, temp, "Food"));
            temp = 0;
            for(Expense x : loading) { if(x.getExpenseCategory().equals("Insurance")) { temp = temp + x.getExpenseAmount(); } }
            totaled.add(new Expense(userID, temp, "Insurance"));
            temp = 0;
            for(Expense x : loading) { if(x.getExpenseCategory().equals("Healthcare")) { temp = temp + x.getExpenseAmount(); } }
            totaled.add(new Expense (userID, temp, "Healthcare"));
            temp = 0;
            for(Expense x : loading) { if(x.getExpenseCategory().equals("Entertainment")) { temp = temp + x.getExpenseAmount(); } }
            totaled.add(new Expense(userID, temp, "Entertainment"));
            temp = 0;

            // Add the remaining slice
            for (Expense expense : totaled) {
                pieEntries.add(new PieEntry((float) expense.getExpenseAmount()));
            }
            if (total > 0) { pieEntries.add(new PieEntry((float) total, "")); }
        } catch (IOException e) {
            Log.e("PullExpenseData", "File Not Found");
            e.printStackTrace();
        }

        // Create PieDataSet
        PieDataSet dataSet = new PieDataSet(pieEntries, "Expenses");
        dataSet.setColors(new int[]{R.color.green, R.color.red, R.color.yellow, R.color.blue, R.color.orange, R.color.purple, R.color.white}, this);
        dataSet.setValueTextSize(14f);
        dataSet.setDrawValues(false);


        // Create PieData
        PieData data = new PieData(dataSet);

        // Configure the PieChart
        pieChart.setData(data);
        pieChart.setUsePercentValues(false); // Enable percentage display
        pieChart.getDescription().setEnabled(false); // Remove chart description
        pieChart.setDrawHoleEnabled(false); // Enable center hole
        pieChart.setHoleRadius(30f); // Size of the hole
        pieChart.setTransparentCircleRadius(40f); // Outer transparent circle
        pieChart.setEntryLabelTextSize(12f); // Label text size
        pieChart.getLegend().setEnabled(false); // Enable legend
        pieChart.invalidate();

        //Set Text View Displaying Total Spent
        TextView userTotalSpent = findViewById(R.id.user_total_spent);
        total = startBudget - total;
        if(total > (startBudget * 0.75) && total < (startBudget)) {
            userTotalSpent.setText("Total Spent: $" + df.format(total));
            userTotalSpent.setTextColor(getResources().getColor(R. color. yellow)); //yellow means caution, almost at limit
        }
        else if(total >= startBudget){
            userTotalSpent.setText("Total Spent: $" + df.format(total));
            userTotalSpent.setTextColor(getResources().getColor(R. color. red)); //red means danger, at or above limit
        }
        else{
            userTotalSpent.setText("Total Spent: $" + df.format(total));
        }
    }
}