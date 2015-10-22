package com.infinity.model;

public class DrawerItem {

    private String itemName;
    private int imgResID;
    private String nameUser;
    private String statusUser;

    public DrawerItem(String itemName, int imgResID) {
        this.itemName = itemName;
        this.imgResID = imgResID;
    }

    public DrawerItem(String nameUser, String statusUser, int imgResID) {
        this.nameUser = nameUser;
        this.statusUser = statusUser;
        this.imgResID = imgResID;

    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        itemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(String statusUser) {
        this.statusUser = statusUser;
    }
}
