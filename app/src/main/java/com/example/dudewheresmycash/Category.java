package com.example.dudewheresmycash;

public class Category {
    private String categoryName;
    private String categoryColor;
    private String categoryDescription;

    public Category(String categoryName, String categoryColor, String categoryDescription){
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryColor(){
        return this.categoryColor;
    }

    public void setCategoryColor(String categoryColor){
        this.categoryColor = categoryColor;
    }
    public String getCategoryName(){
        return this.categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
    public String getCategoryDescription(){
        return this.categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription){
        this.categoryDescription = categoryDescription;
    }

}
