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

/**
 * Manages a collection of {@link Category} objects, allowing for loading, saving, and managing
 * categories in budget tracker application. Supports operations for associating categories with users.
 */
public class CategoryTracker {

    /**
     * Tag used for logging messages.
     */
    private static final String TAG = "Category";

    /**
     * A list of {@link Category} objects managed by this tracker.
     */
    private ArrayList<Category> categories;

    /**
     * Constructs a new {@code CategoryTracker} with an empty list of categories.
     */
    public CategoryTracker(){
        categories = new ArrayList<Category>();
    }

    /**
     * Retrieves the list of categories managed by this tracker.
     *
     * @return a list of {@link Category} objects
     */
    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    /**
     * Sets the list of categories managed by this tracker.
     *
     * @param categories a list of {@link Category} objects
     */
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    /**
     * Copies a file from the assets folder to internal storage if it does not already exist.
     *
     * @param context         the application context
     * @param assetFileName   the name of the asset file to copy
     * @param internalFileName the name of the destination file in internal storage
     */
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

    /**
     * Loads categories from a specified internal file. If the user does not have existing categories,
     * default categories are created.
     *
     * @param context  the application context
     * @param filename the name of the file in internal storage
     * @param userId   the ID of the user whose categories are being loaded
     */
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

    /**
     * Saves the current list of categories to a specified internal file.
     *
     * @param context  the application context
     * @param filename the name of the file in internal storage
     */
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

    /**
     * Adds a set of default categories for a specific user.
     *
     * @param userId the ID of the user for whom to add default categories
     */
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

    /**
     * Retrieves a category by its name and user ID.
     *
     * @param userCategory the name of the category
     * @param userId       the ID of the user who owns the category
     * @return the {@link Category} object, or {@code null} if not found
     */
    public Category getCategoryByID(String userCategory, String userId){
        for(Category category : categories){
            if(category.getUserId().equals(userId) && category.getCategoryName().equals(userCategory)){
                return category;
            }
        }
        return null;
    }

    /**
     * Removes a specified category from the list of categories.
     *
     * @param category the {@link Category} to remove
     * @return {@code true} if the category was successfully removed; {@code false} otherwise
     */
    public boolean removeCategory(Category category) {
        return categories.remove(category); // Remove from the ArrayList
    }
}
