package com.example.shopmanager.utils;

import android.content.Context;

public class GetStoreData {
    Context context;
    public GetStoreData(Context context){
        this.context = context;
    }
    public String getStoreName(){
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_name","Demo Store");
    }
    public String getStoreAddr(){
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_addr","Demo Store Address");
    }
    public String getStoreContact(){
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_contact","Demo Store");
    }
    public String getStoreUPIId(){
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_upi_id","Demo Store");
    }
    public String getStoreEmail(){
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_email","Demo Store");
    }
    public String getStorePassword(){
        return context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_password","Demo Store");
    }

    public void setStoreName(String name){
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit().putString("store_name",name);
    }

    public void setStoreEmail(String email){
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit().putString("store_email",email);
    }

    public void setStoreContact(String contact){
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit().putString("store_contact",contact);
    }
    public void setStoreUPIId(String upiId){
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit().putString("store_upi_id",upiId);
    }
    public void setStoreAddr(String addr){
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit().putString("store_addr",addr);
    }
    public void setStorePassword(String password){
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).edit().putString("store_password",password);
    }





}
