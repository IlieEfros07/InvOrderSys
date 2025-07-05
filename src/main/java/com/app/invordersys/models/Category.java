package com.app.invordersys.models;

public class Category {

    public int category_id;

    public String category_name;

    public Category(int catId , String catName ){this.category_id=catId; this.category_name=catName;}

    public int getId(){
        return this.category_id;
    }

    public String getName(){
        return this.category_name;
    }

    @Override
    public String toString(){
        return this.category_name;
    }


}
