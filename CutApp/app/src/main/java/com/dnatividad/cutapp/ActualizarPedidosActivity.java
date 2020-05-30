package com.dnatividad.cutapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActualizarPedidosActivity extends AppCompatActivity {
    private ImageView img;
    private TextView codped;
    private TextView fechpedido;
    private TextView codprod;
    private TextView codtitu;
    private TextView usua;
    private TextView nom;
    private TextView ape;
    private TextView dir;
    private TextView telf;
    private TextView esta;
    private TextView fechentrega;

    private TextView fechenaci;
    private TextView porcione;
    private TextView sabores;
    private TextView mensa;
    private TextView info;

    //Para mostrar la imagen de codigo a bitmap
    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_pedidos);

        Spinner spi_estado = (Spinner) findViewById(R.id.spinner_estado);
        String[] estados = {"RECIBIDO","CONFIRMADO","EN PROCESO","EN REPARTO","ENTREGADO"};
        spi_estado.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados));

        //coge el texto de los campos del catalogo
        img=(ImageView) findViewById(R.id.imgFotoPerfil);
        codped = (TextView)findViewById(R.id.act_cod_pedido);
        fechpedido = (TextView)findViewById(R.id.act_fechapedido);
        codprod = (TextView)findViewById(R.id.act_cod_producto);
        codtitu = (TextView)findViewById(R.id.act_Producto);
        usua = (TextView)findViewById(R.id.act_usuario);
        nom = (TextView)findViewById(R.id.act_nombre);
        ape = (TextView)findViewById(R.id.act_apellido);
        dir = (TextView)findViewById(R.id.act_direcion);
        telf = (TextView)findViewById(R.id.act_telefono);
        esta = (TextView)findViewById(R.id.act_estado);
        fechentrega = (TextView)findViewById(R.id.act_fechaentrega);

        fechenaci = (TextView)findViewById(R.id.act_nacimiento);
        porcione= (TextView)findViewById(R.id.act_porcion);
        sabores= (TextView)findViewById(R.id.act_sabores);
        mensa= (TextView)findViewById(R.id.act_dedicatorias);
        info= (TextView)findViewById(R.id.act_infoadicional);

        String datos0 = getIntent().getStringExtra("foto");
        String datos1 = getIntent().getStringExtra("cod_pedido");
        String datos2 = getIntent().getStringExtra("cod_producto");
        String datos4 = getIntent().getStringExtra("titulo");
        String datos6 = getIntent().getStringExtra("fecha_pedido");
        String datos7 = getIntent().getStringExtra("fecha_entrega");
        String datos8 = getIntent().getStringExtra("cod_usuario");
        String datos9 = getIntent().getStringExtra("nombre");
        String datos10 = getIntent().getStringExtra("apellido");
        String datos12 = getIntent().getStringExtra("telefono");
        String datos11 = getIntent().getStringExtra("direccion");
        String datos18 = getIntent().getStringExtra("estado");

        String datos13 = getIntent().getStringExtra("fecha_nacimiento");
        String datos14 = getIntent().getStringExtra("porciones");
        String datos15 = getIntent().getStringExtra("sabor");
        String datos16 = getIntent().getStringExtra("mensaje_torta");
        String datos17 = getIntent().getStringExtra("info_adicional");

        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(datos0),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        //*************************************************************************************************
        img.setImageBitmap(bitmap);
        codped.setText(datos1);
        fechpedido.setText(datos6);
        codprod.setText(datos2);
        codtitu.setText(datos4);
        usua.setText(datos8);
        nom.setText(datos9);
        ape.setText(datos10);
        telf.setText(datos12);
        dir.setText(datos11);
        esta.setText(datos18);
        fechentrega.setText(datos7);

        fechenaci.setText(datos13);
        porcione.setText(datos14);
        sabores.setText(datos15);
        mensa.setText(datos16);
        info.setText(datos17);



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

    public void ActualizarPedido(View v){

        TextView act_cod_pedido = (TextView) findViewById(R.id.act_cod_pedido);

        Spinner spinner_estado = (Spinner) findViewById(R.id.spinner_estado);
        final String estado = spinner_estado.getSelectedItem().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_pedido", act_cod_pedido.getText().toString())
                .addFormDataPart("estado", estado)
                .build();
        Log.i("Actualizar ====>", act_cod_pedido.getText().toString());
        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/act_pedidos")
                .url("http://cutapp.atwebpages.com/index.php/act_pedidos")
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
                    Log.i("Actualizar ====>", estado);

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

        MisPedidos();
        ActualizarPedidosActivity.this.finish();
    }

    public void AnularPedido(View v){

        TextView act_cod_pedido = (TextView) findViewById(R.id.act_cod_pedido);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_pedido", act_cod_pedido.getText().toString())
                .build();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/anular_pedidos")
                .url("http://cutapp.atwebpages.com/index.php/anular_pedidos")
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

        MisPedidos();
        ActualizarPedidosActivity.this.finish();
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
}
