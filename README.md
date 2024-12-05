# Dude Where's My Cash - Budget Tracker App
## Overview
Dude Where's My Cash is a personal budget tracker application designed to help users manage their finances. It allows users to track their expenses, manage multiple accounts, set notifications for budgeting goals, and visualize their spending habits.

## Features

- User Accounts: Users can create an account, log in, and view their personalized data.
- Expense Tracking: Users can add, edit, and categorize their expenses.
- Notifications: Set up notifications for upcoming expenses or budgeting goals.
- Budget Overview: View an overview of current budget status, spending patterns, and more.
- Account Info: Display and manage user account details like ID, username, and budget.
- Settings: Customize the app and configure user preferences.
- Monthly Spending Overview: View a bar chart that compares total spending amount for current and previous months.
- Sign Out: Sign out of the app and clear session data.
  
## Technologies Used
- Android: The app is built using Android Studio and follows modern Android development practices.
- Java: The app uses Java for backend logic.
- Shared Preferences: Used to store user session data and preferences.
- CSV: Expense and user account data are stored in CSV format for easy access and persistence.
  
## Setup
Requirements
- Android Studio: You need to have Android Studio installed on your machine to run and build the app.
- AVD: Android Virtual Device (or an actual Android device) for testing.
  
Getting Started
1. Clone the Repository: https://github.com/S-F-III/Dude-Wheres-My-Cache.git
2. Open the Project in Android Studio
 - Open Android Studio and choose Open an existing project.
 - Select the folder where you cloned the repository.
3.  Run the App
 - Set up an emulator or connect an Android device.
 - Click on the Run button in Android Studio to deploy the app.
4. Set Up Required Files
 - Ensure that the app has access to necessary resources such as CSV files (categories.csv) in the assets folder.
   
## Usage
Signing Up and Logging In
1. Open the app and navigate to the Sign Up screen.
2. Enter a username, ID, password, and budget amount.
3. Click Sign Up to create an account.
4. To log in, enter your credentials on the Log In screen.

Tracking Expenses
1. Navigate to the Expenses section from the main menu.
2. Add new expenses by clicking Add Expense.
3. Select a category, enter the amount, category, description, date, and if it is a recurring expense.
4. View the list of your tracked expenses, which is categorized for better organization.

Notifications
1. Set up reminders for upcoming expenses in the Notifications section.
2. Add a notification by specifying the title and date.
3. Notifications will be displayed dynamically on the screen based on the date and category.

Viewing the Overview
1. Access the Overview to get a snapshot of your financial status.
2. View your current budget, total spending amount, and all categories that make up your pie chart.
3. Click on categories to display their description
   
Managing Account Info
1. Navigate to Account Info to view your profile information.
2. Modify your information however you would like.

Viewing Monthly Spending
1. Navigate to View Monthly Spending to compare spending amounts between current and previous months
   
Sign Out
1. To sign out, click Sign Out from the menu, which will clear your session data.
   
## File Structure
- assets/: Contains CSV files for account and notification data.
- src/: Contains Java files for activities, adapters, and logic.
- res/: Contains XML files for layouts, resources, and styles.
- AndroidManifest.xml: App configuration and activity declarations.
  
## Screenshots
### Start Screen

![image](https://github.com/user-attachments/assets/80a1db65-56a5-46b5-867b-5e88bd24a43c)

### Sign Up Screen

![image](https://github.com/user-attachments/assets/4e61224b-325a-4e19-b604-60f1865d976d)

### Overview Page

![image](https://github.com/user-attachments/assets/cf132552-e19f-4438-b5bd-55a704752d87)

### Expenses

![image](https://github.com/user-attachments/assets/9fc12f5d-581f-45a0-ad22-600c8ed7483b)

### Notifications

![image](https://github.com/user-attachments/assets/c35d9098-c8b7-4e93-a8a1-7b67dcd7b53d)

## Contributing
### Bug Fixes and Features
Feel free to open issues or submit pull requests to contribute to this project. Whether it's fixing bugs, improving performance, or adding new features, your contributions are welcome!

## Known Issues
- Account creation date currently does not work as intended, it only saves the users sign up date. Whenever the user logs in, it cannot pull the creation date anymore.
- The export data button does nothing right now, it is functionless
- Can still press backspace to go into the previous page after changing any of the user information.

### Steps for Contributing
1. Fork the repository.
2. Create a new branch for your changes (git checkout -b feature-name).
3. Commit your changes (git commit -am 'Add new feature').
4. Push to the branch (git push origin feature-name).
5. Submit a pull request.
   
## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- Steve Fleming
- Darius Chavez
- Tala Kamil
- Ashley Salas
- Android Developer Documentation - for providing valuable resources and guidance throughout the development of the app.
- Android Studio - for the IDE that was used to develop this app.
