package com.yonastedela63.mylibrary;

import android.graphics.Bitmap;

public class MainActivityFavBook {
    private String photo_url;
    private String aboutBook;


    public MainActivityFavBook(String photo_url, String aboutBook) {

        this.photo_url = photo_url;
        this.aboutBook = aboutBook;
    }

    public void setPhoto_url() {

        this.photo_url = photo_url;
    }


    public String getPhoto_url() {
        return photo_url;
    }

    public String getAboutBook() {
        return aboutBook;
    }

    public void setAboutBook(String aboutBook) {
        this.aboutBook = aboutBook;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
