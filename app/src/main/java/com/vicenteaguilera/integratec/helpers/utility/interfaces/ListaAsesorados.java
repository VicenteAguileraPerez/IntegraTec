package com.vicenteaguilera.integratec.helpers.utility.interfaces;

import com.vicenteaguilera.integratec.models.Alumno;
import com.vicenteaguilera.integratec.models.Asesorado;

import java.util.List;

public interface ListaAsesorados {
    void getAsesorados(List<Asesorado> asesoradoList);
    void getAlumno(Alumno alumno);
}
