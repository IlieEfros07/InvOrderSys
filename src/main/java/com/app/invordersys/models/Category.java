package com.app.invordersys.models;

public class Category {

    private int categoryId;
    private String categoryName;

    public Category(int catId, String catName) {
        this.categoryId = catId;
        this.categoryName = catName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getId() {
        return categoryId;
    }

    public String getName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
