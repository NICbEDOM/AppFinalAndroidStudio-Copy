package com.example.proyectoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfoEvento extends AppCompatActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private TextView titulo,fecha,hora,lugar;
    private String latitud,longitud, idEvento;
    private Button btAsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_evento);
        titulo = (TextView)findViewById(R.id.infoEvento_tvNombre);
        fecha = (TextView)findViewById(R.id.infoEvento_tvFecha);
        lugar = (TextView)findViewById(R.id.infoEvento_tvLugar);
        hora = (TextView)findViewById(R.id.infoEvento_tvHora);
        btAsis = (Button)findViewById(R.id.infoEvento_confirmarAsis);

        titulo.setText(this.getIntent().getStringExtra("ENombre"));
        fecha.setText(this.getIntent().getStringExtra("EFecha"));
        lugar.setText(this.getIntent().getStringExtra("ELugar"));
        hora.setText(this.getIntent().getStringExtra("EHora"));
        idEvento = this.getIntent().getStringExtra(("idEvento"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String lat=this.getIntent().getStringExtra("ELatitud");
        String lon =this.getIntent().getStringExtra("ELongitud");
        Double longitud = Double.parseDouble(lon);
        Double latitud = Double.parseDouble(lat);
        // Add a marker in Sydney and move the camera
        LatLng san_fernando = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(san_fernando).title("Sede san Fernando").snippet("Esta es la sede número 2 de la UAO"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(san_fernando));


    }

    public void confirmarAsistencia(View view){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setMessage("¿Asistiras al evento?");
        alertBox.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new InfoEvento.Insertar(InfoEvento.this).execute();
            }
        });

        alertBox.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(InfoEvento.this,cursoHome.class);
                startActivity(in);
            }
        });
        alertBox.show();
    }

    void mensaje(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    private boolean insertar() {

        String url = Constants.URL + "claseUAO/insert_Asistencia.php";

        //DATOS
        List<NameValuePair> nameValuePairs; // definimos la lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(3); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("evento", idEvento.trim()));
        nameValuePairs.add(new BasicNameValuePair("curso", GlobalInfo.getUserActual().getId()));
        nameValuePairs.add(new BasicNameValuePair("usuario", GlobalInfo.getCursoActual()));

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
            boolean  seInserto = insertar();
            if (seInserto)
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Asistencia confirmada", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(InfoEvento.this,cursoHome.class);
                        startActivity(in);
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

}