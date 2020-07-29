package com.vicenteaguilera.integratec.models;

import com.google.cloud.Timestamp;

;

public class RealtimeAsesoria
{
    private String lugar;
    private String URL;
    private String materia;
    private Timestamp hora_inicio;
    private Timestamp hora_fin;
    private String informacion;

    public RealtimeAsesoria(String lugar, String URL, String materia, Timestamp hora_inicio, Timestamp hora_fin, String informacion) {
        this.lugar = lugar;
        this.URL = URL;
        this.materia = materia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.informacion = informacion;
    }
}
