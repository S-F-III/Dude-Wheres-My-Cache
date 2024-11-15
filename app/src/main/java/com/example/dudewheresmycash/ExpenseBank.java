package com.example.dudewheresmycash;

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

public class ExpenseBank {

    public ArrayList<Expense> expenses;
    private final Activity activity;
    private static final String FILENAME = "expense-list.csv";
    private static final String TAG = "ExpenseBank";

    public ExpenseBank(Activity activity){
        this.activity = activity;
        expenses = new ArrayList<Expense>();
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    // Method to initialize or create the expense list file if it doesn't exist
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

    // Load expenses from the file
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

    // Method to add a new user account
    public void addUserExpense(Expense expense) {
        if (expenses == null) {
            expenses = new ArrayList<>();
        }
        if (expenses != null) {
            expenses.add(expense);
        }
    }


    // Save all user expenses to the file
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

    public void removeUserExpense(String expenseID){

    }
}
