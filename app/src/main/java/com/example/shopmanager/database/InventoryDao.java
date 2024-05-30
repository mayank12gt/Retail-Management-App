package com.example.shopmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.ItemBarcode;

import java.util.List;

@Dao
public interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addItemtoInventory(InventoryItem item);

    @Delete
    void removeItemfromInventory(InventoryItem item);

    @Update
    public void updateIteminInventory(InventoryItem item);

    @Query("select * from inventory_items order by item_id DESC ")
    LiveData<List<InventoryItem>> getAllItemsinInventory();

    @Query("update inventory_items set available_stock=:availableStock where item_id= :itemId")
    public void setAvailableStock(int availableStock, int itemId);

    @Query("select item_id from item_barcodes where code=:code")
    public int getItemIdforBarcode(String code);

    @Query("select * from inventory_items where item_id=:invoiceItemId")
   public InventoryItem getItemfromId(int invoiceItemId);

    @Insert
    void addBarcodetoInventoryItem(ItemBarcode barcode);

    @Query("select item_id from inventory_items where QR_sku=:code")
    int getItemIdforQRCode(String code);

    @Query("select 1 from inventory_items where QR_sku = :qrSKU and available_stock > 0")
    int checkIfItemisInStock(String qrSKU);

    @Query("select 1 from inventory_items where QR_sku = :qrSKU ")
    int checkIfItemAlreadyExists(String qrSKU);

    @Query("select * from inventory_items where available_stock < low_stock_level or available_stock < 1 order by item_id DESC ")
    LiveData<List<InventoryItem>> getOutofStockItems();
    @Query("select * from inventory_items where is_Barcode = :isBarcode and available_stock > 0 order by item_id DESC ")
    LiveData<List<InventoryItem>> getNonBarcodeandInStockItemsinInventory(boolean isBarcode);
    @Query("select available_stock from inventory_items where item_id = :itemId ")
    int getAvailableStockforItem(int itemId);


    @Query("update inventory_items set webpage_id=:webId where item_id= :id")

    void setWebIdforItem(int id, String webId);
}
