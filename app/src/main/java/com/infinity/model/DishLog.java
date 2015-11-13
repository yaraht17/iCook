package com.infinity.model;

public class DishLog {
    private int id;
    private String name;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DishLog(String name, String date) {

        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishLog(int id, String name, String date) {


        this.id = id;
        this.name = name;
        this.date = date;
    }
}
