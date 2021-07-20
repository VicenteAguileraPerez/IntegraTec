package com.vicenteaguilera.integratec.helpers.utility.interfaces;

import com.vicenteaguilera.integratec.models.Asesoria;
import com.vicenteaguilera.integratec.models.Mensaje;

import java.util.List;

public interface ListaAsesorias
{
    public void getAsesorias(List<Asesoria> asesoriaList);
    public void getMensajes(List<Mensaje> mensajeList);
}
