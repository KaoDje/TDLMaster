package com.example.tdlmaster;

import android.content.Context;

import java.util.Date;

public class Task {
    private int id; // Identifiant de la Task dans la db
    private String title;
    private String description;
    private Date createdAt;
    private Date completedAt;
    private boolean isCompleted;

    // Constructors

    public Task() {}

    public Task(String title) {
        this.title = title;
        this.description = "";
        this.createdAt = new Date();
        this.completedAt = null;
        this.isCompleted = false;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdAt = new Date();
        this.completedAt = null;
        this.isCompleted = false;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public void setIsCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }

    public void completeTask(Context context) {
        this.isCompleted = true;
        this.completedAt = new Date();
        DatabaseHelper db = new DatabaseHelper(context);
        db.completeTask(this.id);
    }

    public void undoCompletion(Context context) {
        this.isCompleted = false;
        this.completedAt = null;
        DatabaseHelper db = new DatabaseHelper(context);
        db.undoCompletion(this.id);
    }

}
