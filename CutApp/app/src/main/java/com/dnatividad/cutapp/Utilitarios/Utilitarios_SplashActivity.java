package com.dnatividad.cutapp.Utilitarios;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.dnatividad.cutapp.R;
import com.dnatividad.cutapp.Seguridad.Seguridad_LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

public class Utilitarios_SplashActivity extends AppCompatActivity {
    private final int DURACION_SPLASH=2500;//Tiempo que durara la foto
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_SCALED);
        setContentView(R.layout.activity_utilitarios_splash);

        new Handler().postDelayed(new Runnable(){

            public void run() {
                Intent intent= new Intent(Utilitarios_SplashActivity.this, Seguridad_LoginActivity.class);
                startActivity(intent);
                finish();

            };
        }, DURACION_SPLASH);
    }
}
