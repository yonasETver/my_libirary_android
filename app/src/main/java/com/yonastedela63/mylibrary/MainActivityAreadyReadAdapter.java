package com.yonastedela63.mylibrary;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class MainActivityAreadyReadAdapter extends RecyclerView.Adapter<MainActivityAreadyReadAdapter.ViewHolder>  {

    private ArrayList<MainActivityAreadyBook> bookDetailsAready = new ArrayList<MainActivityAreadyBook>();
    private Context context;
    private int buttonBackgroundColor;
    private int buttonForgroundColor;
    private SharedPreferences sharedPref;
    Intent intent;

    public MainActivityAreadyReadAdapter(Context context) {

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
    public MainActivityAreadyReadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_aready_read_adapter, parent, false);
        MainActivityAreadyReadAdapter.ViewHolder holder = new MainActivityAreadyReadAdapter.ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(bookDetailsAready.get(position).getPhoto_url());
        holder.bookPhotoAready.setImageBitmap(bitmap);
        holder.infoBoxAready.setText(bookDetailsAready.get(position).getTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.infoBoxAready.setBackgroundTintList(ColorStateList.valueOf(buttonBackgroundColor));
            holder.infoBoxAready.setTextColor(buttonForgroundColor);
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

    public void setChange(String color){
        int colorResId = context.getResources().getIdentifier(color, "color", context.getPackageName());
        buttonBackgroundColor = ContextCompat.getColor(context, colorResId);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return bookDetailsAready.size();
    }

    public void setBookDetailsAready(ArrayList<MainActivityAreadyBook> bookDetailsAready) {
        this.bookDetailsAready = bookDetailsAready;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bookPhotoAready;
        private TextView infoBoxAready;
        private CardView cardAreadyRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookPhotoAready = itemView.findViewById(R.id.bookPhotoAready);
            infoBoxAready = itemView.findViewById(R.id.infoBoxAready);
            cardAreadyRead = itemView.findViewById(R.id.cardAreadyRead);

        }
    }

}