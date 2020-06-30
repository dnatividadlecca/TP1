package com.proyectofinal.coronavirus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerCountries;
    ConstraintLayout constraintLayoutButtonInformationCountry, constraintLayoutButtonSearch, constraintLayoutButtonPhotos, constraintLayoutButtonMap, constraintLayoutButtonHealth, constraintLayoutButtoncall;
    String urlOrigin;
    boolean isEnabledButtons;
    String countryReports;
    JSONArray jsonArrayCountryReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUrlOrigin();
        setValues();
        assignReferences();
        enableButtons(false);
        getData();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setValues(){
        isEnabledButtons=false;
    }

    public void assignReferences() {
        spinnerCountries = findViewById(R.id.spinnerCountries);
        constraintLayoutButtonSearch=findViewById(R.id.constraintLayoutButtonSearch);
        constraintLayoutButtonSearch.setOnClickListener(this);
        constraintLayoutButtonInformationCountry = findViewById(R.id.constraintLayoutButtonInformationCountry);
        constraintLayoutButtonInformationCountry.setOnClickListener(this);
        constraintLayoutButtonPhotos = findViewById(R.id.constraintLayoutButtonPhotos);
        constraintLayoutButtonPhotos.setOnClickListener(this);
        constraintLayoutButtonMap = findViewById(R.id.constraintLayoutButtonMap);
        constraintLayoutButtonMap.setOnClickListener(this);
        constraintLayoutButtonHealth = findViewById(R.id.constraintLayoutButtonHealth);
        constraintLayoutButtonHealth.setOnClickListener(this);
        constraintLayoutButtoncall = findViewById(R.id.constraintLayoutButtoncall);
        constraintLayoutButtoncall.setOnClickListener(this);
    }

    void getData(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin+"/api/v1/get-country-reports");
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    BufferedReader br=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String response="";
                    String line = "";
                    while ((line=br.readLine()) != null) {
                        response += line;
                    }
                    updateSpinnerCountries(response);
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateSpinnerCountries(String response){
        try {
            countryReports=response;
            JSONObject jsonObject = new JSONObject(response);
            jsonArrayCountryReports = jsonObject.getJSONArray("countryReports");
            final ArrayList<String> countries = new ArrayList<String>();
            for(int i=0;i<jsonArrayCountryReports.length();i++){
                final JSONObject jsonObjectCountryReports=jsonArrayCountryReports.getJSONObject(i);
                countries.add(jsonObjectCountryReports.getString("country"));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, countries);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCountries.setAdapter(adapter);
                    enableButtons(true);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void enableButtons(boolean value){
        isEnabledButtons=value;
        if(value){
            constraintLayoutButtonInformationCountry.setAlpha(1);
            constraintLayoutButtonSearch.setAlpha(1);
            constraintLayoutButtonPhotos.setAlpha(1);
            constraintLayoutButtonMap.setAlpha(1);
            constraintLayoutButtonHealth.setAlpha(1);
            constraintLayoutButtoncall.setAlpha(1);
        }else{
            constraintLayoutButtonInformationCountry.setAlpha(0.5f);
            constraintLayoutButtonSearch.setAlpha(0.5f);
            constraintLayoutButtonPhotos.setAlpha(0.5f);
            constraintLayoutButtonMap.setAlpha(0.5f);
            constraintLayoutButtonHealth.setAlpha(0.5f);
            constraintLayoutButtoncall.setAlpha(0.5f);
        }
    }

    @Override
    public void onClick(View v) {
        if(isEnabledButtons){
            switch (v.getId()) {
                case R.id.constraintLayoutButtonInformationCountry:
                    try {
                        JSONObject jsonObjectCountryReport = jsonArrayCountryReports.getJSONObject(spinnerCountries.getSelectedItemPosition());
                        Intent intent = new Intent(getApplicationContext(), InformationCountryActivity.class);
                        intent.putExtra("urlOrigin", urlOrigin);
                        intent.putExtra("country", jsonObjectCountryReport.getString("country"));
                        intent.putExtra("countryReports", countryReports);
                        intent.putExtra("cases", jsonObjectCountryReport.getString("cases"));
                        intent.putExtra("deaths", jsonObjectCountryReport.getString("deaths"));
                        intent.putExtra("recovered", jsonObjectCountryReport.getString("recovered"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.constraintLayoutButtonSearch:
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("urlOrigin", urlOrigin);
                    intent.putExtra("countryReports", countryReports);
                    startActivity(intent);
                    break;
                case R.id.constraintLayoutButtonPhotos:
                    Intent intent2 = new Intent(getApplicationContext(), PhotosActivity.class);
                    intent2.putExtra("urlOrigin", urlOrigin);
                    intent2.putExtra("countryReports", countryReports);
                    startActivity(intent2);
                    break;
                case R.id.constraintLayoutButtonMap:
                    Intent intent3 = new Intent(getApplicationContext(), MapsActivity.class);
                    intent3.putExtra("urlOrigin", urlOrigin);
                    intent3.putExtra("countryReports", countryReports);
                    startActivity(intent3);
                    break;
                case R.id.constraintLayoutButtonHealth:
                    Intent intent4 = new Intent(getApplicationContext(), HealthActivity.class);
                    intent4.putExtra("urlOrigin", urlOrigin);
                    intent4.putExtra("countryReports", countryReports);
                    startActivity(intent4);
                    break;
                case R.id.constraintLayoutButtoncall:
                    Intent intent5 = new Intent(getApplicationContext(), CallActivity.class);
                    intent5.putExtra("urlOrigin", urlOrigin);
                    intent5.putExtra("countryReports", countryReports);
                    startActivity(intent5);
                    break;
            }
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
