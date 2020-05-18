package com.example.datasender.SocketPart;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class DataSenderTask extends AsyncTask<String, Void, Boolean> {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Boolean doInBackground(String ... params) {
        try {
            CarSocket carSocket = CarSocket.getInstance();
            Log.d("info", "start connection");
            carSocket.connectToDevice(params[0], Integer.parseInt(params[1]));

            if(carSocket.isConnected()){
                String messageFromServer = carSocket.getMessage();

                if(messageFromServer != null){
                    Log.d("info", "connect successful");

                    if(carSocket.sendMessage("2")) {
                        //отправляем сериализованные данные
                        Thread.sleep(500);
                        carSocket.sendMessage(params[2]);

//                        Log.d("info", "start serialize operation");
//                        SerializeObject.Serialize( params[2], DataPart.getInstance());
//                        Log.d("info", "start sended file operation");
//                        Boolean b = carSocket.sendObject(params[2]);

                        carSocket.unconnectToDevice();

                        return true;
//                        return carSocket.sendMessage(DataPart.getInstance().toString());

                    }
                }

            }else {
                Log.d("info", "Can't connected");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}