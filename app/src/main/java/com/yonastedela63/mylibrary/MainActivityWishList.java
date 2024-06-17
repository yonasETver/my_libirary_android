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

public class MainActivityWishList extends AppCompatActivity implements TopBar.OnChangedListenerAll {

    private RecyclerView bookDitailParentWish;
    private View view;
    private Fragment fragment;
    private SharedPreferences sharedPref;
    private TextView txtTitleWish;
    private RelativeLayout mainActivityWish;
    private String imgPath;
    private String start_date;
    private Button back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wish_list);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        bookDitailParentWish = findViewById(R.id.bookDitailParentWish);
        txtTitleWish = findViewById(R.id.txtTitleWish);
        mainActivityWish = findViewById(R.id.mainActivityWish);
        back = findViewById(R.id.back);

        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitleWish.setTextSize(fontSize);

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            mainActivityWish.setBackgroundColor(Color.BLACK);
            txtTitleWish.setBackgroundColor(Color.BLACK);
            txtTitleWish.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            mainActivityWish.setBackgroundColor(Color.WHITE);
            txtTitleWish.setBackgroundColor(Color.WHITE);
            txtTitleWish.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleWish.setTypeface(typeface, Typeface.BOLD);

        view = findViewById(R.id.txtTitleWish);
        view.bringToFront();

        view.bringToFront();

        readData("","","");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityWishList.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("Range")
    public void readData(String set, String nightMode, String color){
        ArrayList<MainActivityWhishListBook> bookDetailsWish = new ArrayList<>();
        MyDataSource dataSource = new MyDataSource(this);
        dataSource.open();
        Cursor cursor = dataSource.getWishData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    imgPath = cursor.getString(cursor.getColumnIndex("image_path"));
                    start_date = cursor.getString(cursor.getColumnIndex("start_date"));
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    bookDetailsWish.add(new MainActivityWhishListBook(imgPath, "Wish to start: " + start_date));
                    MainActivityWishListAdapter adapter = new MainActivityWishListAdapter(this);
                    adapter.setBookDetailsWish(bookDetailsWish);
                    if(set.equals("night")) {
                        adapter.setNightMode(nightMode);
                    }else if(set.equals("color")) {
                        adapter.setChange(color);
                    }
                    bookDitailParentWish.setAdapter(adapter);
                    bookDitailParentWish.setLayoutManager(new GridLayoutManager(this, 3));
                }
            } finally {
                cursor.close(); // Close the cursor in a finally block
            }
        }

        dataSource.close();
    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        txtTitleWish.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleWish.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            mainActivityWish.setBackgroundColor(Color.BLACK);
            txtTitleWish.setBackgroundColor(Color.BLACK);
            txtTitleWish.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            mainActivityWish.setBackgroundColor(Color.WHITE);
            txtTitleWish.setBackgroundColor(Color.WHITE);
            txtTitleWish.setTextColor(Color.BLACK);
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