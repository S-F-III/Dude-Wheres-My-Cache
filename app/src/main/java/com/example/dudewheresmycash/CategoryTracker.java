package com.example.dudewheresmycash;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CategoryTracker {
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

    public void addCategory(Category category){
        if(categories != null){
            categories.add(category);
        }
    }

    public void loadCategories(OverviewActivity activity){
        AssetManager manager = activity.getAssets();
        Scanner scan = null;
        String filename = "categories.csv";
        try{
            // Open the CSV file from the assets folder
            InputStream file = manager.open(filename);
            scan = new Scanner(file);
            scan.nextLine(); // Skip the header line
            // Read each line of the CSV and create a Category object
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(",");
                addCategory(new Category(line[0],line[1],line[2]));
            }
        }
        catch (IOException e) {} // Exception handling can be added if necessary
    }

    public Category getCategory(String categoryType){
        for (Category category : categories){
            if(category.getCategoryName().equals(categoryType)){
                return category;
            }
        }
        return null;
    }
}
