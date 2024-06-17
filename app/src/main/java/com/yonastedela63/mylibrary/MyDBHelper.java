package com.yonastedela63.mylibrary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "book_db";
    private static final int DATABASE_VERSION = 1;
    private String sql = "CREATE TABLE IF NOT EXISTS Books (" +
            "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_title TEXT," +
            "book_author TEXT," +
            "book_description TEXT," +
            "book_publisher TEXT," +
            "book_publisher_year INTEGER," +
            "book_page_number INTEGER," +
            "image_path TEXT," +
            "start_date DATE"+
            ")";
    private String sql2 = "CREATE TABLE IF NOT EXISTS Aready_Read (" +
            "c_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_id INTEGER," +
            "end_date DATE," +
            "FOREIGN KEY (book_id) REFERENCES Books(book_id)" +
            ")";
    private String sql3 = "CREATE TABLE IF NOT EXISTS Current_Reading (" +
            "c_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_id INTEGER," +
            "start_date DATE," +
            "FOREIGN KEY (book_id) REFERENCES Books(book_id)" +
            ")";
    private String sql4 = "CREATE TABLE IF NOT EXISTS Wish_list (" +
            "c_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_id INTEGER," +
            "start_date DATE," +
            "FOREIGN KEY (book_id) REFERENCES Books(book_id)" +
            ")";
    private String sql5 = "CREATE TABLE IF NOT EXISTS Fav_book (" +
            "c_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_id INTEGER," +
            "FOREIGN KEY (book_id) REFERENCES Books(book_id)" +
            ")";

    public MyDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO: database upgrade if needed
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    public boolean isBooksTableExists() {
        return isTableExists("Books");
    }
}
