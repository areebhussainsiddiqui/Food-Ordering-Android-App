package com.example.cipherslab.myapplication.ItemList;

public class SubMenu {

    private String name;
    private String picture;
    private String descrpition_;
    private int price;

    //getter
    public String getName() {
        return name;
    }
    public String getPicture() {
        return picture;
    }
    public int getPrice() {
        return price;
    }
    public String getDescrpition_() {
        return descrpition_;
    }

    //Setter
    public void setName(String name) {
        this.name = name;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setDescrpition_(String descrpition_) {
        this.descrpition_ = descrpition_;
    }

   //Constructor
    public SubMenu(String name, String picture, int price, String descrip) {
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.descrpition_ = descrip;
    }



}
