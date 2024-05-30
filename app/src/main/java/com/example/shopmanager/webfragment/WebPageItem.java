package com.example.shopmanager.webfragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebPageItem {

    public String getWeb_id() {
        return web_id;
    }

    public void setWeb_id(String web_id) {
        this.web_id = web_id;
    }

    @SerializedName("_id")
    @Expose
    private String web_id;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("unit")
        @Expose
        private String unit;
        @SerializedName("available_stock")
        @Expose
        private Integer availableStock;
        @SerializedName("selling_price")
        @Expose
        private Double sellingPrice;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("item_image")
        @Expose
        private String itemImage;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Integer getAvailableStock() {
            return availableStock;
        }

        public void setAvailableStock(Integer availableStock) {
            this.availableStock = availableStock;
        }

        public Double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(Double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getItemImage() {
            return itemImage;
        }

        public void setItemImage(String itemImage) {
            this.itemImage = itemImage;
        }

    }

