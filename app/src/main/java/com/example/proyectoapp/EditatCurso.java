package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditatCurso extends AppCompatActivity {
    //Nombre del curso
    EditText nombreCurso;
    //Descripcion del curso
    EditText descripcionCurso;
    //Duracion
    EditText DuracionCurso;
    //Horario
    TextView resultDia;
    CheckBox lunes, martes, miercoles, jueves, viernes;
    EditText horaInicio, horaFin;
    //PARCIALES
    //private int mYear, mMonth, mDay;
    EditText prParcial, seParcial, teParcial;
    Calendar calendar;
    //NOVEADES

    String estado;
    List<String> idDocente;
    List<usuarios> listaUsuarios ;
    List<String> listaInfoDocente;
    String infoCurso;

    //INSERTAR A BASE DE DATOS
    Button btnInsertar;
    Spinner spDocente;
    String nombreDoc;
    String idDoc;

    String idCurso;
    cursos cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editat_curso);
        setContentView(R.layout.activity_crear_curso);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editar Curso");
        spDocente =(Spinner)findViewById(R.id.EditarCurso_spDocente);
        //NOMBRE CURSO
        nombreCurso = (EditText) findViewById(R.id.EditarCurso_EtNCurso);
        //DESCRIPCION CURSO
        descripcionCurso = (EditText) findViewById(R.id.EtDescripcionCurso);
        //Duracion Curso
        DuracionCurso = findViewById(R.id.EditarCurso_EtDuracionCurso);
        idCurso = getIntent().getStringExtra("CursoAEditar");
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().
                setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        DuracionCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        DuracionCurso.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });

        //HORARIO
        //seleccion de dia
        resultDia = findViewById(R.id.TvResultDia);
        lunes = (CheckBox) findViewById(R.id.EditarCurso_CBLunes);
        martes = (CheckBox) findViewById(R.id.EditarCurso_CBmartes);
        miercoles = (CheckBox) findViewById(R.id.EditarCurso_CBMiercoles);
        jueves = (CheckBox) findViewById(R.id.EditarCurso_CBJueves);
        viernes = (CheckBox) findViewById(R.id.EditarCurso_CBViernes);
        updateRadioGroup(lunes);
        //seleccion de hora
        horaInicio = findViewById(R.id.EditarCurso_EtHoraInicio);
        horaFin = findViewById(R.id.EditarCurso_EtHoraFin);
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditatCurso.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        horaInicio.setText(hourOfDay + ":" + minutes);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        horaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditatCurso.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        horaFin.setText(hourOfDay + ":" + minutes);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        //PARCIALES
        prParcial = findViewById(R.id.EditarCurso_EtParcial1);
        seParcial = findViewById(R.id.EditarCurso_EtParcial2);
        teParcial = findViewById(R.id.EtParcial3);
        Calendar calendar = Calendar.getInstance();
        //1
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateCalendar();
            }

            private void updateCalendar() {
                String format = "MM/dd/yy";
                SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
                prParcial.setText(format1.format(calendar.getTime()));
            }
        };
        prParcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditatCurso.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //2
        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateCalendar();
            }

            private void updateCalendar() {
                String format = "MM/dd/yy";
                SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
                seParcial.setText(format1.format(calendar.getTime()));
            }
        };
        seParcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditatCurso.this, date2, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //3
        DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateCalendar();
            }

            private void updateCalendar() {
                String format = "MM/dd/yy";
                SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);
                teParcial.setText(format1.format(calendar.getTime()));
            }
        };
        teParcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditatCurso.this, date3, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Novedades


        //INSERTAR
        btnInsertar = findViewById(R.id.EditarCurso_btnAnadirCurso);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!nombreCurso.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!DuracionCurso.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!resultDia.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!horaInicio.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!horaFin.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!prParcial.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!seParcial.getText().toString().trim().equalsIgnoreCase("")) ||
                        (!teParcial.getText().toString().trim().equalsIgnoreCase(""))) {

                    new EditatCurso.Modificar(EditatCurso.this).execute();
                } else {

                    Toast.makeText(EditatCurso.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        new EditatCurso.TraerDocente(EditatCurso.this).execute();

    }

    public void onCheckboxClicked(View view) {
        if (lunes.isChecked()) {
            resultDia.setText(lunes.getText());
            updateRadioGroup(lunes);
            martes.setEnabled(false);
            miercoles.setEnabled(false);
            jueves.setEnabled(false);
            viernes.setEnabled(false);
        } else if (martes.isChecked()) {
            resultDia.setText(martes.getText());
            updateRadioGroup(martes);
            lunes.setEnabled(false);
            miercoles.setEnabled(false);
            jueves.setEnabled(false);
            viernes.setEnabled(false);
        } else if (miercoles.isChecked()) {
            resultDia.setText(miercoles.getText());
            updateRadioGroup(miercoles);
            martes.setEnabled(false);
            lunes.setEnabled(false);
            jueves.setEnabled(false);
            viernes.setEnabled(false);
        } else if (jueves.isChecked()) {
            resultDia.setText(jueves.getText());
            updateRadioGroup(jueves);
            martes.setEnabled(false);
            miercoles.setEnabled(false);
            lunes.setEnabled(false);
            viernes.setEnabled(false);
        } else if (viernes.isChecked()) {
            resultDia.setText(viernes.getText());
            updateRadioGroup(viernes);
            martes.setEnabled(false);
            miercoles.setEnabled(false);
            jueves.setEnabled(false);
            lunes.setEnabled(false);
        } else {
            resultDia.setText("Escoga un dia");
            lunes.setEnabled(true);
            martes.setEnabled(true);
            miercoles.setEnabled(true);
            jueves.setEnabled(true);
            viernes.setEnabled(true);
            lunes.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
            martes.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
            miercoles.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
            jueves.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
            viernes.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
        }
    }

    private void updateRadioGroup(CheckBox selected) {

        lunes.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
        martes.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
        miercoles.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
        jueves.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
        viernes.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_group_background));
        //sabado.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.radio_group_background));

        selected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_on));
    }

    private boolean editar() {

        String url = Constants.URL + "claseUAO/updateCurso.php";

        //DATOS
        List<NameValuePair> nameValuePairs; // definimos la lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(11); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("id_curs", idCurso));
        nameValuePairs.add(new BasicNameValuePair("nombre_curso", nombreCurso.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("descripcion", descripcionCurso.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("duracion", DuracionCurso.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("dia", resultDia.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("horaInico", horaInicio.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("horaFin", horaFin.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("primParcial", prParcial.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("segParcial", seParcial.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("terParcial", teParcial.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("novedades", "1"));
        boolean response = APIHandler.POST(url, nameValuePairs);

        int idActual=Integer.parseInt(idDoc);




        url = Constants.URL + "claseUAO/updateParticipacion.php";
        nameValuePairs = new ArrayList<NameValuePair>(3); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso));
        nameValuePairs.add(new BasicNameValuePair("nuevo", idDocente.get(spDocente.getSelectedItemPosition())));
        nameValuePairs.add(new BasicNameValuePair("actual", idActual+""));

        response = APIHandler.POST(url, nameValuePairs);

        url = Constants.URL + "claseUAO/insertParticipacion.php";
        String indiceD =idDocente.get(spDocente.getSelectedItemPosition());
        nameValuePairs = new ArrayList<NameValuePair>(2); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("user",  idDocente.get(spDocente.getSelectedItemPosition())));
        nameValuePairs.add(new BasicNameValuePair("curso", idCurso));
        response = APIHandler.POST(url, nameValuePairs);
        return response;
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

    private void regresar(){
        Intent i = new Intent(this, consultarCurso.class);
        startActivity(i);
    }

    private Boolean consultar() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/get-curso-by-id.php"; // Ruta
        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                cur = new cursos(json_array.getJSONObject(0));// instanciamos la clase multa para obtener los datos json
            }
        }

        url = Constants.URL + "claseUAO/getDocenteCurso.php"; // Ruta
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", idCurso)); // pasamos el id al servicio php
        nameValuePairs.add(new BasicNameValuePair("tipo", "Docente")); // pasamos el id al servicio php

        json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("curso");// accedemos al objeto json llamado multas
            if (json_array.length() > 0) { // si lo encontrado tiene al menos un registro
                String nombre = json_array.getJSONObject(0).getString("nombre");
                String apellido = json_array.getJSONObject(0).getString("apellidos");// instanciamos la clase multa para obtener los datos json
                nombreDoc= nombre+" "+apellido;
                idDoc = json_array.getJSONObject(0).getString("id");
            }

        }

        return true;
    }

    class trarInfoCurso extends AsyncTask<String, String, String> {
        private Activity context;

        trarInfoCurso(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {

                final boolean f= consultar();

                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (cur != null)
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            actualizarPagina();

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

    void actualizarPagina(){
         nombreCurso.setText(cur.getNombre());
         descripcionCurso.setText(cur.getDescripcion());
         DuracionCurso.setText(cur.getDuracion());
         resultDia.setText(cur.getDia());
         horaInicio.setText(cur.getHoraInico());
         horaFin.setText(cur.getHoraFin());
         prParcial.setText(cur.getPrimParcial());
         seParcial.setText(cur.getSegParcial());
         teParcial.setText(cur.getTerParcial());

         for(int i =0;i<idDocente.size();i++){

             if(idDocente.get(i).equals(idDoc)){
                 Log.d("key of the message", "--------------------------" + idDocente.get(i)+"."+idDoc);
                 spDocente.setSelection(i);
             }
         }
    }

    private ArrayList<usuarios> traerDocentes() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getAllDocente.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", "2"));
        nameValuePairs.add(new BasicNameValuePair("tabla", "q"));

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice

        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas

            ArrayList<usuarios> listaUsu = new ArrayList<usuarios>();
            listaInfoDocente=new ArrayList<String>();
            idDocente=new ArrayList<String>();
            listaUsuarios=new ArrayList<usuarios>();

            for(int i = 0;i<json_array.length();i++){
                usuarios user = new usuarios(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                String userInfo = user.getNombre() + " " + user.getApellidos();
                listaInfoDocente.add(userInfo);
                idDocente.add(user.getId());
                listaUsu.add(user);
            }
            return listaUsu;
        }
        return null;
    }


    class TraerDocente extends AsyncTask<String, String, String> {
        private Activity context;

        TraerDocente(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                listaUsuarios = traerDocentes();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (listaUsuarios != null)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            llenarSpinner();
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

    void  llenarSpinner(){
        ArrayAdapter<String> Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaInfoDocente);
        spDocente.setAdapter(Adapter);
        new EditatCurso.trarInfoCurso(EditatCurso.this).execute();
    }
    }