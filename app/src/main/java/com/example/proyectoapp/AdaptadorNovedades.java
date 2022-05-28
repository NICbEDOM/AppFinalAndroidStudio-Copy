package com.example.proyectoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class AdaptadorNovedades extends BaseAdapter {

    private static LayoutInflater inflater=null;
    Context contexto;
    String [][] datos;

    public AdaptadorNovedades(Context contexto, String[][] datos) {
        this.contexto = contexto;
        this.datos = datos;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.lista_novedades,null);
        TextView titulo = (TextView) vista.findViewById(R.id.AdapNov_Titulo);
        TextView ususario = (TextView) vista.findViewById(R.id.AdapNov_usuario);
        TextView fecha = (TextView) vista.findViewById(R.id.AdapNov_hora);
        TextView descripcion = (TextView) vista.findViewById(R.id.AdapNov_descripcion);
        titulo.setText(datos[i][0]);
        ususario.setText(datos[i][1]);
        fecha.setText(datos[i][2]);
        descripcion.setText(datos[i][3]);
        return vista;
    }
    @Override
    public int getCount() {
        return datos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
