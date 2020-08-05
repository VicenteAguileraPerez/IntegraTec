package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private EditText editText_Nombre;
    private EditText editText_Tema;
    private EditText editText_Fecha;
    private Spinner spinner_Carrera;
    private Spinner spinner_Asignatura;

    private final String [] MATERIAS  ={"Seleccione una materia...","Álgebra", "Álgebra lineal", "Cálculo diferencial", "Cálculo integral", "Cálculo vectorial", "Ecuaciones diferenciales", "Química", "Física"};
    private final String [] CARRERAS ={"Seleccione una carrera","Ingeniería en Sistemas Computacionales", "Ingeniería en Administración", "Ingeniería en Mecatrónica", "Ingeniería Industrial", "Ingeniería en Mecánica", "Ingeniería en Industrias Alimentarias", "Ingeniería Civil"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_Nombre = findViewById(R.id.editText_Nombre);
        editText_Tema = findViewById(R.id.editText_Tema);
        editText_Fecha = findViewById(R.id.editText_Fecha);
        spinner_Carrera = findViewById(R.id.spinner_Carrera);
        spinner_Asignatura = findViewById(R.id.spinner_Asignatura);

        ArrayAdapter<String> arrayAdapter_Carreras= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CARRERAS);
        ArrayAdapter<String> arrayAdapter_Materias= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MATERIAS);

        spinner_Asignatura.setAdapter(arrayAdapter_Materias);
        spinner_Carrera.setAdapter(arrayAdapter_Carreras);
    }
}
