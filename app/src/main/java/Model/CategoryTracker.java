package Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class CategoryTracker {

    private static final String TAG = "Category";
    private ArrayList<Category> categories;

    public CategoryTracker(){
        categories = new ArrayList<Category>();
    }

    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void initializeInternalStorage(Context context, String assetFileName, String internalFileName) {
        File internalFile = new File(context.getFilesDir(), internalFileName);

        if (internalFile.exists()) {
            Log.i(TAG, "Internal file already exists: " + internalFileName);
            return;
        }

        try (InputStream assetInputStream = context.getAssets().open(assetFileName);
             FileOutputStream internalOutputStream = new FileOutputStream(internalFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = assetInputStream.read(buffer)) > 0) {
                internalOutputStream.write(buffer, 0, length);
            }

            Log.i(TAG, "Successfully copied asset to internal storage: " + internalFileName);
        } catch (IOException e) {
            Log.e(TAG, "Error initializing internal storage file: " + e.getMessage());
        }
    }

    public void loadCategoriesFromInternalFile(Context context, String filename, String userId) {
        categories.clear(); // Clear the list before loading
        boolean userExists = false;

        try {
            File file = new File(context.getFilesDir(), filename);
            if (!file.exists()) {
                Log.e(TAG, "Internal file not found: " + filename);
                return;
            }

            InputStream inputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(inputStream);
            scanner.nextLine(); // Skip header if present

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    Category category = new Category(tokens[0], tokens[1], tokens[2], tokens[3]);
                    categories.add(category);
                    if (tokens[3].equals(userId)) {
                        userExists = true;
                    }
                } else {
                    Log.e(TAG, "Malformed line: " + line);
                }
            }

            scanner.close();

            // If the user does not have categories, add defaults
            if (!userExists) {
                Log.i(TAG, "No categories found for user: " + userId + ". Adding default categories.");
                addDefaultCategoriesForUser(userId);
            }

            Log.i(TAG, "Categories loaded successfully from " + filename);
        } catch (IOException e) {
            Log.e(TAG, "Error loading categories: " + e.getMessage());
        }
    }

    public void saveCategoriesToInternalFile(Context context, String filename) {
        try {
            File file = new File(context.getFilesDir(), filename);

            StringBuilder data = new StringBuilder();

            // Optionally add a header line
            data.append("Name,Color,Description,UserId\n");

            for (Category category : categories) {
                data.append(category.getCategoryName()).append(",")
                        .append(category.getCategoryColor()).append(",")
                        .append(category.getCategoryDescription()).append(",")
                        .append(category.getUserId()).append("\n");
            }

            // Write all data to the file
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(data.toString().getBytes(StandardCharsets.UTF_8));
            }

            Log.i(TAG, "Categories saved successfully to " + filename);
        } catch (IOException e) {
            Log.e(TAG, "Error saving categories: " + e.getMessage());
        }
    }

    private void addDefaultCategoriesForUser(String userId) {
        // Add default categories for the new user
        categories.add(new Category("Housing","green","Expenses related to residence", userId));
        categories.add(new Category("Transportation", "red", "Expenses related to vehicles", userId));
        categories.add(new Category("Food", "yellow", "Expenses related to food", userId));
        categories.add(new Category("Insurance","blue","Expenses related to insurance", userId));
        categories.add(new Category("Healthcare", "orange", "Expenses related to medical items", userId));
        categories.add(new Category("Entertainment", "purple", "Expenses related to personal entertainment", userId));
        categories.add(new Category("Unspent", "white", "Remaining space for expenses", userId));
    }

    public Category getCategoryByID(String userCategory, String userId){
        for(Category category : categories){
            if(category.getUserId().equals(userId) && category.getCategoryName().equals(userCategory)){
                return category;
            }
        }
        return null;
    }

    public boolean removeCategory(Category category) {
        return categories.remove(category); // Remove from the ArrayList
    }
}
