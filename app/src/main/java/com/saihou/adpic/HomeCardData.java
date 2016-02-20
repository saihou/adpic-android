package com.saihou.adpic;

/**
 * Created by saihou on 2/19/16.
 */
public class HomeCardData {
    String username;
    String time;

    public HomeCardData(String username, String time) {
        this.username = username;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }
    public String getTime() {
        return time;
    }
}
