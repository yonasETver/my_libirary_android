package com.yonastedela63.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yonastedela63.mylibrary.MyDataSource;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivityGetFromCamera extends AppCompatActivity implements TopBar.OnChangedListenerAll {

    private Fragment fragment;
    private ImageView takeImage;
    private MaterialButton imageButton, submitBtn;
    private EditText editTitleBook,editAuthorBook,editDeskrBook,editTextPublisher,
                     editPublishedYear,editTotalPage;
    private TextView textBookTitle,textAuthorBook,textDescrip,txtPublisherBook,
                     textPublishedYear, pageNumber;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean validChecked = false;
    private boolean imgChecked = false;
    private TextView textViewTitle;
    private SharedPreferences sharedPref;
    private ConstraintLayout mainActivityCamera;

    File imageFile;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_get_from_camera);

        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        fragment = new TopBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, fragment);
        fragmentTransaction.commit();

        mainActivityCamera = findViewById(R.id.mainActivityCamera);
        takeImage = findViewById(R.id.takeImage);
        imageButton = findViewById(R.id.imageButton);
        editTitleBook = findViewById(R.id.editTitleBook);
        editAuthorBook = findViewById(R.id.editAuthorBook);
        editDeskrBook = findViewById(R.id.editDeskrBook);
        editTextPublisher = findViewById(R.id.editTextPublisher);
        editTotalPage = findViewById(R.id.editTotalPage);
        editPublishedYear = findViewById(R.id.editPublishedYear);
        submitBtn = findViewById(R.id.submitBtn);

        textBookTitle = findViewById(R.id.textBookTitle);
        textAuthorBook = findViewById(R.id.textAuthorBook);
        textDescrip = findViewById(R.id.textDescrip);
        txtPublisherBook = findViewById(R.id.txtPublisherBook);
        textPublishedYear = findViewById(R.id.textPublishedYear);
        pageNumber = findViewById(R.id.pageNumber);

        textViewTitle = findViewById(R.id.textViewTitle);

        // set font size
        int fontSize = sharedPref.getInt("fontSizeHolder", 14);
        textViewTitle.setTextSize(fontSize);
        submitBtn.setTextSize(fontSize);
        //set color
        int colorResId = getResources().getIdentifier(sharedPref.getString("colorBg", "btn_light_green"), "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);
        imageButton.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        submitBtn.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        //set font family
        sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int fontResId = getResources().getIdentifier(sharedPref.getString("fontFamilyHolder", "lora"), "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        textViewTitle.setTypeface(typeface);
        submitBtn.setTypeface(typeface);
        //night mode
        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            mainActivityCamera.setBackgroundColor(Color.BLACK);
            textBookTitle.setTextColor(Color.WHITE);
            textAuthorBook.setTextColor(Color.WHITE);
            textDescrip.setTextColor(Color.WHITE);
            txtPublisherBook.setTextColor(Color.WHITE);
            textPublishedYear.setTextColor(Color.WHITE);
            pageNumber.setTextColor(Color.WHITE);
            editTitleBook.setHintTextColor(Color.GRAY);
            editAuthorBook.setHintTextColor(Color.GRAY);
            editDeskrBook.setHintTextColor(Color.GRAY);
            editTextPublisher.setHintTextColor(Color.GRAY);
            editTotalPage.setHintTextColor(Color.GRAY);
            editPublishedYear.setHintTextColor(Color.GRAY);
            imageButton.setIconResource(R.drawable.ic_photo_camera2);
            submitBtn.setTextColor(Color.WHITE);
            textViewTitle.setTextColor(Color.WHITE);
            textViewTitle.setBackgroundResource(R.drawable.strok_shape_white);
            editTitleBook.setTextColor(Color.WHITE);
            editAuthorBook.setTextColor(Color.WHITE);
            editDeskrBook.setTextColor(Color.WHITE);
            editTextPublisher.setTextColor(Color.WHITE);
            editPublishedYear.setTextColor(Color.WHITE);
            editTotalPage.setTextColor(Color.WHITE);
            imageButton.setIconTintResource(R.color.white);
        }else{
            mainActivityCamera.setBackgroundColor(Color.WHITE);
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip.setTextColor(Color.BLACK);
            txtPublisherBook.setTextColor(Color.BLACK);
            textPublishedYear.setTextColor(Color.BLACK);
            pageNumber.setTextColor(Color.BLACK);
            editTitleBook.setHintTextColor(Color.GRAY);
            editAuthorBook.setHintTextColor(Color.GRAY);
            editDeskrBook.setHintTextColor(Color.GRAY);
            editTextPublisher.setHintTextColor(Color.GRAY);
            editTotalPage.setHintTextColor(Color.GRAY);
            editPublishedYear.setHintTextColor(Color.GRAY);
            imageButton.setIconResource(R.drawable.ic_photo_camera);
//            imageButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            submitBtn.setTextColor(Color.BLACK);
            textViewTitle.setTextColor(Color.BLACK);
            textViewTitle.setBackgroundResource(R.drawable.strok_shape);
            editTitleBook.setTextColor(Color.BLACK);
            editAuthorBook.setTextColor(Color.BLACK);
            editDeskrBook.setTextColor(Color.BLACK);
            editTextPublisher.setTextColor(Color.BLACK);
            editPublishedYear.setTextColor(Color.BLACK);
            editTotalPage.setTextColor(Color.BLACK);
            imageButton.setIconTintResource(R.color.black);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for camera permission
                if (ContextCompat.checkSelfPermission(MainActivityGetFromCamera.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(MainActivityGetFromCamera.this,
                            new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                } else {
                    // If permission is granted, start camera intent
                    dispatchTakePictureIntent();
                }

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    validOther();
                    isImage();

                if(!validChecked || !imgChecked ){
                    Toast.makeText(MainActivityGetFromCamera.this, "Please make sure there is no error", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(MainActivityGetFromCamera.this, "Perfect!!", Toast.LENGTH_SHORT).show();
                    MyDataSource dataSource = new MyDataSource(MainActivityGetFromCamera.this);
                    dataSource.open();

                    Date currentDate = new Date();
                    // Define the desired date format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    String formattedDate = dateFormat.format(currentDate);

                    dataSource.insertData(editTitleBook.getText().toString(),
                                          editAuthorBook.getText().toString(),
                                          editDeskrBook.getText().toString(),
                                          editTextPublisher.getText().toString(),
                                          Integer.parseInt(editPublishedYear.getText().toString()),
                                          Integer.parseInt(editTotalPage.getText().toString()),
                                          String.valueOf(imageFile),
                                          formattedDate

                    );

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    private void isImage(){

        if(takeImage.getDrawable() == null) {
            int color = getResources().getColor(R.color.yellow); // replace your_color with the desired color resource
            takeImage.setBackgroundColor(color);
            imgChecked = false;
        } else {
            imgChecked = true;
        }
    }

    private void validOther(){
        String nightMode = sharedPref.getString("nightMode", "OFF");

        // Create a shape drawable with a rectangle shape
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        // Set the border width and color
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.yellow)); // Set border color
        shapeDrawable.getPaint().setStrokeWidth(2); // Set border width

        // Create a shape drawable with a rectangle shape
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(new RectShape());
        // Set the border width and color
        shapeDrawable2.getPaint().setColor(Color.WHITE); // Set border color
        shapeDrawable2.getPaint().setStrokeWidth(2); // Set border width

        ShapeDrawable shapeDrawable3 = new ShapeDrawable(new RectShape());
        shapeDrawable3.getPaint().setColor(Color.BLACK); // Set border color
        shapeDrawable3.getPaint().setStrokeWidth(2);

        // Get the current instance of Calendar
        Calendar calendar = Calendar.getInstance();
        // Get the current year
        int year = calendar.get(Calendar.YEAR);

        if(editTitleBook.getText().toString().length() < 7){
            editTitleBook.setBackground(shapeDrawable);
            textBookTitle.setText("Title can't be less than 7 character!!");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            textBookTitle.setTextColor(Color.RED);
            validChecked = false;
        }else if(editAuthorBook.getText().toString().length() < 7){
            if(nightMode.equals("ON")){
                editTitleBook.setBackground(shapeDrawable3);
            }else {
                editTitleBook.setBackground(shapeDrawable2);
            }
            editAuthorBook.setBackground(shapeDrawable);
            textBookTitle.setText("Title:");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setText("Author can't be less than 7 character!!");
            textAuthorBook.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            textAuthorBook.setTextColor(Color.RED);
            validChecked = false;
        }else if(editDeskrBook.getText().toString().length() < 15){
            if(nightMode.equals("ON")){
                editTitleBook.setBackground(shapeDrawable3);
                editAuthorBook.setBackground(shapeDrawable3);
            }else {
                editTitleBook.setBackground(shapeDrawable2);
                editAuthorBook.setBackground(shapeDrawable2);
            }
            editDeskrBook.setBackground(shapeDrawable);
            textBookTitle.setText("Title:");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setText("Author:");
            textAuthorBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip .setText("Description can't be less than 15 character!!");
            textDescrip .setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            textDescrip .setTextColor(Color.RED);
            validChecked = false;
        }else if(editTextPublisher.getText().toString().length() < 7){
            if(nightMode.equals("ON")) {
                editTitleBook.setBackground(shapeDrawable3);
                editAuthorBook.setBackground(shapeDrawable3);
                editDeskrBook.setBackground(shapeDrawable3);
            }else {
                editTitleBook.setBackground(shapeDrawable2);
                editAuthorBook.setBackground(shapeDrawable2);
                editDeskrBook.setBackground(shapeDrawable2);
            }
            editTextPublisher.setBackground(shapeDrawable);
            textBookTitle.setText("Title:");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setText("Author:");
            textAuthorBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip.setText("Description:");
            textDescrip.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textDescrip.setTextColor(Color.BLACK);
            txtPublisherBook .setText("Publisher can't be less than 7 character!!");
            txtPublisherBook .setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            txtPublisherBook .setTextColor(Color.RED);
            validChecked = false;
        }else if(editPublishedYear.getText().toString().length() != 4 || !isNumeric(editPublishedYear.getText().toString()) ||
                (Integer.parseInt(editPublishedYear.getText().toString()) < 1800) || (Integer.parseInt(editPublishedYear.getText().toString()) > year)){
            if(nightMode.equals("ON")) {
                editTitleBook.setBackground(shapeDrawable3);
                editAuthorBook.setBackground(shapeDrawable3);
                editDeskrBook.setBackground(shapeDrawable3);
                editTextPublisher.setBackground(shapeDrawable3);
            }else {
                editTitleBook.setBackground(shapeDrawable2);
                editAuthorBook.setBackground(shapeDrawable2);
                editDeskrBook.setBackground(shapeDrawable2);
                editTextPublisher.setBackground(shapeDrawable2);
            }
            editPublishedYear.setBackground(shapeDrawable);
            textBookTitle.setText("Title:");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setText("Author:");
            textAuthorBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip.setText("Description:");
            textDescrip.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textDescrip.setTextColor(Color.BLACK);
            txtPublisherBook.setText("Publisher:");
            txtPublisherBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            txtPublisherBook.setTextColor(Color.BLACK);
            textPublishedYear .setText("Published year must be 1800-Current year!!");
            textPublishedYear .setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            textPublishedYear .setTextColor(Color.RED);
            validChecked = false;
        }else if(!isNumeric(editTotalPage.getText().toString()) || Integer.parseInt(editTotalPage.getText().toString()) < 1 ||
                Integer.parseInt(editTotalPage.getText().toString()) > 4000){
            if(nightMode.equals("ON")) {
                editTitleBook.setBackground(shapeDrawable3);
                editAuthorBook.setBackground(shapeDrawable3);
                editDeskrBook.setBackground(shapeDrawable3);
                editTextPublisher.setBackground(shapeDrawable3);
                editPublishedYear.setBackground(shapeDrawable3);
            }else{
                editTitleBook.setBackground(shapeDrawable2);
                editAuthorBook.setBackground(shapeDrawable2);
                editDeskrBook.setBackground(shapeDrawable2);
                editTextPublisher.setBackground(shapeDrawable2);
                editPublishedYear.setBackground(shapeDrawable2);
            }
            editTotalPage.setBackground(shapeDrawable);
            textBookTitle.setText("Title:");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setText("Author:");
            textAuthorBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip.setText("Description:");
            textDescrip.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textDescrip.setTextColor(Color.BLACK);
            txtPublisherBook.setText("Publisher:");
            txtPublisherBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            txtPublisherBook.setTextColor(Color.BLACK);
            textPublishedYear.setText("Published Year:");
            textPublishedYear.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textPublishedYear.setTextColor(Color.BLACK);
            pageNumber .setText("Page number must be number & 1-4000!!");
            pageNumber .setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            pageNumber .setTextColor(Color.RED);
            validChecked = false;
        }else {
            if(nightMode.equals("ON")){
                editTitleBook.setBackground(shapeDrawable3);
                editAuthorBook.setBackground(shapeDrawable3);
                editDeskrBook.setBackground(shapeDrawable3);
                editTextPublisher.setBackground(shapeDrawable3);
                editPublishedYear.setBackground(shapeDrawable3);
                editTotalPage.setBackground(shapeDrawable3);
            }else {
                editTitleBook.setBackground(shapeDrawable2);
                editAuthorBook.setBackground(shapeDrawable2);
                editDeskrBook.setBackground(shapeDrawable2);
                editTextPublisher.setBackground(shapeDrawable2);
                editPublishedYear.setBackground(shapeDrawable2);
                editTotalPage.setBackground(shapeDrawable2);
            }

            textBookTitle.setText("Title:");
            textBookTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setText("Author:");
            textAuthorBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip.setText("Description:");
            textDescrip.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textDescrip.setTextColor(Color.BLACK);
            txtPublisherBook.setText("Publisher:");
            txtPublisherBook.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            txtPublisherBook.setTextColor(Color.BLACK);
            textPublishedYear.setText("Published Year:");
            textPublishedYear.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textPublishedYear.setTextColor(Color.BLACK);
            pageNumber.setText("Toptal Page Number:");
            pageNumber.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            pageNumber.setTextColor(Color.BLACK);
            validChecked = true;
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // Regular expression to match numbers
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                takeImage.setImageBitmap(imageBitmap);
                saveImageToStorage(imageBitmap);
            }
        }
    }

    private void saveImageToStorage(Bitmap bitmap) {
        String filename = "image_" + System.currentTimeMillis() + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = new File(storageDir, filename);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFontSizeChenger(int fontSize) {
        textViewTitle.setTextSize(fontSize);
        submitBtn.setTextSize(fontSize);
    }

    @Override
    public void onFontFamilyChenger(String fontFamily) {
        int fontResId = getResources().getIdentifier(fontFamily, "font", getPackageName());
        Typeface typeface = ResourcesCompat.getFont(this,fontResId);
        textViewTitle.setTypeface(typeface);
        submitBtn.setTypeface(typeface);

    }

    @Override
    public void onNightModeChnger(String nightMode) {
        if(nightMode.equals("ON")){
            mainActivityCamera.setBackgroundColor(Color.BLACK);
            textBookTitle.setTextColor(Color.WHITE);
            textAuthorBook.setTextColor(Color.WHITE);
            textDescrip.setTextColor(Color.WHITE);
            txtPublisherBook.setTextColor(Color.WHITE);
            textPublishedYear.setTextColor(Color.WHITE);
            pageNumber.setTextColor(Color.WHITE);
            editTitleBook.setHintTextColor(Color.GRAY);
            editAuthorBook.setHintTextColor(Color.GRAY);
            editDeskrBook.setHintTextColor(Color.GRAY);
            editTextPublisher.setHintTextColor(Color.GRAY);
            editTotalPage.setHintTextColor(Color.GRAY);
            editPublishedYear.setHintTextColor(Color.GRAY);
            imageButton.setIconResource(R.drawable.ic_photo_camera2);
            submitBtn.setTextColor(Color.WHITE);
            textViewTitle.setTextColor(Color.WHITE);
            textViewTitle.setBackgroundResource(R.drawable.strok_shape_white);
            editTitleBook.setTextColor(Color.WHITE);
            editAuthorBook.setTextColor(Color.WHITE);
            editDeskrBook.setTextColor(Color.WHITE);
            editTextPublisher.setTextColor(Color.WHITE);
            editPublishedYear.setTextColor(Color.WHITE);
            editTotalPage.setTextColor(Color.WHITE);
            imageButton.setIconTintResource(R.color.white);
        }else{
            mainActivityCamera.setBackgroundColor(Color.WHITE);
            textBookTitle.setTextColor(Color.BLACK);
            textAuthorBook.setTextColor(Color.BLACK);
            textDescrip.setTextColor(Color.BLACK);
            txtPublisherBook.setTextColor(Color.BLACK);
            textPublishedYear.setTextColor(Color.BLACK);
            pageNumber.setTextColor(Color.BLACK);
            editTitleBook.setHintTextColor(Color.GRAY);
            editAuthorBook.setHintTextColor(Color.GRAY);
            editDeskrBook.setHintTextColor(Color.GRAY);
            editTextPublisher.setHintTextColor(Color.GRAY);
            editTotalPage.setHintTextColor(Color.GRAY);
            editPublishedYear.setHintTextColor(Color.GRAY);
            imageButton.setIconResource(R.drawable.ic_photo_camera);
            submitBtn.setTextColor(Color.BLACK);
            textViewTitle.setTextColor(Color.BLACK);
            textViewTitle.setBackgroundResource(R.drawable.strok_shape);
            editTitleBook.setTextColor(Color.BLACK);
            editAuthorBook.setTextColor(Color.BLACK);
            editDeskrBook.setTextColor(Color.BLACK);
            editTextPublisher.setTextColor(Color.BLACK);
            editPublishedYear.setTextColor(Color.BLACK);
            editTotalPage.setTextColor(Color.BLACK);
            imageButton.setIconTintResource(R.color.black);
        }
    }

    @Override
    public void onColorChanged(String color) {
        int colorResId = getResources().getIdentifier(color, "color", getPackageName());
        int colorValue = ContextCompat.getColor(this, colorResId);

        imageButton.setBackgroundTintList(ColorStateList.valueOf(colorValue));
        submitBtn.setBackgroundTintList(ColorStateList.valueOf(colorValue));

    }
}
