package Model;

import java.time.LocalDate;

public class Notification {

    private String title;
    private LocalDate date;
    private String owner;

    public Notification(String title, LocalDate date, String owner) {
        this.title = title;
        this.date = date;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
