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

public class MainActivityWishListAdapter extends RecyclerView.Adapter<MainActivityWishListAdapter.ViewHolder>  {

    private ArrayList<MainActivityWhishListBook> bookDetailsWish = new ArrayList<MainActivityWhishListBook>();
    private Context context;
    private int buttonBackgroundColor;
    private int buttonForgroundColor;
    private SharedPreferences sharedPref;
    Intent intent;

    public MainActivityWishListAdapter(Context context) {
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
    public MainActivityWishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_wish_list_adapter, parent, false);
        MainActivityWishListAdapter.ViewHolder holder = new MainActivityWishListAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(bookDetailsWish.get(position).getPhoto_url());
        holder.bookPhotoWish.setImageBitmap(bitmap);
        holder.infoBoxWish.setText(bookDetailsWish.get(position).getTime());
        holder.infoBoxWish.setTextColor(buttonForgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.infoBoxWish.setBackgroundTintList(ColorStateList.valueOf(buttonBackgroundColor));
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
        return bookDetailsWish.size();
    }

    public void setBookDetailsWish(ArrayList<MainActivityWhishListBook> bookDetailsWish) {
        this.bookDetailsWish = bookDetailsWish;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bookPhotoWish;
        private TextView infoBoxWish;
        private CardView cardWishRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookPhotoWish = itemView.findViewById(R.id.bookPhotoWhish);
            cardWishRead= itemView.findViewById(R.id.cardWishRead);
            infoBoxWish = itemView.findViewById(R.id.infoBoxWish);

        }
    }
}
