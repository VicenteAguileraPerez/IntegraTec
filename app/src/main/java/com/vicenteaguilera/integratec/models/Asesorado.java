package com.vicenteaguilera.integratec.models;

public class Asesorado {
    private String nombre;
    private String nControl;
    private String carrera;
    private String tema;
    private String fecha;
    private String materia;
    private String idAsesor;

    public Asesorado(){}

    public Asesorado(String nombre, String nControl, String carrera, String tema, String fecha, String materia, String idAsesor) {
        this.nombre = nombre;
        this.nControl = nControl;
        this.carrera = carrera;
        this.tema = tema;
        this.fecha = fecha;
        this.materia = materia;
        this.idAsesor = idAsesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getnControl() {
        return nControl;
    }

    public void setnControl(String nControl) {
        this.nControl = nControl;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getSemestre() {
        return idAsesor;
    }

    public void setSemestre(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getData()
    {
        return   nombre+" "+ nControl;
    }
}
