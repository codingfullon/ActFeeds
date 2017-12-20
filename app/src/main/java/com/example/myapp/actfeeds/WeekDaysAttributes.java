package com.example.myapp.actfeeds;

/**
 * Created by KavitaPC on 10/27/2017.
 */

public class WeekDaysAttributes {

    String itemName;
    boolean aBoolean;

    public WeekDaysAttributes(String itemName, boolean aBoolean){
        this.itemName = itemName;
        this.aBoolean = aBoolean;
    }
    public String getItemName(){
        return itemName;
    }
    public boolean getBoolean(){
        return aBoolean;
    }
}
