package com.infinity.model;

public class UserItem {
    private int id;
    private String name;
    private String birthdate;
    private double height;
    private double weight;
    private int sex;

    public UserItem(String name, String birthdate, double height, double weight, int sex) {
        this.name = name;
        this.birthdate = birthdate;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
    }

    public UserItem(int id, String name, String birthdate, double height, double weight, int sex) {

        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
