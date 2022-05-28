package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsignarEstudiantes extends AppCompatActivity {

    private Spinner spCurso,spEstudiante;
    private Button btInsert;
    private ArrayList<String> listaInfoUser;
    private ArrayList<String> listaIDUser;
    private ArrayList<String> listaInfoCursos;
    private ArrayList<String> listaIDcursos;
    private String indice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_estudiantes);
        spCurso = (Spinner) findViewById(R.id.AsignarEstudiante_SpCurso);
        spEstudiante = (Spinner) findViewById(R.id.AsignarEstudiante_SpEstu);
        btInsert = (Button) findViewById(R.id.AsignarEstudiante_btAsignar);
        indice ="0";
        new AsignarEstudiantes.TraerCursos(this).execute();

        spCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 indice =listaIDcursos.get(spCurso.getSelectedItemPosition());
                 traerUs();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        traerUs();

        btInsert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    new AsignarEstudiantes.Insertar(AsignarEstudiantes.this).execute();
                }
        });
    }

    public void traerUs(){
        new AsignarEstudiantes.TraerUsuarios(this).execute();
    }

    private Boolean TraerCursos() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getNombresCursos.php"; // Ruta

        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("Evento", "0")); // pasamos el id al servicio php
        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("curso");// accedemos al objeto json llamado multas

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro

                listaInfoCursos=new ArrayList<String>();
                listaIDcursos=new ArrayList<String>();
                for(int i = 0;i<json_array.length();i++){
                    String id = json_array.getJSONObject(i).getString("id_curs");
                    String nombre = json_array.getJSONObject(i).getString("nombre_curso");// instanciamos la clase multa para obtener los datos json
                    String Info= nombre;
                    listaInfoCursos.add(Info);
                    listaIDcursos.add(id);
                }

                return true;
            }
            return null;
        }
        return null;
    }

    private boolean insertarParticipacion() {

        String url = Constants.URL + "claseUAO/insertParticipacion.php";
        String indiceU =listaIDUser.get(spEstudiante.getSelectedItemPosition());
        Log.d("key of the message", "************************************* " + indiceU +" "+indice);
        //DATOS
        List<NameValuePair> nameValuePairs; // definimos la lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("user", indiceU.trim()));
        nameValuePairs.add(new BasicNameValuePair("curso", indice.trim()));

        boolean response = APIHandler.POST(url, nameValuePairs);
        return response;
    }
    private Boolean traerEstudiantes() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getUsersSinCurso.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", indice)); // pasamos el id al servicio php
        nameValuePairs.add(new BasicNameValuePair("tipo", "Estudiante")); // pasamos el id al servicio php
        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("curso");// accedemos al objeto json llamado multas

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro

                listaInfoUser=new ArrayList<String>();
                listaIDUser=new ArrayList<String>();
                for(int i = 0;i<json_array.length();i++){
                    String id = json_array.getJSONObject(i).getString("id");
                    String nombre = json_array.getJSONObject(i).getString("nombre");
                    String apellido = json_array.getJSONObject(i).getString("apellidos");// instanciamos la clase multa para obtener los datos json
                    String Info= nombre+" "+apellido;
                    listaInfoUser.add(Info);
                    listaIDUser.add(id);
                }
                return true;
                //  return user;// retornamos la multa
            }
            return null;
        }
        return null;
    }

    class TraerCursos extends AsyncTask<String, String, String> {
        private Activity context;

        TraerCursos(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
               Boolean func = TraerCursos();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (func)
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


    class TraerUsuarios extends AsyncTask<String, String, String> {
        private Activity context;

        TraerUsuarios(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                Boolean inf = traerEstudiantes();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (inf)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {

                            llenarListViewUser();
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

    class Insertar extends AsyncTask<String, String, String> {
        private Activity context;

        Insertar(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            boolean  seInserto = insertarParticipacion();

            if (seInserto)
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "usuario insertado", Toast.LENGTH_LONG).show();
                        traerUs();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

            return null;
        }
    }



    public void llenarListView() {
        ArrayAdapter<String> Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaInfoCursos);
        spCurso.setAdapter(Adapter);
    }

    public void llenarListViewUser() {
        ArrayAdapter<String> Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaInfoUser);
        spEstudiante.setAdapter(Adapter);
    }
}