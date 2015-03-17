package com.example.udatabox.udatabox_mobileapp;

import java.io.Serializable;

/**
 * Created by Carlos on 22-02-2015.
 */
public class Procesos implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String fecha;
    private String numeroExp;
    private int estado;

    public Procesos(String nombre, String fecha, String numeroExp) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.numeroExp = numeroExp;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumeroExp() {
        return numeroExp;
    }

    public void setNumeroExp(String numeroExp) {
        this.numeroExp = numeroExp;
    }
}
