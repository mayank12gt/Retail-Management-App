package com.example.shopmanager.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class ItemCategory {
    @ColumnInfo(name = "category_id")
    private int id;
    @ColumnInfo(name = "category")
    @PrimaryKey
    @NonNull
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }
}
