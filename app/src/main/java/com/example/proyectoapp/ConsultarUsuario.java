package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.HardwareRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ConsultarUsuario extends AppCompatActivity {
    private EditText edBuscador;
    private ListView lvAllUser;
    private String tipoUser;
    private ArrayList<usuarios> listaUsuarios;
    private ArrayList<String> listaInfo;
    private Button btBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_usuario);

        edBuscador = (EditText)findViewById(R.id.consultarUser_etBuscar);
        lvAllUser = (ListView)findViewById(R.id.ConsultarUser_lvUsuarios);
        btBuscar = (Button)findViewById(R.id.ConsultarUsuario_btBuscar);
        tipoUser = getIntent().getStringExtra("TipoUsuario");
        listaUsuarios = new ArrayList<usuarios>();
        listaInfo = new ArrayList<String>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Consultar Usuarios");
        lvAllUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                enviarAEditar(position);
            }
        });

        new ConsultarUsuario.TraerUsuarios(ConsultarUsuario.this).execute();

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edBuscador.getText().toString().trim().equalsIgnoreCase("")){
                    new ConsultarUsuario.BuscarUser(ConsultarUsuario.this).execute();
                }else{
                    new ConsultarUsuario.TraerUsuarios(ConsultarUsuario.this).execute();
                }

            }
        });
    }

    void enviarAEditar(int pos){
        Intent i = new Intent(this, EditarUsuario.class);
        i.putExtra("UsuarioAEditar", listaUsuarios.get(pos).id);
        startActivity(i);
    }

    private ArrayList<usuarios> consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getUsers.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("tipo", tipoUser.trim())); // pasamos el id al servicio php



        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        Log.d("key of the message", "--------------------------" + json);

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio

            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            JSONArray jArray = new JSONArray();

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                ArrayList<usuarios> listaUsu = new ArrayList<usuarios>();
                listaInfo=new ArrayList<String>();
                listaUsuarios=new ArrayList<usuarios>();
               for(int i = 0;i<json_array.length();i++){
                   usuarios user = new usuarios(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                    String userInfo = user.getNombre() + " " + user.getApellidos();
                   listaInfo.add(userInfo);
                   listaUsu.add(user);
               }

               return listaUsu;
              //  return user;// retornamos la multa
            }
            return null;
        }
        return null;
    }



    private ArrayList<usuarios> buscarUsuarioPorNombre() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/get-by-user.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("tabla", "nombre"));
        nameValuePairs.add(new BasicNameValuePair("usuario", edBuscador.getText().toString().trim())); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas

            ArrayList<usuarios> listaUsu = new ArrayList<usuarios>();
            listaInfo=new ArrayList<String>();
            listaUsuarios=new ArrayList<usuarios>();
            for(int i = 0;i<json_array.length();i++){
                usuarios user = new usuarios(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                String userInfo = user.getNombre() + " " + user.getApellidos();
                listaInfo.add(userInfo);
                listaUsu.add(user);
            }
            return listaUsu;
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
                listaUsuarios = consultar();
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

    public void llenarListView(){
        ArrayAdapter<String>Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaInfo);
        lvAllUser.setAdapter(Adapter);
    }


    class BuscarUser extends AsyncTask<String, String, String> {
        private Activity context;

        BuscarUser(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                listaUsuarios = buscarUsuarioPorNombre();
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
}