package com.yonastedela63.mylibrary;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.yonastedela63.mylibrary.MyDBHelper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements TopBar.OnChangedListenerAll {
    private ImageView logoId;
    private TextView txtAppName;
    private Button btnAllBook, btnCurrentBook, btnAreadyBook, btnWishlist, btnFav, btnAbout;
    Intent intent;
    private FloatingActionButton floatingActionButtonAddBook , floatingActionButtonCamera, floatingActionButtonGallery;
    private boolean cliked = false;
    private Fragment fragment;
    private SharedPreferences sharedPref;
    private ConstraintLayout mainActivity;
    /*
       Animations
    * */
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    public MainActivity() {
        // Default constructor
    }

   // @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBHelper dbHelper = new MyDBHelper(this);

        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        mainActivity = findViewById(R.id.mainActivity);

        logoId = findViewById(R.id.logoId);
        btnAllBook = findViewById(R.id.btnAllBook);
        btnCurrentBook = findViewById(R.id.btnCurrentBook);
        btnAreadyBook = findViewById(R.id.btnAreadyBook);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnFav = findViewById(R.id.btnFav);
        btnAbout = findViewById(R.id.btnAbout);

        //set fontFamily from sharedPrefrences
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        btnAllBook.setTypeface(typeface, Typeface.BOLD);
        btnCurrentBook.setTypeface(typeface, Typeface.BOLD);
        btnAreadyBook.setTypeface(typeface , Typeface.BOLD);
        btnWishlist.setTypeface(typeface , Typeface.BOLD);
        btnFav.setTypeface(typeface, Typeface.BOLD);
        btnAbout.setTypeface(typeface, Typeface.BOLD);

        //set  font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        btnAllBook.setTextSize(fontSize);
        btnCurrentBook.setTextSize(fontSize);
        btnAreadyBook.setTextSize(fontSize);
        btnWishlist.setTextSize(fontSize);
        btnFav.setTextSize(fontSize);
        btnAbout.setTextSize(fontSize);

        //set color
        int colorResId = getResources().getIdentifier(sharedPref.getString("colorBg", "btn_light_green"), "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnAllBook.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnCurrentBook.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnAreadyBook.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnWishlist.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnFav.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnAbout.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        }

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            mainActivity.setBackgroundColor(Color.BLACK);
            btnAllBook.setTextColor(Color.WHITE);
            btnCurrentBook.setTextColor(Color.WHITE);
            btnAreadyBook.setTextColor(Color.WHITE);
            btnWishlist.setTextColor(Color.WHITE);
            btnFav.setTextColor(Color.WHITE);
            btnAbout.setTextColor(Color.WHITE);
        }else{
            mainActivity.setBackgroundColor(Color.WHITE);
            btnAllBook.setTextColor(Color.BLACK);
            btnCurrentBook.setTextColor(Color.BLACK);
            btnAreadyBook.setTextColor(Color.BLACK);
            btnWishlist.setTextColor(Color.BLACK);
            btnFav.setTextColor(Color.BLACK);
            btnAbout.setTextColor(Color.BLACK);
        }

        /*Animation*/
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        floatingActionButtonAddBook = findViewById(R.id.floatingActionButtonAddBook);
        floatingActionButtonCamera = findViewById(R.id.floatingActionButtonCamera);
        floatingActionButtonGallery = findViewById(R.id.floatingActionButtonGallery);

        //
        btnAllBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), MainActivityAllBook.class);
                view.getContext().startActivity(intent);
            }
        });

        //
        btnCurrentBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), MainActivityCurrentReading.class);
                view.getContext().startActivity(intent);
            }
        });

        //
        btnAreadyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), MainActivityAreadyRead.class);
                view.getContext().startActivity(intent);
            }
        });

        //
        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), MainActivityWishList.class);
                view.getContext().startActivity(intent);
            }
        });

        //
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), MainActivityFav.class);
                view.getContext().startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), MainActivityAbout.class);
                view.getContext().startActivity(intent);
            }
        });


        /*
        * Floating action button event to move
        * */

        /*
        floatingActionButtonAddBook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        floatingActionButtonAddBook.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        floatingActionButtonAddBook.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    }
                }
                return false;
            }
        });

          */

        floatingActionButtonAddBook.setOnLongClickListener(new View.OnLongClickListener(){
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
        floatingActionButtonAddBook.setOnClickListener(new View.OnClickListener() {
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
                     floatingActionButtonCamera.setVisibility(View.VISIBLE);
                     floatingActionButtonGallery.setVisibility(View.VISIBLE);
                 }else {
                     floatingActionButtonCamera.setVisibility(View.INVISIBLE);
                     floatingActionButtonGallery.setVisibility(View.INVISIBLE);
                 }
            }

            //to ban the button not clicable
            private void setClickable(boolean clickable){
                if(!clickable){
                    floatingActionButtonCamera.setClickable(true);
                    floatingActionButtonGallery.setClickable(true);
                }else {
                    floatingActionButtonCamera.setClickable(false);
                    floatingActionButtonGallery.setClickable(false);
                }
            }

            private void setAnimation(boolean cliked ){
                 if(!cliked){
                     floatingActionButtonCamera.startAnimation(fromBottom);
                     floatingActionButtonGallery.startAnimation(fromBottom);
                     floatingActionButtonAddBook.setAnimation(rotateOpen);
                 }else {
                     floatingActionButtonCamera.startAnimation(toBottom);
                     floatingActionButtonGallery.startAnimation(toBottom);
                     floatingActionButtonAddBook.setAnimation(rotateClose);
                 }
            }
        });

        /*
        * Expanden button clicked
        * */
        floatingActionButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Camera clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(view.getContext(), MainActivityGetFromCamera.class);
                //intent.putExtra("flag","gggg");
                view.getContext().startActivity(intent);
            }
        });

        floatingActionButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Gallery clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(view.getContext(), MainActivityGetFromGallery.class);
                //intent.putExtra("flag","gggg");
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        btnAllBook.setTextSize(fontSize);
        btnCurrentBook.setTextSize(fontSize);
        btnAreadyBook.setTextSize(fontSize);
        btnWishlist.setTextSize(fontSize);
        btnFav.setTextSize(fontSize);
        btnAbout.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);

        // Set the font to the button
        btnAllBook.setTypeface(typeface, Typeface.BOLD);
        btnCurrentBook.setTypeface(typeface, Typeface.BOLD);
        btnAreadyBook.setTypeface(typeface, Typeface.BOLD);
        btnWishlist.setTypeface(typeface, Typeface.BOLD);
        btnFav.setTypeface(typeface, Typeface.BOLD);
        btnAbout.setTypeface(typeface, Typeface.BOLD);
    }

    @Override
    public void onNightModeChnger(String nightMode) {
         if(nightMode.equals("ON")){
             mainActivity.setBackgroundColor(Color.BLACK);
             btnAllBook.setTextColor(Color.WHITE);
             btnCurrentBook.setTextColor(Color.WHITE);
             btnAreadyBook.setTextColor(Color.WHITE);
             btnWishlist.setTextColor(Color.WHITE);
             btnFav.setTextColor(Color.WHITE);
             btnAbout.setTextColor(Color.WHITE);
         }else{
             mainActivity.setBackgroundColor(Color.WHITE);
             btnAllBook.setTextColor(Color.BLACK);
             btnCurrentBook.setTextColor(Color.BLACK);
             btnAreadyBook.setTextColor(Color.BLACK);
             btnWishlist.setTextColor(Color.BLACK);
             btnFav.setTextColor(Color.BLACK);
             btnAbout.setTextColor(Color.BLACK);
         }
    }

    @Override
    public void onColorChanged(String color) {
        int colorResId = getResources().getIdentifier(color, "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnAllBook.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnCurrentBook.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnAreadyBook.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnWishlist.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnFav.setBackgroundTintList(ColorStateList.valueOf(colorValue));
            btnAbout.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        }
    }
}