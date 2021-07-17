package com.vicenteaguilera.integratec.helpers.utility.helpers;

public enum NombresDirectorios
{
    NOMBRE_DIRECTORIO("PDFsIntegraTec"),NOMBRE_DOCUMENTO("PDF_Asesorias"),
    NOMBRE_DOCUMENTO2("PDF_Asesorados"),ETIQUETA_ERROR("ERROR");

    public String texto;
    NombresDirectorios(String texto)
    {
        this.texto=texto;
    }
}
