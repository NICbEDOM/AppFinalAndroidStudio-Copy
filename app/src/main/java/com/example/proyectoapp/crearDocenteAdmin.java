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
import java.util.List;;

public class crearDocenteAdmin extends AppCompatActivity {
    private EditText Etnombre, EtApellido, EtCedula, EtFacultad, EtPrograma, EtUsuario, ETContraseña;
    private Spinner spRol;
    private Button btCrear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_docente_admin);
        btCrear = (Button)findViewById(R.id.crearUser_BtCrear);
        Etnombre = (EditText)findViewById(R.id.crearUser_etNombre);
        EtApellido = (EditText)findViewById(R.id.crearUser_etApellido);
        EtCedula = (EditText)findViewById(R.id.crearUser_etCedula);
        EtFacultad = (EditText)findViewById(R.id.crearUser_etFacultad);
        EtPrograma = (EditText)findViewById(R.id.crearUser_etPrograma);
        EtUsuario = (EditText)findViewById(R.id.crearUser_etUsuario);
        ETContraseña = (EditText)findViewById(R.id.crearUser_etContraseña);
        spRol = (Spinner) findViewById(R.id.crearUser_SpRol);

        String[] lista={"Estudiante","Docente"};
        ArrayAdapter<String>Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lista);
        spRol.setAdapter(Adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Crear Usuario");

        btCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ((!Etnombre.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!EtApellido.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!EtCedula.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!EtFacultad.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!EtPrograma.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!EtUsuario.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!ETContraseña.getText().toString().trim().equalsIgnoreCase(""))){

                    new Insertar(crearDocenteAdmin.this).execute();
                } else {

                    Toast.makeText(crearDocenteAdmin.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean insertar() {

        String url = Constants.URL + "claseUAO/add.php";

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

    class Insertar extends AsyncTask<String, String, String> {
        private Activity context;

        Insertar(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                boolean  seInserto = false;
                usuarios user = BuscarUsuario();
                if(user == null){
                    seInserto = insertar();
                }


                if (seInserto)
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "usuario insertado", Toast.LENGTH_LONG).show();
                            Etnombre.setText("");
                            EtApellido.setText("");
                            EtCedula.setText("");
                            EtFacultad.setText("");
                            EtPrograma.setText("");
                            EtUsuario.setText("");
                            ETContraseña.setText("");
                        }
                    });
                else
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(user != null){
                                Toast.makeText(context, "Ese usuario ya existe", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, "usuario no insertado", Toast.LENGTH_LONG).show();
                            }

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

    private usuarios BuscarUsuario() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/get-by-user.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("tabla", "usuario"));
        nameValuePairs.add(new BasicNameValuePair("usuario",EtUsuario.getText().toString().trim())); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                usuarios user = new usuarios(json_array.getJSONObject(0));// instanciamos la clase multa para obtener los datos json
                return user;// retornamos la multa
            }
            return null;
        }
        return null;
    }
}