package com.proyectofinal.coronavirus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class InformationCountryActivity extends AppCompatActivity implements View.OnClickListener {

    String urlOrigin;
    String countryReports;
    String country, cases, deaths, recovered;
    ImageView imageViewFlag;
    TextView textViewCountryName, textViewCases, textViewDeaths, textViewRecovered;
    ConstraintLayout constraintLayoutButtonSearch, constraintLayoutButtonPhotos, constraintLayoutButtonMap, constraintLayoutButtonHealth, constraintLayoutButtoncall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_country);
        setUrlOrigin();
        setCountryReports();
        setValues();
        setReferences();
        updateInformationCountry();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setCountryReports(){
        countryReports = getIntent().getStringExtra("countryReports");
    }

    void setValues() {
        country = getIntent().getStringExtra("country");
        cases = getIntent().getStringExtra("cases");
        deaths = getIntent().getStringExtra("deaths");
        recovered = getIntent().getStringExtra("recovered");
    }

    void setReferences() {
        imageViewFlag=findViewById(R.id.imageViewFlag);
        textViewCountryName=findViewById(R.id.textViewCountryName);
        textViewCases=findViewById(R.id.textViewCases);
        textViewDeaths=findViewById(R.id.textViewDeaths);
        textViewRecovered=findViewById(R.id.textViewRecovered);
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

    void updateInformationCountry() {
        Picasso.get().load(urlOrigin+"/images/countryflags/"+getIntent().getStringExtra("country")+".jpg").into(imageViewFlag);
        textViewCountryName.setText(country);
        textViewCases.setText(cases);
        textViewDeaths.setText(deaths);
        textViewRecovered.setText(recovered);
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
