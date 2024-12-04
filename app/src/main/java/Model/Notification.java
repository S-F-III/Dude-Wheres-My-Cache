package Model;

import java.time.LocalDate;

/**
 * Represents a notification in a budget tracking application.
 * Each notification is associated with a user, has a title and date
 */
public class Notification {

    /**
     * Title of the Notification.
     */
    private String title;

    /**
     * Date the notification is set for.
     */
    private LocalDate date;

    /**
     * Unique identifier for the notification.
     */
    private String owner;

    /**
     * Constructs a fully initialized {@code Notification}.
     *
     * @param title             the title of the notification
     * @param date              the date of the notification
     * @param owner             the owner of the notification
     */
    public Notification(String title, LocalDate date, String owner) {
        this.title = title;
        this.date = date;
        this.owner = owner;
    }

    /**
     * Retrieves the user ID associated with this notification.
     *
     * @return the user ID for this notification
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Updates the user ID associated with this notification.
     *
     * @param owner the new user ID for this notification
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Retrieves the date set for this notification.
     *
     * @return the date set for this notification
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date for this notification.
     *
     * @param date the date for the notification
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves the title of this notification.
     *
     * @return the title of this notification
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this notification.
     *
     * @param title the title for the notification
     */
    public void setTitle(String title) {
        this.title = title;
    }


}
