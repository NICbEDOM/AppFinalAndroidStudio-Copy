package com.example.proyectoapp;

import org.json.JSONException;
import org.json.JSONObject;

public class novedades {

    String id, titulo, descripcion, Creador, curso, fechaCreacion;
    public novedades(JSONObject objetoJSON) throws JSONException {
        this.id = objetoJSON.getString("id_novedad");
        this.titulo = objetoJSON.getString("titulos_novedad");
        this.descripcion = objetoJSON.getString("descripciones_novedad");
        this.Creador = objetoJSON.getString("creador_novedad");
        this.curso = objetoJSON.getString("curso_novedad");
        this.fechaCreacion = objetoJSON.getString("creacion_novedad");


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreador() {
        return Creador;
    }

    public void setCreador(String creador) {
        Creador = creador;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


}
