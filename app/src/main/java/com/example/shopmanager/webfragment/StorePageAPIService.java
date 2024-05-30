package com.example.shopmanager.webfragment;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StorePageAPIService {
    @Headers("Content-Type: application/json")
    @POST("v1/rows/cBGUcoNNd4kZ")
    Call<List<WebPageItem>> postData(@Body List<WebPageItem> data);

    @Headers("Content-Type: application/json")
    @PUT("v1/rows/cBGUcoNNd4kZ")
    Call<WebPageItem> updateData(@Body WebPageItem item);
}
