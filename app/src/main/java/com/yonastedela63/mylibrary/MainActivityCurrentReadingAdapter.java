package com.yonastedela63.mylibrary;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivityCurrentReadingAdapter extends RecyclerView.Adapter<MainActivityCurrentReadingAdapter.ViewHolder>  {

    private ArrayList<MainActivityCurrentBook> bookDetailsCurrent = new ArrayList<>();
    private Context context;
    private int buttonBackgroundColor;
    private int buttonForgroundColor;
    private SharedPreferences sharedPref;
    Intent intent;

    public MainActivityCurrentReadingAdapter(Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_current_readning_adapter, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(bookDetailsCurrent.get(position).getPhoto_url());
        holder.bookPhotoCurrent.setImageBitmap(bitmap);
        holder.infoBox.setText(bookDetailsCurrent.get(position).getTime());
        holder.infoBox.setBackgroundColor(buttonBackgroundColor);
        holder.infoBox.setTextColor(buttonForgroundColor);

        holder.cardCurrentRead.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivityCurrentReadingBookForFavAndEnd.class);
                // Assuming bookDetailsCurrent contains the data you want to pass to the next activity
                intent.putExtra("photoUrl", bookDetailsCurrent.get(position).getPhoto_url());
                intent.putExtra("book_id", bookDetailsCurrent.get(position).getBook_id());
                view.getContext().startActivity(intent);
                return true; // Return true to consume the long click event
            }
        });
    }



    @Override
    public int getItemCount() {
        return bookDetailsCurrent.size();
    }

    public void setBookDetailsCurrent(ArrayList<MainActivityCurrentBook> bookDetailsCurrent) {
        this.bookDetailsCurrent = bookDetailsCurrent;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView bookPhotoCurrent;
        private TextView infoBox;
        private CardView cardCurrentRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookPhotoCurrent = itemView.findViewById(R.id.bookPhotoCurrent);
            infoBox = itemView.findViewById(R.id.infoBox);
            cardCurrentRead = itemView.findViewById(R.id.cardCurrentRead);

        }
    }

}




