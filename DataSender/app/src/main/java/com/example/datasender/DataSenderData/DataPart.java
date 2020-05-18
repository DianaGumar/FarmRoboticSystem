package com.example.datasender.DataSenderData;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataPart implements Serializable {

    private static DataPart instance;

    public static DataPart getInstance(){
        return (instance == null) ? instance = new DataPart() : instance;
    }

    public int ID;
    private double Data;
    private String Time;

    public void setData(int id, double data){
        ID = id;
        Data = data;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Time = dateFormat.format(cal.getTime());
    }

    @NonNull
    @Override
    public String toString() {
        return ID + ";" + Time + ";" + Data;
    }
}
