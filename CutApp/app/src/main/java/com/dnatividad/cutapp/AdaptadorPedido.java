package com.dnatividad.cutapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorPedido extends BaseAdapter {
    private Context context;
    private ArrayList<EntidadPedido> listItemsPedido;


    public AdaptadorPedido(Context context, ArrayList<EntidadPedido> listItems) {
        this.context = context;
        this.listItemsPedido = listItems;
    }


    @Override
    public int getCount() {
        return listItemsPedido.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemsPedido.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //aca se crea cada items y se le asignan los valores de cada elemento de cada items
        EntidadPedido item = (EntidadPedido) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.itemspedido,null);

        ImageView imgFoto =(ImageView) convertView.findViewById(R.id.imgFotoPedido);
        TextView codPed =(TextView) convertView.findViewById(R.id.tvCodigoPedido);
        TextView fecPed =(TextView) convertView.findViewById(R.id.tvFechaPedido);
        TextView  codPro =(TextView) convertView.findViewById(R.id.tvCodigoProducto);
        TextView  prodNam =(TextView) convertView.findViewById(R.id.tvProducto);
        TextView prodIng =(TextView) convertView.findViewById(R.id.tvProdIngrediente);
        TextView prodPecio =(TextView) convertView.findViewById(R.id.tvProdPrecio);
        TextView  usuaName =(TextView) convertView.findViewById(R.id.tvUsuarioNombre);
       /* TextView  codUsua =(TextView) convertView.findViewById(R.id.tvCodigoUsuario);
        TextView usuaApell =(TextView) convertView.findViewById(R.id.tvUsuarioApellido);
        TextView usuaDirec =(TextView) convertView.findViewById(R.id.tvUsuarioDireccion);
        TextView  usuaTelf =(TextView) convertView.findViewById(R.id.tvUsuarioTelefono);
        TextView  usuaFecNAc =(TextView) convertView.findViewById(R.id.tvUsuarioFechaNacimiento);
        TextView prodPorci =(TextView) convertView.findViewById(R.id.tvProductoPorciones);
        TextView usuaSabor =(TextView) convertView.findViewById(R.id.tvProductoSabor);
        TextView  pedMensa =(TextView) convertView.findViewById(R.id.tvPedidoMensaje);
        TextView  pedInfo =(TextView) convertView.findViewById(R.id.tvPedidoInformacion);*/
        TextView  pedFecEnt =(TextView) convertView.findViewById(R.id.tvPedidoFechaEntrega);
        TextView  pedEstado =(TextView) convertView.findViewById(R.id.tvPedidoEstado);


        imgFoto.setImageBitmap(item.getImgFoto());
        codPed.setText(item.getCodPed());
        fecPed.setText(item.getFecPed());
        codPro.setText(item.getCodPro());
        prodNam.setText(item.getProdNam());
        prodIng.setText(item.getProdIng());
        prodPecio.setText(item.getProdPecio());
        usuaName.setText(item.getUsuaName()+" "+item.getUsuaApell());
        /*usuaApell.setText(item.getUsuaApell());
        codUsua.setText(item.getCodUsua());
        usuaDirec.setText(item.getUsuaDirec());
        usuaTelf.setText(item.getUsuaTelf());
        usuaFecNAc.setText(item.getUsuaFecNAc());
        prodPorci.setText(item.getProdPorci());
        usuaSabor.setText(item.getUsuaSabor());
        pedMensa.setText(item.getPedMensa());
        pedInfo.setText(item.getPedInfo());*/
        pedFecEnt.setText(item.getPedFecEnt());
        pedEstado.setText(item.getPedEstado());
        return convertView;
    }
}
