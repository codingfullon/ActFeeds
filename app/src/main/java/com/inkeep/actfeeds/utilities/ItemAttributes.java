package com.inkeep.actfeeds.utilities;

/**
 * Created by KavitaPC on 10/19/2017.
 */

public class ItemAttributes {
    String itemName;
    String icon;

    public ItemAttributes(String itemName, String icon){
        this.itemName = itemName;
        this.icon = icon;
    }
    public String getItemName(){
        return itemName;
    }
    public String getIconName(){
        return icon;
    }
}
