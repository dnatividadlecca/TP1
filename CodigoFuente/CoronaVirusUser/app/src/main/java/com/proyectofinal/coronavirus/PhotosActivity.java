package com.proyectofinal.coronavirus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewLoadingPhotos;
    LinearLayout linearLayout;
    ConstraintLayout constraintLayoutButtonSearch, constraintLayoutButtonPhotos, constraintLayoutButtonMap, constraintLayoutButtonHealth, constraintLayoutButtoncall;
    String urlOrigin;
    String countryReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        setUrlOrigin();
        setCountryReports();
        setReferences();
        getData();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setCountryReports(){
        countryReports = getIntent().getStringExtra("countryReports");
    }

    void setReferences(){
        textViewLoadingPhotos=findViewById(R.id.textViewLoadingPhotos);
        linearLayout=findViewById(R.id.linearLayout);
        constraintLayoutButtonSearch=findViewById(R.id.constraintLayoutButtonSearch);
        constraintLayoutButtonSearch.setOnClickListener(this);
        constraintLayoutButtonPhotos=findViewById(R.id.constraintLayoutButtonPhotos);
        constraintLayoutButtonPhotos.setOnClickListener(this);
        constraintLayoutButtonMap=findViewById(R.id.constraintLayoutButtonMap);
        constraintLayoutButtonMap.setOnClickListener(this);
        constraintLayoutButtonHealth=findViewById(R.id.constraintLayoutButtonHealth);
        constraintLayoutButtonHealth.setOnClickListener(this);
        constraintLayoutButtoncall=findViewById(R.id.constraintLayoutButtoncall);
        constraintLayoutButtoncall.setOnClickListener(this);
    }

    void getData(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin+"/api/v1/get-photos");
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
                    updatePhotosList(response);
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void updatePhotosList(String reportList) {
        try {
            JSONObject jsonObject = new JSONObject(reportList);
            JSONArray jsonArray = jsonObject.getJSONArray("photos");
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject photo = jsonArray.getJSONObject(i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addPhoto(photo.getString("imageName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewLoadingPhotos.setVisibility(View.GONE);
            }
        });
    }

    void addPhoto(final String imageName){
        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams paramsRelativeLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsRelativeLayout.setMargins(20,20,20,20);
        relativeLayout.setLayoutParams(paramsRelativeLayout);
        relativeLayout.setPadding(25,25,25,25);
        relativeLayout.setBackgroundColor(Color.rgb(196,217,233));
        linearLayout.addView(relativeLayout);

        ImageView imageViewFlag = new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams paramsParentFlag = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        imageViewFlag.setBackgroundColor(Color.GRAY);
        imageViewFlag.setLayoutParams(paramsParentFlag);
        imageViewFlag.setAdjustViewBounds(true);

        relativeLayout.addView(imageViewFlag);
        Picasso.get().load(urlOrigin+"/uploads/photos/"+imageName).into(imageViewFlag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
