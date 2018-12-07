package com.aye10032.roomsurveillance.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.aye10032.roomsurveillance.R;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class sendClass {

    public static boolean doit(int flag,int value){

        boolean changed = false;

        if (flag == 1){

            if (value == 1 && DataVoid.getFire_Data() == 0){
                changed = true;
                DataVoid.setFire_Data(1);
                new Thread(new send("0")).start();
            }else if (value == 0 && DataVoid.getFire_Data() == 1){
                DataVoid.setFire_Data(0);
            }

        }else if (flag ==2){

            if (value == 1 && DataVoid.getSecure_Data() == 0){
                changed = true;
                DataVoid.setSecure_Data(1);
                new Thread(new send("1")).start();
            }else if (value == 0 && DataVoid.getSecure_Data() == 1){
                DataVoid.setSecure_Data(0);
            }

        }
        return changed;
    }

    private static class send implements Runnable{

        String str = "";

        public send(String flag){
            this.str = flag;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket("106.12.35.79", 14451);
                socket.getOutputStream().write(str.getBytes());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
