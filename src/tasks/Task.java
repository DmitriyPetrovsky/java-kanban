package tasks;

import enums.Status;
import manager.Manager;


public class Task {
    private String taskName;
    private String info;
    private final int id;
    private Status status;

    public Task(String taskName, String info) {
        this.taskName = taskName;
        this.info = info;
        this.status = Status.NEW;
        this.id = 1000 + Manager.getTaskCounter();
        Manager.increaseTaskCounter();
    }

    public String getTaskName() {
        return taskName;
    }

    public String getInfo() {
        return info;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        String result;
        result = "Type = 'Задача', ID = '" + getId() + "', name = '" + getTaskName() +
                "', info = '" + getInfo() + "', status = '" + getStatus() + "'.";
        return result;
    }

}
