package com.aye10032.roomsurveillance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText name;
    EditText pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        pswd = findViewById(R.id.pswd);

        findViewById(R.id.loginbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, R.string.nameex, Toast.LENGTH_SHORT).show();
                }else if (pswd.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, R.string.pswdex, Toast.LENGTH_SHORT).show();
                }else {
                    ApplicationUtil apputil = new ApplicationUtil();
                    try {
                        apputil.init();
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LoginActivity.this, ControlActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
