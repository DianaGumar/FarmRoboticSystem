package com.example.datasender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.datasender.SocketPart.CarSocket;
import com.example.datasender.DataSenderData.DataPart;

public class MainActivity extends AppCompatActivity {

    private Button b_enter;
    private EditText port;
    private EditText ip;
    private EditText id;
    private EditText data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_enter = findViewById(R.id.b_connect);
        port = findViewById(R.id.car_port);
        ip = findViewById(R.id.car_ip);
        id = findViewById(R.id.car_id);
        data = findViewById(R.id.car_data);


        b_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String p = port.getText().toString();
                String ip_str = ip.getText().toString();

                //создаём объект сокета
                //но не подключаем
                CarSocket carSocket = CarSocket.getInstance();
                carSocket.ip = ip_str;
                carSocket.port = p;


                //создааём объект данных
                DataPart dataPart = DataPart.getInstance();
                dataPart.setData(
                        Integer.parseInt(id.getText().toString()),
                        Double.parseDouble(data.getText().toString())
                );

                //отправляемся в рабочую активити
                Intent intent2 = new Intent(getApplicationContext(), WorkActivity.class);
                startActivity(intent2);


            }
        });

    }
}
