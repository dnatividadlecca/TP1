package com.proyectofinal.coronavirusadmin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextUserName, editTextPassword;
    Button buttonSignIn;
    TextView textViewUnauthorized;
    String urlOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUrlOrigin();
        setContentView(R.layout.activity_main);
        setReferences();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    public void setReferences() {
        editTextUserName=findViewById(R.id.editTextUserName);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonSignIn=findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        textViewUnauthorized=findViewById(R.id.textViewUnauthorized);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                if(isVerified()){
                    sendData();
                }else {
                    analyseResponse("unauthorized");
                }
                break;
        }
    }

    boolean isVerified() {
        return !editTextUserName.getText().toString().equals("") && !editTextPassword.getText().toString().equals("");
    }

    void sendData(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin+"/api/v1/login");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userName", editTextUserName.getText().toString());
                    jsonObject.put("password", editTextPassword.getText().toString());
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(jsonObject.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    BufferedReader br=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String response="";
                    String line = "";
                    while ((line=br.readLine()) != null) {
                        response += line;
                    }
                    analyseResponse(response);
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void analyseResponse(String response){
        switch (response){
            case "authorized":
                Intent intent = new Intent(getApplicationContext(), CountryReportActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "unauthorized":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewUnauthorized.setVisibility(View.VISIBLE);
                    }
                });
                hideKeyboard();
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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
