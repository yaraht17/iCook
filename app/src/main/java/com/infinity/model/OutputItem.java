package com.infinity.model;

public class OutputItem {
    private String talk;
    private String mess;

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public OutputItem(String talk, String mess) {
        this.talk = talk;
        this.mess = mess;
    }

    public OutputItem(String result) {
        this.talk = result;
        this.mess = result;
    }
}
