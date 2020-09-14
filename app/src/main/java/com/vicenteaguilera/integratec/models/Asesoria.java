package com.vicenteaguilera.integratec.models;

import java.util.Date;

public class Asesoria
{
    private String idAsesor, nombre, materia, h_inicio, h_final;
    private Date fecha;

    public Asesoria(){}

    public Asesoria(String idAsesor, String nombre, String materia, Date fecha, String h_inicio, String h_final) {
        this.idAsesor = idAsesor;
        this.nombre = nombre;
        this.materia = materia;
        this.fecha = fecha;
        this.h_inicio = h_inicio;
        this.h_final = h_final;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getH_inicio() {
        return h_inicio;
    }

    public void setH_inicio(String h_inicio) {
        this.h_inicio = h_inicio;
    }

    public String getH_final() {
        return h_final;
    }

    public void setH_final(String h_final) {
        this.h_final = h_final;
    }
}
