package com.yonastedela63.mylibrary;

import static android.graphics.Color.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;

public class BookDetailAdapter extends RecyclerView.Adapter<BookDetailAdapter.ViewHolder> {

    private ArrayList<BookDetail> bookDetails = new ArrayList<>();
    private Context context;
    Intent intent;
    private int buttonBackgroundColor;
    private int buttonForgroundColor;
    private SharedPreferences sharedPref;
    private ArrayList<ViewHolder> visibleViewHolders = new ArrayList<>(); // Track visible ViewHolders

    public BookDetailAdapter(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int colorResId = context.getResources().getIdentifier(sharedPref.getString("colorBg", "btn_light_green"), "color", context.getPackageName());
        buttonBackgroundColor = ContextCompat.getColor(context, colorResId);

        //for night mode
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            buttonForgroundColor = WHITE;
        }else{
            buttonForgroundColor= BLACK;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_all_book, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(bookDetails.get(position).getPhoto_url());
        holder.bookPhoto.setImageBitmap(bitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.btnDitail.setBackgroundTintList(ColorStateList.valueOf(buttonBackgroundColor));
            holder.btnDitail.setTextColor(buttonForgroundColor);
        } else {
            holder.btnDitail.setBackgroundColor(buttonBackgroundColor);
        }

        // Add ViewHolder to the list of visible ViewHolders
        if (!visibleViewHolders.contains(holder)) {
            visibleViewHolders.add(holder);
        }

        holder.btnDitail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click

                intent = new Intent(view.getContext(), MainActivityDetailsAboutAllBooks.class);
                intent.putExtra("bookTitle", bookDetails.get(position).getTitle());
                intent.putExtra("bookDescription",bookDetails.get(position).getAboutBook());
                intent.putExtra("bookAuthor", bookDetails.get(position).getAuthor());
                intent.putExtra("bookPublisher", bookDetails.get(position).getPublisher());
                intent.putExtra("PublishedYear", bookDetails.get(position).getYearPeblished());
                intent.putExtra("pageNumber", bookDetails.get(position).getPageNumber());
                intent.putExtra("imagePath", bookDetails.get(position).getPhoto_url());
                intent.putExtra("bookID", bookDetails.get(position).getBookID());

                view.getContext().startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return bookDetails.size();
    }

    public void setBookDetails(ArrayList<BookDetail> bookDetails) {
        this.bookDetails = bookDetails;
        notifyDataSetChanged();
    }

    public void setNightMode(String nightMode){
        if(nightMode.equals("ON")){
            buttonForgroundColor = WHITE;
        }else{
            buttonForgroundColor= BLACK;
        }

        notifyDataSetChanged();
    }

    public void setChange(String color){
        int colorResId = context.getResources().getIdentifier(color, "color", context.getPackageName());
        buttonBackgroundColor = ContextCompat.getColor(context, colorResId);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookPhoto;
        private Button btnDitail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookPhoto = itemView.findViewById(R.id.bookPhoto);
            btnDitail = itemView.findViewById(R.id.btnDitail);
        }
    }
}
