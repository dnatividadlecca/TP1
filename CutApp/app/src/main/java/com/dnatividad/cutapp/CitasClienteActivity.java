package com.dnatividad.cutapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CitasClienteActivity extends AppCompatActivity {
    private ImageView img;
    private TextView cod;
    private TextView prod;
    private TextView ing;

    //extraer la fecha del calendario
    CalendarView calendarView;
    TextView mydate;

    private static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;

    //declaro mi variable fotoEnBase64 que almacenara el codigo
    String fotoEnBase64 ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_clientes);

        //coge el texto de los campos del catalogo
        img=(ImageView) findViewById(R.id.imgfotoServicio);
        cod = (TextView)findViewById(R.id.txt_idServicio);
        prod = (TextView)findViewById(R.id.txt_nombreServicio);
        ing = (TextView)findViewById(R.id.txt_ingredientes);

        String datos0 = getIntent().getStringExtra("foto");
        String datos1 = getIntent().getStringExtra("codigo");
        String datos2 = getIntent().getStringExtra("titulo");
        String datos3 = getIntent().getStringExtra("ingredientes");

        //obtengo la imagen codificada como string y se la envio al metodo base64ToBitmap la cual me devuelve la imagen
        byte [] encodeByte = Base64.decode(String.valueOf(datos0),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        //*************************************************************************************************
        img.setImageBitmap(bitmap);
        cod.setText(datos1);
        prod.setText(datos2);
        ing.setText(datos3);

        //carga los spinner sabores, spinner porciones del pedido
        Spinner spi_porcion = (Spinner) findViewById(R.id.spinner_porciones);
        String[] porciones = {"10","20","30","40","50","60","70","80","90","100","110","120"};
        spi_porcion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, porciones));

        Spinner spi_sabor = (Spinner) findViewById(R.id.spinner_sabor);
        String[] sabores = {"TORTA DE CHOCOLATE","TORTA DE ZANAHORIA","KEKE DE NOVIA","TORTA DE CHOCOCHIP", "TORTA DE RED VELVET" , "TORTA DE VAINILLA" , "MARMOLEADO"};
        spi_sabor.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sabores));

        //extraer la fecha del calendario
        calendarView = (CalendarView)findViewById(R.id.simpleCalendarView);
        mydate=(TextView)findViewById(R.id.fechaCita);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i +"/"+ (i1+1) + "/" + i2;
                mydate.setText(date);
            }
        });
        ObtenerCodigoPedido();

    }
    //*************************Obtener Codigo Pedido Incrementado en 1 *********************************************
    public void ObtenerCodigoPedido(){
        final TextView codpedido=findViewById(R.id.txt_cod_pedido);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/obtenerUltimoCodPedido")
                .url("http://cutapp.atwebpages.com/index.php/obtenerUltimoCodPedido")
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
                    Log.i("COD PEDIDO ====>", cadenaJson);

                    try {
                        JSONArray resp= new JSONArray(cadenaJson);

                        if ((resp == null) && (resp.equals(" "))){
                            // Log.i("no entro ====>", cadenaJson);
                        }
                        else{
                            String valor="";
                            for(int index = 0;index < resp.length(); index++) {
                                JSONObject jsonObject = resp.getJSONObject(index);
                                valor = jsonObject.getString("cod_pedido");
                                valor=AunmentarUnoMasCodigo(valor);
                                //obtengo los valores del JSONArray y lo convierto a aun jsonObject para poder obtener sus valores
                            }
                            codpedido.setText(valor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    //obtengo el valor la cadena qu tiene la consulta con el ultimo codigo de pedido y le aumento en uno para el nuevo pedido
    public String AunmentarUnoMasCodigo(String cadena){
        String numCuenta = cadena;
        int valor=Integer.parseInt(numCuenta)+1;
        cadena=valor+"".trim();
        return cadena;


    }
    //**********************************************************************************************

    //decodifica la imagem y la devuelve como BITMAP
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public void RegistrarPedido(View v){

        TextView reg_cod_pedido = (TextView) findViewById(R.id.txt_cod_pedido);
        TextView reg_cod_producto = (TextView) findViewById(R.id.txt_idServicio);

        //extraer la fecha del sistema
        SimpleDateFormat simpleDatePickerFechaPedido = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = simpleDatePickerFechaPedido.format(date);


        Spinner spinner_porciones = (Spinner) findViewById(R.id.spinner_porciones);
        String porciones = spinner_porciones.getSelectedItem().toString();

        Spinner spinner_sabor = (Spinner) findViewById(R.id.spinner_sabor);
        String sabor = spinner_sabor.getSelectedItem().toString();

        EditText reg_mensaje = (EditText) findViewById(R.id.txt_dedicatoria);
        EditText reg_adicional = (EditText) findViewById(R.id.txt_mensaje);

        //--------------obtengo el correo almacenado a la hora que se logueo------------------------
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        String correo = prefs.getString("CADENA", "");
        Log.i("usuario logueado ====>", correo);
        //------------------------------------------------------------------------------------------

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cod_pedido", reg_cod_pedido.getText().toString())
                .addFormDataPart("cod_producto", reg_cod_producto.getText().toString())
                .addFormDataPart("cod_usuario",correo )
                .addFormDataPart("fecha_pedido", fecha)
                .addFormDataPart("fecha_entrega", mydate.getText().toString())
                .addFormDataPart("porciones", porciones)
                .addFormDataPart("sabor", sabor)
                .addFormDataPart("mensaje_torta", reg_mensaje.getText().toString())
                .addFormDataPart("info_adicional", reg_adicional.getText().toString())
                .addFormDataPart("estado", "RECIBIDO")
                .build();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/reg_pedidos")
                .url("http://cutapp.atwebpages.com/index.php/reg_pedidos")
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
                    Log.i("PEDIDO ====>", cadenaJson);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast= Toast.makeText(getApplicationContext(), "Registro de Pedido exitoso", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            Catalogo();
                            CitasClienteActivity.this.finish();
                        }
                    });

                }
            }
        });


    }

    //------------------------------------MENU-----------------------------------------------

    //metodo para mostrar y ocultar en menu
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
            MenuItem itemMenuCatalogo = menu.findItem(R.id.item_registroCitas);
            itemMenuCatalogo.setVisible(true);

            MenuItem itemMenuPedidos = menu.findItem(R.id.item_misCitas);
            itemMenuPedidos.setVisible(true);

            MenuItem itemMenuLogin = menu.findItem(R.id.item_login);
            itemMenuLogin.setVisible(false);

            MenuItem itemMenuRegistrar = menu.findItem(R.id.item_registroUsuarios);
            itemMenuRegistrar.setVisible(false);

        }else {
            MenuItem itemMenuPedidos = menu.findItem(R.id.item_misCitas);
            itemMenuPedidos.setVisible(false);

            MenuItem itemMenuLogin = menu.findItem(R.id.item_login);
            itemMenuLogin.setVisible(false);

            MenuItem itemMenuRegistrar = menu.findItem(R.id.item_registroUsuarios);
            itemMenuRegistrar.setVisible(false);

            MenuItem itemMenuCatalogo = menu.findItem(R.id.item_registroCitas);
            itemMenuCatalogo.setVisible(true);
        }
        //------------------------------------------------------------------------------------------

        return true;
    }

    //metodo para asignar las funciones de las opciones
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id ==R.id.item_login){
            Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
            Login();
        }
        else if (id ==R.id.item_registroUsuarios){
            Toast.makeText(this,"Registrar usurio", Toast.LENGTH_SHORT).show();
            RegistrarUsuario();
        }
        else if (id ==R.id.item_nosotros){
            Toast.makeText(this,"Nosotros", Toast.LENGTH_SHORT).show();
            Nosotros();
        }
        else if (id ==R.id.item_contactenos){
            Toast.makeText(this,"Contactenos", Toast.LENGTH_SHORT).show();
            Contactenos();
        }
        else if (id ==R.id.item_ubicanos){
            Toast.makeText(this,"Ubícanos", Toast.LENGTH_SHORT).show();
            Ubicanos();
        }
        else if (id ==R.id.item_registroCitas){
            Toast.makeText(this,"Catalogo", Toast.LENGTH_SHORT).show();
            Catalogo();
        }
        else if (id ==R.id.item_misCitas){
            Toast.makeText(this,"Mis Pedidos", Toast.LENGTH_SHORT).show();
            MisPedidos();
        }
        else if (id ==R.id.item_registroServicios){
            Toast.makeText(this,"Reg. Producto", Toast.LENGTH_SHORT).show();
            reg_producto();
        }
        else if (id ==R.id.item_misServicios){
            Toast.makeText(this,"Mis Productos", Toast.LENGTH_SHORT).show();
            MisProductos();
        }
        return super.onOptionsItemSelected(item);

    }


    //Navegacion de los botones del menu
    public void Login(){
        Intent login = new Intent(this, Seguridad_LoginActivity.class);
        startActivity(login);
    }
    public void RegistrarUsuario(){
        Intent registrarusuario = new Intent(this, Seguridad_RegistrarUsuarioActivity.class);
        startActivity(registrarusuario);
    }
    public void Nosotros(){
        Intent nosotros = new Intent(this, Nosotros_Cliente_NosotrosActivity.class);
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
        Intent producto = new Intent(this, Servicios_Admin_RegistrarServicioActivity.class);
        startActivity(producto);
    }
    public void MisProductos(){
        Intent misproducto = new Intent(this, Servicios_Admin_MisServiciosActivity.class);
        startActivity(misproducto);
    }
}
