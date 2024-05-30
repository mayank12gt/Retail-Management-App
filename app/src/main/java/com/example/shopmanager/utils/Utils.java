package com.example.shopmanager.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDate(long timestamp){
        Date date = new Date(timestamp);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm a");


        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
