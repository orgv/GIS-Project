package com.example.consolefindergis;

public class SalesInfo {
    public SalesInfo(String title, String description, String sellerName, String itemPrice, String sellerNumber,String city) {
        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
        this.itemPrice = itemPrice;
        this.city=city;
        this.sellerNumber = sellerNumber;
    }

    public SalesInfo() {
    }

    String title;
    String city;
    String description;
    String sellerName;
    String itemPrice;
    String sellerNumber;
}
