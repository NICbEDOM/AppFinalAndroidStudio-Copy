package com.example.proyectoapp;

public class GlobalInfo {
    public static usuarios userActual = new usuarios();
    public static String cursoActual = "0";

    public static String getCursoActual() {
        return cursoActual;
    }

    public static void setCursoActual(String cursoActual) {
        GlobalInfo.cursoActual = cursoActual;
    }

    public static usuarios getUserActual() {
        return userActual;
    }

    public static void setUserActual(usuarios userActual) {
        GlobalInfo.userActual = userActual;
    }
}
