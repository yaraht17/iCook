package com.infinity.clock;

/**
 * Created by Administrator on 11/4/2015.
 */
public class Entity {
    private String time;
    private Boolean state;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Entity(String time, Boolean state) {
        this.time = time;
        this.state = state;
    }
}
