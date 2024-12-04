package com.example.dudewheresmycash;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.*;
import java.io.*;

import Model.AccountManager;
import Model.Category;
import Model.CategoryTracker;
import Model.Expense;
import Model.UserAccount;
/**
 * The {@code OverviewActivity} class is the main activity for the financial tracking app.
 * This activity provides an overview of the user's financial data, including a pie chart
 * to visualize spending and a personalized message based on the time of day.
 * Key Features:
 * - Displays the user's budget and spending categories.
 * - Includes a pie chart for visualizing spending distribution.
 * - Provides navigation to other parts of the app via a hamburger menu.
 * - Integrates with user account data using {@link AccountManager}.
 */

public class OverviewActivity extends AppCompatActivity {

    // Formatter for displaying numbers with two decimal points
    private static final DecimalFormat df = new DecimalFormat("0.00");

    // Core objects for managing user data and displaying it in the UI
    private AccountManager accountManager;
    private UserAccount userAccount;
    private CategoryTracker categoryTracker;
    PieChart pieChart;


    /**
     * Called when the activity is first created.
     * This method initializes the UI components, retrieves user data from shared preferences,
     * and sets up dynamic content like the pie chart and category layout.
     *
     * @param savedInstanceState Saved state of the activity if it is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge content for modern Android design
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_overview);

        // Apply window insets for seamless integration with system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
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
       Button okayButton = findViewById(R.id.okayButton);

        /**
         * Sets up the listener for the OK button.
         * When clicked, hides the category description view.
         */
       okayButton.setOnClickListener(v -> findViewById(R.id.categoryDescription).setVisibility(View.GONE));

        /**
         * Sets up the listener for the hamburger menu button.
         * When clicked, displays the hamburger menu view.
         */
        hbMenu.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.VISIBLE));

        /**
         * Sets up the listener for the secondary hamburger menu button.
         * When clicked, hides the hamburger menu view.
         */
        hbMenu2.setOnClickListener(v -> findViewById(R.id.hamburgerMenu).setVisibility(View.GONE));

        /**
         * Sets up the listener for the Overview button.
         * When clicked, navigates to the Overview screen.
         */
        overviewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchOverview();
            }
        });

        /**
         * Sets up the listener for the Expenses button.
         * When clicked, navigates to the Expenses screen.
         */
        expenseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchExpenses();
            }
        });

        /**
         * Sets up the listener for the Notifications button.
         * When clicked, navigates to the Notifications screen.
         */
        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchNotfications();
            }
        });

        /**
         * Sets up the listener for the Account Info button.
         * When clicked, navigates to the Account Info screen.
         */
        accountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAccountInfo();
            }
        });

        /**
         * Sets up the listener for the Settings button.
         * When clicked, navigates to the Settings screen.
         */
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSettings();
            }
        });

        /**
         * Sets up the listener for the Monthly Spending button.
         * When clicked, navigates to the Monthly Spending screen.
         */
        monthlySpendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMonthlySpending();
            }
        });

        /**
         * Sets up the listener for the Sign Out button.
         * When clicked, logs the user out and navigates to the Sign Out screen.
         */
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
            // Initialize account manager and fetch user data
            accountManager = new AccountManager(this);
            accountManager.initializeAccountList();
            userAccount = accountManager.getUserAccount(userId);
        }

        if (userAccount != null) {
            // Display personalized greetings and user budget
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

        // Setup dynamic content
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER_ID").apply();
        editor.clear(); // Clear all stored data
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void createCategoryList(){
        categoryTracker = new CategoryTracker();
        categoryTracker.initializeInternalStorage(this, "categories.csv", "internal-categories.csv");
        // Check and add user-specific categories if needed
        categoryTracker.loadCategoriesFromInternalFile(this, "internal-categories.csv", userAccount.getUserID());
        // Save the updated categories to persist changes
        categoryTracker.saveCategoriesToInternalFile(this, "internal-categories.csv");
    }

    private void dynamicCategorySetup(){
        /*
         *This code serves as a dynamic way to load all categories, including custom ones
         * It will make a scrollable view that will have a picture of the color to the left
         * And the name of the category to the right
         * We load a list of categories from a file
         * and iterate through them to populate the category list
         */
        // Initialize the list of categories
        createCategoryList();

        LinearLayout categoryLayoutMain = findViewById(R.id.category_layout);
        categoryLayoutMain.removeAllViews(); // Clear any previous data before adding new views
        for(Category category : categoryTracker.getCategories()){
            if(userAccount.getUserID().equals(category.getUserId())) {
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
                categoryImage.setPadding(22, 0, 0, 0);

                // Create the TextView
                TextView categoryName = new TextView(this);
                categoryName.setText(category.getCategoryName());
                categoryName.setTextSize(24);
                categoryName.setTypeface(null, Typeface.BOLD); // Make text bold
                int colorResource = getResources().getIdentifier(category.getCategoryColor(), "color", getPackageName());
                categoryName.setTextColor(ContextCompat.getColor(this, colorResource)); // OPTIONAL: Set the color to match category color
                categoryName.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        100));
                categoryName.setPadding(16, 0, 0, 0); // Optional padding to separate text from image

                categoryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView description = findViewById(R.id.descriptionText);
                        description.setText(category.getCategoryDescription());
                        findViewById(R.id.categoryDescription).setVisibility(View.VISIBLE);
                    }
                });

                // Add ImageView and TextView to the horizontal layout
                categoryLayout.addView(categoryImage);
                categoryLayout.addView(categoryName);
                categoryLayout.setPadding(0, 0, 0, 12);

                // Add the horizontal layout to the main category layout
                categoryLayoutMain.addView(categoryLayout);
            }
        }
    }

    /**
     * Creates and configures a PieChart based on user expenses for the current month.
     *
     * This method reads expense data from a CSV file, filters it by the user's ID and
     * the current month, and organizes it into categories. It then calculates the total
     * expense for each category, adds this data to a PieChart, and updates a TextView to
     * display the total amount spent.
     *
     * @param activity  the activity from which this method is called, used for accessing files and resources
     * @param total     the user's total budget for the current month
     * @param pieChart  the PieChart object to be populated with expense data
     * @param userID    the ID of the user whose expenses will be displayed
     */
    private void createPieChart(OverviewActivity activity, double total, PieChart pieChart, String userID){
        // Initialize category list and variables for storing data
        createCategoryList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ArrayList<Expense> loading = new ArrayList<>();
        ArrayList<Expense> totaled = new ArrayList<>();
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();
        String fileName = "expense-list.csv";
        double startBudget = total; //need this to show correct color for amount spent at the bottom of this method

        File expenses = new File(activity.getFilesDir(), fileName);
        try (BufferedReader read = new BufferedReader(new FileReader(expenses))) {
            // Read and parse the CSV file line by line
            String line;
            while ((line = read.readLine()) != null) {
                String[] values = line.split(",");
                String owner = values[1];
                String category = values[3];
                LocalDate date = LocalDate.parse(values[5].trim(), formatter);
                double value = Double.parseDouble(values[2]);

                // Filter expenses by user ID
                if (owner.equals(userID)) {
                    loading.add(new Expense(owner, value, category, date));
                }
            }

            // Sum expenses for each category for the current month
            for (Category category : categoryTracker.getCategories()) {
                if(userAccount.getUserID().equals(category.getUserId())) {
                    String currentCategory = category.getCategoryName();
                    double temp = 0;
                    LocalDate date = LocalDate.now();
                    YearMonth currentMonthYear = YearMonth.now();

                    // Sum all expenses under the current category
                    for (Expense x : loading) {
                        if (x.getExpenseCategory().equals(currentCategory) && YearMonth.from(x.getDate()).equals(currentMonthYear)) {
                            temp += x.getExpenseAmount();
                            date = x.getDate();
                            Log.i("PullExpenseData", "Date: " + date);
                        }
                    }
                    // Add the totaled expense for this category to the list
                    if (currentCategory.equals("Unspent")) {
                    } else if (date != null) {
                        totaled.add(new Expense(userID, temp, currentCategory));
                    }
                }
            }

            Log.i("PullExpenseData", "Number of Categories: " + totaled.size());



            // Add categorized data to the PieChart entries
            for (Expense expense : totaled) {
                Log.i("PullExpenseData", "Expense: " + expense.getExpenseAmount() + "for " + expense.getExpenseCategory());
                total -= expense.getExpenseAmount();
                pieEntries.add(new PieEntry((float) expense.getExpenseAmount()));
            }

            // Add the remaining budget to the chart
            if (total > 0) { pieEntries.add(new PieEntry((float) total)); }
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

        // Configure the PieChart and its appearance
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