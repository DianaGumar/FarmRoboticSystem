package com.example.datasender.SocketPart;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//singletone
public class CarSocket {

    private  String   TAG_LOG = "info";

    public String ip;
    public String  port;
    private Socket socket;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    public boolean isConnected(){
        return socket.isConnected();
    }

    private CarSocket(){
        socket = new Socket();
    }

    private static CarSocket instance;

    public static CarSocket getInstance(){
        return (instance == null) ? instance = new CarSocket() : instance;
    }


    public boolean unconnectToDevice()
    {
        try
        {
            dataInputStream.close();
            Log.d(TAG_LOG, "dataInputStream close");
            dataOutputStream.close();
            Log.d(TAG_LOG, "dataOutputStream close");
            socket.close();
            Log.d(TAG_LOG, "Soket close");

            instance = null;
            return true;

        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }


    public void connectToDevice(String ip, int port)
    {
        try
        {
            Log.d(TAG_LOG, "start created new Soket object");
            Log.d(TAG_LOG, ip);
            Log.d(TAG_LOG, String.valueOf(port));

            socket.connect(new InetSocketAddress(ip,port));
            Log.d(TAG_LOG, "new Soket object");

        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            instance = null;
        } catch (IOException e) {
            e.printStackTrace();
            instance = null;
        }

    }


    public boolean sendObject(String name) throws IOException {

        try
        {
            if(dataOutputStream == null){
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                Log.d(TAG_LOG, "output stream created");
            }

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(name));
//        bos = new BufferedOutputStream(client.getOutputStream());
            byte[] byteArray = new byte[8192];
            int in = 0;
            while ((in = bis.read(byteArray)) != -1){
                dataOutputStream.write(byteArray,0,in);
            }
            bis.close();
//            bos.close();

//            dataOutputStream.write(str.getBytes());
            dataOutputStream.flush();

            Log.d(TAG_LOG, "output stream by object worked");
            return true;

        }
        catch (IOException e)
        { e.printStackTrace(); }

        return false;


    }


    public boolean sendMessage(String str)
    {
        try
        {
            if(dataOutputStream == null){
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                Log.d(TAG_LOG, "output stream created");
            }
            dataOutputStream.write(str.getBytes());
            dataOutputStream.flush();


            Log.d(TAG_LOG, "output stream worked");
            return true;

        }
        catch (IOException e)
        { e.printStackTrace(); }

        return false;
    }

    public String getMessage()
    {
        String s = null;

        try
        {
            if(dataInputStream == null){
                dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                Log.d(TAG_LOG, "input stream created");
            }

            byte[] b = new byte[13];
            int i = dataInputStream.read(b);

            if(i > 0){
                s = new String(b);
            }

            Log.d(TAG_LOG, "input stream worked");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (IOException e)
        { e.printStackTrace(); }

        return s;
    }



}
