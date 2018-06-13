package com.czopi.administrador.lifeagenda;

import org.joda.time.LocalDate;

public class Task {
    int id;
    String title;
    LocalDate dueDate;
    LocalDate creationDate;
    String description;
    int priority;

        public Task(int pId, String pTitle, LocalDate pDueDate, LocalDate pCreationDate, int pPriority, String pDescription) {
        id = pId;
        title = pTitle;
        dueDate = pDueDate;
        creationDate = pCreationDate;
        description = pDescription;
        priority = pPriority + 1;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDue () {
        LocalDate today = new LocalDate();
        return dueDate.isBefore(today);
    }

}
