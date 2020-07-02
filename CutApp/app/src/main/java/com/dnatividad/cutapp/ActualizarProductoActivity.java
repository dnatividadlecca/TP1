package com.dnatividad.cutapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActualizarProductoActivity extends AppCompatActivity {
    private  ImageView img;
    private TextView codprod;
    private EditText titu;
    private EditText ingre;
    private EditText precio;

    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;

    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_producto);

        //coge el texto de los campos del catalogo
        img=(ImageView) findViewById(R.id.imgFotoPerfil);
        codprod = (TextView)findViewById(R.id.act_pro_codigo);
        titu = (EditText)findViewById(R.id.act_pro_titulo);
        ingre = (EditText)findViewById(R.id.act_pro_descripcion);
        precio = (EditText)findViewById(R.id.act_pro_precio);

        String datos0 = getIntent().getStringExtra("foto");
        String datos1 = getIntent().getStringExtra("cod_producto");
        String datos2 = getIntent().getStringExtra("titulo");
        String datos3 = getIntent().getStringExtra("ingredientes");
        String datos4 = getIntent().getStringExtra("precio");

        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(datos0),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        //*************************************************************************************************

        img.setImageBitmap(bitmap);
        codprod.setText(datos1);
        titu.setText(datos2);
        ingre.setText(datos3);
        precio.setText(datos4);


    }

    //decodifica la imagem y la devuelve como BITMAP
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        //--------------obtengo el correo almacenado a la hora que se logueo------------------------
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        String permiso = prefs.getString("PERMISO", "");
        Log.i("permiso logueado ====>", permiso.trim());
        //------------------------------------------------------------------------------------------

        //otorgo permiso de acceso a las opciones del menu
        if(permiso.equals("true")){
            //esta linea permite hacer visible un item del menu
            MenuItem item = menu.findItem(R.id.item_6);
            item.setVisible(true);
            MenuItem item2 = menu.findItem(R.id.item_7);
            item2.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.item_7);
            item.setVisible(false);
        }
        //------------------------------------------------------------------------------------------
        MenuItem itemMenuRegistrar = menu.findItem(R.id.item_11);
        itemMenuRegistrar.setVisible(true);
        //------------------------------------------------------------------------------------------
        return true;
    }

    public void ActualizarProducto(View v){

        //-------------------------------capturo la imagen-----------------------------------------

        ImageView fotografia= (ImageView) findViewById(R.id.imgFotoPerfil);
        Bitmap bitmap = ((BitmapDrawable) fotografia.getDrawable()).getBitmap();

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        //******************************************************************************************

        TextView act_cod_producto = (TextView) findViewById(R.id.act_pro_codigo);
        EditText act_titulo = (EditText) findViewById(R.id.act_pro_titulo);
        EditText act_descripcion = (EditText) findViewById(R.id.act_pro_descripcion);
        EditText act_precio = (EditText) findViewById(R.id.act_pro_precio);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_producto", act_cod_producto.getText().toString())
                .addFormDataPart("titulo", act_titulo.getText().toString())
                .addFormDataPart("ingredientes", act_descripcion.getText().toString())
                .addFormDataPart("precio", act_precio.getText().toString())
                .addFormDataPart("foto", fotoEnBase64)
                .build();


        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/act_productos")
                .url("http://cutapp.atwebpages.com/index.php/act_productos")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String cadenaJson = response.body().string();


                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(), "Actualizacion exitosa", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

                }
            }
        });

        MisProductos();
        ActualizarProductoActivity.this.finish();
    }

    //**********************************Metodo de anular Pedido*************************************
    public void AnularProducto(View v){

        TextView act_cod_producto = (TextView) findViewById(R.id.act_pro_codigo);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_producto", act_cod_producto.getText().toString())
                .build();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/anular_productos")
                .url("http://cutapp.atwebpages.com/index.php/anular_productos")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String cadenaJson = response.body().string();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(), "Anulacion exitosa", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });

                }
            }
        });

        MisProductos();
        ActualizarProductoActivity.this.finish();
    }


    //metodo para asignar las funciones de las opciones
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id ==R.id.item_1){
            Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
            Login();
        }
        else if (id ==R.id.item_2){
            Toast.makeText(this,"Registrar usurio", Toast.LENGTH_SHORT).show();
            RegistrarUsuario();
        }
        else if (id ==R.id.item_3){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_4){
            Toast.makeText(this,"Contactenos", Toast.LENGTH_SHORT).show();
            Contactenos();
        }
        else if (id ==R.id.item_5){
            Toast.makeText(this,"Ubícanos", Toast.LENGTH_SHORT).show();
            Ubicanos();
        }
        else if (id ==R.id.item_6){
            Toast.makeText(this,"Catalogo", Toast.LENGTH_SHORT).show();
            Catalogo();
        }
        else if (id ==R.id.item_7){
            Toast.makeText(this,"Mis Pedidos", Toast.LENGTH_SHORT).show();
            MisPedidos();
        }
        else if (id ==R.id.item_8){
            Toast.makeText(this,"Reg. Producto", Toast.LENGTH_SHORT).show();
            reg_producto();
        }
        else if (id ==R.id.item_9){
            Toast.makeText(this,"Mis Productos", Toast.LENGTH_SHORT).show();
            MisProductos();
        }
        else if (id ==R.id.item_11){
            //Toast.makeText(this,"Cerrar Sesión", Toast.LENGTH_SHORT).show();
            cerrarSesion();
        }
        return super.onOptionsItemSelected(item);
    }


    //Navegacion de los botones del menu
    public void Login(){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }
    public void RegistrarUsuario(){
        Intent registrarusuario = new Intent(this, RegistrarUsuarioActivity.class);
        startActivity(registrarusuario);
    }
    public void Nosotros(){
        Intent nosotros = new Intent(this, NosotrosActivity.class);
        startActivity(nosotros);
    }
    public void Contactenos(){
        Intent contactenos = new Intent(this, ContactenosActivity.class);
        startActivity(contactenos);
    }
    public void Ubicanos(){
        Intent ubicanos = new Intent(this, UbicanosActivity.class);
        startActivity(ubicanos);
    }
    public void Catalogo(){
        Intent Catalogo = new Intent(this, CatalogoActivity.class);
        startActivity(Catalogo);
    }
    public void MisPedidos(){
        Intent mispedidos = new Intent(this, MisPedidosActivity.class);
        startActivity(mispedidos);
    }
    public void reg_producto(){
        Intent producto = new Intent(this, RegistrarServicioActivity.class);
        startActivity(producto);
    }
    public void MisProductos(){
        Intent misproducto = new Intent(this, MisServiciosActivity.class);
        startActivity(misproducto);
    }
    private void cerrarSesion(){
        DialogInterface.OnClickListener confirmacion = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Limpia Preferencias
                        SharedPreferences preferences = getSharedPreferences("PREFERENCIAS",MODE_PRIVATE);
                        preferences.edit().clear().commit();

                        //Regresa Pantalla Login
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lbl_confirmacion_cerrar_sesion).setPositiveButton(R.string.lbl_confirmacion_si, confirmacion)
                .setNegativeButton(R.string.lbl_confirmacion_no, confirmacion).show();
    }

    //********************************************** CAMARA ****************************************
    public void seleccionarImagenDesdeGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imgFotoPerfil = (ImageView) findViewById(R.id.imgFotoPerfil);
        Bitmap img = (Bitmap)data.getExtras().get("data");
        imgFotoPerfil.setImageBitmap(img);

        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    try {
                        Uri selectedImage = data.getData();

                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        final Bitmap bmp = BitmapFactory.decodeStream(imageStream);


                        imgFotoPerfil.setImageBitmap(bmp);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    }



    //*****************************************************************************************************************************
}
