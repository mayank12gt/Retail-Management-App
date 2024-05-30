package com.example.shopmanager.webfragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StorePageAPIClient {

    private static Retrofit retrofit;

    public static synchronized Retrofit getRetrofit(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl("https://api.tablebackend.com/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
