package com.example.dudewheresmycash;

public class UserAccount {
    private String userID;
    private String userPassword;
    private String userName;
    private String userBudget;

    public UserAccount(String userID, String userPassword, String userName, String userBudget){
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userBudget = userBudget;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBudget() {
        return userBudget;
    }

    public void setUserBudget(String userBudget) {
        this.userBudget = userBudget;
    }


}
