package com.example.datasender.DataSenderData;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializeObject {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static <T> void Serialize(String name, T t){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(name)))
        {
            oos.writeObject(t);
            oos.flush();

            Log.d("info", "serialize operation ended");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
