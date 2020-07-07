package com.dnatividad.cutapp.Utilitarios.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnatividad.cutapp.Utilitarios.Entidades.Citas;
import com.dnatividad.cutapp.R;

import java.util.ArrayList;

public class AdaptadorCitas extends BaseAdapter {
    private Context context;
    private ArrayList<Citas> listItems;

    public AdaptadorCitas(Context context, ArrayList<Citas> listItems) {
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
        Citas item = (Citas) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_detalle,null);
        ImageView imgFoto =(ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvCodigo =(TextView) convertView.findViewById(R.id.tvCodigo);
        TextView tvCodigoServicio =(TextView) convertView.findViewById(R.id.tvCodigoServicio);
        TextView tvTitulo =(TextView) convertView.findViewById(R.id.tvTitulo);
        TextView  tvContenido =(TextView) convertView.findViewById(R.id.tvIngrediente);
        TextView  tvPrecio =(TextView) convertView.findViewById(R.id.tvPrecio);
        TextView tvEstado = (TextView) convertView.findViewById(R.id.tvCitaDetalleEstado);


        imgFoto.setImageBitmap(item.getLista_servicios().get(0).getIdServicio().getImgFoto());
        tvCodigo.setText("Cita #: " + String.valueOf(item.getIdCita()));
        tvCodigoServicio.setText(String.valueOf(item.getLista_servicios().get(0).getIdServicio().getIdServicio()));
        tvTitulo.setText(item.getLista_servicios().get(0).getIdServicio().getNombreServicio());
        tvContenido.setText(item.getLista_servicios().get(0).getIdServicio().getDescripcionServicio());
        tvPrecio.setText("S/." + item.getLista_servicios().get(0).getIdServicio().getCostoServicio());
        tvEstado.setText("Estado: " +  item.getEstado());

        return convertView;
    }
}
