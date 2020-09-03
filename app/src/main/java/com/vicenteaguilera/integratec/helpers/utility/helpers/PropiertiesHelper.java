package com.vicenteaguilera.integratec.helpers.utility.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PropiertiesHelper {

    public static String obtenerFecha() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    public static final String [] MATERIAS  ={"Seleccione una materia...","Álgebra", "Álgebra lineal", "Cálculo diferencial", "Cálculo integral", "Cálculo vectorial", "Ecuaciones diferenciales", "Química", "Física"};
    public static final String [] LUGARES ={"Seleccione un lugar...","Biblioteca", "Telemática", "Edificio A", "Edificio F","Otro(Especifique)"};
    public static final String [] CARRERAS ={"Seleccione una carrera...","Ingeniería en Sistemas Computacionales", "Ingeniería en Administración", "Ingeniería en Mecatrónica", "Ingeniería Industrial", "Ingeniería en Mecánica", "Ingeniería en Industrias Alimentarias", "Ingeniería Civil"};
    public static final String [] SEMESTRES ={"Seleccione un semestre..","1A", "1B", "2A", "2B", "3A", "3B", "4A", "4B", "5A", "5B", "6A", "6B", "7A", "7B", "8A", "8B", "9A", "9B", "10A", "10B"};
    public static final String CODIGO_CB = "IaTmSpUit4";
    public static final String EMAIL ="integratecitsu@gmail.com";
    public static final String PASSWORD = "IntegraTec1234";
}
