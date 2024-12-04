package Model;

import java.time.LocalDate;

/**
 * Represents an expense in a budget tracking application.
 * Each expense is associated with a user, has a category, amount, description, date,
 * and may be marked as recurring.
 */
public class Expense {

    /**
     * Unique identifier for the expense.
     */
    private int expenseID;

    /**
     * Owner of the expense.
     */
    private String expenseOwner;

    /**
     * Amount of the expense.
     */
    private double expenseAmount;

    /**
     * Category of the expense.
     */
    private String expenseCategory;

    /**
     * Description of the expense.
     */
    private String expenseDescription;

    /**
     * Date the expense occurred.
     */
    private LocalDate date;

    /**
     * Indicates if the expense is recurring.
     */
    private boolean isRecurring;

    /**
     * Constructs a fully initialized {@code Expense}.
     *
     * @param expenseID         the unique identifier of the expense
     * @param expenseOwner      the owner of the expense
     * @param expenseAmount     the amount of the expense
     * @param expenseCategory   the category of the expense
     * @param expenseDescription a description of the expense
     * @param date              the date of the expense
     * @param isRecurring       whether the expense is recurring
     */
    public Expense(int expenseID, String expenseOwner, double expenseAmount, String expenseCategory, String expenseDescription, LocalDate date, boolean isRecurring){
        this.expenseID = expenseID;
        this.expenseOwner = expenseOwner;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.expenseDescription = expenseDescription;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    /**
     * Constructs an {@code Expense} with owner, amount, category, and date.
     *
     * @param expenseOwner    the owner of the expense
     * @param expenseAmount   the amount of the expense
     * @param expenseCategory the category of the expense
     * @param date            the date of the expense
     */
    public Expense(String expenseOwner, double expenseAmount, String expenseCategory, LocalDate date) {
        this.expenseOwner = expenseOwner;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.date = date;
    }

    /**
     * Constructs an {@code Expense} with owner, amount, and category.
     *
     * @param expenseOwner    the owner of the expense
     * @param expenseAmount   the amount of the expense
     * @param expenseCategory the category of the expense
     */
    public Expense(String expenseOwner, double expenseAmount, String expenseCategory) {
        this.expenseOwner = expenseOwner;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
    }

    /**
     * Retrieves the unique identifier for this expense.
     *
     * @return the expense ID
     */
    public int getExpenseID(){
        return expenseID;
    }

    /**
     * Sets the unique identifier for this expense.
     *
     * @param expenseID the expense ID
     */
    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    /**
     * Retrieves the owner of this expense.
     *
     * @return the expense owner
     */
    public String getExpenseOwner() {
        return expenseOwner;
    }

    /**
     * Sets the owner of this expense.
     *
     * @param expenseOwner the expense owner
     */
    public void setExpenseOwner(String expenseOwner) {
        this.expenseOwner = expenseOwner;
    }

    /**
     * Retrieves the amount of this expense.
     *
     * @return the expense amount
     */
    public double getExpenseAmount() {
        return expenseAmount;
    }

    /**
     * Sets the amount of this expense.
     *
     * @param expenseAmount the expense amount
     */
    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    /**
     * Retrieves the category of this expense.
     *
     * @return the expense category
     */
    public String getExpenseCategory() {
        return expenseCategory;
    }

    /**
     * Sets the category of this expense.
     *
     * @param expenseCategory the expense category
     */
    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    /**
     * Retrieves the description of this expense.
     *
     * @return the expense description
     */
    public String getExpenseDescription() {
        return expenseDescription;
    }

    /**
     * Sets the description of this expense.
     *
     * @param expenseDescription the expense description
     */
    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    /**
     * Retrieves the date of this expense.
     *
     * @return the date of the expense
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of this expense.
     *
     * @param date the date of the expense
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Checks whether this expense is recurring.
     *
     * @return {@code true} if the expense is recurring; {@code false} otherwise
     */
    public boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Sets whether this expense is recurring.
     *
     * @param recurring {@code true} if the expense is recurring; {@code false} otherwise
     */
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }
}
