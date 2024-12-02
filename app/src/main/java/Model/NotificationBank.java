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

public class NotificationBank {

    public ArrayList<Notification> notifications;
    private final Activity activity;
    private static final String file = "notification-list.csv";
    private static final String TAG = "NotificationBank";

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }
    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public NotificationBank(Activity activity){
        this.activity = activity;
        notifications = new ArrayList<Notification>();
    }
    public Notification findNotificationByOwner(String userID){
        for (Notification x : notifications){
            if(x.getOwner().equals(userID)){
                return x;
            }
        }
        return null;
    }

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

    public void addUserNotification(Notification notification) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        if (notifications != null) {
            notifications.add(notification);
        }
    }

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

    public boolean removeNotification(Notification notification) {
        return notifications.remove(notification); // Remove from the ArrayList
    }
}
