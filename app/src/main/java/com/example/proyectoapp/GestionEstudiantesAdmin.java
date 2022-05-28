package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;


public class GestionEstudiantesAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_estudiantes_admin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Estudiantes");

    }

    public void irACrearEstudiante(View view){
        Intent i = new Intent(this, crearDocenteAdmin.class);
        startActivity(i);
    }

    public void irAConsultarEstudiantes(View view){
        Intent i = new Intent(this, ConsultarUsuario.class);
        i.putExtra("TipoUsuario", "Estudiante");
        startActivity(i);
    }
}