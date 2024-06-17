package com.yonastedela63.mylibrary;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.yonastedela63.mylibrary.Alarm;
import com.yonastedela63.mylibrary.AlertAdapter;
import com.yonastedela63.mylibrary.MyDataSource;
import com.yonastedela63.mylibrary.R;
import com.yonastedela63.mylibrary.TopBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class alertClass extends AppCompatActivity implements TopBar.OnChangedListenerAll {

    private RecyclerView allertClass;
    private Fragment fragment;
    private String book_title;
    private long differenceInDays;
    private RelativeLayout alermActivity;
    private SharedPreferences sharedPref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_class);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();



        // Instantiate RecyclerView
        allertClass = findViewById(R.id.allertClass);
        alermActivity = findViewById(R.id.alermActivity);

        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            alermActivity.setBackgroundColor(BLACK);
        }else{
            alermActivity.setBackgroundColor(WHITE);
        }


        getAlermMsg("", "");
    }

    @SuppressLint("Range")
    public void getAlermMsg(String set, String nightMode){
        ArrayList<Alarm> alertMsg = new ArrayList<>();
        MyDataSource dataSource = new MyDataSource(this);
        dataSource.open();

        Cursor cursor = dataSource.getAllData();
        Cursor cursor2 = dataSource.getCurrentData();

        try {
            if (cursor != null && cursor2 != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String bookID = cursor.getString(cursor.getColumnIndex("book_id"));
                    boolean found = false;
                    cursor2.moveToPosition(-1);
                    while (cursor2.moveToNext()) {
                        @SuppressLint("Range") String bookIDCurrent = cursor2.getString(cursor2.getColumnIndex("book_id"));
                        if (bookID.equals(bookIDCurrent)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        @SuppressLint("Range") String start_date = cursor.getString(cursor.getColumnIndex("start_date"));
                        book_title = cursor.getString(cursor.getColumnIndex("book_title"));
                        Date currentDate = new Date();
                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                        try {
                            // Parse start date
                            Date startDate = dateFormat.parse(start_date);

                            // Calculate the difference in milliseconds between the two dates
                            long differenceInMillis = currentDate.getTime() - startDate.getTime();

                            // Convert the difference to days
                            differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);
                            long hours = TimeUnit.MILLISECONDS.toHours(differenceInMillis);
                            long minute = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis);
                            if(differenceInDays > 0){
                                alertMsg.add(new Alarm("Please start reading the \"" + book_title + "\" book, as it has been on your reading list for " + differenceInDays + " days!"));
                            }else if(hours > 0 && differenceInDays == 0){
                                alertMsg.add(new Alarm("Please start reading the \"" + book_title + "\" book, as it has been on your reading list for " + hours + " hours!"));
                            }else {
                                alertMsg.add(new Alarm("Please start reading the \"" + book_title + "\" book, as it has been on your reading list for " + minute + " minutes!"));
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                            allertClass.setLayoutManager(layoutManager);
                            AlertAdapter adapter = new AlertAdapter(this);
                            adapter.setAlermMsg(alertMsg);
                            if(set.equals("night")){
                                adapter.setNightMode(nightMode);
                            }
                            allertClass.setAdapter(adapter);
                        } catch (ParseException e) {
                            // Handle parsing exception
                            e.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        }


        dataSource.close();
    }

    @Override
    public void onFontSizeChenger(int fontSize) {

    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {

    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            alermActivity.setBackgroundColor(BLACK);
        }else{
            alermActivity.setBackgroundColor(WHITE);
        }
        getAlermMsg("night", nightMode);

    }

    @Override
    public void onColorChanged(String color) {

    }
}
