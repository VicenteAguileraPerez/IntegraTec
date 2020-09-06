package com.vicenteaguilera.integratec.models;

public class Alumno {
    private int id;
    private String nombre;
    private int nControl;
    private String carrera;
    private String semestre;
    private String tema;
    private String fecha;
    private String materia;

    public Alumno(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getnControl() {
        return nControl;
    }

    public void setnControl(int nControl) {
        this.nControl = nControl;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
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
        return  id +" "+ nombre+" "+ nControl+" "+ carrera+" "+semestre+" "+tema+" "+fecha+" "+materia;
    }
}
