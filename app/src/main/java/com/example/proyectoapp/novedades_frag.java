package com.example.proyectoapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class novedades_frag extends Fragment {
    private ArrayList<novedades> listanovedades;
    private Button btCrearNov;
    private ArrayList<String> listaNombres;
    private ListView lvAllNovedades;
    private String idCurso;
    String [][] listaInfo;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_novedades_frag,null);
        lvAllNovedades = (ListView) view.findViewById(R.id.AsistentesEvento_tvEstu);
        btCrearNov = (Button) view.findViewById(R.id.Estu_novedades_frag_btCrear);

        listanovedades = new ArrayList<novedades>();
        listaNombres = new ArrayList<String>();
        idCurso = getActivity().getIntent().getStringExtra("IdCurso");
        new novedades_frag.TraerNovedades(getActivity()).execute();

        btCrearNov.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EscribirNovedad.class);
                i.putExtra("idCur",idCurso);
                startActivity(i);
            }
            });

        return view;
    }

    private ArrayList<novedades> consultar() throws JSONException, IOException {
        ArrayList<novedades> listaNov = new ArrayList<novedades>();
        String url = Constants.URL + "claseUAO/getNovedadesbyCurso.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        Log.d("key of the message", "--------------------------" + json);

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio

            JSONArray json_array = object.optJSONArray("novedad");// accedemos al objeto json llamado multas

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro


                listanovedades=new ArrayList<novedades>();
                for(int i = 0;i<json_array.length();i++){
                    novedades nove = new novedades(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                    String userInfo = nove.getTitulo() + " " + nove.getDescripcion();
                    listaNov.add(nove);
                }
            }
        }

        for(int i =0;i<listaNov.size();i++){
             url = Constants.URL + "claseUAO/get-by-id.php"; // Ruta

            nameValuePairs = new ArrayList<NameValuePair>(3);//definimos array
            nameValuePairs.add(new BasicNameValuePair("id", listaNov.get(i).getCreador())); // pasamos el id al servicio php
            nameValuePairs.add(new BasicNameValuePair("tabla", "id")); // pasamos el id al servicio php
            //DATOS


             json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

            Log.d("key of the message", "--------------------------" + json);

            if (json != null) { // si la respuesta no es vacia
                JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
                JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas

                if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                    usuarios user = new usuarios(json_array.getJSONObject(0));// instanciamos la clase multa para obtener los datos json
                    String res = user.getNombre()+" "+ user.getApellidos();
                    listaNombres.add(res);
                }
            }
        }
        return listaNov;
    }

    class TraerNovedades extends AsyncTask<String, String, String> {
        private Activity context;

        TraerNovedades(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                listanovedades = consultar();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (listanovedades != null)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {

                            try {
                                llenarListView();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    public void llenarListView() throws JSONException {

        /*for(int i =0;i<listanovedades.size();i++){
            novedades e = listanovedades.get(i);

            String info = "\b "+e.getTitulo()+"\b0 \n"+
                   listaNombres.get(i)+"-"+e.getFechaCreacion()+"\n"+
                    e.getDescripcion();
            listaInfo.add(info);
        }*/
        listaInfo = new String[listanovedades.size()][4];
        for(int i =0;i<listanovedades.size();i++){

                listaInfo[i][0] = listanovedades.get(i).getTitulo();
                listaInfo[i][1] = listaNombres.get(i);
                listaInfo[i][2] = listanovedades.get(i).getFechaCreacion();
                listaInfo[i][3] = listanovedades.get(i).getDescripcion();

        }
        lvAllNovedades.setAdapter(new AdaptadorNovedades(getActivity(),listaInfo));
    }


    private String consultarUser(String idUser) throws JSONException {
        return null;
    }
}