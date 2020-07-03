package com.dnatividad.cutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dnatividad.cutapp.Adaptadores.AdaptadorCitas;
import com.dnatividad.cutapp.Adaptadores.AdaptadorServicios;
import com.dnatividad.cutapp.Entidades.Citas;
import com.dnatividad.cutapp.Entidades.Citas_Servicios;
import com.dnatividad.cutapp.Entidades.Peluqueria;
import com.dnatividad.cutapp.Entidades.Servicios;
import com.dnatividad.cutapp.Entidades.Usuarios;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MisCitas extends AppCompatActivity {
    private ListView listItems;
    private AdaptadorCitas adaptadorCitas;

    String urlOrigin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_citas);
        setUrlOrigin();
        cargarCitas();
    }

    private void setUrlOrigin() {
        urlOrigin = getString(R.string.urlOrigin);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);

        //--------------obtengo el correo almacenado a la hora que se logueo------------------------
        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        String permisoAdmin = prefs.getString("PERMISOADMIN", "");
        String sesionIniciada = prefs.getString("SESIONINICIADA", "");
        Log.i("permiso Admin ====>", permisoAdmin.trim());
        Log.i("sesion Iniciada ====>", sesionIniciada.trim());
        //------------------------------------------------------------------------------------------

        //otorgo permiso de acceso a las opciones del menu
        if(permisoAdmin.equals("true")){
            switch (sesionIniciada){
                case "true":
                    //esta linea permite hacer visible un item del menu
                    MenuItem itemMenuCatalogo = menu.findItem(R.id.item_6);
                    itemMenuCatalogo.setVisible(true);
                    MenuItem itemMenuPedidos = menu.findItem(R.id.item_7);
                    itemMenuPedidos.setVisible(true);
                    MenuItem itemMenuRegProd = menu.findItem(R.id.item_8);
                    itemMenuRegProd.setVisible(true);
                    MenuItem itemMenuLogin = menu.findItem(R.id.item_1);
                    itemMenuLogin.setVisible(false);
                    MenuItem itemMenuRegistrar = menu.findItem(R.id.item_2);
                    itemMenuRegistrar.setVisible(false);
                    break;
                case "false":
                    MenuItem itemMenuMisCitas = menu.findItem(R.id.item_7);
                    itemMenuMisCitas.setVisible(true);
                    break;
            }
        }else {
            MenuItem itemMenuPedidos = menu.findItem(R.id.item_7);
            itemMenuPedidos.setVisible(false);
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

    public void cargarCitas(){

        SharedPreferences prefs = getSharedPreferences("PREFERENCIAS", Context.MODE_PRIVATE);
        final Integer idUsusarioSesion = Integer.parseInt(prefs.getString("IDUSUSARIO", ""));
        Log.i("Id Usuario Sesion ====>", idUsusarioSesion.toString());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlOrigin+"/citas/listar/" + idUsusarioSesion);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");

                    int responseCode = httpURLConnection.getResponseCode();
                    String responseMessage = httpURLConnection.getResponseMessage();
                    Log.i("response", responseMessage);
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader br=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String response="";
                        String line = "";
                        while ((line=br.readLine()) != null) {
                            response += line;
                        }
                        updateLista(response);

                    }else{
                        Log.v("CatalogClient", "Response code:"+ responseCode);
                        Log.v("CatalogClient", "Response message:"+ responseMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }
        });
    }

    void updateLista(String reportList) {

        final ArrayList<Citas>ListItems = new ArrayList<>();
        listItems = (ListView) findViewById(R.id.listaMisCitas);
        try {
            Log.i("reporte", reportList);
            JSONArray jsonArray = new JSONArray(reportList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);
                //Log.i("citas",objJson.toString());

                //region UsuarioRegistro
                JSONObject objJsonusuario = objJson.getJSONObject("usuario");
                //Log.i("objJsonusuario", objJsonusuario.toString());
                Usuarios usuarioRegistro = new Usuarios(
                        Integer.parseInt(objJsonusuario.getString("idUsuario")),
                        objJsonusuario.getString("nombreUsuario") + "\n",
                        objJsonusuario.getString("apellidoPaterno") + "\n",
                        objJsonusuario.getString("apellidoMaterno") + "\n",
                        "" + "\n",
                        1,
                        "" + "\n",
                        "" + "\n"
                );
                //endregion

                //region ListadoServicios
                JSONArray objJsonservicios = objJson.getJSONArray("servicios");
                ArrayList<Citas_Servicios> listaServicios = new ArrayList<>();
                //Log.i("objJsonservicios", objJsonservicios.toString());
                for (int j = 0; j < objJsonservicios.length(); j++) {
                    JSONObject objJsonServicio = objJsonservicios.getJSONObject(j);
                     listaServicios.add(
                             new Citas_Servicios(
                                     Integer.parseInt(objJson.getString("idCita")),
                                     new Servicios(
                                             Integer.parseInt(objJsonServicio.getString("idServicio")),
                                             objJsonServicio.getString("nombreServicio") + "\n",
                                             Double.parseDouble(objJsonServicio.getString("costoServicio")),
                                             objJsonServicio.getString("descripcionServicio") + "\n",
                                             objJsonServicio.getString("fotoServicio") + "\n"
                                     )
                             )
                     );
                }

                //endregion

                ListItems.add(new Citas(
                        Integer.parseInt(objJson.getString("idCita")),
                        objJson.getString("fechaCita") + "\n",
                        objJson.getString("horaCita") + "\n",
                        Boolean.parseBoolean(objJson.getString("calificadaCita")),
                        objJson.getString("comentarioCita") + "\n",
                        objJson.getString("estadoCita")+"\n",
                        usuarioRegistro,
                        listaServicios
                ));

            }

            runOnUiThread(new Runnable() {
                //Muestro el contenido del arraylist
                public void run() {
                    adaptadorCitas = new AdaptadorCitas(MisCitas.this, ListItems);
                    listItems.setAdapter(adaptadorCitas);
                }
            });

            listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {

                    Citas cita = ListItems.get(position);
                    Intent i = new Intent(getApplicationContext(), DetalleCita.class);
                    i.putExtra("idCita", String.valueOf(cita.getIdCita()));
                    i.putExtra("nombreServicio", cita.getLista_servicios().get(0).getIdServicio().getNombreServicio());
                    i.putExtra("descripcionServicio", cita.getLista_servicios().get(0).getIdServicio().getDescripcionServicio());
                    i.putExtra("costoServicio", String.valueOf(cita.getLista_servicios().get(0).getIdServicio().getCostoServicio()));
                    i.putExtra("estadoServicio", cita.getEstado());
                    startActivity(i);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        listItems = (ListView) findViewById(R.id.listaMisCitas);
        final ArrayList<Citas>ListItems = new ArrayList<Citas>();

        ListItems.add(new Citas(
                1,
                "20/02/2020",
                "10:00 am",
                false,
                "",
                new Usuarios(1,"Juan","Perez","Valdivia","",0,"juan@gmail.com",""),
                new Servicios(1,"Corte de cabello",20.0,"Corte para caballeros simple","",
                        new Peluqueria(1,"","","","","","")),"Generada"
                )

        );
        ListItems.add(new Citas(
                        2,
                        "23/10/2020",
                        "12:00 am",
                        false,
                        "",
                        new Usuarios(1,"Juan","Perez","Valdivia","",0,"juan@gmail.com",""),
                        new Servicios(1,"Corte de cabello",20.0,"Corte para caballeros simple","",
                                new Peluqueria(1,"","","","","","")),"Generada"
                )
        );
*/
        /*
        runOnUiThread(new Runnable() {
            //Muestro el contenido del arraylist
            public void run() {
                adaptadorCitas = new AdaptadorCitas(MisCitas.this, ListItems);
                listItems.setAdapter(adaptadorCitas);
            }
        });
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Citas citas = ListItems.get(position);
                Intent i = new Intent(getApplicationContext(), DetalleCita.class);
                i.putExtra("idServicio", String.valueOf(citas.getIdCita()));
                //i.putExtra("nombreServicio", citas.getServicios_registro().getNombreServicio());
                //i.putExtra("descripcionServicio", citas.getServicios_registro().getDescripcionServicio());
                //i.putExtra("costoServicio", String.valueOf(citas.getServicios_registro().getCostoServicio()));
                //i.putExtra("estadoServicio", String.valueOf(citas.getEstado()));
                startActivity(i);
            }
        });
         */
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

    //region Navegacion
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
    //endregion
}
