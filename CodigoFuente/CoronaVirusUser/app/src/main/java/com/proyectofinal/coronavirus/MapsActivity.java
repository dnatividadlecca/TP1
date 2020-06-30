package com.proyectofinal.coronavirus;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    String urlOrigin;
    String countryReports;
    ConstraintLayout constraintLayoutButtonSearch, constraintLayoutButtonPhotos, constraintLayoutButtonMap, constraintLayoutButtonHealth, constraintLayoutButtoncall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUrlOrigin();
        setCountryReports();
        setReferences();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setCountryReports(){
        countryReports = getIntent().getStringExtra("countryReports");
    }

    public void setReferences() {
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        try {
            JSONObject jsonObject = new JSONObject(countryReports);
            JSONArray jsonArrayCountryReports = jsonObject.getJSONArray("countryReports");
            for(int i=0;i<jsonArrayCountryReports.length();i++){
                final JSONObject jsonObjectCountryReports= jsonArrayCountryReports.getJSONObject(i);
                String country = jsonObjectCountryReports.getString("country");
                int cases = jsonObjectCountryReports.getInt("cases");
                int deaths = jsonObjectCountryReports.getInt("deaths");
                int recovered = jsonObjectCountryReports.getInt("recovered");
                double latitude = jsonObjectCountryReports.getDouble("latitude");
                double longitude = jsonObjectCountryReports.getDouble("longitude");
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(country).snippet("Casos: "+cases+"\n"+"Muertos: "+deaths+"\n"+"Recuperados: "+recovered));
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        LinearLayout info = new LinearLayout(getApplicationContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getApplicationContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getApplicationContext());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
