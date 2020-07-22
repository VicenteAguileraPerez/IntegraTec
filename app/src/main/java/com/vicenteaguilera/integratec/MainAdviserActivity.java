package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainAdviserActivity extends AppCompatActivity {

    private Spinner spinner_materias;
    private Spinner spinner_lugares;
    private RadioGroup radioGroup;
    private TextView textView_URL;
    private EditText editText_HoraInicio;
    private EditText editText_HoraFinalizacion;
    private EditText editTextTextMultiLine;
    private Switch switchEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adviser);

        spinner_materias = findViewById(R.id.spinner_Materia);
        spinner_lugares = findViewById(R.id.spinner_Lugar);
        radioGroup = findViewById(R.id.radioGroup);
        textView_URL = findViewById(R.id.textView_URL);
        editText_HoraInicio = findViewById(R.id.editText_HoraInicio);
        editText_HoraFinalizacion = findViewById(R.id.editTextTime_Finalizacion);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        switchEstado = findViewById(R.id.switch_Estado);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked)
                {
                    if(checkedRadioButton.getId()==R.id.radioButton_AOnline)
                    {
                        spinner_lugares.setVisibility(View.GONE);

                        textView_URL.setVisibility(View.VISIBLE);
                        spinner_materias.setVisibility(View.VISIBLE);
                        editText_HoraInicio.setVisibility(View.VISIBLE);
                        editText_HoraFinalizacion.setVisibility(View.VISIBLE);
                        editTextTextMultiLine.setVisibility(View.VISIBLE);

                    }else if(checkedRadioButton.getId()==R.id.radioButton_APresencial)
                    {
                        textView_URL.setVisibility(View.GONE);

                        spinner_lugares.setVisibility(View.VISIBLE);
                        spinner_materias.setVisibility(View.VISIBLE);
                        editText_HoraInicio.setVisibility(View.VISIBLE);
                        editText_HoraFinalizacion.setVisibility(View.VISIBLE);
                        editTextTextMultiLine.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        ArrayAdapter<CharSequence> arrayAdapterLugares = ArrayAdapter.createFromResource(this, R.array.string_lugares, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> arrayAdapterMaterias = ArrayAdapter.createFromResource(this, R.array.string_materias, android.R.layout.simple_spinner_item);

        spinner_lugares.setAdapter(arrayAdapterLugares);
        spinner_materias.setAdapter(arrayAdapterMaterias);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id){
            case R.id.item_AcercaDe:
                Toast.makeText(this, "Acerca de...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_QuejasSugerencias:
                Toast.makeText(this, "Quejas y sugerencias...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_CerrarSesion:
                Toast.makeText(this, "Cerrar sesión...", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
