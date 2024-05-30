package com.example.shopmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopmanager.models.ItemBarcode;

import java.util.List;

@Dao
public interface ItemBarcodesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addBarcodetoItem(ItemBarcode barcode);

    @Delete
    void removeBarcodefromItem(ItemBarcode barcode);

    @Query("delete from item_barcodes where code = :code")
    void removeBarcodefromItem(String code);

    @Update
    public void updateBarcode(ItemBarcode barcode);


    @Query("select code from item_barcodes order by item_Id DESC ")
    List<String> getAllBarcodeValues();

    @Query("select 1 from item_barcodes where code = :code")
    int checkIfBarcodeAlreadyExists(String code);

    @Query("select * from item_barcodes where item_id= :itemId order by item_Id DESC ")
    LiveData<List<ItemBarcode>> getAllBarcodesforItem(int itemId);

    @Query("select count(*)  from item_barcodes where item_id= :itemId")
    public int getAvailableStockforItem(int itemId);


}
