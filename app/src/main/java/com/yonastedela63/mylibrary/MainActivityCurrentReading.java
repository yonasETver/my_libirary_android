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

import java.util.ArrayList;
import com.yonastedela63.mylibrary.MyDataSource;

public class MainActivityCurrentReading extends AppCompatActivity implements TopBar.OnChangedListenerAll {
    private RecyclerView bookDitailParentCurrent;
    private View view;
    private Fragment fragment;
    private String imgPath;
    private String start_date;
    private String book_id;
    private RelativeLayout activityCurrent;
    private SharedPreferences sharedPref;
    private TextView txtTitleCurrent;
    private Button back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_current_reading);
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        bookDitailParentCurrent = findViewById(R.id.bookDitailParentCurrent);
        txtTitleCurrent = findViewById(R.id.txtTitleCurrent);
        back = findViewById(R.id.back);

        view = findViewById(R.id.txtTitleCurrent);
        view.bringToFront();
        activityCurrent = findViewById(R.id.activityCurrent);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitleCurrent.setTextSize(fontSize);

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            activityCurrent.setBackgroundColor(Color.BLACK);
            txtTitleCurrent.setBackgroundColor(Color.BLACK);
            txtTitleCurrent.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            activityCurrent.setBackgroundColor(Color.WHITE);
            txtTitleCurrent.setBackgroundColor(Color.WHITE);
            txtTitleCurrent.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleCurrent.setTypeface(typeface, Typeface.BOLD);

        readData("","","");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityCurrentReading     .this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("Range")
    public void readData(String set, String nightMode, String color){
        ArrayList<MainActivityCurrentBook> bookDetailsCurrent = new ArrayList<>();
        MyDataSource dataSource = new MyDataSource(this);
        dataSource.open();
        Cursor cursor = dataSource.getCurrentData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    book_id = cursor.getString(cursor.getColumnIndex("book_id"));
                    imgPath = cursor.getString(cursor.getColumnIndex("image_path"));
                    start_date = cursor.getString(cursor.getColumnIndex("start_date"));
                    bookDetailsCurrent.add(new MainActivityCurrentBook(imgPath, "Started: " + start_date, book_id));
                    MainActivityCurrentReadingAdapter adapter = new MainActivityCurrentReadingAdapter(this);
                    adapter.setBookDetailsCurrent(bookDetailsCurrent);
                    if(set.equals("night")){
                        adapter.setNightMode(nightMode);
                    }else if(set.equals("color")){
                        adapter.setChange(color);
                    }
                    bookDitailParentCurrent.setAdapter(adapter);
                    bookDitailParentCurrent.setLayoutManager(new GridLayoutManager(this, 3));
                }
            } finally {
                cursor.close(); // Close the cursor in a finally block
            }
        }

        dataSource.close();
    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        txtTitleCurrent.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleCurrent.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            activityCurrent.setBackgroundColor(Color.BLACK);
            txtTitleCurrent.setBackgroundColor(Color.BLACK);
            txtTitleCurrent.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            activityCurrent.setBackgroundColor(Color.WHITE);
            txtTitleCurrent.setBackgroundColor(Color.WHITE);
            txtTitleCurrent.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }
        readData("night", nightMode, "");
    }

    @Override
    public void onColorChanged(String color) {
        readData("color", "", color);
    }
}