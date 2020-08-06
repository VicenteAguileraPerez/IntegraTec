package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import net.glxn.qrgen.android.QRCode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateCodeQRActivity extends AppCompatActivity {

    private EditText editText_Nombre;
    private EditText editText_Tema;
    private Spinner spinner_Carrera;
    private Spinner spinner_Asignatura;
    private ImageView imageView;
    private CardView cardView_BtnCrearQR;

    private Calendar myCalendar = Calendar.getInstance();

    private final String [] MATERIAS  ={"Seleccione una materia...","Álgebra", "Álgebra lineal", "Cálculo diferencial", "Cálculo integral", "Cálculo vectorial", "Ecuaciones diferenciales", "Química", "Física"};
    private final String [] CARRERAS ={"Seleccione una carrera...","Ingeniería en Sistemas Computacionales", "Ingeniería en Administración", "Ingeniería en Mecatrónica", "Ingeniería Industrial", "Ingeniería en Mecánica", "Ingeniería en Industrias Alimentarias", "Ingeniería Civil"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_code_q_r);

        editText_Nombre = findViewById(R.id.editText_Nombre);
        editText_Tema = findViewById(R.id.editText_Tema);
        spinner_Carrera = findViewById(R.id.spinner_Carrera);
        spinner_Asignatura = findViewById(R.id.spinner_Asignatura);
        imageView = findViewById(R.id.imageView);
        cardView_BtnCrearQR = findViewById(R.id.cardView_ButtonCrearQR);

        imageView.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> arrayAdapter_Carreras= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CARRERAS);
        ArrayAdapter<String> arrayAdapter_Materias= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MATERIAS);

        spinner_Asignatura.setAdapter(arrayAdapter_Materias);
        spinner_Carrera.setAdapter(arrayAdapter_Carreras);

        cardView_BtnCrearQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearQR();
            }
        });

    }

    private void crearQR()
    {
        if(!editText_Nombre.getText().toString().isEmpty())
        {
            if(spinner_Carrera.getSelectedItemPosition() != 0)
            {
                if(spinner_Asignatura.getSelectedItemPosition() != 0)
                {
                    if(!editText_Tema.getText().toString().isEmpty())
                    {

                        String texto = editText_Nombre.getText().toString() + "_" + spinner_Carrera.getSelectedItem().toString() + "_" +
                                spinner_Asignatura.getSelectedItem().toString() + "_" + editText_Tema.getText().toString();

                        Bitmap bitmap = QRCode.from(texto).withSize(300, 300).bitmap();
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Snackbar.make(findViewById(android.R.id.content), "Debes ingresar el tema de asesoria.", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Debes seleccionar una materia.", Snackbar.LENGTH_SHORT).show();
                }
            }
            else
            {
                Snackbar.make(findViewById(android.R.id.content), "Debes seleccionar una carrera.", Snackbar.LENGTH_SHORT).show();
            }
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content), "Debes ingresar tú nombre.", Snackbar.LENGTH_SHORT).show();
        }
    }



}
