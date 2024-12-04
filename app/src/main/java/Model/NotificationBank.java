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
import java.util.*;

/**
 * Manages a collection of user notifications, allowing for loading, saving,
 * and finding notifications associated with specific users.
 */
public class NotificationBank {

    /**
     * List of all notifications managed by this NotificationBank.
     */
    public ArrayList<Notification> notifications;

    /**
     * The activity context used for file operations.
     */
    private final Activity activity;

    /**
     * The name of the file where notification data is stored.
     */
    private static final String file = "notification-list.csv";

    /**
     * Tag used for logging purposes.
     */
    private static final String TAG = "NotificationBank";

    /**
     * Retrieves the list of notifications.
     *
     * @return the list of notifications
     */
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Sets the list of notifications.
     *
     * @param notifications the list of notifications to set
     */
    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * Constructs a {@code NotificationBank} object associated with a given activity.
     *
     * @param activity the activity context used for file operations
     */
    public NotificationBank(Activity activity){
        this.activity = activity;
        notifications = new ArrayList<Notification>();
    }

    /**
     * Finds a notification associated with a specific user by their ID.
     *
     * @param userID the user ID to search for
     * @return the first notification found for the specified user, or {@code null} if none exist
     */
    public Notification findNotificationByOwner(String userID){
        for (Notification x : notifications){
            if(x.getOwner().equals(userID)){
                return x;
            }
        }
        return null;
    }

    /**
     * Initializes the notification list by either loading existing data from the file
     * or creating a new file if none exists.
     */
    public void initializeNotificationList() {
        try {
            // Attempt to read the file
            InputStream in = activity.openFileInput(file);
            loadUserNotification(in);
            Log.i(TAG, "File found, loading notification data...");

        } catch (FileNotFoundException e) {
            // If file does not exist, create it
            Log.e(TAG, "File not found. Creating new notification list file: " + file);
            try {
                OutputStream out = activity.openFileOutput(file, Context.MODE_PRIVATE);
                Log.i(TAG, "File created successfully.");
                out.close();
            } catch (FileNotFoundException e2) {
                Log.e(TAG, "Failed to create file: " + file);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Loads notifications from an input stream.
     *
     * @param in the input stream containing notification data
     */
    public void loadUserNotification(InputStream in) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (in != null) {
            Scanner scan = new Scanner(in);
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");

                if (tokens.length == 3) {
                    Log.d("NotificationBank", "Loading notification: " + tokens[0]);
                    try {
                        LocalDate date = LocalDate.parse(tokens[1].trim(), formatter);
                        addUserNotification(new Notification(tokens[0],date,tokens[2]));
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                        System.out.println("Invalid date format for notification: " + tokens[1]);
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
     * Adds a new notification to the list.
     *
     * @param notification the notification to add
     */
    public void addUserNotification(Notification notification) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        if (notifications != null) {
            notifications.add(notification);
        }
    }

    /**
     * Saves all notifications to the file.
     */
    public void saveNotificationToFile() {
        try {
            OutputStream out = activity.openFileOutput(file, Context.MODE_PRIVATE);
            for (Notification x : notifications) {
                String notificationData = x.getTitle() + "," + x.getDate() + "," + x.getOwner() + "\n";
                out.write(notificationData.getBytes(StandardCharsets.UTF_8));
            }
            out.close();
            Log.i(TAG, "notification data saved successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to save notification data.");
            e.printStackTrace();
        }
    }

    /**
     * Removes a notification from the list.
     *
     * @param notification the notification to remove
     * @return {@code true} if the notification was successfully removed, {@code false} otherwise
     */
    public boolean removeNotification(Notification notification) {
        return notifications.remove(notification); // Remove from the ArrayList
    }
}
