package com.example.shopmanager.models;

public class Alert {
    InventoryItem item;

    public Alert(InventoryItem item, String alert) {
        this.item = item;
        this.alert = alert;
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    String alert;
}
