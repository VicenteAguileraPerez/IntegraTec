package com.vicenteaguilera.integratec.models;

import android.os.Parcelable;

import com.vicenteaguilera.integratec.helpers.utility.helpers.DateHelper;

import java.io.Serializable;

public class Mensaje implements Serializable
{
    String id;
    String fecha;
    String from;//de
    String mensaje;// mensaje

    public Mensaje(String id, String fecha, String from, String mensaje) {
        this.id = id;
        this.fecha = fecha;
        this.from = from;
        this.mensaje = mensaje;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getFrom() {
        return from;
    }

    public String getMensaje() {
        return mensaje;
    }
}
