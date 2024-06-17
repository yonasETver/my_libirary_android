package com.yonastedela63.mylibrary;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder>  {

    private ArrayList<Alarm> alermMsg = new ArrayList<>();
    private Context  context;
    private int textColor;
    private int textBacground;
    private SharedPreferences sharedPref;

    public AlertAdapter(Context context) {
        this.context = context;
        //for night mode
        sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("ON")){
            textColor = WHITE;
            textBacground = GRAY;
        }else{
            textColor= BLACK;
            textBacground = Color.rgb(255,190,125);
        }
    }

    @NonNull
    @Override
    public AlertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_alert_adapter, parent, false);
        AlertAdapter.ViewHolder holder = new AlertAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.alermInfo.setText(alermMsg.get(position).getAlertMsg());
        holder.alermInfo.setTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.alermInfo.setBackgroundTintList(ColorStateList.valueOf(textBacground));
        }
    }

    @Override
    public int getItemCount() {
        return alermMsg.size();
    }

    public void setAlermMsg(ArrayList<Alarm> alermMsg) {
        this.alermMsg = alermMsg;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView alermInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            alermInfo = itemView.findViewById(R.id.alertMsg);
        }
    }

    public void setNightMode(String nightMode){
        if(nightMode.equals("ON")){
            textColor = WHITE;
            textBacground = GRAY;
        }else{
            textColor= BLACK;
            textBacground = Color.rgb(255,190,125);
        }

        notifyDataSetChanged();
    }

}