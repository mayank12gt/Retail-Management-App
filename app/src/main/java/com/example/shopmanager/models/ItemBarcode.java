package com.example.shopmanager.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_barcodes",
        foreignKeys = @ForeignKey(entity = InventoryItem.class,
                parentColumns = "item_id",
                childColumns = "item_id",
                onDelete =ForeignKey.CASCADE))

public class ItemBarcode {
    @ColumnInfo(name = "item_id")
    private int id;
    @ColumnInfo(name = "code")
    @PrimaryKey
    @NonNull
    private String barcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
