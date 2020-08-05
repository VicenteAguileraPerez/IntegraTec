package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import net.glxn.qrgen.android.QRCode;

public class MainActivity extends AppCompatActivity {

    private EditText editText_Nombre;
    private EditText editText_Tema;
    private EditText editText_Fecha;
    private Spinner spinner_Carrera;
    private Spinner spinner_Asignatura;
    private ImageView imageView;
    private CardView cardView_BtnCrearQR;

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
        if(editText_Nombre.getText().toString()!="")
        {
            if(spinner_Carrera.getSelectedItemPosition() != 0)
            {
                if(spinner_Asignatura.getSelectedItemPosition() != 0)
                {
                    if(editText_Tema.getText().toString() != "")
                    {
                        if(editText_Fecha.getText().toString() != "")
                        {

                            String texto = editText_Nombre.getText().toString() + "_" + spinner_Carrera.getSelectedItem().toString() + "_"
                                    + spinner_Asignatura.getSelectedItem().toString() + "_" + editText_Tema.getText().toString() + "_"
                                    + editText_Fecha.getText().toString();

                            Bitmap bitmap = QRCode.from(texto).withSize(300, 300).bitmap();
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {

                    }
                }
                else
                {

                }
            }
            else
            {

            }
        }
        else
        {
            Snackbar.make(null, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}
