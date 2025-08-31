package com.example.bruh;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomManager {

    void setit(Activity activity, int selectedItemId){

        BottomNavigationView btm = activity.findViewById(R.id.bottom_navigation);
        btm.setSelectedItemId(selectedItemId);
        btm.setOnItemSelectedListener(item ->{
        int id = item.getItemId();
        if(id == R.id.item_1){
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return true;
        }
        else if(id == R.id.item_2){
            Intent intent = new Intent(activity, activity_weather.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return true;
        }
        else if(id == R.id.item_3){
            Intent intent = new Intent(activity, Currency.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return true;
        }
        else if(id == R.id.item_4){
            Intent intent = new Intent(activity, NewsMain.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return true;
        }
        return true;
    });
    }

}
