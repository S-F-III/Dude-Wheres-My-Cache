package Model;

/**
 * Represents a category in a budget tracker application.
 * Each category is associated with a user and includes a name, color, and description.
 */
public class Category {

    /**
     * The name of the category.
     */
    private String categoryName;

    /**
     * The color associated with the category, typically represented as a string (e.g., hex code or color name).
     */
    private String categoryColor;

    /**
     * A description of the category.
     */
    private String categoryDescription;

    /**
     * The ID of the user who owns this category.
     */
    private String userId;

    /**
     * Constructs a {@code Category} with the specified attributes.
     *
     * @param categoryName        the name of the category
     * @param categoryColor       the color associated with the category
     * @param categoryDescription a description of the category
     * @param userId              the ID of the user who owns this category
     */
    public Category(String categoryName, String categoryColor, String categoryDescription, String userId){
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.categoryDescription = categoryDescription;
        this.userId = userId;
    }

    /**
     * Retrieves the color associated with this category.
     *
     * @return the color of the category
     */
    public String getCategoryColor(){
        return this.categoryColor;
    }

    /**
     * Retrieves the name of this category.
     *
     * @return the name of the category
     */
    public String getCategoryName(){
        return this.categoryName;
    }

    /**
     * Updates the name of this category.
     *
     * @param categoryName the new name for the category
     */
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    /**
     * Retrieves the description of this category.
     *
     * @return the description of the category
     */
    public String getCategoryDescription(){
        return this.categoryDescription;
    }

    /**
     * Updates the description of this category.
     *
     * @param categoryDescription the new description for the category
     */
    public void setCategoryDescription(String categoryDescription){
        this.categoryDescription = categoryDescription;
    }

    /**
     * Retrieves the ID of the user who owns this category.
     *
     * @return the user ID associated with this category
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Updates the user ID associated with this category.
     *
     * @param userId the new user ID for this category
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
