package com.dnatividad.cutapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class splashActivity extends AppCompatActivity {
    private final int DURACION_SPLASH=2500;//Tiempo que durara la foto
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_SCALED);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){

            public void run() {
                Intent intent= new Intent(splashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            };
        }, DURACION_SPLASH);
    }
}
