package com.yonastedela63.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityDetailsAboutAllBooks extends AppCompatActivity implements TopBar.OnChangedListenerAll{

    private ImageView imageViewDitail;
    private FloatingActionButton floatingActionButtonSelect;
    private Button  startReading, makewishlist;
    private boolean cliked = false;
    private Fragment fragment;

    private TextView txtTitle,txtDescrip,txtAuthor,txtPublisher,txtPubYear,txtPageNum;
    private String bookID;
    private SharedPreferences sharedPref;
    private LinearLayout linearLayout;
    private RelativeLayout layoutForButton, bookdetail;
    private TextView txtTitleDetail;
    private Button back;
    /*
      Animations
   * */
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_details_about_all_books);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        //find view
        txtTitle = findViewById(R.id.txtTitle);
        txtDescrip =findViewById(R.id.txtDescrip);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtPublisher = findViewById(R.id.txtPublisher);
        txtPubYear = findViewById(R.id.txtPubYear);
        txtPageNum = findViewById(R.id.txtPageNum);
        imageViewDitail = findViewById(R.id.imageViewDitail);

        //button
        floatingActionButtonSelect = findViewById(R.id.floatingActionButtonSelect);
        startReading = findViewById(R.id.startReading);
        makewishlist = findViewById(R.id.makewishlist);
        linearLayout = findViewById(R.id.linearLayout);
        layoutForButton = findViewById(R.id.layoutForButton);
        bookdetail = findViewById(R.id.bookdetail);
        txtTitleDetail = findViewById(R.id.txtTitleDetail);
        back = findViewById(R.id.back);

        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleDetail.setTypeface(typeface, Typeface.BOLD);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        txtTitleDetail.setTextSize(fontSize);

        //set color
        int colorResId = getResources().getIdentifier(sharedPref.getString("colorBg", "btn_light_green"), "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);
        linearLayout.setBackgroundColor(colorValue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutForButton.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            floatingActionButtonSelect.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            startReading.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            makewishlist.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        }

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            bookdetail.setBackgroundColor(Color.BLACK);
            startReading.setTextColor(Color.WHITE);
            makewishlist.setTextColor(Color.WHITE);
            txtTitle.setTextColor(Color.WHITE);
            txtDescrip.setTextColor(Color.WHITE);
            txtAuthor.setTextColor(Color.WHITE);
            txtPublisher.setTextColor(Color.WHITE);
            txtPubYear.setTextColor(Color.WHITE);
            txtPageNum.setTextColor(Color.WHITE);
            txtTitleDetail.setBackgroundColor(Color.BLACK);
            txtTitleDetail.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }

        }else{
            bookdetail.setBackgroundColor(Color.WHITE);
            startReading.setTextColor(Color.BLACK);
            makewishlist.setTextColor(Color.BLACK);
            txtTitle.setTextColor(Color.BLACK);
            txtDescrip.setTextColor(Color.BLACK);
            txtAuthor.setTextColor(Color.BLACK);
            txtPublisher.setTextColor(Color.BLACK);
            txtPubYear.setTextColor(Color.BLACK);
            txtPageNum.setTextColor(Color.BLACK);
            txtTitleDetail.setBackgroundColor(Color.WHITE);
            txtTitleDetail.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        Intent intent = getIntent();
        if (intent != null) {
            String bookTitle = intent.getStringExtra("bookTitle");
            String bookDescription = intent.getStringExtra("bookDescription");
            String bookAuthor = intent.getStringExtra("bookAuthor");
            String bookPublisher = intent.getStringExtra("bookPublisher");
            String PublishedYear = intent.getStringExtra("PublishedYear");
            String pageNumber = intent.getStringExtra("pageNumber");
            String img = intent.getStringExtra("imagePath");
            bookID = intent.getStringExtra("bookID");
            txtTitle.setText("Title:-  "+bookTitle);
            txtDescrip.setText("Description:-  "+bookDescription);
            txtAuthor.setText("Author:-  "+bookAuthor);
            txtPublisher.setText("Publisher:-  "+bookPublisher);
            txtPubYear.setText("Published year:-  "+PublishedYear);
            txtPageNum.setText("Total page:-  "+pageNumber);
            Bitmap bitmap = BitmapFactory.decodeFile(img);
            imageViewDitail.setImageBitmap(bitmap);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivityDetailsAboutAllBooks.this, MainActivityAllBook.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        /*Animation*/
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        /*Expandable floating action button*/
        floatingActionButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivityDetailsAboutAllBooks.this, "Button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        floatingActionButtonSelect.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_MOVE:
                                view.setX(event.getRawX() - 120);
                                view.setY(event.getRawY() - 425);
                                break;
                            case MotionEvent.ACTION_UP:
                                view.setOnTouchListener(null);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                return true;
            }

        });

        /*Expandable floating action button*/
        floatingActionButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }

            private void onAddButtonClicked() {
                setVisibility(cliked );
                setAnimation(cliked );
                setClickable(cliked);
                cliked = !cliked;
            }

            private void setVisibility(boolean cliked ){
                if(!cliked){
                    startReading.setVisibility(View.VISIBLE);
                    makewishlist.setVisibility(View.VISIBLE);
                }else {
                    startReading.setVisibility(View.INVISIBLE);
                    makewishlist.setVisibility(View.INVISIBLE);
                }
            }

            //to ban the button not clicable
            private void setClickable(boolean clickable){
                if(!clickable){
                    startReading.setClickable(true);
                    makewishlist.setClickable(true);
                }else {
                    startReading.setClickable(false);
                    makewishlist.setClickable(false);
                }
            }

            private void setAnimation(boolean cliked ){
                if(!cliked){
                    startReading.startAnimation(fromBottom);
                    makewishlist.startAnimation(fromBottom);
                    floatingActionButtonSelect.setAnimation(rotateOpen);
                }else {
                    startReading.startAnimation(toBottom);
                    makewishlist.startAnimation(toBottom);
                    floatingActionButtonSelect.setAnimation(rotateClose);
                }
            }
        });

        /*
         * Expanden button clicked
         * */
        startReading.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
               //Toast.makeText(MainActivityDetailsAboutAllBooks.this, "Start reading clicked", Toast.LENGTH_SHORT).show();
                // Get the current date and time
                Date currentDate = new Date();
                // Define the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                String formattedDate = dateFormat.format(currentDate);

                MyDataSource ds = new MyDataSource(MainActivityDetailsAboutAllBooks.this);
                ds.open();

                Cursor cursor = ds.getCurrentDataOnly()    ;
                boolean flag = false;
                if (cursor != null) {
                    try {
                        while (cursor.moveToNext()) {
                            // Retrieve data from the cursor
                            String currID = cursor.getString(cursor.getColumnIndex("book_id"));
                            if(currID.equals(bookID)){
                               flag = true;
                               break;
                            }
                        }
                    } finally {
                        cursor.close(); // Close the cursor in a finally block
                    }
                }
                if(!flag) {
                    ds.insertDataCurrent(Integer.parseInt(bookID), formattedDate);
                }
                ds.close();
            }
        });

        makewishlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivityDetailsAboutAllBooks.this, "Make wish list clicked", Toast.LENGTH_SHORT).show();
                MyDataSource ds = new MyDataSource(MainActivityDetailsAboutAllBooks.this);
                ds.open();
                // Get the current date and time
                Date currentDate = new Date();
                // Define the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                String formattedDate = dateFormat.format(currentDate);

                Cursor cursor = ds.getWishOnly();
                boolean flag = false;
                if (cursor != null) {
                    try {
                        while (cursor.moveToNext()) {
                            // Retrieve data from the cursor
                            String currID = cursor.getString(cursor.getColumnIndex("book_id"));
                            if(currID.equals(bookID)){
                                flag = true;
                                break;
                            }
                        }
                    } finally {
                        cursor.close(); // Close the cursor in a finally block
                    }
                }
                if(!flag) {
                    ds.insertDataWish(Integer.parseInt(bookID),formattedDate);
                }

                ds.close();
            }
            });
    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        txtTitleDetail.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        txtTitleDetail.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            bookdetail.setBackgroundColor(Color.BLACK);
            startReading.setTextColor(Color.WHITE);
            makewishlist.setTextColor(Color.WHITE);
            txtTitle.setTextColor(Color.WHITE);
            txtDescrip.setTextColor(Color.WHITE);
            txtAuthor.setTextColor(Color.WHITE);
            txtPublisher.setTextColor(Color.WHITE);
            txtPubYear.setTextColor(Color.WHITE);
            txtPageNum.setTextColor(Color.WHITE);
            txtTitleDetail.setBackgroundColor(Color.BLACK);
            txtTitleDetail.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else{
            bookdetail.setBackgroundColor(Color.WHITE);
            startReading.setTextColor(Color.BLACK);
            makewishlist.setTextColor(Color.BLACK);
            txtTitle.setTextColor(Color.BLACK);
            txtDescrip.setTextColor(Color.BLACK);
            txtAuthor.setTextColor(Color.BLACK);
            txtPublisher.setTextColor(Color.BLACK);
            txtPubYear.setTextColor(Color.BLACK);
            txtPageNum.setTextColor(Color.BLACK);
            txtTitleDetail.setBackgroundColor(Color.WHITE);
            txtTitleDetail.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

    }

    @Override
    public void onColorChanged(String color) {
        int colorResId = getResources().getIdentifier(color, "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);
        linearLayout.setBackgroundColor(colorValue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutForButton.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            floatingActionButtonSelect.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            startReading.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            makewishlist.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        }

    }
}