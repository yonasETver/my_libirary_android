package com.yonastedela63.mylibrary;

import android.graphics.Bitmap;

public class MainActivityAreadyBook {
    private String photo_url;
    private String time;

    public MainActivityAreadyBook(String photo_url, String time) {
        this.photo_url = photo_url;
        this.time = time;
    }

    public void setPhoto_url() {
        this.photo_url = photo_url;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getTime() {
        return time;
    }
}
