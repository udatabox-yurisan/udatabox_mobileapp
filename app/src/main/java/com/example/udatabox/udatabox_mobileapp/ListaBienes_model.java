package com.example.udatabox.udatabox_mobileapp;

/**
 * Created by Carlos on 16-03-2015.
 */
public class ListaBienes_model {
    String biene, clase;

    public ListaBienes_model(String biene, String clase) {
        this.biene = biene;
        this.clase = clase;
    }

    public String getBiene() {
        return biene;
    }

    public void setBiene(String biene) {
        this.biene = biene;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
}
