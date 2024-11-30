package com.example.dudewheresmycash;

import java.time.LocalDate;

public class Expense {
    private int expenseID;
    private String expenseOwner;
    private double expenseAmount;
    private String expenseCategory;
    private String expenseDescription;
    private LocalDate date;
    private boolean isRecurring;

    public Expense(int expenseID, String expenseOwner, double expenseAmount, String expenseCategory, String expenseDescription, LocalDate date, boolean isRecurring){
        this.expenseID = expenseID;
        this.expenseOwner = expenseOwner;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.expenseDescription = expenseDescription;
        this.date = date;
        this.isRecurring = isRecurring;
    }
    public Expense(String expenseOwner, double expenseAmount, String expenseCategory) {
        this.expenseOwner = expenseOwner;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
    }

    public int getExpenseID(){
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseOwner() {
        return expenseOwner;
    }

    public void setExpenseOwner(String expenseOwner) {
        this.expenseOwner = expenseOwner;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return isRecurring;
    }
}
