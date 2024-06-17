package com.yonastedela63.mylibrary;//package com.yonastedela63.mylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class TopBar<privat> extends Fragment {
    private int count = 0;
    private int size = 0;
    private boolean flag = false;
    private BluetoothAdapter bluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    private SharedPreferences sharedPref;
    private OnChangedListenerAll onChangedListener;


    // Interface definition for communication with the activity
    public interface OnChangedListenerAll {
        void onFontSizeChenger(int fontSize);
        void onFontFamilyChenger(String fontFamily);
        void onNightModeChnger(String nightMode);
        void onColorChanged(String color);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onChangedListener = (OnChangedListenerAll) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnColorChangedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyDataSource dataSource = new MyDataSource(requireContext());
        dataSource.open();

        Cursor cursor = dataSource.getAllData();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    // Retrieve data from the cursor
                    @SuppressLint("Range") String bookID = cursor.getString(cursor.getColumnIndex("book_id"));
                    size++;
                    Cursor cursor2 = dataSource.getCurrentData();
                    if (cursor2 != null) {
                        try {
                            while (cursor2.moveToNext()) {
                                @SuppressLint("Range") String bookIDCurrent = cursor2.getString(cursor2.getColumnIndex("book_id"));
                                if (bookID.equals(bookIDCurrent)) {
                                    count++;
                                }
                            }
                        } finally {
                            cursor2.close();
                        }
                    }
                }
            } finally {
                cursor.close(); // Close the cursor in a finally block
            }
        }

        dataSource.close();
        alermTxt = view.findViewById(R.id.alermTxt);
        int result = size - count;
        if (result == 0) {
            alermTxt.setVisibility(View.GONE);

        } else if (result > 9) {
            alermTxt.setVisibility(View.VISIBLE);
            setTextAlert(("9+"));

        } else {
            alermTxt.setVisibility(View.VISIBLE);
            setTextAlert(String.valueOf(result));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    View view;
    private ImageView menu_icon, alarm_icon, setting_icon;
    private TextView alermTxt,titleLogo;
    private RelativeLayout topBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_top_bar, container, false);
        setHasOptionsMenu(true);
        sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        alermTxt = view.findViewById(R.id.alermTxt);
        topBar = view.findViewById(R.id.topBar);
        menu_icon = view.findViewById(R.id.menu_icon);
        alarm_icon = view.findViewById(R.id.alarm_icon);
        setting_icon = view.findViewById(R.id.setting_icon);
        titleLogo = view.findViewById(R.id.titleLogo);
        String nightMode = sharedPref.getString("nightMode", "OFF");
        if(nightMode.equals("OFF")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setting_icon.setImageResource(R.drawable.ic_setting_book);
                alarm_icon.setImageResource(R.drawable.ic_notification_book);
                menu_icon.setImageResource(R.drawable.ic_menu_book);
                titleLogo.setTextColor(Color.BLACK);
                alermTxt.setTextColor(Color.BLACK);
                topBar.setBackgroundColor(Color.RED);
                Window window = requireActivity().getWindow();;
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.RED);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setting_icon.setImageResource(R.drawable.ic_setting_book2);
                alarm_icon.setImageResource(R.drawable.ic_notification_book2);
                menu_icon.setImageResource(R.drawable.ic_menu_book2);
                titleLogo.setTextColor(Color.WHITE);
                alermTxt.setTextColor(Color.WHITE);
                topBar.setBackgroundColor(getResources().getColor(R.color.light_dark));
                Window window = requireActivity().getWindow();;
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.light_dark));
            }
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getActivity(), "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return view;
        }

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), menu_icon);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());


                // Access the menu item directly by its ID
                //for font family
                MenuItem menuItem = popupMenu.getMenu().findItem(R.id.cheack1);
                MenuItem menuItem2 = popupMenu.getMenu().findItem(R.id.cheack2);
                MenuItem menuItem3 = popupMenu.getMenu().findItem(R.id.cheack3);
                MenuItem menuItem4 = popupMenu.getMenu().findItem(R.id.cheack4);
                MenuItem menuItem5 = popupMenu.getMenu().findItem(R.id.cheack5);
                //for font  size
                MenuItem menuItem6 = popupMenu.getMenu().findItem(R.id.font12);
                MenuItem menuItem7 = popupMenu.getMenu().findItem(R.id.font13);
                MenuItem menuItem8 = popupMenu.getMenu().findItem(R.id.font14);
                MenuItem menuItem9 = popupMenu.getMenu().findItem(R.id.font15);
                MenuItem menuItem10 = popupMenu.getMenu().findItem(R.id.font16);
                //for night mode
                MenuItem menuItem11 = popupMenu.getMenu().findItem(R.id.nightModeSwitch);
                //for color
                MenuItem menuItem12 = popupMenu.getMenu().findItem(R.id.colorPurple);
                MenuItem menuItem13 = popupMenu.getMenu().findItem(R.id.colorOrange);
                MenuItem menuItem14 = popupMenu.getMenu().findItem(R.id.colorRed);
                MenuItem menuItem15 = popupMenu.getMenu().findItem(R.id.colorLightBlue);
                MenuItem menuItem16 = popupMenu.getMenu().findItem(R.id.colorGreen);


                if (menuItem != null && menuItem2 != null && menuItem3 != null && menuItem4 != null && menuItem5 != null) {
                    menuItem.setTitle(sharedPref.getString("fontFamily1", "Agbalumo"));
                    menuItem2.setTitle(sharedPref.getString("fontFamily2", "Montserrat"));
                    menuItem3.setTitle(sharedPref.getString("fontFamily3", "Oswald"));
                    menuItem4.setTitle(sharedPref.getString("fontFamily4", "Lora"));
                    menuItem5.setTitle(sharedPref.getString("fontFamily5", "Edu TAS Beginner"));
                }

                //for font
                if (menuItem6 != null && menuItem7 != null && menuItem8 != null && menuItem9 != null && menuItem10 != null) {
                    menuItem6.setTitle(sharedPref.getString("fontSize12", "12"));
                    menuItem7.setTitle(sharedPref.getString("fontSize13", "13"));
                    menuItem8.setTitle(sharedPref.getString("fontSize14", "14"));
                    menuItem9.setTitle(sharedPref.getString("fontSize15", "15"));
                    menuItem10.setTitle(sharedPref.getString("fontSize16", "16"));
                }

                //for night mode
                if (menuItem11 != null) {
                    menuItem11.setTitle(sharedPref.getString("nightMode", "OFF"));
                }

                //for color
                if (menuItem12 != null && menuItem13 != null && menuItem14 != null && menuItem15 != null && menuItem16 != null) {
                    menuItem12.setTitle(sharedPref.getString("color1", "Purple"));
                    menuItem13.setTitle(sharedPref.getString("color2", "Orange"));
                    menuItem14.setTitle(sharedPref.getString("color3", "Red"));
                    menuItem15.setTitle(sharedPref.getString("color4", "Light Blue"));
                    menuItem16.setTitle(sharedPref.getString("color5", "Light Green"));
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int menuItemId = menuItem.getItemId(); // Get the ID of the clicked item

                        // Handle menu item clicks
                        switch (menuItemId) {
                            case R.id.cheack1:
                                //Toast.makeText(getActivity(), "A........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontFamily1", "Agbalumo             \u2713");
                                editor.putString("fontFamily2", "Montserrat");
                                editor.putString("fontFamily3", "Oswald");
                                editor.putString("fontFamily4", "Lora");
                                editor.putString("fontFamily5", "Edu TAS Beginner");

                                editor.putString("fontFamilyHolder", "agbalumo");
                                onChangedListener.onFontFamilyChenger("agbalumo");
                                editor.apply();
                                return true;
                            case R.id.cheack2:
                                //Toast.makeText(getActivity(), "B........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontFamily1", "Agbalumo");
                                editor.putString("fontFamily2", "Montserrat           \u2713");
                                editor.putString("fontFamily3", "Oswald");
                                editor.putString("fontFamily4", "Lora");
                                editor.putString("fontFamily5", "Edu TAS Beginner");

                                editor.putString("fontFamilyHolder", "montserrat");
                                onChangedListener.onFontFamilyChenger("montserrat");
                                editor.apply();
                                return true;
                            case R.id.cheack3:
                                //Toast.makeText(getActivity(), "C........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontFamily1", "Agbalumo");
                                editor.putString("fontFamily2", "Montserrat");
                                editor.putString("fontFamily3", "Oswald               \u2713");
                                editor.putString("fontFamily4", "Lora");
                                editor.putString("fontFamily5", "Edu TAS Beginner");

                                editor.putString("fontFamilyHolder", "oswald");
                                onChangedListener.onFontFamilyChenger("oswald");
                                editor.apply();
                                return true;
                            case R.id.cheack4:
                                //Toast.makeText(getActivity(), "D........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontFamily1", "Agbalumo");
                                editor.putString("fontFamily2", "Montserrat");
                                editor.putString("fontFamily3", "Oswald");
                                editor.putString("fontFamily4", "Lora                 \u2713");
                                editor.putString("fontFamily5", "Edu TAS Beginner");

                                editor.putString("fontFamilyHolder", "lora");
                                onChangedListener.onFontFamilyChenger("lora");
                                editor.apply();
                                return true;
                            case R.id.cheack5:
                                //Toast.makeText(getActivity(), "E........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontFamily1", "Agbalumo");
                                editor.putString("fontFamily2", "Montserrat");
                                editor.putString("fontFamily3", "Oswald");
                                editor.putString("fontFamily4", "Lora");
                                editor.putString("fontFamily5", "Edu TAS Beginner     \u2713");

                                editor.putString("fontFamilyHolder", "edu_tas_beginner");
                                onChangedListener.onFontFamilyChenger("edu_tas_beginner");
                                editor.apply();
                                return true;
                            case R.id.font12:
                                //Toast.makeText(getActivity(), "12........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontSize12", "12             \u2713");
                                editor.putString("fontSize13", "13");
                                editor.putString("fontSize14", "14");
                                editor.putString("fontSize15", "15");
                                editor.putString("fontSize16", "16");

                                editor.putInt("fontSizeHolder", 12);
                                onChangedListener.onFontSizeChenger(12);
                                editor.apply();
                                return true;
                            case R.id.font13:
                                //Toast.makeText(getActivity(), "13........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontSize12", "12");
                                editor.putString("fontSize13", "13             \u2713");
                                editor.putString("fontSize14", "14");
                                editor.putString("fontSize15", "15");
                                editor.putString("fontSize16", "16");

                                editor.putInt("fontSizeHolder", 13);
                                onChangedListener.onFontSizeChenger(13);
                                editor.apply();
                                return true;
                            case R.id.font14:
                                //Toast.makeText(getActivity(), "14........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontSize12", "12");
                                editor.putString("fontSize13", "13");
                                editor.putString("fontSize14", "14             \u2713");
                                editor.putString("fontSize15", "15");
                                editor.putString("fontSize16", "16");

                                editor.putInt("fontSizeHolder", 14);
                                onChangedListener.onFontSizeChenger(14);
                                editor.apply();
                                return true;
                            case R.id.font15:
                                //Toast.makeText(getActivity(), "15........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontSize12", "12");
                                editor.putString("fontSize13", "13");
                                editor.putString("fontSize14", "14");
                                editor.putString("fontSize15", "15             \u2713");
                                editor.putString("fontSize16", "16");

                                editor.putInt("fontSizeHolder", 15);
                                onChangedListener.onFontSizeChenger(15);
                                editor.apply();
                                return true;
                            case R.id.font16:
                                //Toast.makeText(getActivity(), "16........", Toast.LENGTH_SHORT).show();
                                editor.putString("fontSize12", "12");
                                editor.putString("fontSize13", "13");
                                editor.putString("fontSize14", "14");
                                editor.putString("fontSize15", "15");
                                editor.putString("fontSize16", "16             \u2713");

                                editor.putInt("fontSizeHolder", 16);
                                onChangedListener.onFontSizeChenger(16);
                                editor.apply();
                                return true;
                            case R.id.nightModeSwitch:
                                String str = sharedPref.getString("nightMode", "OFF");
                                if (str.equals("OFF")) {
                                    editor.putString("nightMode", "ON");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        setting_icon.setImageResource(R.drawable.ic_setting_book2);
                                        alarm_icon.setImageResource(R.drawable.ic_notification_book2);
                                        menu_icon.setImageResource(R.drawable.ic_menu_book2);
                                        titleLogo.setTextColor(Color.WHITE);
                                        alermTxt.setTextColor(Color.WHITE);
                                        topBar.setBackgroundColor(getResources().getColor(R.color.light_dark));
                                        Window window = requireActivity().getWindow();;
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(getResources().getColor(R.color.light_dark));
                                    }
                                    onChangedListener.onNightModeChnger("ON");
                                } else {
                                    editor.putString("nightMode", "OFF");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        setting_icon.setImageResource(R.drawable.ic_setting_book);
                                        alarm_icon.setImageResource(R.drawable.ic_notification_book);
                                        menu_icon.setImageResource(R.drawable.ic_menu_book);
                                        titleLogo.setTextColor(Color.BLACK);
                                        alermTxt.setTextColor(Color.BLACK);
                                        topBar.setBackgroundColor(Color.RED);
                                        Window window = requireActivity().getWindow();;
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(Color.RED);
                                    }
                                    onChangedListener.onNightModeChnger("OFF");
                                }
                                editor.apply();
                                return true;
                            case R.id.colorGreen:
                                editor.putString("color5", "Light Green              \u2713");
                                editor.putString("color1", "Purple");
                                editor.putString("color2", "Orange");
                                editor.putString("color3", "Red");
                                editor.putString("color4", "Light Blue");

                                editor.putString("colorBg", "btn_light_green");
                                onChangedListener.onColorChanged("btn_light_green");
                                editor.apply();
                                return true;
                            case R.id.colorPurple:
                                editor.putString("color5", "Light Green");
                                editor.putString("color1", "Purple              \u2713");
                                editor.putString("color2", "Orange");
                                editor.putString("color3", "Red");
                                editor.putString("color4", "Light Blue");

                                editor.putString("colorBg", "bar_purple");
                                onChangedListener.onColorChanged("bar_purple");
                                editor.apply();
                                return true;
                            case R.id.colorOrange:
                                editor.putString("color5", "Light Green");
                                editor.putString("color1", "Purple");
                                editor.putString("color2", "Orange              \u2713");
                                editor.putString("color3", "Red");
                                editor.putString("color4", "Light Blue");

                                editor.putString("colorBg", "bar_orange");
                                onChangedListener.onColorChanged("bar_orange");
                                editor.apply();
                                return true;
                            case R.id.colorRed:
                                editor.putString("color5", "Light Green");
                                editor.putString("color1", "Purple");
                                editor.putString("color2", "Orange");
                                editor.putString("color3", "Red                 \u2713");
                                editor.putString("color4", "Light Blue");

                                editor.putString("colorBg", "bar_red");
                                onChangedListener.onColorChanged("bar_red");
                                editor.apply();
                                return true;
                            case R.id.colorLightBlue:
                                editor.putString("color5", "Light Green");
                                editor.putString("color1", "Purple");
                                editor.putString("color2", "Orange");
                                editor.putString("color3", "Red");
                                editor.putString("color4", "Light Blue          \u2713");

                                editor.putString("colorBg", "light_blue");
                                onChangedListener.onColorChanged("light_blue");
                                editor.apply();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        alarm_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Alerm clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), alertClass.class);
                //intent.putExtra("flag","gggg");
                view.getContext().startActivity(intent);
            }
        });

        setting_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Setting clicked", Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu = new PopupMenu(getActivity(), menu_icon);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_setting, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Toast.makeText(getActivity(), "Menu clicked" + menuItem, Toast.LENGTH_SHORT).show();

                        int menuItemId = menuItem.getItemId();
                        if (menuItemId == R.id.site1) { // Correctly comparing menu item ID
                            //Toast.makeText(getContext(), "Site1", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pdfdrive.com"));

                            // Use requireActivity() to get the activity associated with the fragment
                            if (requireActivity().getPackageManager() != null && intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "No app to handle this action", Toast.LENGTH_SHORT).show();
                            }
                        }else if(menuItemId == R.id.site2){
                            //Toast.makeText(getContext(), "Site2", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://manybooks.net"));

                            // Use requireActivity() to get the activity associated with the fragment
                            if (requireActivity().getPackageManager() != null && intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "No app to handle this action", Toast.LENGTH_SHORT).show();
                            }
                        }else if(menuItemId == R.id.site3){
                            //Toast.makeText(getContext(), "Site 3", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gutenberg.org"));

                            // Use requireActivity() to get the activity associated with the fragment
                            if (requireActivity().getPackageManager() != null && intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "No app to handle this action", Toast.LENGTH_SHORT).show();
                            }
                        }else if(menuItemId == R.id.site4){
                            //Toast.makeText(getContext(), "Site 4", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://openlibrary.org"));

                            // Use requireActivity() to get the activity associated with the fragment
                            if (requireActivity().getPackageManager() != null && intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "No app to handle this action", Toast.LENGTH_SHORT).show();
                            }
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.popup_menu, menu); // Inflate your menu resource
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setTextAlert(String msg) {
        if (alermTxt != null) {
            alermTxt.setText(msg);
        }
    }

}