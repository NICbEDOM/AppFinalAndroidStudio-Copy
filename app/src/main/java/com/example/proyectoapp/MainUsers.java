package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainUsers extends AppCompatActivity {

    private ListView lvCursos;
    private String idUser;
    String [][] datosCurso;
    String[][] listaInfo;
    private ArrayList<String> idsCursos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_users);
        lvCursos=(ListView)findViewById(R.id.fragEstudiantes_lista);
        idUser=getIntent().getStringExtra("idUserMain");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cursos");

        new MainUsers.TraerCursos(MainUsers.this).execute();

        lvCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                enviarAHomeUsers(position);
            }
        });

    }

    void enviarAHomeUsers(int pos){
        Intent i = new Intent(this, cursoHome.class);
        i.putExtra("IdCurso", idsCursos.get(pos));
        GlobalInfo.setCursoActual(idsCursos.get(pos));
        startActivity(i);
    }

    private String[][] consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getCursosUser.php"; // Ruta
        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(3);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idUser)); // pasamos el id al servicio php
        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("curso");// accedemos al objeto json llamado multas
            JSONArray jArray = new JSONArray();
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                Log.d("key of the message", "------------------------------ " + json_array.length());
                datosCurso = new String[json_array.length()][4];
                listaInfo = new String[json_array.length()][4];
                idsCursos=new ArrayList<String>();
                for(int i = 0;i<json_array.length();i++){
                    String idC =json_array.getJSONObject(i).getString("id_curs");
                    String nombre =json_array.getJSONObject(i).getString("nombre_curso");
                    String dia =json_array.getJSONObject(i).getString("dia");
                    String horaIni =json_array.getJSONObject(i).getString("horaInico");
                    String horaFin =json_array.getJSONObject(i).getString("horaFin");

                    listaInfo[i][0] = nombre;
                    listaInfo[i][1] = dia;
                    listaInfo[i][2] = horaIni;
                    listaInfo[i][3] = horaFin;
                    idsCursos.add(idC);

                }
                return listaInfo;
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
                datosCurso = consultar();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (datosCurso != null)
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
        lvCursos.setAdapter(new adaptadorCurso(this,listaInfo));
    }

}