package com.example.shopmanager.models;

import androidx.annotation.NonNull;

public class CategoryItem {
    private String category;
    private Boolean isAddItem;

    @NonNull
    @Override
    public String toString() {
        return category;
    }

    public CategoryItem(String category, Boolean isAddItem) {
        this.category = category;
        this.isAddItem = isAddItem;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getAddItem() {
        return isAddItem;
    }

    public void setAddItem(Boolean addItem) {
        isAddItem = addItem;
    }
}
