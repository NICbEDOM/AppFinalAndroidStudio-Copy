package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

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

public class IniciarSesion extends AppCompatActivity {
    private EditText etNombreUsu, etContraseña;
    private Button btVolver, btIniciarSesion;
    private usuarios user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        etNombreUsu = (EditText)findViewById(R.id.IniciarSesion_EtNombreUsuario);
        etContraseña = (EditText)findViewById(R.id.IniciarSesion_ETContraseña);
        btIniciarSesion=(Button) findViewById(R.id.IniciarSesion_BtIniciarSesion);
        btVolver=(Button) findViewById(R.id.IniciarSesion_btVolver);

        btIniciarSesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Iniciarsesion(IniciarSesion.this).execute();
            }
        });

    }

    private usuarios consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/get-by-user.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("tabla", "usuario"));
        nameValuePairs.add(new BasicNameValuePair("usuario", etNombreUsu.getText().toString().trim())); // pasamos el id al servicio php

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

    class Iniciarsesion extends AsyncTask<String, String, String> {
        private Activity context;

        Iniciarsesion(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                user = consultar();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (user != null)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {

                            String contr = user.getContraseña();
                            String userCont =etContraseña.getText().toString();
                            Log.d("key of the message", "The message " + contr + " , "+userCont);
                        if(contr.equals(userCont)){

                           enviarAHome(user);
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

    void enviarAHome(usuarios user1){
        if(user1.rol.equals("admin")){
            Intent i = new Intent(IniciarSesion.this, gestion_admin.class);
            i.putExtra("idUserMain",user1.id);
            startActivity(i);
        }

      /*  if(user1.rol.equals("Docente")){
            Intent i = new Intent(IniciarSesion.this, gestion_admin.class);
            startActivity(i);
        }*/

        if(user1.rol.equals("Estudiante") || user1.rol.equals("Docente")){
            Intent i = new Intent(IniciarSesion.this,MainUsers.class);
            i.putExtra("idUserMain",user1.id);
            GlobalInfo.setUserActual(user);
            startActivity(i);
        }


    }

}