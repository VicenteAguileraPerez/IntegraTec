package com.vicenteaguilera.integratec.models;

public class Asesoria
{
    private String idAsesor, nombre, materia, fecha, h_inicio, h_final;

    public Asesoria(){}

    public Asesoria(String idAsesor, String nombre, String materia, String fecha, String h_inicio, String h_final) {
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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
