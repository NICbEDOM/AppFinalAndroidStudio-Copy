package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class gestion_cursosAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cursos_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gestionar Cursos");
    }
    public void btnCrearCurso(View view){
        Intent CrearCursos = new Intent(this, crearCurso.class);
        startActivity(CrearCursos);
    }

    public void btnConsultarCurso(View view){
        Intent consultarCurso = new Intent(this, consultarCurso.class);
        startActivity(consultarCurso);
    }
}