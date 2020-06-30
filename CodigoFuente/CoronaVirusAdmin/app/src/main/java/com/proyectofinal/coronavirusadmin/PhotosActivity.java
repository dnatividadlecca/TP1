package com.proyectofinal.coronavirusadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewLoadingPhotos;
    LinearLayout linearLayout;
    ConstraintLayout constraintLayoutButtonCamera, constraintLayoutButtonCountryReport, constraintLayoutButtonPhotos;
    String urlOrigin;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        setUrlOrigin();
        setReferences();
        getData();
    }

    void setUrlOrigin(){
        urlOrigin = getIntent().getStringExtra("urlOrigin");
    }

    void setReferences(){
        textViewLoadingPhotos=findViewById(R.id.textViewLoadingPhotos);
        linearLayout=findViewById(R.id.linearLayout);
        constraintLayoutButtonCamera=findViewById(R.id.constraintLayoutButtonCamera);
        constraintLayoutButtonCamera.setOnClickListener(this);
        constraintLayoutButtonCountryReport=findViewById(R.id.constraintLayoutButtonCountryReport);
        constraintLayoutButtonCountryReport.setOnClickListener(this);
        constraintLayoutButtonPhotos=findViewById(R.id.constraintLayoutButtonPhotos);
        constraintLayoutButtonPhotos.setOnClickListener(this);
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
        paramsRelativeLayout.setMargins(20,0,20,20);
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

        Button buttonEdit = new Button(this);
        RelativeLayout.LayoutParams paramsButtonEdit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsButtonEdit.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsButtonEdit.setMargins(0,20,20,0);
        paramsButtonEdit.width=120;
        paramsButtonEdit.height=60;
        buttonEdit.setText("Borrar");
        buttonEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        buttonEdit.setBackgroundColor(Color.rgb(160,0,0));
        buttonEdit.setTextColor(Color.WHITE);
        buttonEdit.setPadding(0,0,0,0);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(urlOrigin+"/api/v1/delete-photo");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("imageName", imageName);
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
                            int serverResponseCode = httpURLConnection.getResponseCode();
                            if(serverResponseCode==200){
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            httpURLConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        buttonEdit.setLayoutParams(paramsButtonEdit);
        relativeLayout.addView(buttonEdit);
    }

    private File createImageFile() throws IOException {
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName+".jpg");
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.constraintLayoutButtonCamera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID+".fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
                break;
            case R.id.constraintLayoutButtonCountryReport:
                Intent intent2 = new Intent(getApplicationContext(), CountryReportActivity.class);
                intent2.putExtra("urlOrigin", urlOrigin);
                startActivity(intent2);
                break;
            case R.id.constraintLayoutButtonPhotos:
                Intent intent3 = new Intent(getApplicationContext(), PhotosActivity.class);
                intent3.putExtra("urlOrigin", urlOrigin);
                startActivity(intent3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            file = new File(currentPhotoPath);
            sendImage();
        }
    }

    void sendImage(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlOrigin+"/api/v1/upload-photo");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + "photo.jpg" + "\"" + lineEnd);
                    dataOutputStream.writeBytes("Content-Type: image/*" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead>0) {
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    httpURLConnection.getResponseCode();
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
