package com.yonastedela63.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import com.yonastedela63.mylibrary.MyDataSource;

public class MainActivityAllBook extends AppCompatActivity implements TopBar.OnChangedListenerAll{

    private RecyclerView bookDitailParent;
    private View view;
    private Fragment fragment;
    private SharedPreferences sharedPref;
    private String bookID;
    private String bookTitle;
    private String bookAuthor;
    private String bookDescription;
    private String bookPublisher;
    private String bookPublisherYear;
    private String bookPageNumber;
    private String imagePath;
    private TextView txtTitle;
    private RelativeLayout allBook;
    private Button back;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_all_book_main);

        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        ArrayList <BookDetail> bookDetails = new ArrayList<>();
        bookDitailParent = findViewById(R.id.bookDitailParent);
        view = findViewById(R.id.txtTitle);
        view.bringToFront();

        txtTitle = findViewById(R.id.txtTitle);
        allBook = findViewById(R.id.allBook);
        back = findViewById(R.id.back);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitle.setTextSize(fontSize);

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            allBook.setBackgroundColor(Color.BLACK);
            txtTitle.setBackgroundColor(Color.BLACK);
            txtTitle.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            allBook.setBackgroundColor(Color.WHITE);
            txtTitle.setBackgroundColor(Color.WHITE);
            txtTitle.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }


        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitle.setTypeface(typeface, Typeface.BOLD);


        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.cover_photo);

        readData("", "", "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityAllBook.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressLint("Range")
    public void readData(String set, String nightMode, String color){
        ArrayList <BookDetail> bookDetails = new ArrayList<>();
        MyDataSource dataSource = new  MyDataSource(MainActivityAllBook.this);
        dataSource.open();

        Cursor cursor = dataSource.getAllData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    bookID = cursor.getString(cursor.getColumnIndex("book_id"));
                    bookTitle = cursor.getString(cursor.getColumnIndex("book_title"));
                    bookAuthor = cursor.getString(cursor.getColumnIndex("book_author"));
                    bookDescription = cursor.getString(cursor.getColumnIndex("book_description"));
                    bookPublisher = cursor.getString(cursor.getColumnIndex("book_publisher"));
                    bookPublisherYear = cursor.getString(cursor.getColumnIndex("book_publisher_year"));
                    bookPageNumber = cursor.getString(cursor.getColumnIndex("book_page_number"));
                    imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
                    bookDetails.add(new BookDetail(bookID, imagePath, bookTitle, bookAuthor, bookPageNumber, bookDescription, bookPublisherYear, bookPublisher));
                    BookDetailAdapter adapter = new BookDetailAdapter(this);
                    adapter.setBookDetails(bookDetails);
                    if(set.equals("night")){
                        adapter.setNightMode(nightMode);
                    }else if (set.equals("color")){
                        adapter.setChange(color);
                    }
                    bookDitailParent.setAdapter(adapter);
                    bookDitailParent.setLayoutManager(new GridLayoutManager(this, 3));
                }

            } finally {
                cursor.close(); // Close the cursor in a finally block
            }
        }

        dataSource.close();
    }

    @Override
    public void onFontSizeChenger(int fontSize) {

        txtTitle.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitle.setTypeface(typeface, Typeface.BOLD);

    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            allBook.setBackgroundColor(Color.BLACK);
            txtTitle.setBackgroundColor(Color.BLACK);
            txtTitle.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            allBook.setBackgroundColor(Color.WHITE);
            txtTitle.setBackgroundColor(Color.WHITE);
            txtTitle.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitle.setTextSize(fontSize);

         readData("night", nightMode, "");
    }

    @Override
    public void onColorChanged(String color) {
        readData("color", "", color);
    }
}