package com.vicenteaguilera.integratec.models;

public class RealtimeAsesoria
{
    //string url imagen del asesor
    // nombre del asesor
    //hora inicio
    //hora fin
    private String idAsesor;
    private String lugar;
    private String URL;
    private String materia;
    private String hora_inicio;
    private String hora_fin;
    private String informacion;
    private String fecha;
    private String nombre;
    private String image_asesor;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImage_asesor() {
        return image_asesor;
    }

    public void setImage_asesor(String image_asesor) {
        this.image_asesor = image_asesor;
    }

    public RealtimeAsesoria(String idAsesor, String lugar, String URL, String materia, String hora_inicio, String hora_fin, String informacion, String fecha, String nombre, String image_asesor) {
        this.idAsesor = idAsesor;
        this.lugar = lugar;
        this.URL = URL;
        this.materia = materia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.informacion = informacion;
        this.fecha = fecha;
        this.nombre = nombre;
        this.image_asesor = image_asesor;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}