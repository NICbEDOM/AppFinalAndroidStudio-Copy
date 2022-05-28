package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class Gestion_DocentesAdmin extends AppCompatActivity {

    private Button btCualquierCOsa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_docentes_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Docentes");


    }

    public void irACrearDocente(View view){
        Intent i = new Intent(this, crearDocenteAdmin.class);
        startActivity(i);
    }

    public void irAConsultarUser(View view){
        Intent i = new Intent(this, ConsultarUsuario.class);
        i.putExtra("TipoUsuario", "Docente");
        startActivity(i);
    }


}