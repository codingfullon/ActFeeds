package com.inkeep.actfeeds.utilities;

/**
 * Created by KavitaPC on 12/14/2017.
 * To set tag for 2 values in adapter
 */

public class MyObject {
    private int idUserHabit;
    private int idHabitStatus;
    MyObject(int idUserHabit, int idHabitStatus){
        this.idUserHabit=idUserHabit;
        this.idHabitStatus=idHabitStatus;
    }
    public int getIdUserHabit(){
        return idUserHabit;
    }
    public int getIdHabitStatus(){
        return idHabitStatus;
    }
}
