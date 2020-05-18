package com.example.carclient.ClientSocket;

import android.os.AsyncTask;
import android.util.Log;

public class CarTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String ... params) {
        try {
            CarSocket carSocket = CarSocket.getInstance();
            carSocket.connectToDevice(params[0], Integer.parseInt(params[1]));

            if(carSocket.isConnected()){
                String messageFromServer = carSocket.getMessage();

                if(messageFromServer != null){
                    Log.d("info", "connect successful");

                    return true;
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