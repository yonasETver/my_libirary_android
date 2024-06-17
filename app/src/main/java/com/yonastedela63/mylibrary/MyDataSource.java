package com.yonastedela63.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDataSource {
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;

    public MyDataSource(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //insert to the table of all book
    public void insertData(String book_title, String book_author,
                           String book_description, String book_publisher,
                           int book_publisher_year, int book_page_number,
                           String image_path, String start_date) {
        ContentValues values = new ContentValues();
        values.put("book_title", book_title);
        values.put("book_author", book_author);
        values.put("book_description", book_description);
        values.put("book_publisher", book_publisher);
        values.put("book_publisher_year", book_publisher_year);
        values.put("book_page_number", book_page_number);
        values.put("image_path", image_path);
        values.put("start_date ", start_date);
        database.insert("Books", null, values);
    }

    //insertion of aready read
    public void insertDataAready(int book_id, String end_date ){
        ContentValues values = new ContentValues();
        values.put("book_id",book_id);
        values.put("end_date", String.valueOf(end_date));
        database.insert("Aready_Read", null, values);
    }

    //insertion of current reading
    public void insertDataCurrent(int book_id, String start_date ){
        ContentValues values = new ContentValues();
        values.put("book_id",book_id);
        values.put("start_date", String.valueOf(start_date));
        database.insert("Current_Reading", null, values);
    }

    //insertion of wish list
    public void insertDataWish(int book_id, String start_date ){
        ContentValues values = new ContentValues();
        values.put("book_id",book_id);
        values.put("start_date", String.valueOf(start_date));
        database.insert("Wish_list", null, values);
    }

    //insertion of fav book
    public void insertDataFav(int book_id){
        ContentValues values = new ContentValues();
        values.put("book_id",book_id);
        database.insert("Fav_book", null, values);
    }


    //read from table
    public Cursor getAllData() {
        return database.rawQuery("SELECT * FROM Books", null);
    }

    public Cursor getAreadyData() {
        String query = "SELECT Books.*, Aready_Read.* " +
                "FROM Books " +
                "JOIN Aready_Read ON Books.book_id = Aready_Read.book_id";

        return database.rawQuery(query, null);
    }

    public Cursor getAreadyOnly(){
        String query = "SELECT * FROM Aready_Read";

        return database.rawQuery(query, null);
    }

    public Cursor getFavData() {
        String query = "SELECT Books.*, Fav_book.* " +
                "FROM Books " +
                "JOIN Fav_book ON Books.book_id = Fav_book.book_id";

        return database.rawQuery(query, null);
    }

    public Cursor getFavOnly(){
        String query = "SELECT * FROM Fav_book";

        return database.rawQuery(query, null);
    }


    public Cursor getCurrentData() {
        String query = "SELECT Books.*, Current_Reading.* " +
                "FROM Books " +
                "JOIN Current_Reading ON Books.book_id = Current_Reading.book_id";

        return database.rawQuery(query, null);

    }

    public Cursor getCurrentDataOnly(){
        String query = "SELECT * FROM Current_Reading";

        return database.rawQuery(query, null);
    }

    public Cursor getWishData() {
        String query = "SELECT Books.*, Wish_list.* " +
                "FROM Books" +
                "JOIN Wish_list ON Books.book_id = Wish_list.book_id";

        return database.rawQuery(query, null);

    }

    public Cursor getWishOnly(){
        String query = "SELECT * FROM Wish_list";

        return database.rawQuery(query, null);
    }
}
