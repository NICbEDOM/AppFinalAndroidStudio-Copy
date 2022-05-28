package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class gestion_admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gestion");

    }



    public void btnCursos(View view){
        Intent gestionCursos = new Intent(this, gestion_cursosAdmin.class);
        startActivity(gestionCursos);
    }

    public void btDocentes(View view){
        Intent gestionCursos = new Intent(this, Gestion_DocentesAdmin.class);
        startActivity(gestionCursos);
    }

    public void btEstudiantes(View view){
        Intent gestionCursos = new Intent(this, GestionEstudiantesAdmin.class);
        startActivity(gestionCursos);
    }

    public void btNovedades(View view){
        Intent gestionCursos = new Intent(this, AsignarEstudiantes.class);
        startActivity(gestionCursos);
    }


}