package com.infinity.model;

public class PersonalItem {
    private String like;
    private String dislike;
    private String sick;

    public PersonalItem() {
    }

    public String getLike() {

        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    public PersonalItem(String like, String dislike, String sick) {

        this.like = like;
        this.dislike = dislike;
        this.sick = sick;
    }
}
