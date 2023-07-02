package com.example.karnekalist;

public class RecyclerAdapterModel {
    String task;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RecyclerAdapterModel(String task, int id) {
        this.task = task;
        this.id = id;
    }
    public RecyclerAdapterModel() {
    }

    public RecyclerAdapterModel(String task) {
        this.task = task;
    }
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String task() {
        return task;
    }
}
