package com.example.proyectoapp;

import org.json.JSONException;
import org.json.JSONObject;

public class cursos {

    String id, nombre, descripcion, duracion, dia, horaInico, horaFin, primParcial, segParcial, terParcial, novedades;
    public cursos(JSONObject objetoJSON) throws JSONException {
        this.id = objetoJSON.getString("id_curs");
        this.nombre = objetoJSON.getString("nombre_curso");
        this.descripcion = objetoJSON.getString("descripcion");
        this.duracion = objetoJSON.getString("duracion");
        this.dia = objetoJSON.getString("dia");
        this.horaInico = objetoJSON.getString("horaInico");
        this.horaFin = objetoJSON.getString("horaFin");
        this.primParcial = objetoJSON.getString("primParcial");
        this.segParcial = objetoJSON.getString("segParcial");
        this.terParcial = objetoJSON.getString("terParcial");
        this.novedades = objetoJSON.getString("novedades");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInico() {
        return horaInico;
    }

    public void setHoraInico(String horaInico) {
        this.horaInico = horaInico;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getPrimParcial() {
        return primParcial;
    }

    public void setPrimParcial(String primParcial) {
        this.primParcial = primParcial;
    }

    public String getSegParcial() {
        return segParcial;
    }

    public void setSegParcial(String segParcial) {
        this.segParcial = segParcial;
    }

    public String getTerParcial() {
        return terParcial;
    }

    public void setTerParcial(String terParcial) {
        this.terParcial = terParcial;
    }

    public String getNovedades() {
        return novedades;
    }

    public void setNovedades(String novedades) {
        this.novedades = novedades;
    }
}
