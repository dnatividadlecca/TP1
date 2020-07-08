package com.dnatividad.cutapp.Utilitarios.ManejoMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dnatividad.cutapp.R;

public class controlMenuOpciones {

    public void asignarOpcionesAcceso(Menu menu, String permisoAdmin, String sesionIniciada){
        MenuItem item;

        switch (sesionIniciada){
            case "true":
                item = menu.findItem(R.id.item_login);
                item.setVisible(false);

                item = menu.findItem(R.id.item_registroUsuarios);
                item.setVisible(false);

                switch (permisoAdmin){
                    case "true":
                        item = menu.findItem(R.id.item_misServicios);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_registroServicios);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_misCitas);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_registroCitas);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_reporteCitas);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_citasPorCalificar);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_misCalificaciones);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_nosotros);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_nosotrosEdicion);
                        item.setVisible(true);
                        break;

                    case "false":
                        item = menu.findItem(R.id.item_misServicios);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_registroServicios);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_misCitas);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_registroCitas);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_reporteCitas);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_citasPorCalificar);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_misCalificaciones);
                        item.setVisible(false);

                        item = menu.findItem(R.id.item_nosotros);
                        item.setVisible(true);

                        item = menu.findItem(R.id.item_nosotrosEdicion);
                        item.setVisible(false);
                        break;
                }

                item = menu.findItem(R.id.item_cerrarSesion);
                item.setVisible(true);
                break;
            case "false":
                item = menu.findItem(R.id.item_login);
                item.setVisible(true);

                item = menu.findItem(R.id.item_registroUsuarios);
                item.setVisible(true);

                item = menu.findItem(R.id.item_cerrarSesion);
                item.setVisible(false);
                break;
        }
    }

}
