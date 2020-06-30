package com.proyectofinal.coronavirusadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextIp;
    Button buttonIp;
    TextView textViewWrongIp;
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setReferences();
        runAfterTime(1000);
    }

    void setReferences(){
        editTextIp=findViewById(R.id.editTextIp);
        buttonIp=findViewById(R.id.buttonIp);
        buttonIp.setOnClickListener(this);
        textViewWrongIp=findViewById(R.id.textViewWrongIp);
    }

    public void runAfterTime(final long time) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                editTextIp.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        };
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonIp:
                if(editTextIp.getText().toString().equals("")){
                    textViewWrongIp.setVisibility(View.VISIBLE);
                    hideKeyboard();
                }else{
                    urlOrigin="http://"+editTextIp.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("urlOrigin", urlOrigin);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    void hideKeyboard() {
        View view = this.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
