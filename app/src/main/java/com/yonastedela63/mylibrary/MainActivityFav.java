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

public class MainActivityFav extends AppCompatActivity implements TopBar.OnChangedListenerAll{

    private RecyclerView bookDitailParentFav;
    private View view;
    private Fragment fragment;
    private SharedPreferences sharedPref;
    private TextView txtTitleFav;
    private RelativeLayout mainActivityFav;
    private String book_title;
    private String book_author;
    private String book_page_number;
    private String imgPath;
    private Button back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fav);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        bookDitailParentFav = findViewById(R.id.bookDitailParentFav);
        txtTitleFav  = findViewById(R.id.txtTitleFav);
        mainActivityFav = findViewById(R.id.mainActivityFav);
        back = findViewById(R.id.back);

        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitleFav.setTextSize(fontSize);

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            mainActivityFav.setBackgroundColor(Color.BLACK);
            txtTitleFav.setBackgroundColor(Color.BLACK);
            txtTitleFav.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            mainActivityFav.setBackgroundColor(Color.WHITE);
            txtTitleFav.setBackgroundColor(Color.WHITE);
            txtTitleFav.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleFav.setTypeface(typeface, Typeface.BOLD);

        view = findViewById(R.id.txtTitleFav);
        view.bringToFront();

        readData("", "", "");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityFav.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressLint("Range")
    public void readData(String set, String nightMode, String color){
        ArrayList<MainActivityFavBook> bookDetailsFav = new ArrayList<>();
        MyDataSource dataSource = new MyDataSource(this);
        dataSource.open();
        Cursor cursor = dataSource.getFavData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    book_title = cursor.getString(cursor.getColumnIndex("book_title"));
                    book_author = cursor.getString(cursor.getColumnIndex("book_author"));
                    book_page_number = cursor.getString(cursor.getColumnIndex("book_page_number"));
                    imgPath = cursor.getString(cursor.getColumnIndex("image_path"));
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                    bookDetailsFav.add(new MainActivityFavBook(imgPath, book_title + "\n" +  book_author + "\n" +  book_page_number+"P"));
                    MainActivityFavAdapter adapter = new MainActivityFavAdapter(this);
                    adapter.setBookDetailsFav(bookDetailsFav);
                    if(set.equals("night")){
                        adapter.setNightMode(nightMode);
                    }else if(set.equals("color")){
                        adapter.setChange(color);
                    }
                    bookDitailParentFav.setAdapter(adapter);
                    bookDitailParentFav.setLayoutManager(new GridLayoutManager(this, 3));
                }
            } finally {
                cursor.close(); // Close the cursor in a finally block
            }
        }

        dataSource.close();
    }
    @Override
    public void onFontSizeChenger(int fontSize) {
        txtTitleFav.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleFav.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            mainActivityFav.setBackgroundColor(Color.BLACK);
            txtTitleFav.setBackgroundColor(Color.BLACK);
            txtTitleFav.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            mainActivityFav.setBackgroundColor(Color.WHITE);
            txtTitleFav.setBackgroundColor(Color.WHITE);
            txtTitleFav.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }
        readData("night",nightMode, "");
    }

    @Override
    public void onColorChanged(String color) {
        readData("color", "", color);
    }
}