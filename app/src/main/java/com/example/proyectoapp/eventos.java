package com.example.proyectoapp;

import org.json.JSONException;
import org.json.JSONObject;


public class eventos {

    String id, titulo, descripcion, fechaHora,lugar, Creador, curso, fechaCreacion;

    public eventos(JSONObject objetoJSON) throws JSONException {
        this.id = objetoJSON.getString("id_eventos");
        this.titulo = objetoJSON.getString("titulos_eventos");
        this.descripcion = objetoJSON.getString("descripciones_eventos");
        this.fechaHora = objetoJSON.getString("fechaHora_eventos");
        this.lugar = objetoJSON.getString("lugar_eventos");
        this.Creador = objetoJSON.getString("fk_usuarios");
        this.curso = objetoJSON.getString("fk_cursos");
        this.fechaCreacion = objetoJSON.getString("inserciones");


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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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
