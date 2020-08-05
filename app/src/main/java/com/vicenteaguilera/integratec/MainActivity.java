package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private EditText editText_Nombre;
    private EditText editText_Tema;
    private EditText editText_Fecha;
    private Spinner spinner_Carrera;
    private Spinner spinner_Asignatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_Nombre = findViewById(R.id.editText_Nombre);
        editText_Tema = findViewById(R.id.editText_Tema);
        editText_Fecha = findViewById(R.id.editText_Fecha);
        spinner_Carrera = findViewById(R.id.spinner_Carrera);
        spinner_Asignatura = findViewById(R.id.spinner_Asignatura);
    }
}
