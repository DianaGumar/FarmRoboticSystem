package com.example.carclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carclient.ClientSocket.CarSocket;
import com.example.carclient.fragments.FragmentController;
import com.example.carclient.fragments.FragmentDataSender;
import com.example.carclient.fragments.FragmentMap;

import java.util.concurrent.ExecutionException;

public class WorkActivity extends AppCompatActivity {

    private Button b_controller;
    private Button b_map;
    private Button b_data_sender;

    private Button b_unconnected;

    protected TextView tv_connected;

    private CarSocket carSocket;
//    private ClientTypeTask clientTypeTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        tv_connected = findViewById(R.id.tv_connected);

        b_unconnected = findViewById(R.id.b_unconnected);
        b_controller = findViewById(R.id.b_controller);
        b_map = findViewById(R.id.b_map);
        b_data_sender = findViewById(R.id.b_data_sender);

        carSocket = CarSocket.getInstance();
//        clientTypeTask = new ClientTypeTask();

        b_unconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientTypeTask c = new ClientTypeTask();
                c.execute(0);

                boolean b = false;
                try {
                    b = c.get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(b){
                    if(carSocket.unconnectToDevice()){
                        tv_connected.setText("unconnected");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"unconnected" ,Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"can't unconnected" ,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        //прежде чем переходить на другой тип клиента необходимо убедиться- закончил ли работу другой тип
        //после того как клиент выполняет свою задачу- он разрывает соединение

        b_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //отправляю серверу необходимое сообщение, указывая на тип подключаемого клиента
                if(tv_connected.getText().equals("unconnected")){
                    Log.d("info", "click 1");
                    tv_connected.setText("connected");
                    new ClientTypeTask().execute(1);
                }
            }
        });

        b_data_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_connected.getText().equals("unconnected")){
                    tv_connected.setText("connected");
                    new ClientTypeTask().execute(2);
                }
            }
        });

        b_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_connected.getText().equals("unconnected")){
                    tv_connected.setText("connected");
                    new ClientTypeTask().execute(3);
                }
            }
        });

    }



    //единственная задача-отправить серверу сообщение о том, какой тип контроллера выбран
    //и открывать соответствующий фрагмент
    class ClientTypeTask extends AsyncTask<Integer, Void, Boolean> {

        private int id;

        //маленький фабричный метод
        private Fragment getFragment(int id){
            switch (id){
                case 0: return null;
                case 1: return new FragmentController();
                case 2: return new FragmentDataSender();
                case 3: return new FragmentMap();
            }
            return null;
        }


        @Override
        protected Boolean doInBackground(Integer ... params) {
            Log.d("info", "start asynck task with 1");
            try {

                if(carSocket.sendMessage(params[0].toString())) {
                    id = params[0];
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){
                //подключаю нужный фрагмент
                Log.d("info", "start go to fragment");
                Log.d("info", "id = ".concat(String.valueOf(id)));
                Fragment dv = getFragment(id);
                if(dv !=null){
                    Log.d("info", "fragment != null");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_data_list, dv).commit();
                }else {
                    Log.d("info", "fragment == null");
                }

            }else {
                Toast.makeText(getApplicationContext(),"can't send message to server" ,Toast.LENGTH_LONG).show();
            }

        }


    }



}
