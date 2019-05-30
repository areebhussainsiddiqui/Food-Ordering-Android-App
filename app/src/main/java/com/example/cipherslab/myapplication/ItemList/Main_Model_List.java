package com.example.cipherslab.myapplication.ItemList;

public class Main_Model_List {


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public Main_Model_List(String category_Name,String image) {
        Image = image;
        Category_Name = category_Name;
    }

    private String Image;
    private String Category_Name;
}
