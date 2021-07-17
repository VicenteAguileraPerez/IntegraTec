package com.vicenteaguilera.integratec.helpers.utility.interfaces;

import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.util.List;
import java.util.Map;

public interface ListaAsesores
{
    void getAsesoresRealtime(List<RealtimeAsesoria> realtimeAsesoriaList);
    void getAsesoresAll(Map<String,String> asesores);
}
