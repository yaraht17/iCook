package com.infinity.data;

import com.infinity.model.DishItem;
import com.infinity.model.UserItem;

import java.util.ArrayList;

public class Data {

    public static ArrayList<DishItem>[] dishCache = new ArrayList[6];
    public static ArrayList<UserItem> usersCache;

    public static ArrayList<DishItem> recomendDish;

    public static ArrayList<String> stepInstruction;
}
