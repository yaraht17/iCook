package com.infinity.data;

import com.infinity.icook.R;

public class Var {

    public static final String MY_PREFERENCES = "PRE_LOGIN";
    public static final String ACCESS_TOKEN = "token";

    public static final String URL_HOST_DEMO = "http://api.androidhive.info/json/movies.json";
    public static final String URL_HOST = "http://zozy.me";

    //Login
    public static final String LOGIN_ID = "id";

    public static final String API_GET_LIST = URL_HOST + "/icook/cat?id=";
    public static final String API_LOGIN = URL_HOST + "/icook/accounts/login";
    public static final String API_ADD_USER = URL_HOST + "/icook/accounts/info";
    public static final String API_GET_USER = URL_HOST + "/icook/accounts/info/";
    public static final String API_CHAT = URL_HOST + "/icook/chat?mess=";
    public static final String API_SEND_TOKEN = URL_HOST + "/icook/chat...";
    //Dish
    public static final String DISH_ID = "id";
    public static final String DISH_NAME = "name";
    public static final String DISH_INTRODUCE = "introduce";
    public static final String DISH_IMAGE = "image";
    public static final String DISH_INSTRUCTION = "instruction";
    public static final String DISH_AOP = "aop";
    public static final String DISH_MATERIALS = "material";

    //material
    public static final String MATERIAL_ID = "id";
    public static final String MATERIAL_NAME = "name";
    public static final String MATERIAL_AMOUNT = "amount";
    public static final String MATERIAL_UNIT = "unit";

    //User
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_BIRTH = "birthday";
    public static final String USER_SEX = "sex";
    public static final String USER_HEIGHT = "height";
    public static final String USER_WEIGHT = "weight";
    public static final String USER_EMAIL = "email";


    //key truyen extra
    public static final String DISH_EXTRA = "DISH";

    public static final int DRAWABLE_LIST[] = {R.drawable.avatar, R.drawable.avatar1, R.drawable.avatar2,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar3};
    public static final String CLOCK_TIME = "clock";

    public static final String[] nextStep = {"tiếp", "rồi sao nữa", "tiếp theo", "tiếp đi", "tiếp theo là gì", "xong rồi"};

    public static final String[] done = {"thôi", "xong rồi", "tôi biết rồi", "được rồi", "tôi biết làm rồi"};

}
