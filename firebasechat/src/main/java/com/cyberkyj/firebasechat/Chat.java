package com.cyberkyj.firebasechat;

public class Chat {
    String userName;
    String contents;

    public Chat(){

    }

    public Chat(String userName, String contents) {
        this.userName = userName;
        this.contents = contents;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
