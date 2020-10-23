package com.example.androidproject01.models;


public class Category {
    private String categoryName;
    private int CategoryID;

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName, int categoryID) {
        this.categoryName = categoryName;
        CategoryID = categoryID;
    }

}
