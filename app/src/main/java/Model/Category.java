package Model;

public class Category {
    private String categoryName;
    private String categoryColor;
    private String categoryDescription;
    private String userId;

    public Category(String categoryName, String categoryColor, String categoryDescription, String userId){
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.categoryDescription = categoryDescription;
        this.userId = userId;
    }

    public String getCategoryColor(){
        return this.categoryColor;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
