package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class crearCurso extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_curso);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Crear Curso");
        spDocente =(Spinner)findViewById(R.id.EditarCurso_spDocente);
        //NOMBRE CURSO
        nombreCurso = (EditText) findViewById(R.id.EditarCurso_EtNCurso);
        //DESCRIPCION CURSO
        descripcionCurso = (EditText) findViewById(R.id.EtDescripcionCurso);
        //Duracion Curso
        DuracionCurso = findViewById(R.id.EditarCurso_EtDuracionCurso);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(crearCurso.this, new TimePickerDialog.OnTimeSetListener() {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(crearCurso.this, new TimePickerDialog.OnTimeSetListener() {
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
                new DatePickerDialog(crearCurso.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(crearCurso.this, date2, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(crearCurso.this, date3, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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

                    new Insertar(crearCurso.this).execute();
                } else {

                    Toast.makeText(crearCurso.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new TraerDocente(crearCurso.this).execute();
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

    private boolean insertar() {
        String url = Constants.URL + "claseUAO/insert_curso.php";
        //DATOS
        List<NameValuePair> nameValuePairs; // definimos la lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(10); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("nombre", nombreCurso.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("descripcion", descripcionCurso.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("duracion", DuracionCurso.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("dia", resultDia.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("horaInico", horaInicio.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("horaFin", horaInicio.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("primParcial", prParcial.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("segParcial", seParcial.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("terParcial", teParcial.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("novedades", "1"));

        boolean response = APIHandler.POST(url, nameValuePairs);


        return response;
    }

    class Insertar extends AsyncTask<String, String, String> {
        private Activity context;

        Insertar(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            if (insertar())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Curso insertado", Toast.LENGTH_LONG).show();
                        nombreCurso.setText("");
                        descripcionCurso.setText("");
                        DuracionCurso.setText("");
                        resultDia.setText("");
                        horaInicio.setText("");
                        horaFin.setText("");
                        prParcial.setText("");
                        seParcial.setText("");
                        teParcial.setText("");
                        traerCur();
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Curso no insertado", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }
    void traerCur(){
        new TraerCurso(crearCurso.this).execute();
    }

    private Boolean TraerCursos() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getUltimoCurso.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(1);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", "nombre"));


        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "The message " + json);
        if (json != null) { // si la respuesta no es vacia
            JSONObject object = new JSONObject(json); // creamos el objeto json que recorrera el servicio
            JSONArray json_array = object.optJSONArray("user");// accedemos al objeto json llamado multas

            ArrayList<cursos> listaCur = new ArrayList<cursos>();

            for(int i = 0;i<json_array.length();i++){
                cursos cur = new cursos(json_array.getJSONObject(i));// instanciamos la clase multa para obtener los datos json
                infoCurso  = json_array.getJSONObject(i).getString("id_curs");

            }

        }
        url = Constants.URL + "claseUAO/insertParticipacion.php";
        String indiceD =idDocente.get(spDocente.getSelectedItemPosition());
        nameValuePairs = new ArrayList<NameValuePair>(2); // tamaño del array
        nameValuePairs.add(new BasicNameValuePair("user", indiceD.trim()));
        nameValuePairs.add(new BasicNameValuePair("curso", infoCurso));
        boolean response = APIHandler.POST(url, nameValuePairs);
        return true;
    }


    class TraerCurso extends AsyncTask<String, String, String> {
        private Activity context;

        TraerCurso(Activity context) {
            this.context = context;
        }

        protected String doInBackground(String... params) {
            try {
                Boolean b = TraerCursos();
                //  Toast.makeText(MainActivity.this, "Hay informacion por llenar", Toast.LENGTH_SHORT).show();
                if (b)
                    context.runOnUiThread(new Runnable() {


                        @Override
                        public void run() {


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

    private ArrayList<usuarios> traerDocentes() throws JSONException, IOException {

        String url = Constants.URL + "claseUAO/getAllDocente.php"; // Ruta

        //DATOS
        List<NameValuePair> nameValuePairs; // lista de datos
        nameValuePairs = new ArrayList<NameValuePair>(2);//definimos array
        nameValuePairs.add(new BasicNameValuePair("id", "2"));
        nameValuePairs.add(new BasicNameValuePair("tabla", "q"));

        String json = APIHandler.POSTRESPONSE(url, nameValuePairs); // creamos var json que se le asocia la respuesta del webservice
        Log.d("key of the message", "--------------------------" + json);
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
    }
}


