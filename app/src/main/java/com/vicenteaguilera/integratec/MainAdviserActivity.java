package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainAdviserActivity extends AppCompatActivity {

    private Spinner spinner_materias;
    private Spinner spinner_lugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adviser);

        spinner_materias = findViewById(R.id.spinner_Materia);
        spinner_lugares = findViewById(R.id.spinner_Lugar);

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
