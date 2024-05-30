package com.example.shopmanager.webfragment;

import android.content.Context;
import android.util.Log;

import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.repo.Repo;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncData {

    StorePageAPIService apiService;

    Repo repo;
    public SyncData(Repo repo){
         apiService = StorePageAPIClient.getRetrofit().create(StorePageAPIService.class);
         this.repo = repo;
    }

    public  void addItemstoWebpage(List<InventoryItem> items){



        Log.d("sync","called");
        List<WebPageItem> itemList = new ArrayList<>();


        for (InventoryItem it:
             items) {
            itemList.add(it.toWebPageItem(it));
        }

        apiService.postData(itemList).enqueue(new Callback<List<WebPageItem>>() {
            @Override
            public void onResponse(Call<List<WebPageItem>> call, Response<List<WebPageItem>> response) {
                if (response.isSuccessful()) {

                    List<WebPageItem> result = response.body();
                    Log.d("added",response.toString());
                    for (WebPageItem i:
                         result) {
                        Log.d("addedItemid",String.valueOf(i.getId()));
                        Log.d("addedItemwebid",String.valueOf(i.getWeb_id()));
                        repo.setWebIdforItem(i.getId(),i.getWeb_id());
                    }
                    // Process the result
                } else {
                    // Handle error response
                    Log.e("Error", "Error in API request: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<WebPageItem>> call, Throwable t) {
                Log.e("Error", "Error in API request: " + t.getMessage());
            }
        });
    }

    public  void updateItemonWebPage(InventoryItem item) {


        Log.d("webid",item.getWebpage_id());
        WebPageItem item1 = item.toWebPageItem(item);
        item1.setWeb_id(item.getWebpage_id());


        apiService.updateData(item1).enqueue(new Callback<WebPageItem>() {
            @Override
            public void onResponse(Call<WebPageItem> call, Response<WebPageItem> response) {
                if (response.isSuccessful()) {

                   WebPageItem result = response.body();
                    Log.d("added",response.toString());

                        Log.d("addedItemid",String.valueOf(result.getId()));
                        Log.d("addedItemwebid",String.valueOf(result.getWeb_id()));


                    // Process the result
                } else {
                    // Handle error response
                    Log.e("Error", "Error in API request: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WebPageItem> call, Throwable t) {

            }
        });

    }
}
