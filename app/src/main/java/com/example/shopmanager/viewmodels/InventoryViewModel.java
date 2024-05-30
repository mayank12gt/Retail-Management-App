package com.example.shopmanager.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Query;

import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.ItemBarcode;
import com.example.shopmanager.repo.Repo;
import com.example.shopmanager.webfragment.SyncData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryViewModel extends AndroidViewModel {

    Repo repo;
    MutableLiveData<Boolean> getAllItemsLoadingStatus = new MutableLiveData<>(true);
    public InventoryViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application.getApplicationContext());
    }

    public LiveData<List<InventoryItem>> getAllItemsinInventory(){

        return repo.getAllItemsinInventory();
    }

    // To add items to inventory through barcode
    public void addItemtoInventory(InventoryItem item, List<ItemBarcode> barcodes){
        ExecutorService service= Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                repo.addItemtoInventory(item,barcodes);
            }
        });

    }

    // To add items to inventory by QR
    public void addItemtoInventory(InventoryItem item){

        ExecutorService service= Executors.newSingleThreadExecutor();
        service.execute(new Runnable(){
            @Override
            public void run() {repo.addItemtoInventory(item);}});
    }

    public void removeItemfromInventory(InventoryItem item){
        repo.removeItemfromInventory(item);
    }

    public void updateIteminInventory(InventoryItem item){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                repo.updateIteminInventory(item);
            }
        });

    }

    public int getAvailableStockforItem(int itemId){
        return repo.getAvailableStockforItem(itemId);
    }

    public boolean checkIfBarcodeAlreadyExists(String code){
     return repo.checkIfBarcodeAlreadyExists(code);

    }

    public void addBarcodestoItem(List<String> codesList, int id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                for (String c:
                        codesList) {
                    ItemBarcode barcode = new ItemBarcode();
                    barcode.setId(id);
                    barcode.setBarcode(c);
                    repo.addBarcodetoInventoryItem(barcode);

                }


                int availableStock = repo.getAvailableStockforItem((int) id);
                repo.setAvailableStockforItem(availableStock, (int) id);
                InventoryItem item = repo.getItemfromId(id);
                item.setAvailableStock(availableStock);
                Log.d("item",String.valueOf(item.getId()));
                item.setTotalCost(Double.valueOf(availableStock*item.getPurchasePrice()));
                item.setTotalValue(availableStock*item.getSellingPrice());
                Log.d("totalCost",String.valueOf(availableStock*item.getPurchasePrice()));
                Log.d("totalValue",String.valueOf(availableStock*item.getSellingPrice()));
                repo.updateIteminInventory(item);

            }
        });


    }


    public boolean checkIfItemAlreadyExists(String qrSKU) {
        return repo.checkIfItemAlreadyExists(qrSKU);
    }

    public InventoryItem getItemfromSKU(String qrSKU) {

        return repo.getItemfromSKU(qrSKU);
    }

    public LiveData<List<InventoryItem>> getOutofStockItems() {
        return repo.getOutofStockItems();
    }

    public LiveData<List<InventoryItem>> getNonBarcodeandInStockItemsinInventory() {
        return repo.getNonBarcodeandInStockItemsinInventory();
    }
}
