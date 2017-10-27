package com.example.kavitapc.fitnessreminder;

/**
 * Created by KavitaPC on 10/19/2017.
 */

public class ItemAttributes {
    String itemName;
    int icon;

    public ItemAttributes(String itemName, int icon){
        this.itemName = itemName;
        this.icon = icon;
    }
    public String getItemName(){
        return itemName;
    }
    public int getIcon(){
        return icon;
    }
}
