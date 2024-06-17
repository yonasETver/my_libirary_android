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
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityAbout extends AppCompatActivity implements TopBar.OnChangedListenerAll{


    private Fragment fragment;
    private TextView topTitle, appName,textView3,textView4, textView5,textView7, bookRight;
    private SharedPreferences sharedPref;
    private ConstraintLayout mainActivityAbout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_about);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        topTitle = findViewById(R.id.topTitle);
        appName = findViewById(R.id.appName);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView7 = findViewById(R.id.textView7);
        bookRight = findViewById(R.id.bookRight);
        mainActivityAbout = findViewById(R.id.mainActivityAbout);


        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        topTitle.setTypeface(typeface, Typeface.BOLD);
        appName.setTypeface(typeface, Typeface.BOLD);
        textView3.setTypeface(typeface , Typeface.BOLD);
        textView4.setTypeface(typeface , Typeface.BOLD);
        textView5.setTypeface(typeface, Typeface.BOLD);
        textView7.setTypeface(typeface, Typeface.BOLD);
        bookRight.setTypeface(typeface, Typeface.BOLD);


        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            mainActivityAbout.setBackgroundColor(Color.BLACK);
            topTitle.setTextColor(Color.WHITE);
            appName.setTextColor(Color.WHITE);
            textView3.setTextColor(Color.WHITE);
            textView4.setTextColor(Color.WHITE);
            textView5.setTextColor(Color.WHITE);
            textView7.setTextColor(Color.WHITE);
            bookRight.setTextColor(Color.WHITE);
        }else{
            mainActivityAbout.setBackgroundColor(Color.WHITE);
            topTitle.setTextColor(Color.BLACK);
            appName.setTextColor(Color.BLACK);
            textView3.setTextColor(Color.BLACK);
            textView4.setTextColor(Color.BLACK);
            textView5.setTextColor(Color.BLACK);
            textView7.setTextColor(Color.BLACK);
            bookRight.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onFontSizeChenger(int fontSize) {

    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        topTitle.setTypeface(typeface, Typeface.BOLD);
        appName.setTypeface(typeface, Typeface.BOLD);
        textView3.setTypeface(typeface , Typeface.BOLD);
        textView4.setTypeface(typeface , Typeface.BOLD);
        textView5.setTypeface(typeface, Typeface.BOLD);
        textView7.setTypeface(typeface, Typeface.BOLD);
        bookRight.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            mainActivityAbout.setBackgroundColor(Color.BLACK);
            topTitle.setTextColor(Color.WHITE);
            appName.setTextColor(Color.WHITE);
            textView3.setTextColor(Color.WHITE);
            textView4.setTextColor(Color.WHITE);
            textView5.setTextColor(Color.WHITE);
            textView7.setTextColor(Color.WHITE);
            bookRight.setTextColor(Color.WHITE);
        }else{
            mainActivityAbout.setBackgroundColor(Color.WHITE);
            topTitle.setTextColor(Color.BLACK);
            appName.setTextColor(Color.BLACK);
            textView3.setTextColor(Color.BLACK);
            textView4.setTextColor(Color.BLACK);
            textView5.setTextColor(Color.BLACK);
            textView7.setTextColor(Color.BLACK);
            bookRight.setTextColor(Color.BLACK);
        }

    }

    @Override
    public void onColorChanged(String color) {

    }
}