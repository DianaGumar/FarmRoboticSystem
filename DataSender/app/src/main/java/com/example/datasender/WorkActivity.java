package com.example.datasender;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.datasender.SocketPart.CarSocket;
import com.example.datasender.DataSenderData.DataPart;
import com.example.datasender.SocketPart.DataSenderTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class WorkActivity extends AppCompatActivity {

    LinearLayout ll_work;

    DataPart dataPart;

    TextView timer;
    TextView status;
    TextView sendered_data;

    Button b_timer_deny;
    Button b_timer_add;
    Button b_timer_enter;

    CountDownTimer countDownTimer;
    int time = 5000;
    boolean timer_start = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        ll_work = findViewById(R.id.ll_work);

        dataPart = DataPart.getInstance();

        timer = findViewById(R.id.timer);
        status = findViewById(R.id.tv_status);
        sendered_data = findViewById(R.id.tv_sendered_data);

        b_timer_add = findViewById(R.id.b_timer_add);
        b_timer_deny = findViewById(R.id.b_timer_deny);
        b_timer_enter = findViewById(R.id.b_timer_enter);

        sendered_data.setText(dataPart.toString());

        updateTimer();



        b_timer_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timer_start){
                    timer_start = true;
                    b_timer_enter.setText("stop");
                    //60 00 00 = 10 мин =>
                    countDownTimer = new CountDownTimer(time, 1000) {
                        public void onTick(long millisUntilFinished) {
                            time -= 1000;
                            updateTimer();
                        }
                        public void onFinish() {
                            timer.setText("0:0");

                            //пытается подключиться и передать данные
                            ConnectToCar();
                        }
                    };

                    countDownTimer.start();

                }else {
                    countDownTimer.cancel();
                    timer_start = false;
                    b_timer_enter.setText("start");
                }
            }
        });


        b_timer_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timer_start){
                    time += 1000;
                    updateTimer();
                }
            }
        });

        b_timer_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timer_start){
                    time -= 1000;
                    updateTimer();
                }
            }
        });

    }


    public void updateTimer(){
        int minutes = (int)time / 60000;
        int seconds = (int)time % 60000 / 1000;

        String timeText = minutes + ":";
        if(seconds< 10){
            timeText += 0;
        }
        timeText += seconds;

        timer.setText(timeText);

    }


    public void ConnectToCar(){

//        String name = getApplicationInfo().dataDir + "/data.dat";

        //передаём данные в виде строки- без использования сериализации
        DataSenderTask dataSenderTask = new DataSenderTask();
        dataSenderTask.execute(CarSocket.getInstance().ip, CarSocket.getInstance().port,  dataPart.toString()+"]");


        boolean connected = false;
        try {
            //изза ожидания skipped >100 frames
            connected = dataSenderTask.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Toast.makeText(getApplicationContext(),
//                String.valueOf(connected) ,Toast.LENGTH_SHORT).show();

        status.setText(String.valueOf(connected));
        if(connected){
            ll_work.setBackgroundColor(0xFF82B21E);
        }else {
            ll_work.setBackgroundColor(0xFF9B3C2E);
        }


    }


}
