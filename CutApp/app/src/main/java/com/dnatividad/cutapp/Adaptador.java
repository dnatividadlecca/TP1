package com.dnatividad.cutapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    private Context context;
    private ArrayList<Entidad> listItems;

    public Adaptador(Context context, ArrayList<Entidad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    //cuantos datos son los que se van a cargar
    public int getCount() {
        return listItems.size();
    }
    //devuelve la posicion
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
        Entidad item = (Entidad) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.items,null);
        ImageView imgFoto =(ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvCodigo =(TextView) convertView.findViewById(R.id.tvCodigo);
        TextView tvTitulo =(TextView) convertView.findViewById(R.id.tvTitulo);
        TextView  tvContenido =(TextView) convertView.findViewById(R.id.tvIngrediente);
        TextView  tvPrecio =(TextView) convertView.findViewById(R.id.tvPrecio);


        imgFoto.setImageBitmap(item.getImgFoto());
        tvCodigo.setText(item.getCodigo());
        tvTitulo.setText(item.getTitulo());
        tvContenido.setText(item.getContenido());
        tvPrecio.setText(item.getPrecio());
        return convertView;
    }
}

