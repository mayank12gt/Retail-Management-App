package com.example.shopmanager.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.shopmanager.webfragment.WebPageItem;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "inventory_items")
public class InventoryItem implements Serializable {

    @ColumnInfo(name = "item_id",index = true)
    @PrimaryKey(autoGenerate = true)

 private int id;

    @ColumnInfo(name = "webpage_id")
    private String webpage_id;

    public String getWebpage_id() {
        return webpage_id;
    }

    public void setWebpage_id(String webpage_id) {
        this.webpage_id = webpage_id;
    }

    @ColumnInfo(name = "desc")
    private String desc;
    @ColumnInfo(name = "name")
    @NonNull
    private String name;


    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "low_stock_level")
    private int lowStockLevel;
    @ColumnInfo(name = "available_stock")
    private int availableStock;
    @ColumnInfo(name = "unit")
    private String unit;
    @ColumnInfo(name = "purchase_price")
    private double purchasePrice;
    @ColumnInfo(name = "total_cost")
    private double totalCost;
    @ColumnInfo(name = "selling_price")
    private double sellingPrice;
    @ColumnInfo(name = "total_value")
    private double totalValue;


    @ColumnInfo(name = "is_Barcode")
    private boolean isBarcode;

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    @ColumnInfo(name = "QR_sku",defaultValue ="")

    private String SKU;

    @ColumnInfo(name="item_image")
    String image;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getIsBarcode() {
        return isBarcode;
    }

    public void setIsBarcode(boolean barcode) {
        isBarcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLowStockLevel() {
        return lowStockLevel;
    }

    public void setLowStockLevel(int lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public WebPageItem toWebPageItem(InventoryItem inItem) {
        Log.d("itemId",String.valueOf(inItem.getId()));
        WebPageItem item = new WebPageItem();
        item.setName(inItem.getName());
        item.setDesc(inItem.getDesc());
        item.setId(inItem.getId());
        item.setAvailableStock(inItem.getAvailableStock());
        item.setCategory(inItem.getCategory());
        item.setSellingPrice(inItem.getSellingPrice());
        item.setItemImage("");
        item.setUnit(inItem.getUnit());
        Log.d("webitem",String.valueOf(item.getId()));
        return item;
    }

//    public String getSku() {
//        return sku;
//    }
//
//    public void setSku(String sku) {
//        this.sku = sku;
//    }
}
