package com.example.udatabox.udatabox_mobileapp;

/**
 * Created by Carlos on 03-03-2015.
 */
public class ListaFamiliares_model {
    private String relacion;
    private String nombre;
    private String edad;

    public ListaFamiliares_model(String relacion, String nombre, String edad) {
        this.relacion = relacion;
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
