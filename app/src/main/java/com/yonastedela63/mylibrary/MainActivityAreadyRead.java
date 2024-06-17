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

public class MainActivityAreadyRead extends AppCompatActivity implements TopBar.OnChangedListenerAll {

    private RecyclerView bookDitailParentAready;
    private View view;
    private Fragment fragment;
    private String end_date;
    private String imgPath;
    private TextView txtTitleAready;
    private SharedPreferences sharedPref;
    private RelativeLayout mainActivityAready;
    private Button back;

    //    @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aready_read);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();
        bookDitailParentAready = findViewById(R.id.bookDitailParentAready);
        txtTitleAready = findViewById(R.id.txtTitleAready);
        mainActivityAready = findViewById(R.id.mainActivityAready);
        back = findViewById(R.id.back);
        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitleAready.setTextSize(fontSize);

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if (nightMode.equals("ON")) {
            mainActivityAready.setBackgroundColor(Color.BLACK);
            txtTitleAready.setBackgroundColor(Color.BLACK);
            txtTitleAready.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            mainActivityAready.setBackgroundColor(Color.WHITE);
            txtTitleAready.setBackgroundColor(Color.WHITE);
            txtTitleAready.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this, fontResId);
        txtTitleAready.setTypeface(typeface, Typeface.BOLD);

        view = findViewById(R.id.txtTitleAready);
        view.bringToFront();
        readData("", "", "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityAreadyRead.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("Range")
    public void readData(String set, String nightMode, String color) {
        ArrayList<MainActivityAreadyBook> bookDetailsAready = new ArrayList<>();
        MyDataSource dataSource = new MyDataSource(this);
        dataSource.open();
        Cursor cursor = dataSource.getAreadyData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    end_date = cursor.getString(cursor.getColumnIndex("end_date"));
                    imgPath = cursor.getString(cursor.getColumnIndex("image_path"));
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    bookDetailsAready.add(new MainActivityAreadyBook(imgPath, "Finished: " + end_date));
                    MainActivityAreadyReadAdapter adapter = new MainActivityAreadyReadAdapter(this);
                    adapter.setBookDetailsAready(bookDetailsAready);
                    if (set.equals("night")) {
                        adapter.setNightMode(nightMode);
                    } else if (set.equals("color")) {
                        adapter.setChange(color);
                    }
                    bookDitailParentAready.setAdapter(adapter);
                    bookDitailParentAready.setLayoutManager(new GridLayoutManager(this, 3));
                }
            } finally {
                cursor.close(); // Close the cursor in a finally block
            }
        }

        dataSource.close();
    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        txtTitleAready.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this, fontResId);
        txtTitleAready.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if (nightMode.equals("ON")) {
            mainActivityAready.setBackgroundColor(Color.BLACK);
            txtTitleAready.setBackgroundColor(Color.BLACK);
            txtTitleAready.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            mainActivityAready.setBackgroundColor(Color.WHITE);
            txtTitleAready.setBackgroundColor(Color.WHITE);
            txtTitleAready.setTextColor(Color.BLACK);
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