package com.example.proyectoapp;

import org.json.JSONException;
import org.json.JSONObject;


public class usuarios {

    String id, cedulas, nombre, apellidos, facultad, programa, usuario, contraseña,rol;
//id,cedulas,nombre,apellidos,facultad,programa,usuario,contraseña,rol
    public usuarios(JSONObject objetoJSON) throws JSONException {
        this.id = objetoJSON.getString("id");
        this.cedulas = objetoJSON.getString("cedulas");
        this.nombre = objetoJSON.getString("nombre");
        this.apellidos = objetoJSON.getString("apellidos");
        this.facultad = objetoJSON.getString("facultad");
        this.programa = objetoJSON.getString("programa");
        this.usuario = objetoJSON.getString("usuario");
        this.contraseña = objetoJSON.getString("contrasena");
        this.rol = objetoJSON.getString("rol");
    }

    public usuarios() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCedulas() {
        return cedulas;
    }

    public void setCedulas(String cedulas) {
        this.cedulas = cedulas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
