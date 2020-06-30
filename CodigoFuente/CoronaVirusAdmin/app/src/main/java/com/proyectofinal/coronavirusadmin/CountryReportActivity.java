package com.proyectofinal.coronavirusadmin;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryReportActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewLoadingData;
    LinearLayout linearLayout;
    String urlOrigin;
    ConstraintLayout constraintLayoutButtonCountryReport, constraintLayoutButtonPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_report);
        setUrlOrigin();
        setReferences();
        getData();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setReferences(){
        textViewLoadingData=findViewById(R.id.textViewLoadingData);
        linearLayout=findViewById(R.id.linearLayout);
        constraintLayoutButtonCountryReport=findViewById(R.id.constraintLayoutButtonCountryReport);
        constraintLayoutButtonCountryReport.setOnClickListener(this);
        constraintLayoutButtonPhotos=findViewById(R.id.constraintLayoutButtonPhotos);
        constraintLayoutButtonPhotos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                    updateReportList(response);
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void updateReportList(String reportList) {
        try {
            JSONObject jsonObject = new JSONObject(reportList);
            JSONArray jsonArray = jsonObject.getJSONArray("countryReports");
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject countryReport = jsonArray.getJSONObject(i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addReport(countryReport.getString("id"), countryReport.getString("country"), urlOrigin+"/images/countryflags/"+countryReport.getString("country")+".jpg",Integer.parseInt(countryReport.getString("cases")),Integer.parseInt(countryReport.getString("deaths")),Integer.parseInt(countryReport.getString("recovered")));
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
                textViewLoadingData.setVisibility(View.GONE);
            }
        });
    }

    void addReport(final String id, final String country, String urlFlag, final int cases, final int deaths, final int recovered){
        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams paramsRelativeLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,165);
        paramsRelativeLayout.setMargins(20,0,20,20);
        relativeLayout.setLayoutParams(paramsRelativeLayout);
        relativeLayout.setPadding(25,25,0,0);
        relativeLayout.setBackgroundColor(Color.rgb(196,217,233));
        linearLayout.addView(relativeLayout);

        ImageView imageViewFlag = new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams paramsParentFlag = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsParentFlag.width=90;
        paramsParentFlag.height=63;
        imageViewFlag.setBackgroundColor(Color.GRAY);
        imageViewFlag.setLayoutParams(paramsParentFlag);
        relativeLayout.addView(imageViewFlag);
        Picasso.get().load(urlFlag).into(imageViewFlag);

        TextView textViewCountry = new TextView(getApplicationContext());
        textViewCountry.setText(Html.fromHtml("<b>País: </b>"+country));
        textViewCountry.setTextColor(Color.BLACK);
        textViewCountry.setPadding(110,4,0,0);
        relativeLayout.addView(textViewCountry);

        TextView textViewCases= new TextView(getApplicationContext());
        textViewCases.setText(Html.fromHtml("<b>Casos: </b>"+Integer.toString(cases)));
        textViewCases.setTextColor(Color.rgb(240,180,0));
        textViewCases.setPadding(10,75,0,0);
        textViewCases.setGravity(Gravity.CENTER|Gravity.START);
        textViewCases.setLayoutParams(paramsRelativeLayout);
        relativeLayout.addView(textViewCases);

        TextView textViewDeaths = new TextView(getApplicationContext());
        textViewDeaths.setText(Html.fromHtml("<b>Muertes: </b>"+Integer.toString(deaths)));
        textViewDeaths.setTextColor(Color.rgb(200,0,0));
        textViewDeaths.setPadding(0,75,40,0);
        textViewDeaths.setGravity(Gravity.CENTER);
        textViewDeaths.setLayoutParams(paramsRelativeLayout);
        relativeLayout.addView(textViewDeaths);

        TextView textViewRecovered = new TextView(getApplicationContext());
        textViewRecovered.setText(Html.fromHtml("<b>Recuperados: </b>"+Integer.toString(recovered)));
        textViewRecovered.setTextColor(Color.rgb(20,200,0));
        textViewRecovered.setPadding(0,75,10,0);
        textViewRecovered.setGravity(Gravity.CENTER|Gravity.END);
        textViewRecovered.setLayoutParams(paramsRelativeLayout);
        relativeLayout.addView(textViewRecovered);

        Button buttonEdit = new Button(this);
        RelativeLayout.LayoutParams paramsButtonEdit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsButtonEdit.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsButtonEdit.setMargins(0,0,20,0);
        paramsButtonEdit.width=100;
        paramsButtonEdit.height=50;
        buttonEdit.setText("Editar");
        buttonEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        buttonEdit.setBackgroundColor(Color.rgb(0,90,160));
        buttonEdit.setTextColor(Color.WHITE);
        buttonEdit.setPadding(0,0,0,0);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getApplicationContext(), EditCountryReport.class);
                intent.putExtra("id", id);
                intent.putExtra("urlOrigin", urlOrigin);
                intent.putExtra("country",country);
                intent.putExtra("cases",cases);
                intent.putExtra("deaths",deaths);
                intent.putExtra("recovered",recovered);
                startActivity(intent);
            }
        });
        buttonEdit.setLayoutParams(paramsButtonEdit);
        relativeLayout.addView(buttonEdit);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
