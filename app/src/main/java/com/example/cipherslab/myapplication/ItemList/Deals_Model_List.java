package com.example.cipherslab.myapplication.ItemList;

public class Deals_Model_List {
    public String getProduct_quantitiy() {
        return Product_quantitiy;
    }

    public void setProduct_quantitiy(String product_quantitiy) {
        Product_quantitiy = product_quantitiy;
    }

    public String getProduct_total_price() {
        return Product_total_price;
    }

    public void setProduct_total_price(String product_total_price) {
        Product_total_price = product_total_price;
    }

    private String Product_quantitiy;
    String Product_total_price;

    public Deals_Model_List(String product_quantitiy, String product_name, int product_price, int product_total_price) {
            Product_quantitiy = product_quantitiy;
            Product_total_price = String.valueOf (product_total_price);
            Deal_Name = product_name;
            Deal_Price = String.valueOf (product_price);
    }

    public String getDeal_Name() {
        return Deal_Name;
    }

    public void setDeal_Name(String deal_Name) {
        Deal_Name = deal_Name;
    }

    public String getDeal_Price() {
        return Deal_Price;
    }

    public void setDeal_Price(String deal_Price) {
        Deal_Price = deal_Price;
    }

    public String getDeal_Image() {
        return Deal_Image;
    }

    public void setDeal_Image(String deal_Image) {
        Deal_Image = deal_Image;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getDeal_Descrip() {
        return Deal_Descrip;
    }

    public void setDeal_Descrip(String deal_Descrip) {
        Deal_Descrip = deal_Descrip;
    }

    public Deals_Model_List(String id ,String deal_Name, String deal_Price, String deal_Image, String expiryDate, String deal_Descrip) {
        Deal_Name = deal_Name;
        Deal_Price = deal_Price;
        Deal_Image = deal_Image;
        ExpiryDate = expiryDate;
        Deal_Descrip = deal_Descrip;
    }


    private  String Deal_Name;
    private String Deal_Price;
    private String Deal_Image;
    private String ExpiryDate;
    private String Deal_Descrip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}
