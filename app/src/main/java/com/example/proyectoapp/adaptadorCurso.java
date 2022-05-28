package com.example.proyectoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adaptadorCurso extends BaseAdapter {

    private static LayoutInflater inflater=null;
    Context contexto;
    String [][] datos;

    public adaptadorCurso(Context contexto, String[][] datos) {
        this.contexto = contexto;
        this.datos = datos;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View vista = inflater.inflate(R.layout.lista_cursos,null);
        TextView titulo = (TextView) vista.findViewById(R.id.adaptadorCurso_titulo);
        TextView dia = (TextView) vista.findViewById(R.id.adaptadorCurso_dia);
        TextView inicio = (TextView) vista.findViewById(R.id.adaptadorCurso_inicio);
        TextView fin = (TextView) vista.findViewById(R.id.adaptadorCurso_fin);
        titulo.setText(datos[i][0]);
        dia.setText(datos[i][1]);
        inicio.setText(datos[i][2]);
        fin.setText(datos[i][3]);
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
