package com.vicenteaguilera.integratec.models;

public class RealtimeAsesoria
{
    //string url imagen del asesor
    // nombre del asesor
    //hora inicio
    //hora fin
    private String lugar;
    private String URL;
    private String materia;
    private String url_imagen;
    private String nombre;
    private String hora_inicio;
    private String hora_fin;
    private String informacion;

   /* public RealtimeAsesoria(String lugar, String URL, String materia, Timestamp hora_inicio, Timestamp hora_fin, String informacion) {
        this.lugar = lugar;
        this.URL = URL;
        this.materia = materia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.informacion = informacion;
    }*/

    public RealtimeAsesoria(String lugar, String URL, String materia, String url_imagen, String nombre, String hora_inicio, String hora_fin, String informacion) {
        this.lugar = lugar;
        this.URL = URL;
        this.materia = materia;
        this.url_imagen = url_imagen;
        this.nombre = nombre;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.informacion = informacion;
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

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}
