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

public class consultarCurso extends AppCompatActivity {

    private ListView lvLista;
    private ArrayList<cursos> listaCursos;
    private ArrayList<String> listaInfo;
    private ArrayList<String> idsCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_curso);
        lvLista = (ListView)findViewById(R.id.ConsultarCurso_lista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Consultar Curso");
        new consultarCurso.TraerCursos(this).execute();

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                enviarAEditar(position);
            }
        });
    }

    void enviarAEditar(int pos){
        Intent i = new Intent(this, EditatCurso.class);
        i.putExtra("CursoAEditar", idsCursos.get(pos));
        startActivity(i);
    }

    private ArrayList<String> consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getAllCurso.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", "1")); // pasamos el id al servicio php
        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "444444444444444444444444 " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas


            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                ArrayList<usuarios> listaUsu = new ArrayList<usuarios>();
                listaInfo=new ArrayList<String>();
                listaCursos=new ArrayList<cursos>();
                idsCursos = new ArrayList<String>();
                for(int i = 0;i<json_array.length();i++){
                    cursos curs = new cursos(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                    String nombre = curs.getNombre();
                    String id =curs.getId();
                    listaInfo.add(nombre);
                    idsCursos.add(id);
                }

                return listaInfo;
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
                listaInfo = consultar();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (listaInfo != null)
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
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaInfo);
        lvLista.setAdapter(Adapter);
    }

}