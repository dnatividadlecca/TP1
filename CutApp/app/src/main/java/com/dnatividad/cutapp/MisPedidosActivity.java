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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dnatividad.cutapp.App.Citas.Citas_Admin_ActualizarCitasActivity;
import com.dnatividad.cutapp.App.Nosotros.Nosotros_Cliente_NosotrosActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_LoginActivity;
import com.dnatividad.cutapp.App.Seguridad.Seguridad_RegistrarUsuarioActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_MisServiciosActivity;
import com.dnatividad.cutapp.App.Servicios.Servicios_Admin_RegistrarServicioActivity;
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

public class MisPedidosActivity extends AppCompatActivity {

    //*******************************ListView Imagen2***********************************************
    private ListView listItems;
    private AdaptadorPedido adaptador;

    //**********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pedidos);
        //cargar();
        cargar2();
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
            MenuItem itemMenuCatalogo = menu.findItem(R.id.item_registroCitas);
            itemMenuCatalogo.setVisible(true);

            MenuItem itemMenuRegProd = menu.findItem(R.id.item_registroServicios);
            itemMenuRegProd.setVisible(true);

            MenuItem itemMenuMisProd = menu.findItem(R.id.item_misServicios);
            itemMenuMisProd.setVisible(true);

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
        }
        //------------------------------------------------------------------------------------------
        MenuItem itemMenuRegistrar = menu.findItem(R.id.item_cerrarSesion);
        itemMenuRegistrar.setVisible(true);
        //------------------------------------------------------------------------------------------

        return true;
    }


    public void cargar2(){


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/mispedidos")
                .url("http://cutapp.atwebpages.com/index.php/mispedidos")
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
                    listItems=(ListView) findViewById(R.id.listaMisCitas);
                    final ArrayList<EntidadPedido>ListItems=new ArrayList<>();
                    int i = 0;

                    for (Map<String, Object> x : retorno) {

                        ListItems.add(new EntidadPedido(x.get("foto")+"".trim(),"Pedido N°: "+x.get("cod_pedido")+"".trim(),"Fec. Pedido: "+x.get("fecha_pedido").toString()+
                                "","Cod. PRO - "+x.get("cod_producto").toString()+"",x.get("titulo")+"", x.get("ingredientes")+
                                "","S/.  "+x.get("precio")+"",x.get("cod_usuario")+"",x.get("nombre")+
                                "",x.get("apellido")+"",x.get("direccion")+"",x.get("telefono")+
                                "\n",x.get("fecha_nacimiento")+"",x.get("porciones")+"",x.get("sabor")+
                                "\n",x.get("mensaje_torta")+"",x.get("info_adicional")+
                                "\n","Entrega : "+x.get("fecha_entrega")+"",x.get("estado")+""));


                    }

                    runOnUiThread(new Runnable() {
                        public void run() {

                            adaptador = new AdaptadorPedido(MisPedidosActivity.this, ListItems);
                            listItems.setAdapter(adaptador);



                        }
                    });

                    listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int position, long arg3) {
                            Map<String, Object> x = retorno.get(position);
                            //Log.i("actualizar ====>", x.get("titulo")+"");
                            //startActivity(new Intent(getApplicationContext(),PedidosActivity.class));
                            //intent put extra o shared preference
                            Intent i = new Intent(getApplicationContext(), Citas_Admin_ActualizarCitasActivity.class);
                            i.putExtra("foto",x.get("foto").toString());
                            i.putExtra("cod_pedido",x.get("cod_pedido").toString());
                            i.putExtra("cod_producto",x.get("cod_producto").toString());
                            i.putExtra("titulo",x.get("titulo").toString());
                            i.putExtra("ingredientes",x.get("ingredientes").toString());
                            i.putExtra("precio",x.get("precio").toString());
                            i.putExtra("fecha_pedido",x.get("fecha_pedido").toString());
                            i.putExtra("fecha_entrega",x.get("fecha_entrega").toString());
                            i.putExtra("cod_usuario",x.get("cod_usuario").toString());
                            i.putExtra("nombre",x.get("nombre").toString());
                            i.putExtra("apellido",x.get("apellido").toString());
                            i.putExtra("direccion",x.get("direccion").toString());

                            i.putExtra("telefono",x.get("telefono").toString());
                            i.putExtra("fecha_nacimiento",x.get("fecha_nacimiento").toString());
                            i.putExtra("porciones",x.get("porciones").toString());
                            i.putExtra("sabor",x.get("sabor").toString());
                            i.putExtra("mensaje_torta",x.get("mensaje_torta").toString());
                            i.putExtra("info_adicional",x.get("info_adicional").toString());
                            i.putExtra("estado",x.get("estado").toString());
                            startActivity(i);
                        }
                    });

                }
            }
        });
    }


    public void cargar(){


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //.url("http://lasrositas.dx.am/index.php/mispedidos")
                .url("http://cutapp.atwebpages.com/index.php/mispedidos")
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

                    final String[] matriz = new String[retorno.size()];
                    int i = 0;




                    for (Map<String, Object> x : retorno) {
                        matriz[i++] = (String) ( "\nCod. Pedido : " +x.get("cod_pedido") + "\n" +"Fecha del Pedido : " + x.get("fecha_pedido") +"\n\n" +"Cod. : " +x.get("cod_producto") + "\n"  +  "Producto : " +x.get("titulo")+ "\n" + "Ingredientes : "+  x.get("ingredientes") + "\n" + "Precio S/. "+  x.get("precio") +"\n" + "Cod. Usuario : " + x.get("cod_usuario") + "\n" + "nombre : " + x.get("nombre") + "\n" + "Apellido : " + x.get("apellido") + "\n" + "Dirección : " + x.get("direccion") + "\n" + "Teléfono : " + x.get("telefono") + "\n" + "Fecha de Nacimiento : " + x.get("fecha_nacimiento") + "\n" +"Porciones : " + x.get("porciones") + "\n"  + "Sabor : " + x.get("sabor") + "\n" + "Mensaje de la Torta : " + x.get("mensaje_torta") + "\n" + "Info. Adicional : " + x.get("info_adicional") + "\n\n" + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "Entrega : " + x.get("fecha_entrega") +"\n" +"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+ "Estado : " + x.get("estado") + "\n");
                    }

                    final ListView lstMisPedidos = (ListView)findViewById(R.id.listaMisCitas);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                    MisPedidosActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    matriz);
                            lstMisPedidos.setAdapter(adaptador);

                        }
                    });

                    lstMisPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int position, long arg3) {
                            Map<String, Object> x = retorno.get(position);
                            //Log.i("actualizar ====>", x.get("titulo")+"");
                            //startActivity(new Intent(getApplicationContext(),PedidosActivity.class));
                            //intent put extra o shared preference
                            Intent i = new Intent(getApplicationContext(), Citas_Admin_ActualizarCitasActivity.class);
                            i.putExtra("cod_pedido",x.get("cod_pedido").toString());
                            i.putExtra("cod_producto",x.get("cod_producto").toString());
                            i.putExtra("titulo",x.get("titulo").toString());
                            i.putExtra("ingredientes",x.get("ingredientes").toString());
                            i.putExtra("precio",x.get("precio").toString());
                            i.putExtra("fecha_pedido",x.get("fecha_pedido").toString());
                            i.putExtra("fecha_entrega",x.get("fecha_entrega").toString());
                            i.putExtra("cod_usuario",x.get("cod_usuario").toString());
                            i.putExtra("nombre",x.get("nombre").toString());
                            i.putExtra("apellido",x.get("apellido").toString());
                            i.putExtra("direccion",x.get("direccion").toString());
                            i.putExtra("telefono",x.get("telefono").toString());
                            i.putExtra("fecha_nacimiento",x.get("fecha_nacimiento").toString());
                            i.putExtra("porciones",x.get("porciones").toString());
                            i.putExtra("sabor",x.get("sabor").toString());
                            i.putExtra("mensaje_torta",x.get("mensaje_torta").toString());
                            i.putExtra("info_adicional",x.get("info_adicional").toString());
                            i.putExtra("estado",x.get("estado").toString());
                            startActivity(i);
                        }
                    });

                }
            }
        });
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
        else if (id ==R.id.item_cerrarSesion){
            //Toast.makeText(this,"Cerrar Sesión", Toast.LENGTH_SHORT).show();
            cerrarSesion();
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
                        Intent intent = new Intent(getApplicationContext(), Seguridad_LoginActivity.class);
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
