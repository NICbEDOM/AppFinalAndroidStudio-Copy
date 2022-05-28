package com.example.proyectoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EstudiantesCursos_Frag extends Fragment {
    private ListView listaUsers;
    private ArrayList<usuarios> listaUsuarios;
    private ArrayList<String> listaInfo;
    private String idCurso;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_estudiantes_cursos_frag, null);
        listaUsers = (ListView) view.findViewById(R.id.AsistentesEvento_tvEstu);
        listaUsuarios = new ArrayList<usuarios>();
        listaInfo = new ArrayList<String>();
        idCurso = getActivity().getIntent().getStringExtra("IdCurso");
        new EstudiantesCursos_Frag.TraerUsuarios(getActivity()).execute();
        return view;
    }

    private ArrayList<String> consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getDocenteCurso.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php
        nameValuePairs.add(new BasicNameValuePair("tipo", "Estudiante")); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        Log.d("key of the message", "--------------------------" + json);

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio

            JSONArray json_array = object.optJSONArray("curso");// accedemos al objeto json llamado multas
            JSONArray jArray = new JSONArray();

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                ArrayList<usuarios> listaUsu = new ArrayList<usuarios>();
                listaInfo=new ArrayList<String>();
                listaUsuarios=new ArrayList<usuarios>();
                for(int i = 0;i<json_array.length();i++){
                    String nombre = json_array.getJSONObject(i).getString("nombre");
                    String apellido = json_array.getJSONObject(i).getString("apellidos");// instanciamos la clase multa para obtener los datos json
                    String Info= nombre+" "+apellido;
                    listaInfo.add(Info);
                }

                return listaInfo;
                //  return user;// retornamos la multa
            }
            return null;
        }
        return null;
    }

    class TraerUsuarios extends AsyncTask<String, String, String> {
        private Activity context;

        TraerUsuarios(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                listaInfo = consultar();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (listaUsuarios != null)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {

                            llenarListView();
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

    public void llenarListView() {
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaInfo);
        listaUsers.setAdapter(Adapter);
    }
}