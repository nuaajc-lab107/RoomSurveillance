package com.aye10032.roomsurveillance;

import android.app.Application;

import java.net.Socket;

public class ApplicationUtil extends Application{

    private static Socket socket;
    /*private static Socket imgsocket;
    private static Socket msqsocket;*/

    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("106.12.35.79", 6324);
                    /*imgsocket = new Socket("106.12.35.79",8889);
                    msqsocket = new Socket("106.12.35.79",14451);*/
                    System.out.println("init done");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Socket getSocket() {
        return socket;
    }

    /*public static Socket getImgsocket() {
        return imgsocket;
    }

    public static Socket getMsqsocket() {
        return msqsocket;
    }*/
}
