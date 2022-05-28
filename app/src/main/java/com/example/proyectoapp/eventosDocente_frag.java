package com.example.proyectoapp;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class eventosDocente_frag extends Fragment {
    private TextView listaUsers;
    private ArrayList<eventos> listaEventos;
    private String[][] listaInfo;
    private ArrayList<String> listaNombres;
    private ListView lvAllEvents;
    private String idCurso;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_eventos_studiantes_frag, null);
        //listaUsers = (TextView) view.findViewById(R.id.EventosFragEstu_TVUsers);
        lvAllEvents = (ListView) view.findViewById(R.id.EventosFragEstu_lvUsers);
        listaEventos = new ArrayList<eventos>();
        idCurso = getActivity().getIntent().getStringExtra("IdCurso");
        new eventosDocente_frag.TraerEventos(getActivity()).execute();

        lvAllEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                enviarAAsisEvento(position);
            }
        });
        return view;
    }

    void enviarAAsisEvento(int pos){
        Intent i = new Intent(getActivity(), AsistentesEvento.class);
        i.putExtra("idEvento", listaEventos.get(pos).id);
        startActivity(i);
    }

    private Boolean consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getEventobyCurso.php"; // Ruta
        String userCreadorId="";
        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php
        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        Log.d("key of the message", "--------------------------" + json);

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio

            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            JSONArray jArray = new JSONArray();

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                ArrayList<eventos> listaUsu = new ArrayList<eventos>();
                listaEventos=new ArrayList<eventos>();
                for(int i = 0;i<json_array.length();i++){
                    eventos even = new eventos(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                    String userInfo = even.getTitulo() + " " + even.getDescripcion();

                    listaEventos.add(even);
                    userCreadorId = even.getCreador();

                }
                //  return user;// retornamos la multa
            }

        }

        url = Constants.URL + "claseUAO/get-by-id.php"; // Ruta

        //DATOS
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php
        nameValuePairs.add(new BasicNameValuePair("tabla", "id")); // pasamos el id al servicio php
        json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        Log.d("key of the message", "--------------------------" + json);

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio

            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            JSONArray jArray = new JSONArray();

            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                listaNombres = new ArrayList<String>();


                for(int i = 0;i<json_array.length();i++){
                    usuarios user = new usuarios(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                    String userInfo = user.getNombre() + " " + user.getApellidos();
                    listaNombres.add(userInfo);
                }

                //  return user;// retornamos la multa
            }

        }
        return true;
    }

    class TraerEventos extends AsyncTask<String, String, String> {
        private Activity context;

        TraerEventos(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                Boolean consult = consultar();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (consult)
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
       /*for(int i =0;i<listaEventos.size();i++){
           eventos e = listaEventos.get(i);
           String info = "\b "+e.getTitulo()+"\b0 \n"+
                        listaNombres.get(0)+"-"+e.getFechaHora()+"\n"+
                        e.getDescripcion();
           listaInfo.add(info);
       }
        ArrayAdapter<String> Adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listaInfo);
        lvAllEvents.setAdapter(Adapter);*/

        listaInfo = new String[listaEventos.size()][4];
        for(int i =0;i<listaEventos.size();i++){

            listaInfo[i][0] = listaEventos.get(i).getTitulo();
            listaInfo[i][1] = listaNombres.get(0);
            listaInfo[i][2] = listaEventos.get(i).getFechaCreacion();
            listaInfo[i][3] = listaEventos.get(i).getDescripcion();

        }
        lvAllEvents.setAdapter(new AdaptadorNovedades(getActivity(),listaInfo));
    }
}