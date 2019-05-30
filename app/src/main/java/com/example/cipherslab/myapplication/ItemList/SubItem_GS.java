package com.example.cipherslab.myapplication.ItemList;

public class SubItem_GS {

    private String ItemName;
    private String Image;
    private int ItemPrice;
    private int ItemQuantity;
    private int GrandTotal;
    private int TotalItemPrice;
    private int Id;
    private int count;

    //getter

    public String    getItemName() {
        return ItemName;
    }
    public String    getImage() {
        return Image;
    }
    public int       getItemPrice() {
        return ItemPrice;
    }
    public int       getItemQuantity() {
        return ItemQuantity;
    }
    public int       getTotalAmount() {
        return GrandTotal;
    }
    public int       getTotalItemPrice() {
        return TotalItemPrice;
    }
    public int       getId() {
        return Id;
    }
    public int       getCount() {
        return count;
    }


    //setter
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    public void setImage(int image) {
        Image = String.valueOf (image);
    }
    public void setItemPrice(int itemPrice) {
        ItemPrice = itemPrice;
    }
    public void setItemQuantity(int itemQuantity) {
        ItemQuantity = itemQuantity;
    }
    public void setTotalAmount(int totalAmount) {
        GrandTotal = totalAmount;
    }
    public void setTotalItemPrice(int TotalItem_Price) {
        TotalItemPrice = TotalItem_Price;
    }
    public void setId(int id) {
        this.Id = id;
    }
    public void setCount(int count) {
        this.count = count;
    }



    //Constructers

    public SubItem_GS(String prod_name, int product_price_Int, int count, int p_mult_Q) {
        ItemName = prod_name;
        ItemPrice = product_price_Int;
        ItemQuantity = count;
        TotalItemPrice = p_mult_Q;

    }

    public SubItem_GS(int id, String name, int price, int quantity, int TotalItem_Price, int total) {
    Id  =id;
        ItemName = name;
        ItemPrice = price;
        ItemQuantity = quantity;
        TotalItemPrice = TotalItem_Price;
        GrandTotal = total;

    }

    public SubItem_GS(String product_image, String product_name, int product_price) {
        ItemName = product_name;
        ItemPrice = product_price;
        Image = product_image;
    }


    public SubItem_GS(String itemName, int itemQuantity) {
        ItemName = itemName;
        ItemQuantity = itemQuantity;
    }

    public SubItem_GS(String itemName,int itemPrice ,int itemQuantity) {

        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemQuantity = itemQuantity;
    }






}