package com.yonastedela63.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.yonastedela63.mylibrary.MyDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityCurrentReadingBookForFavAndEnd extends AppCompatActivity implements TopBar.OnChangedListenerAll {
    private ImageView imageMakFav;
    private TextView txtMakeFavAndFinished;
    private Switch switchFinished, switchMakFav;
    private Fragment fragment;
    private SharedPreferences sharedPref;
    private ConstraintLayout txtReadEnd;
    private String book_id;
    private Button back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_current_reading_book_for_fav_and_end);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        imageMakFav = findViewById(R.id.imageMakFav);
        txtMakeFavAndFinished = findViewById(R.id.txtMakeFavAndFinished);
        switchFinished = findViewById(R.id.switchFinished);
        switchMakFav = findViewById(R.id.switchMakFav);
        txtReadEnd = findViewById(R.id.txtReadEnd);
        back =findViewById(R.id.back);

        Intent intent = getIntent();
        String photoUrl = null;
        if (intent != null) {
            photoUrl = intent.getStringExtra("photoUrl");
            book_id = intent.getStringExtra("book_id");
        }
        Bitmap image = BitmapFactory.decodeFile(photoUrl);
        imageMakFav.setImageBitmap(image);

        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtMakeFavAndFinished.setTypeface(typeface, Typeface.BOLD);
        switchFinished.setTypeface(typeface);
        switchMakFav.setTypeface(typeface);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtMakeFavAndFinished.setTextSize(fontSize);

        //set color
        int colorResId = getResources().getIdentifier(sharedPref.getString("colorBg", "btn_light_green"), "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switchFinished.setTrackDrawable(getResources().getDrawable(R.drawable.switch_track));
            switchFinished.setThumbDrawable(getResources().getDrawable(R.drawable.switch_thumb));
            switchMakFav.setTrackDrawable(getResources().getDrawable(R.drawable.switch_track));
            switchMakFav.setThumbDrawable(getResources().getDrawable(R.drawable.switch_thumb));
        }

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            txtReadEnd.setBackgroundColor(Color.BLACK);
            switchMakFav.setTextColor(Color.WHITE);
            switchFinished.setTextColor(Color.WHITE);
            txtMakeFavAndFinished.setTextColor(Color.WHITE);
            txtMakeFavAndFinished.setBackgroundColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            txtReadEnd.setBackgroundColor(Color.WHITE);
            switchMakFav.setTextColor(Color.BLACK);
            switchFinished.setTextColor(Color.BLACK);
            txtMakeFavAndFinished.setTextColor(Color.BLACK);
            txtMakeFavAndFinished.setBackgroundColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityCurrentReadingBookForFavAndEnd.this, MainActivityCurrentReading.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*Checked listener */
        switchFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("Range")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Ensure book_id is not null before parsing
                    if (book_id != null) {
                        MyDataSource md = new MyDataSource(MainActivityCurrentReadingBookForFavAndEnd.this);
                        // Get the current date and time
                        Date currentDate = new Date();
                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                        String formattedDate = dateFormat.format(currentDate);
                        md.open();
                        Cursor cursor = md.getAreadyOnly();
                        boolean flag = false;
                        if (cursor != null) {
                            try {
                                while (cursor.moveToNext()) {
                                    // Retrieve data from the cursor
                                    String currID = cursor.getString(cursor.getColumnIndex("book_id"));
                                    if (currID != null && currID.equals(book_id)) {
                                        flag = true;
                                        break;
                                    }
                                }
                            } finally {
                                cursor.close(); // Close the cursor in a finally block
                            }
                        }

                        if (!flag) {
                            md.insertDataAready(Integer.parseInt(book_id), formattedDate);
                        }

                        md.close();
                    } else {
                        // Handle case where book_id is null
                        Toast.makeText(MainActivityCurrentReadingBookForFavAndEnd.this, "Book ID is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // TODO: remove from database
                }
            }
        });


        switchMakFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("Range")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Ensure book_id is not null before parsing
                    if (book_id != null) {
                        MyDataSource md = new MyDataSource(MainActivityCurrentReadingBookForFavAndEnd.this);
                        md.open();
                        Cursor cursor = md.getFavOnly();
                        boolean flag = false;
                        if (cursor != null) {
                            try {
                                while (cursor.moveToNext()) {
                                    // Retrieve data from the cursor
                                    String currID = cursor.getString(cursor.getColumnIndex("book_id"));
                                    if (currID != null && currID.equals(book_id)) {
                                        flag = true;
                                        break;
                                    }
                                }
                            } finally {
                                cursor.close(); // Close the cursor in a finally block
                            }
                        }
                        if (!flag) {
                            md.insertDataFav(Integer.parseInt(book_id));
                        }
                        md.close();
                    } else {
                        // Handle case where book_id is null
                        Toast.makeText(MainActivityCurrentReadingBookForFavAndEnd.this, "Book ID is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        txtMakeFavAndFinished.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtMakeFavAndFinished.setTypeface(typeface, Typeface.BOLD);
        switchFinished.setTypeface(typeface);
        switchMakFav.setTypeface(typeface);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            txtReadEnd.setBackgroundColor(Color.BLACK);
            switchMakFav.setTextColor(Color.WHITE);
            switchFinished.setTextColor(Color.WHITE);
            txtMakeFavAndFinished.setTextColor(Color.WHITE);
            txtMakeFavAndFinished.setBackgroundColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            txtReadEnd.setBackgroundColor(Color.WHITE);
            switchMakFav.setTextColor(Color.BLACK);
            switchFinished.setTextColor(Color.BLACK);
            txtMakeFavAndFinished.setTextColor(Color.BLACK);
            txtMakeFavAndFinished.setBackgroundColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

    }

    @Override
    public void onColorChanged(String color) {
        //set color
        int colorResId = getResources().getIdentifier(color, "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switchFinished.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            switchMakFav.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        }
    }
}