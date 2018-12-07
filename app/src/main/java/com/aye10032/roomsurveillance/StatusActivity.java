package com.aye10032.roomsurveillance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aye10032.roomsurveillance.util.sendClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StatusActivity extends AppCompatActivity implements View.OnTouchListener {

    GestureDetector gd;
    ScrollView ll;
    HorizontalScrollView hsl;

    protected TextView temperature;
    protected TextView humidity;
    protected TextView isRain;
    protected TextView fen;

    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        isRain = findViewById(R.id.isRain);
        fen = findViewById(R.id.fenchen);

        ll = findViewById(R.id.stl);
        ll.setOnTouchListener(this);
        ll.setLongClickable(true);

        hsl = findViewById(R.id.timsc);
        hsl.scrollTo(100,0);

        rec r = new rec();
        r.execute();

        gd = new GestureDetector(this, mFlingListener);
    }

    private class rec extends AsyncTask<String,String,Void>{

        @Override
        protected Void doInBackground(String... strings) {

            try {
                while(running)
                {
                    Socket client = ApplicationUtil.getSocket();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    IntArray arrayList = new IntArray();

                    int line = -1;
                    while((line = bufferedReader.read()) != -1 && running)
                    {
                        if (line != 97){
                            arrayList.add(line);
                        }else {
                            if (arrayList.size() == 14) {
                                publishProgress("" + arrayList.getelement(0), "" + arrayList.getelement(1), "" + arrayList.getelement(2), String.format( "%.3f",(double)((arrayList.getelement(11)*100) + (arrayList.getelement(12)*10) + arrayList.getelement(13)) / 2000));
                                if (sendClass.doit(1, arrayList.getelement(3))) {
                                    sendNotification("警报！", "有毒气体超标", 1);
                                }
                                if (sendClass.doit(2, arrayList.getelement(4))) {
                                    sendNotification("警报！", "非法入侵", 2);
                                }
                            }
                            arrayList.empty();
                        }
                    }
                    //利用publishProgress更新UI，把当前接收的信息显示出来

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            temperature.setText(values[1]);
            humidity.setText(values[0]);
            fen.setText(values[3]);
            if (values[2].equals("0")){
                isRain.setText("没有下雨哦！");
            }else if (values[2].equals("1")){
                isRain.setText("下雨啦，回家收衣服啦");
            }
            super.onProgressUpdate(values);
        }
    }

    OnGestureListener mFlingListener = new OnGestureListener(){
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                running = false;
                Intent intent = new Intent(StatusActivity.this, ControlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
            return false;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    private void sendNotification(String title,String msg, int id) {
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        String channelID = "channel01";
        String channelNAME = "name01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel mChannel = new  NotificationChannel(channelID,channelNAME,NotificationManager.IMPORTANCE_HIGH);

            notifyManager.createNotificationChannel(mChannel);

            notification = new Notification.Builder(this)
                    .setChannelId(channelID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.icon_new)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .setDefaults( Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND )
                    .build();

        }else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.icon_new)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .setDefaults( Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND );

            notification = notificationBuilder.build();
        }
        notifyManager.notify(id,notification);
    }

    private void init(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +String.valueOf(calendar.get(Calendar.MINUTE));

        switch (hour){
            case "00":
                System.out.println("0");
                break;
            case "01":
                System.out.println("1");
                break;
        }

    };
}
