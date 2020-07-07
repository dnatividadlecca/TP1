package com.dnatividad.cutapp.Utilitarios.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnatividad.cutapp.Utilitarios.Entidades.Servicios;
import com.dnatividad.cutapp.R;

import java.util.ArrayList;

public class AdaptadorServicios extends BaseAdapter {
    private Context context;
    private ArrayList<Servicios> listItems;

    public AdaptadorServicios(Context context, ArrayList<Servicios> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //aca se crea cada items y se le asignan los valores de cada elemento de cada items
        Servicios item = (Servicios) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.items,null);
        ImageView imgFoto =(ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvCodigo =(TextView) convertView.findViewById(R.id.tvCodigo);
        TextView tvTitulo =(TextView) convertView.findViewById(R.id.tvTitulo);
        TextView  tvContenido =(TextView) convertView.findViewById(R.id.tvIngrediente);
        TextView  tvPrecio =(TextView) convertView.findViewById(R.id.tvPrecio);


        imgFoto.setImageBitmap(item.getImgFoto());
        tvCodigo.setText("Servicio #: " + String.valueOf(item.getIdServicio()));
        tvTitulo.setText(item.getNombreServicio());
        tvContenido.setText(item.getDescripcionServicio());
        tvPrecio.setText("S/." + item.getCostoServicio().toString());

        return convertView;
    }
}
