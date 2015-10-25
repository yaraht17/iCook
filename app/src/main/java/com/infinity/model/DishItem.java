package com.infinity.model;

import java.io.Serializable;

public class DishItem implements Serializable {
    private int id;
    private String name;
    private String avatar;
    private String description;

    public DishItem(int id, String name, String avatar, String description) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.description = description;
    }

    public DishItem() {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}