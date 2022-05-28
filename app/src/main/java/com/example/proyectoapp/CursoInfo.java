package com.example.proyectoapp;

import static com.example.proyectoapp.R.id.CursoInfo_edNombreCurso;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CursoInfo extends Fragment {

    private TextView tvnombre,tvhorario,tvdescripcion,tv1P,tv2P,tv3P,tvNombreDocente;
    private String idCurso, nombreDoc;
    private cursos cur;
    private Layout myLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_curso_info, null);

        tvnombre = (TextView)view.findViewById(R.id.CursoInfo_edNombreCurso);
        tvhorario = (TextView)view.findViewById(R.id.cursoInfo_tvDiaCurso);
        tvdescripcion = (TextView)view.findViewById(R.id.cursoInfo_tvDescripcionCurso);
        tv1P = (TextView)view.findViewById(R.id.cursoInfo_tvPar);
        tv2P = (TextView)view.findViewById(R.id.cursoInfo_tvPar2);
        tv3P = (TextView)view.findViewById(R.id.cursoInfo_tvrPar3);
        tvNombreDocente = (TextView)view.findViewById(R.id.cursoinfo_tvnombreDocente);
        idCurso = getActivity().getIntent().getStringExtra("IdCurso");
        new CursoInfo.trarInfoCurso(getActivity()).execute();


        return view;
    }

    private Boolean consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/get-curso-by-id.php"; // Ruta
        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                cur = new cursos(json_array.getJSONObject(0));// instanciamos la clase multa para obtener los datos json
            }
        }

        url = Constants.URL + "claseUAO/getDocenteCurso.php"; // Ruta
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php
        nameValuePairs.add(new BasicNameValuePair("tipo", "Docente")); // pasamos el id al servicio php

       json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("curso");// accedemos al objeto json llamado multas
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                String nombre = json_array.getJSONObject(0).getString("nombre");
                String apellido = json_array.getJSONObject(0).getString("apellidos");// instanciamos la clase multa para obtener los datos json
                nombreDoc= nombre+" "+apellido;

            }

        }
       return true;


    }


    class trarInfoCurso extends AsyncTask<String, String, String> {
        private Activity context;

        trarInfoCurso(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {

                final boolean f= consultar();

                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (cur != null)
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            actualizarPagina(cur,nombreDoc);

                        }
                    });
                else
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_LONG).show();

                        }
                    });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    void empezar(){


    }

    void actualizarPagina(cursos cur,String nombreDoc){
        tvnombre.setText(cur.nombre);
        tvdescripcion.setText(cur.descripcion);
        String horario = cur.dia +"\n"+cur.horaInico+"-"+cur.horaFin;
        tvhorario.setText(horario);
        tv1P.setText(cur.primParcial);
        tv3P.setText(cur.terParcial);
        tv2P.setText(cur.segParcial);
        tvNombreDocente.setText(nombreDoc);
    }

}