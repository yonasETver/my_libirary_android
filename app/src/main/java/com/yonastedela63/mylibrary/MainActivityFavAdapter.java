package com.yonastedela63.mylibrary;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivityFavAdapter extends RecyclerView.Adapter<MainActivityFavAdapter.ViewHolder>  {

    private ArrayList<MainActivityFavBook> bookDetailsFav = new ArrayList<>();
    private Context context;
    private int buttonBackgroundColor;
    private int buttonForgroundColor;
    private SharedPreferences sharedPref;
    Intent intent;

    public MainActivityFavAdapter(Context context) {
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
    public MainActivityFavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_fav_adapter, parent, false);
        MainActivityFavAdapter.ViewHolder holder = new MainActivityFavAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(bookDetailsFav.get(position).getPhoto_url());
        holder.bookPhotoFav.setImageBitmap(bitmap);
        holder.infoBoxFav.setText(bookDetailsFav.get(position).getAboutBook());
        holder.infoBoxFav.setTextColor(buttonForgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.infoBoxFav.setBackgroundTintList(ColorStateList.valueOf(buttonBackgroundColor));
        }
    }

    public void setNightMode(String nightMode){
        if(nightMode.equals("ON")){
            buttonForgroundColor = WHITE;
        }else{
            buttonForgroundColor= BLACK;
        }

        notifyDataSetChanged();
    }

    public void setChange(String color) {
        int colorResId = context.getResources().getIdentifier(color, "color", context.getPackageName());
        buttonBackgroundColor = ContextCompat.getColor(context, colorResId);
    }


        @Override
    public int getItemCount() {
        return bookDetailsFav.size();
    }

    public void setBookDetailsFav(ArrayList<MainActivityFavBook> bookDetailsFav) {
        this.bookDetailsFav = bookDetailsFav;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bookPhotoFav;
        private TextView infoBoxFav;
        private CardView cardFavRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookPhotoFav = itemView.findViewById(R.id.bookPhotoFav);
            cardFavRead = itemView.findViewById(R.id.cardFavRead);
            infoBoxFav = itemView.findViewById(R.id.infoBoxFav);

        }
    }
}
