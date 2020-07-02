package com.dnatividad.cutapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReportePedidosActivity extends AppCompatActivity {


    //*******************************ListView Imagen***********************************************
    private ListView listItems;
    private AdaptadorPedido adaptador;

    //**********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_pedidos);
        CargarMisPedidos();

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

        }else {
            MenuItem itemMenuPedidos = menu.findItem(R.id.item_7);
            itemMenuPedidos.setVisible(false);
            MenuItem itemMenuCatalogo = menu.findItem(R.id.item_6);
            itemMenuCatalogo.setVisible(true);
            MenuItem itemMenuLogin = menu.findItem(R.id.item_1);
            itemMenuLogin.setVisible(false);
            MenuItem itemMenuRegistrar = menu.findItem(R.id.item_2);
            itemMenuRegistrar.setVisible(false);
        }
        //------------------------------------------------------------------------------------------
        MenuItem itemMenuRegistrar = menu.findItem(R.id.item_11);
        itemMenuRegistrar.setVisible(true);
        //------------------------------------------------------------------------------------------

        return true;
    }

    public void CargarMisPedidos(){
        //obtengo el usuario de la sesion iniciada
        SharedPreferences prefs= getSharedPreferences("PREFERENCIAS",MODE_PRIVATE);
        String usuario = prefs.getString("CADENA","");


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/reportes/"+usuario)
                .url("http://cutapp.atwebpages.com/index.php/reportes/"+usuario)
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
                    Log.i("====>", cadenaJson);
                    Gson gson = new Gson();
                    Type stringStringMap = new TypeToken<ArrayList<Map<String, Object>>>() { }.getType();
                    final ArrayList<Map<String, Object>> retorno = gson.fromJson(cadenaJson, stringStringMap);
                    listItems=(ListView) findViewById(R.id.listaMisPedidos);
                    final ArrayList<EntidadPedido>ListItems=new ArrayList<>();
                    int i = 0;
                    for (Map<String, Object> x : retorno) {
                        ListItems.add(new EntidadPedido(x.get("foto")+"".trim(),"Pedido N°: "+x.get("cod_pedido")+"".trim(),"Fec. Pedido: "+x.get("fecha_pedido").toString()+
                                "","Cod. PRO - "+x.get("cod_producto").toString()+"",x.get("titulo")+"", x.get("ingredientes")+
                                "","S/.  "+x.get("precio")+"",x.get("cod_usuario")+"",x.get("nombre")+
                                "",x.get("apellido")+"",x.get("direccion")+"",x.get("telefono")+
                                "",x.get("fecha_nacimiento")+"",x.get("porciones")+"",x.get("sabor")+
                                "",x.get("mensaje_torta")+"",x.get("info_adicional")+
                                "","Fecha de Entrega : "+x.get("fecha_entrega")+"",x.get("estado")+""));
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            adaptador = new AdaptadorPedido(ReportePedidosActivity.this, ListItems);
                            listItems.setAdapter(adaptador);

                        }
                    });
                }

            }
        });
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
}
