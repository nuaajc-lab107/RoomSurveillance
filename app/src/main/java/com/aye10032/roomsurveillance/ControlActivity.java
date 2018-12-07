package com.aye10032.roomsurveillance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aye10032.roomsurveillance.util.DataVoid;
import com.aye10032.roomsurveillance.util.sendClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ImageView swimicon;
    ImageView liveicon;
    ImageView bedicon;
    ImageView pianoicon;
    ImageView canopyicon;
    ImageView curtainicon;
    TextView swimstatus;
    TextView livestatus;
    TextView bedstatus;
    TextView pianostatus;
    TextView canopystatus;
    TextView curtainstatus;
    Button swimbt;
    Button livebt;
    Button bedbt;
    Button pianobt;
    Button canopybt;
    Button curtainbt;
    GestureDetector gd;
    ScrollView ll;
    int data = 0;

    boolean running = true;

    OutputStream os = null;
    BufferedReader bufferedReader = null;

    OnGestureListener mFlingListener = new OnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                running = false;
                Intent intent = new Intent(ControlActivity.this, StatusActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            } else if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                running = false;
                Intent intent = new Intent(ControlActivity.this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
            return false;
        }
    };
    private int Swimming_Pool_ON = 1;
    private int Swimming_Pool_OFF = 2;
    private int Living_Room_ON = 3;
    private int Living_Room_OFF = 4;
    private int Bed_Room_ON = 5;
    private int Bed_Room_OFF = 6;
    private int Piano_Room_ON = 7;
    private int Piano_Room_OFF = 8;
    private int Canopy_OFF = 9;
    private int Canopy_ON = 10;
    private int Curtain_OFF = 11;
    private int Curtain_ON = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        ll = findViewById(R.id.ctl);
        ll.setOnTouchListener(this);
        ll.setLongClickable(true);

        gd = new GestureDetector(this, mFlingListener);

        swimicon = findViewById(R.id.swimlight);
        liveicon = findViewById(R.id.livelight);
        bedicon = findViewById(R.id.bedlight);
        pianoicon = findViewById(R.id.pianolight);
        canopyicon = findViewById(R.id.canopyicon);
        curtainicon = findViewById(R.id.carpticon);

        swimstatus = findViewById(R.id.swimdata);
        livestatus = findViewById(R.id.livedata);
        bedstatus = findViewById(R.id.beddata);
        pianostatus = findViewById(R.id.pianodata);
        canopystatus = findViewById(R.id.canopy);
        curtainstatus = findViewById(R.id.carpt);

        swimbt = findViewById(R.id.swimbt);
        livebt = findViewById(R.id.livebt);
        bedbt = findViewById(R.id.bedbt);
        pianobt = findViewById(R.id.pianobt);
        canopybt = findViewById(R.id.canopybt);
        curtainbt = findViewById(R.id.curtainbt);

        swimbt.setOnClickListener(this);
        livebt.setOnClickListener(this);
        bedbt.setOnClickListener(this);
        pianobt.setOnClickListener(this);
        canopybt.setOnClickListener(this);
        curtainbt.setOnClickListener(this);

        rec r = new rec();
        //r.execute();
        r.executeOnExecutor(Executors.newFixedThreadPool(7));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.swimbt:
                data = DataVoid.getSwimming_Pool_Data();
                if (data == 0) {
                    send sd = new send();
                    sd.execute(Swimming_Pool_ON);
                } else if (data == 1) {
                    send sd = new send();
                    sd.execute(Swimming_Pool_OFF);
                }
                break;
            case R.id.livebt:
                data = DataVoid.getLiving_Room_Data();
                if (data == 0) {
                    send sd = new send();
                    sd.execute(Living_Room_ON);
                } else if (data == 1) {
                    send sd = new send();
                    sd.execute(Living_Room_OFF);
                }
                break;
            case R.id.bedbt:
                data = DataVoid.getBed_Room_Data();
                if (data == 0) {
                    send sd = new send();
                    sd.execute(Bed_Room_ON);
                } else if (data == 1) {
                    send sd = new send();
                    sd.execute(Bed_Room_OFF);
                }
                break;
            case R.id.pianobt:
                data = DataVoid.getPiano_Room_Data();
                if (data == 0) {
                    send sd = new send();
                    sd.execute(Piano_Room_ON);
                } else if (data == 1) {
                    send sd = new send();
                    sd.execute(Piano_Room_OFF);
                }
                break;
            case R.id.canopybt:
                data = DataVoid.getCanopy_Data();
                if (data == 0) {
                    send sd = new send();
                    sd.execute(Canopy_OFF);
                } else if (data == 1) {
                    send sd = new send();
                    sd.execute(Canopy_ON);
                }
                break;
            case R.id.curtainbt:
                data = DataVoid.getCurtain_Data();
                if (data == 0) {
                    send sd = new send();
                    sd.execute(Curtain_OFF);
                } else if (data == 1) {
                    send sd = new send();
                    sd.execute(Curtain_ON);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    private void sendNotification(String title, String msg, int id) {
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        String channelID = "channel01";
        String channelNAME = "name01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_HIGH);

            notifyManager.createNotificationChannel(mChannel);

            notification = new Notification.Builder(this)
                    .setChannelId(channelID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.icon_new)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
                    .build();

        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.icon_new)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND);

            notification = notificationBuilder.build();
        }
        notifyManager.notify(id, notification);
    }

    private class send extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                os.write(integers[0]);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class rec extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                while (running) {
                    Socket client = ApplicationUtil.getSocket();
                    os = client.getOutputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    IntArray arrayList = new IntArray();

                    int line = -1;
                    while ((line = bufferedReader.read()) != -1 && running) {
                        if (line != 97) {
                            arrayList.add(line);
                        } else {
                            if (arrayList.size() == 14) {
                                publishProgress(arrayList.getelement(5), arrayList.getelement(6), arrayList.getelement(7), arrayList.getelement(8), arrayList.getelement(9), arrayList.getelement(10));
                                if (sendClass.doit(1, arrayList.getelement(3))) {
                                    sendNotification("警报！", "有毒气体超标", 1);
                                }
                                if (sendClass.doit(2, arrayList.getelement(4))) {
                                    sendNotification("警报！", "非法入侵", 1);
                                }
                            }
                            arrayList.empty();
                        }
                    }
                }
                //bufferedReader.close();
                //os.close();
                System.out.println("end");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == 1) {
                swimicon.setImageResource(R.drawable.lighton);
                swimstatus.setText("开启");
                swimbt.setText("关闭");
                DataVoid.setSwimming_Pool_Data(1);
            } else if (values[0] == 0) {
                swimicon.setImageResource(R.drawable.lightoff);
                swimstatus.setText("关闭");
                swimbt.setText("开启");
                DataVoid.setSwimming_Pool_Data(0);
            }

            if (values[1] == 1) {
                liveicon.setImageResource(R.drawable.lighton);
                livestatus.setText("开启");
                livebt.setText("关闭");
                DataVoid.setLiving_Room_Data(1);
            } else if (values[1] == 0) {
                liveicon.setImageResource(R.drawable.lightoff);
                livestatus.setText("关闭");
                livebt.setText("开启");
                DataVoid.setLiving_Room_Data(0);
            }

            if (values[2] == 1) {
                bedicon.setImageResource(R.drawable.lighton);
                bedstatus.setText("开启");
                bedbt.setText("关闭");
                DataVoid.setBed_Room_Data(1);
            } else if (values[2] == 0) {
                bedicon.setImageResource(R.drawable.lightoff);
                bedstatus.setText("关闭");
                bedbt.setText("开启");
                DataVoid.setBed_Room_Data(0);
            }

            if (values[3] == 1) {
                pianoicon.setImageResource(R.drawable.lighton);
                pianostatus.setText("开启");
                pianobt.setText("关闭");
                DataVoid.setPiano_Room_Data(1);
            } else if (values[3] == 0) {
                pianoicon.setImageResource(R.drawable.lightoff);
                pianostatus.setText("关闭");
                pianobt.setText("开启");
                DataVoid.setPiano_Room_Data(0);
            }

            if (values[4] == 1) {
                canopyicon.setImageResource(R.drawable.lightoff);
                canopystatus.setText("关闭");
                canopybt.setText("开启");

                DataVoid.setCanopy_Data(1);
            } else if (values[4] == 0) {
                canopyicon.setImageResource(R.drawable.lighton);
                canopystatus.setText("开启");
                canopybt.setText("关闭");
                DataVoid.setCanopy_Data(0);
            }

            if (values[5] == 1) {
                curtainicon.setImageResource(R.drawable.lightoff);
                curtainstatus.setText("关闭");
                curtainbt.setText("开启");
                DataVoid.setCurtain_Data(1);
            } else if (values[5] == 0) {
                curtainicon.setImageResource(R.drawable.lighton);
                curtainstatus.setText("开启");
                curtainbt.setText("关闭");
                DataVoid.setCurtain_Data(0);
            }
            super.onProgressUpdate(values);
        }
    }
}
