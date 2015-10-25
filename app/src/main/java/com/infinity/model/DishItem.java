package com.infinity.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DishItem implements Serializable {
    private int id;
    private String name;
    private String introduce;
    private String image;
    private String instruction;
    private int aop;
    private ArrayList<MaterialItem> materials;

    public DishItem(int id, String name, String image, String introduce) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.introduce = introduce;
    }

    public DishItem(int id, String name, String introduce, String image, String instruction, int aop, ArrayList<MaterialItem> materials) {
        this.id = id;
        this.name = name;
        this.introduce = introduce;
        this.image = image;
        this.instruction = instruction;
        this.aop = aop;
        this.materials = materials;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getAop() {
        return aop;
    }

    public void setAop(int aop) {
        this.aop = aop;
    }

    public ArrayList<MaterialItem> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<MaterialItem> materials) {
        this.materials = materials;
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


}