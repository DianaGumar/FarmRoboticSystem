package com.example.carclient.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carclient.ClientSocket.CarSocket;
import com.example.carclient.R;

public class FragmentController extends Fragment {

//    private Button b_controller_exit;
    private Button b_stop;
    private Button b_forward;
    private Button b_backward;
    private Button b_left;
    private Button b_right;

    private Button b_speed_deny;
    private Button b_speed_add;

    private Button b_degree_deny;
    private Button b_degree_add;

    private TextView tv_speed;
    private TextView tv_degree;

    private CarSocket carSocket;
    private ControllerTask controllerTask;

    private String[] speeds;
    private int speedID;

    private String[] degrees;
    private int degreeID;
    private int time;

    private boolean sended = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_controller, container, false);

        carSocket = CarSocket.getInstance();
        controllerTask = new ControllerTask();

        speeds = new String[] {"l", "m", "h"};
        degrees = new String[] {"30", "90", "180", "360"};
        degreeID = 1;
        time = 2;


        //инициализируем кнопки
//        b_controller_exit = v.findViewById(R.id.b_controller_exit);

        b_stop = v.findViewById(R.id.b_stop);
        b_forward = v.findViewById(R.id.b_forward);
        b_backward = v.findViewById(R.id.b_backward);
        b_left = v.findViewById(R.id.b_left);
        b_right = v.findViewById(R.id.b_right);


        b_speed_deny = v.findViewById(R.id.b_speed_deny);
        b_speed_add = v.findViewById(R.id.b_speed_add);

        b_degree_deny = v.findViewById(R.id.b_degree_deny);
        b_degree_add = v.findViewById(R.id.b_degree_add);

        tv_speed = v.findViewById(R.id.tv_speed);
        tv_degree = v.findViewById(R.id.tv_degree);


        tv_speed.setText(speeds[0]);
        tv_degree.setText(degrees[0]);

        //обработчики нажатий
        //на поворотах градусы
        //на направлении время

//        b_controller_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ControllerTask().execute("0", "");
//                Log.d("info", "exit");
//            }
//        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sended){
                    sended = false;
                    new ControllerTask().execute("s", "");
                }
            }
        });

        b_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sended) {
                    sended = false;
                    ControllerTask c = new ControllerTask();
                    c.execute("f", String.valueOf(time));
                }
//
//                if(c.getStatus() != AsyncTask.Status.FINISHED){
//                    Toast.makeText(getContext(),"asynctask status finishhed" ,Toast.LENGTH_LONG).show();
//                }

//                controllerTask.doInBackground("f", String.valueOf(time));

//                MyThread myThread = new MyThread("f", String.valueOf(time));
//                myThread.start();

            }
        });

        b_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                controllerTask.doInBackground("b", String.valueOf(time));
                if(sended) {
                    sended = false;
                    ControllerTask c = new ControllerTask();
                    c.execute("b", String.valueOf(time));
                }

//                MyThread myThread = new MyThread("b", String.valueOf(time));
//                myThread.start();
            }
        });

        b_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sended) {
                    sended = false;
                    new ControllerTask().execute("rl", String.valueOf(degrees[degreeID]));
                }
            }
        });

        b_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sended) {
                    sended = false;
                    new ControllerTask().execute("rr", String.valueOf(degrees[degreeID]));
                }
            }
        });


        //на кнопки параметров
        b_speed_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sended) {
                    if(speedID > 0) {
                        sended = false;
                        speedID--;
                        Log.d("info", "speed --");
                        //todo перенести логику в метод setSpeedID
                        tv_speed.setText(speeds[speedID]);
                        new ControllerTask().execute(speeds[speedID], "");
                    }
                }
            }
        });

        b_speed_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sended) {
                    Log.d("info", "speed.len ".concat(String.valueOf(speeds.length)));
                    Log.d("info", "speedID+1= ".concat(String.valueOf(speedID + 1)));
                    if(speeds.length > speedID+1) {
                        sended = false;
                        speedID++;
                        Log.d("info", "speed ++");
                        tv_speed.setText(speeds[speedID]);
                        new ControllerTask().execute(speeds[speedID], "");
                    }
                }
            }
        });

        b_degree_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("info", "degreeID= ".concat(String.valueOf(degreeID)));
                if(degreeID > 0){
                    degreeID--;
                    Log.d("info", "degree --");
                    tv_degree.setText(degrees[degreeID]);
                }
            }
        });

        b_degree_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("info", "degrees.len ".concat(String.valueOf(degrees.length)));
                Log.d("info", "degreeID+1= ".concat(String.valueOf(degreeID + 1)));
                if(degrees.length > degreeID+1){
                    Log.d("info", "degree ++");
                    degreeID++;
                    tv_degree.setText(degrees[degreeID]);
                }
            }
        });

        return v;
    }



//    public class MyThread extends Thread {
//
//        String str;
//        String ss;
//
//        MyThread(String str, String ss){
//            this.str = str;
//            this.ss = ss;
//        }
//
//        public void run() {
//            carSocket.sendMessage(str.concat(ss));
//            Log.d("info", "send ".concat(str));
//        }
//    }


    class ControllerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String ... params) {
            try {
                carSocket.sendMessage(params[0].concat(params[1]));
                Log.d("info", "send ".concat(params[0]));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("info", "task ended");
            sended = true;
        }
    }


}
