package com.veera.admin_a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class opening extends AppCompatActivity {

    String btch,ps;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                btch = sharedPreferences.getString("batch", "");
                ps = sharedPreferences.getString("password", "");

                if(btch.isEmpty() || ps.isEmpty() ) {
                    Intent i = new Intent(opening.this, login.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(opening.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }
}
