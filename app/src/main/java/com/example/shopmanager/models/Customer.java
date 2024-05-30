package com.example.shopmanager.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "customers")
public class Customer implements Serializable {
    @ColumnInfo(name = "customer_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "customer_name")
    private String name;

    @ColumnInfo(name = "customer_contact_num")
    private String contactNum;


    @ColumnInfo(name = "customer_email")
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
