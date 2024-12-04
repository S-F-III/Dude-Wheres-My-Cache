package Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages a collection of {@code Expense} objects for a specific user in a budget tracking application.
 * Provides functionality to load, save, retrieve, and modify expenses stored in a CSV file.
 */
public class ExpenseBank {

    /**
     * List of expenses managed by this bank.
     */
    public ArrayList<Expense> expenses;

    /**
     * Activity context used for file operations.
     */
    private final Activity activity;

    /**
     * Name of the file where expenses are stored.
     */
    private static final String FILENAME = "expense-list.csv";

    /**
     * Tag used for logging operations in this class.
     */
    private static final String TAG = "ExpenseBank";

    /**
     * Constructs an {@code ExpenseBank} object and initializes the expense list.
     *
     * @param activity the activity context used for file operations
     */
    public ExpenseBank(Activity activity){
        this.activity = activity;
        expenses = new ArrayList<Expense>();
    }

    /**
     * Retrieves the list of expenses managed by this bank.
     *
     * @return the list of expenses
     */
    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Sets the list of expenses managed by this bank.
     *
     * @param expenses the list of expenses to set
     */
    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Finds and retrieves an expense by its ID and owner.
     *
     * @param expenseID the unique ID of the expense
     * @param userID    the ID of the user who owns the expense
     * @return the expense if found, or {@code null} otherwise
     */
    public Expense getExpenseByID(String expenseID, String userID){
        for (Expense expense : expenses){
            if(expense.getExpenseOwner().equals(userID) && expense.getExpenseID() == Integer.parseInt(expenseID)){
                return expense;
            }
        }
        return null;
    }

    /**
     * Initializes the expense list file. If the file does not exist, it creates a new one.
     */
    public void initializeExpenseList() {
        try {
            // Attempt to read the file
            InputStream in = activity.openFileInput(FILENAME);
            Log.i(TAG, "File found, loading expense data...");
            loadUserExpenses(in);
        } catch (FileNotFoundException e) {
            // If file does not exist, create it
            Log.e(TAG, "File not found. Creating new expense list file: " + FILENAME);
            try {
                OutputStream out = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                Log.i(TAG, "File created successfully.");
                out.close();
            } catch (FileNotFoundException e2) {
                Log.e(TAG, "Failed to create file: " + FILENAME);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Loads user expenses from the specified input stream.
     *
     * @param in the input stream containing expense data
     */
    public void loadUserExpenses(InputStream in) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (in != null) {
            Scanner scan = new Scanner(in);
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");

                if (tokens.length == 7) {
                    try {
                        LocalDate date = LocalDate.parse(tokens[5].trim(), formatter);
                        addUserExpense(new Expense(
                                Integer.parseInt(tokens[0].trim()),
                                tokens[1].trim(),
                                Double.parseDouble(tokens[2].trim()),
                                tokens[3].trim(),
                                tokens[4].trim(),
                                date,
                                Boolean.parseBoolean(tokens[6].trim())
                        ));
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                        System.out.println("Invalid date format for expense: " + tokens[5]);
                    }
                }
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new expense to the list.
     *
     * @param expense the expense to add
     */
    public void addUserExpense(Expense expense) {
        if (expenses == null) {
            expenses = new ArrayList<>();
        }
        if (expenses != null) {
            expenses.add(expense);
        }
    }


    /**
     * Saves all user expenses to the CSV file.
     */
    public void saveExpensesToFile() {
        try {
            OutputStream out = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (Expense expense : expenses) {
                String expenseData = expense.getExpenseID() + "," + expense.getExpenseOwner() + "," + expense.getExpenseAmount() + "," + expense.getExpenseCategory() + "," + expense.getExpenseDescription() + "," + expense.getDate() + "," + expense.isRecurring() + "\n";
                out.write(expenseData.getBytes(StandardCharsets.UTF_8));
            }
            out.close();
            Log.i(TAG, "expense data saved successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to save expense data.");
            e.printStackTrace();
        }
    }

    /**
     * Removes an expense from the list.
     *
     * @param expense the expense to remove
     * @return {@code true} if the expense was successfully removed; {@code false} otherwise
     */
    public boolean removeExpense(Expense expense) {
        return expenses.remove(expense); // Remove from the ArrayList
    }
}
