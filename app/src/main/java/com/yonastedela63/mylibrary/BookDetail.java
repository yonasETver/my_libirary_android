package com.yonastedela63.mylibrary;

import android.graphics.Bitmap;

public class BookDetail {
    private String photo_url;
    private String title;
    private String author;
    private String pageNumber;
    private String aboutBook;
    private String yearPeblished;
    private String Publisher;
    private String bookID;

    public BookDetail(String bookID, String photo_url, String title, String author, String pageNumber, String aboutBook, String yearPeblished, String publisher) {
        this.photo_url = photo_url;
        this.title = title;
        this.author = author;
        this.pageNumber = pageNumber;
        this.aboutBook = aboutBook;
        this.yearPeblished = yearPeblished;
        this.Publisher = publisher;
        this.bookID = bookID;

    }

      /*
      Setter method
      * */
    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public void setTitle(String title) { this.title = title;}

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setAboutBook(String aboutBook) {
        this.aboutBook = aboutBook;
    }

    public void setYearPeblished(String yearPeblished) {
        this.yearPeblished = yearPeblished;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }
    public void setBookID(String bookID) { this.bookID = bookID;}

    /*
    *   Getter methods
    * */

    public String getPhoto_url() {
        return photo_url;
    }

    public String getTitle() {return title;}

    public String getAuthor() {
        return author;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public String getAboutBook() {
        return aboutBook;
    }

    public String getYearPeblished() {
        return yearPeblished;
    }

    public String getPublisher() {
        return Publisher;
    }

    public String getBookID() { return bookID;}
}
