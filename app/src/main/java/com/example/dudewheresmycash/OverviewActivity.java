package com.example.dudewheresmycash;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.Calendar;

public class OverviewActivity extends AppCompatActivity {

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

        Calendar now = Calendar.getInstance();
        String userName = getIntent().getStringExtra("USER_NAME");
        String userBudget = getIntent().getStringExtra("USER_BUDGET");

        //displays a good morning message or good afternoon message with the user's name
        if (now.get(Calendar.HOUR_OF_DAY) < 12) {
            TextView overviewMessage = findViewById(R.id.overview_message);
            overviewMessage.setText("Good Morning " + userName);
        } else {
            TextView overviewMessage = findViewById(R.id.overview_message);
            overviewMessage.setText("Good Afternoon " + userName);
        }

        TextView user_spent_amt = findViewById(R.id.budget_spent_amt);
        user_spent_amt.setText("Current Budget: " + "\n$" +userBudget);

    /*
    *This commented code serves as a dynamic way to load all categories, including custom ones
    * It will make a scrollable view that will have a picture of the color to the left
    * And the name of the category to the right
    * We will need to load a list of categories from a file
    * and iterate through them to populate the category list
    * some work MIGHT still need to be done after this has been uncommented
    * such as: for loop to iterate through category list, exception statements for debugging, etc.
    * use your best judgement to figure out what needs to be created to make
    * this work as it should
                        LinearLayout categoryLayoutMain = findViewById(R.id.category_layout);
                        categoryLayoutMain.removeAllViews(); // Clear any previous data before adding new views

                            // Create a horizontal LinearLayout for each category
                            LinearLayout categoryLayout = new LinearLayout(this);
                            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
                            categoryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            // Create the ImageView (this will be a picture of the color representing the category)
                            ImageView categoryImage = new ImageView(this);
                            String imageName = category.getName().toLowerCase(); // Ensure this matches your image naming
                            int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
                            categoryImage.setImageResource(imageResource);
                            categoryImage.setLayoutParams(new LinearLayout.LayoutParams(50, 50)); // Set size for the image
                            categoryImage.setAdjustViewBounds(true); // Maintain aspect ratio

                            // Create the TextView
                            TextView categoryName = new TextView(this);
                            categoryName.setText(category);
                            categoryName.setTypeface(null, Typeface.BOLD); // Make text bold
                            categoryName.setTextColor(titleColor); // OPTIONAL: Set the color to match category color
                            categoryName.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            category.setPadding(16, 0, 0, 0); // Optional padding to separate text from image

                            // Add ImageView and TextView to the horizontal layout
                            categoryLayout.addView(categoryImage);
                            categoryLayout.addView(categoryName);

                            // Add the horizontal layout to the main category layout
                            categoryLayoutMain.addView(categoryLayout);
     */
    }
}