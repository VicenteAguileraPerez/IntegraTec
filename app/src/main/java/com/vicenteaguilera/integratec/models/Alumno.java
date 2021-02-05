package com.vicenteaguilera.integratec.models;

import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

public class Alumno
{
    private String numeroControl;
    private String nombre;
    private String carrera;

    public Alumno(String numeroControl, String nombre, String carrera) {
        this.numeroControl = numeroControl;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public void setNumeroControl(String numeroControl) {
        this.numeroControl = numeroControl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
