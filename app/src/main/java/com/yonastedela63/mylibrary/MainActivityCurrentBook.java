package com.yonastedela63.mylibrary;

import android.graphics.Bitmap;

public class MainActivityCurrentBook {
    private String photo_url;
    private String time;
    private String book_id;

    public MainActivityCurrentBook(String photo_url, String time, String book_id) {
        this.photo_url = photo_url;
        this.time = time;
        this.book_id = book_id;
    }

    public void setPhoto_url() {
        this.photo_url = photo_url;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getTime() {
        return time;
    }

    public String getBook_id() {
        return book_id;
    }
}
