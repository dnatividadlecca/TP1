package com.proyectofinal.coronavirusadmin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditCountryReport extends AppCompatActivity implements View.OnClickListener {

    TextView textViewCountry;
    ImageView imageViewFlag;
    EditText editTextCases, editTextDeaths, editTextRecovered;
    Button buttonSave;
    String urlOrigin;
    TextView textViewInvalidData;
    ConstraintLayout constraintLayoutButtonCountryReport, constraintLayoutButtonPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_country_report);
        setUrlOrigin();
        setReferences();
        setValues();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setReferences(){
        textViewCountry=findViewById(R.id.textViewCountry);
        imageViewFlag=findViewById(R.id.imageViewFlag);
        editTextCases=findViewById(R.id.editTextCases);
        editTextDeaths=findViewById(R.id.editTextDeaths);
        editTextRecovered=findViewById(R.id.editTextRecovered);
        buttonSave=findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        textViewInvalidData=findViewById(R.id.textViewInvalidData);
        constraintLayoutButtonCountryReport=findViewById(R.id.constraintLayoutButtonCountryReport);
        constraintLayoutButtonCountryReport.setOnClickListener(this);
        constraintLayoutButtonPhotos=findViewById(R.id.constraintLayoutButtonPhotos);
        constraintLayoutButtonPhotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave:
                if(isVerified()){
                    updateData();
                }else {
                    analyseResponse("invalidData");
                }
                break;
            case R.id.constraintLayoutButtonCountryReport:
                Intent intent = new Intent(getApplicationContext(), CountryReportActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case R.id.constraintLayoutButtonPhotos:
                Intent intent2 = new Intent(getApplicationContext(), PhotosActivity.class);
                intent2.putExtra("urlOrigin", urlOrigin);
                startActivity(intent2);
                break;
        }
    }

    void setValues(){
        textViewCountry.setText(getIntent().getStringExtra("country"));
        Picasso.get().load(urlOrigin+"/images/countryflags/"+getIntent().getStringExtra("country")+".jpg").into(imageViewFlag);
        editTextCases.setText(Integer.toString(getIntent().getIntExtra("cases",0)));
        editTextDeaths.setText(Integer.toString(getIntent().getIntExtra("deaths",0)));
        editTextRecovered.setText(Integer.toString(getIntent().getIntExtra("recovered",0)));
    }

    boolean isVerified() {
        if(Integer.parseInt("0"+editTextCases.getText().toString())<0 || Integer.parseInt("0"+editTextDeaths.getText().toString())<0 ||Integer.parseInt("0"+editTextRecovered.getText().toString())<0){
            return false;
        }
        return true;
    }

    void updateData(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin+"/api/v1/update-country-report");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", getIntent().getStringExtra("id"));
                    jsonObject.put("cases", editTextCases.getText().toString());
                    jsonObject.put("deaths", editTextDeaths.getText().toString());
                    jsonObject.put("recovered", editTextRecovered.getText().toString());
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
            case "validData":
                Intent intent = new Intent(getApplicationContext(), CountryReportActivity.class);
                intent.putExtra("urlOrigin", urlOrigin);
                startActivity(intent);
                break;
            case "invalidData":
                textViewInvalidData.setVisibility(View.VISIBLE);
                break;
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
