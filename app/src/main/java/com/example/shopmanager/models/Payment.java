package com.example.shopmanager.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "payments")
public class Payment implements Serializable {
    @ColumnInfo(name = "amount")
    double amount;
    String senderName;
    @ColumnInfo(name = "timestamp")
            @PrimaryKey
    Long timestamp;

    public Payment(double amount,long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;

    }

    public double getAmount() {
        return amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public void setAmount(double amount) {
        this.amount = amount;
    }

}
