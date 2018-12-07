package com.aye10032.roomsurveillance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aye10032.roomsurveillance.util.sendClass;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class HistoryActivity extends AppCompatActivity implements View.OnTouchListener {

    LinearLayout msgsc;
    LinearLayout imgsc;
    LinearLayout ll;

    GestureDetector gd;
    Bitmap bmp;

    boolean running = true;
    OnGestureListener mFlingListener = new OnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                running = false;
                Intent intent = new Intent(HistoryActivity.this, ControlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ll = findViewById(R.id.lin);
        ll.setOnTouchListener(this);
        ll.setLongClickable(true);

        msgsc = findViewById(R.id.hissc);
        imgsc = findViewById(R.id.imghs);

        gd = new GestureDetector(this, mFlingListener);
        findViewById(R.id.button_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh rf = new refresh();
                rf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        refresh rf = new refresh();
        rf.execute();

        rec r = new rec();
        r.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    private void initDialog() {
        final CommonDialog dialog = new CommonDialog(HistoryActivity.this);
        dialog.setImageBmp(bmp)
//                .setTitle("系统提示")
                .setSingle(true).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();

            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
            }
        }).show();
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

    private class rec extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                while (running) {
                    Socket client = ApplicationUtil.getSocket();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    IntArray arrayList = new IntArray();

                    int line = -1;
                    while ((line = bufferedReader.read()) != -1 && running) {
                        if (line != 97) {
                            arrayList.add(line);
                            System.out.println("recing...");
                        } else {
                            System.out.println("end");
                            if (sendClass.doit(1, arrayList.getelement(3))) {
                                sendNotification("警报！", "有毒气体超标", 1);
                            }
                            if (sendClass.doit(2, arrayList.getelement(4))) {
                                sendNotification("警报！", "非法入侵", 2);
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

    }

    private class refresh extends AsyncTask<String, String, Void> {

        @Override
        protected void onProgressUpdate(String... values) {
            String[] stri = values[0].split("\\|");
            final String[] image = values[1].split("\\|");

            msgsc.removeAllViews();
            imgsc.removeAllViews();

            for (int i = stri.length - 1; i > 0; i--) {
                TextView textView = new TextView(HistoryActivity.this);
                textView.setText(stri[i]);
                textView.setTextSize(20);
                msgsc.addView(textView);
            }

            for (int i = 0; i < image.length; i++) {
                final TextView textView = new TextView(HistoryActivity.this);
                textView.setText(image[i]);
                textView.setTextSize(26);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgrec ir = new imgrec();
                        ir.execute(textView.getText().toString());
                    }
                });
                imgsc.addView(textView);
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Socket socket = new Socket("106.12.35.79", 14451);//ApplicationUtil.getMsqsocket();

                socket.getOutputStream().write("100".getBytes());

                InputStream is = socket.getInputStream();

                byte[] data = new byte[1024];

                int length;
                String rec = "";
                while ((length = is.read(data)) != -1) {
                    rec = new String(data, 0, length);
                    break;
                }

                String msg = rec.split("\\&")[0];
                String image = rec.split("\\&")[1];

                publishProgress(msg, image);
                is.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class imgrec extends AsyncTask<String, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            initDialog();

            //Toast.makeText(MainActivity.this,"OK",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Socket socket = new Socket("106.12.35.79", 8889);
                DataInputStream dataInput = new DataInputStream(socket.getInputStream());

                socket.getOutputStream().write(strings[0].getBytes());

                int size = dataInput.readInt();
                byte[] data = new byte[size];
                int len = 0;
                while (len < size) {
                    len += dataInput.read(data, len, size - len);
                }

                ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outPut);

                dataInput.close();

                publishProgress();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
