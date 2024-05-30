package com.example.shopmanager.utils;

import android.util.Log;

import com.example.shopmanager.models.InventoryItem;
import com.google.gson.Gson;

public class JsonParser {
    public static InventoryItem parseJson(String jsonString)  {
        Gson gson = new Gson();
        InventoryItem item= new InventoryItem();
        try {
            item =  gson.fromJson(jsonString, InventoryItem.class);
        }
        catch (Exception e){
            Log.d("Err",e.getLocalizedMessage());
        }

            return item;



    }
}