package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

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

public class EscribirNovedad extends AppCompatActivity {

    private usuarios user;
    private String idCurso;
    private EditText edTitulo, edDescripcion;
    private Button btCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escribir_novedad);

        edTitulo = (EditText) findViewById(R.id.escribirNovedad_ETTitulo);
        edDescripcion = (EditText) findViewById(R.id.EscribirNovedad_ETdescripcion);
        btCrear = (Button) findViewById(R.id.EscribirNoveda_BTCrear);
        user = GlobalInfo.getUserActual();
        idCurso = GlobalInfo.getCursoActual();
        Log.d("key of the message", "************************************* " + idCurso);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Escribir novedad");

        btCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ((!edTitulo.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!edDescripcion.getText().toString().trim().equalsIgnoreCase("")))
                {
                    new EscribirNovedad.Insertar(EscribirNovedad.this).execute();
                } else {
                    Toast.makeText(EscribirNovedad.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean insertar() {

        String url = Constants.URL + "claseUAO/insert_Novedad.php";

        //DATOS
        List<NameValuePair> nameValuePairs; // definimos la lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(4); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("titulo", edTitulo.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("descripcion", edDescripcion.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("creador", user.getId()));
        nameValuePairs.add(new BasicNameValuePair("curso", idCurso));
        boolean response = APIHandler.POST(url, nameValuePairs);
        return response;
    }

    class Insertar extends AsyncTask<String, String, String> {
        private Activity context;

        Insertar(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            boolean  seInserto = false;
            seInserto=insertar();
            if (seInserto)
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Novedad insertado", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(EscribirNovedad.this, cursoHome.class);
                        i.putExtra("IdCurso", idCurso);
                        startActivity(i);
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(user != null){
                            Toast.makeText(context, "Esa novedad  ya existe", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, "novedad no insertado", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            return null;
        }
    }

}