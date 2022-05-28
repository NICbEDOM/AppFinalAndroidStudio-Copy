package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class EditarUsuario extends AppCompatActivity {

    private EditText Etnombre, EtApellido, EtCedula, EtFacultad, EtPrograma, EtUsuario, ETContraseña;
    private Spinner spRol;
    private Button btCrear, btEliminar;
    private usuarios userAEditar;
    private String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        btCrear = (Button)findViewById(R.id.EditUser_BtCrear);
        btEliminar= (Button)findViewById(R.id.editarUser_btEliminar);
        Etnombre = (EditText)findViewById(R.id.EditUser_etNombre);
        EtApellido = (EditText)findViewById(R.id.EditUser_etApellido);
        EtCedula = (EditText)findViewById(R.id.EditUser_etCedula);
        EtFacultad = (EditText)findViewById(R.id.EditUser_etFacultad);
        EtPrograma = (EditText)findViewById(R.id.EditUser_etPrograma);
        EtUsuario = (EditText)findViewById(R.id.EditUser_etUsuario);
        ETContraseña = (EditText)findViewById(R.id.EditUser_etContraseña);
        spRol = (Spinner) findViewById(R.id.EditUser_SpRol);

        idUser = getIntent().getStringExtra("UsuarioAEditar");

        String[] lista={"Estudiante","Docente"};
        ArrayAdapter<String>Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lista);
        spRol.setAdapter(Adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editar usuario");

        new EditarUsuario.BuscarUser(EditarUsuario.this).execute();

        btCrear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                    new EditarUsuario.Modificar(EditarUsuario.this).execute();
            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Eliminar(EditarUsuario.this).execute();
            }
        });

    }

    private boolean editar() {

        String url = Constants.URL + "claseUAO/update.php";

        //DATOS
        List<NameValuePair> nameValuePairs; // definimos la lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(8); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("cedulas", EtCedula.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("nombre", Etnombre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("apellidos", EtApellido.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("facultad", EtFacultad.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("programa", EtPrograma.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("usuario", EtUsuario.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("contrasena", ETContraseña.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("rol", spRol.getSelectedItem().toString().trim()));
        Log.d("key of the message", "************************************* " + nameValuePairs);
        boolean response = APIHandler.POST(url, nameValuePairs);
        return response;
    }

    private usuarios buscarUsuario() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/get-by-id.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("tabla", "id"));
        nameValuePairs.add(new BasicNameValuePair("id", idUser)); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                usuarios user = new usuarios(json_array.getJSONObject(0));// instanciamos la clase multa para obtener los datos json
                return user;// retornamos la multa
            }
        }
        return null;
    }



    class Modificar extends AsyncTask<String, String, String> {
        private Activity context;

        Modificar(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            if (editar())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Usuario modificada", Toast.LENGTH_LONG).show();
                        regresar();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Usuario no encontrada", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    class BuscarUser extends AsyncTask<String, String, String> {
        private Activity context;

        BuscarUser(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                userAEditar = buscarUsuario();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (userAEditar != null)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            llenarCampos();
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

    public void llenarCampos(){
        Etnombre.setText(userAEditar.nombre);
        EtApellido.setText(userAEditar.apellidos);
        EtCedula.setText(userAEditar.cedulas);
        EtFacultad.setText(userAEditar.facultad);
        EtPrograma.setText(userAEditar.programa);
        EtUsuario.setText(userAEditar.usuario);
        ETContraseña.setText(userAEditar.contraseña);
        if(userAEditar.rol.equals("Estudiante")){
            spRol.setSelection(0);
        }else{
            spRol.setSelection(1);
        }

    }

    private void regresar(){
        Intent i = new Intent(this, ConsultarUsuario.class);
        startActivity(i);
    }

    private boolean eliminar() {

        String url = Constants.URL + "claseUAO/delete.php";
        Log.d("key of the message", "-------------------------------------------" + idUser);
        //DATOS
        List<NameValuePair> nameValuePairs;
        nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("tabla", "id"));
        nameValuePairs.add(new BasicNameValuePair("id", idUser)); // pasamos el id al servicio php
        boolean response = APIHandler.POST(url, nameValuePairs); // Enviamos el id al webservices
        return response;
    }

    class Eliminar extends AsyncTask<String, String, String> {
        private Activity context;

        Eliminar(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            if (eliminar())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_LONG).show();
                        regresar();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Usuario no eliminado", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

}