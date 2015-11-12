package com.infinity.model;


public class ChatMessage {
    public long id;
    public boolean left;
    public String message;
    public String img;

    public ChatMessage(long id, boolean left, String message) {
        super();
        this.id = id;
        this.left = left;
        this.message = message;
    }

    public ChatMessage(boolean left, String message, String img) {
        super();
        this.left = left;
        this.message = message;
        this.img = img;
    }
}
