package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vicenteaguilera.integratec.helpers.DataBaseHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.models.Alumno;

import java.util.ArrayList;

public class AsesoradosActivity extends AppCompatActivity {
    private ListView listView_BD;
    private ArrayList<String> listaInformacion;
    private ArrayList<Alumno> listaAlumnos;
    private DataBaseHelper helper;
    private ArrayAdapter arrayAdapterListView;
    private TextView textView_sin_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asesorados);

        textView_sin_data = findViewById(R.id.textView_sin_data);

        listaAlumnos = new ArrayList<Alumno>();
        listaInformacion = new ArrayList<String>();
        listView_BD = findViewById(R.id.listView_BD);

        helper = new DataBaseHelper(AsesoradosActivity.this, PropiertiesHelper.NOMBRE_BD, null, 1);

        consultarBD();

        arrayAdapterListView = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaInformacion);
        listView_BD.setAdapter(arrayAdapterListView);

        listView_BD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogUpdateDelete(listaAlumnos.get(position),position);
            }
        });
    }

    private void showDialogUpdateDelete(final Alumno alumno, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_update_delete, null);
        builder.setView(view).setTitle("Alumno registrado");

        final AlertDialog dialogUpdateDelete = builder.create();
        dialogUpdateDelete.setCancelable(false);
        dialogUpdateDelete.show();

        final EditText editText_NumeroControl = dialogUpdateDelete.findViewById(R.id.editText_numero_Control);
        final EditText editText_Nombre = dialogUpdateDelete.findViewById(R.id.editText_nombre_completo);
        final EditText editText_Tema = dialogUpdateDelete.findViewById(R.id.editText_Tema);

        final Spinner spinner_carrera = dialogUpdateDelete.findViewById(R.id.spinner_carrera);
        final Spinner spinner_semestre = dialogUpdateDelete.findViewById(R.id.spinner_semestre);
        final Spinner spinner_materia = dialogUpdateDelete.findViewById(R.id.spinner_materia);
        final CardView cardview_cancelar = dialogUpdateDelete.findViewById(R.id.cardview_cancelar_d);
        final CardView cardview_borrar = dialogUpdateDelete.findViewById(R.id.cardview_borrar_d);
        final CardView cardview_modificar = dialogUpdateDelete.findViewById(R.id.cardview_modificar_d);

        ArrayAdapter<String> arrayAdapterCarrera = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        spinner_carrera.setAdapter(arrayAdapterCarrera);

        ArrayAdapter<String> arrayAdapterSemestre = new ArrayAdapter<>(this,  R.layout.custom_spinner_item, PropiertiesHelper.SEMESTRES);
        spinner_semestre.setAdapter(arrayAdapterSemestre);

        ArrayAdapter<String> arrayAdapterMateria = new ArrayAdapter<>(this,  R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        spinner_materia.setAdapter(arrayAdapterMateria);

        editText_NumeroControl.setText(String.valueOf(alumno.getnControl()));
        editText_Nombre.setText(alumno.getNombre());
        editText_Tema.setText(alumno.getTema());
        spinner_carrera.setSelection(returnCarrera(alumno.getCarrera()));
        spinner_materia.setSelection(returnMateria(alumno.getMateria()));
        spinner_semestre.setSelection(returnSemestre(alumno.getSemestre()));

        cardview_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(AsesoradosActivity.this);
                dialogConfirm.setTitle("¿Desea eliminar este alumno?");
                dialogConfirm.setMessage("¿Esta seguro de borrar a "+alumno.getNombre()+"?");
                dialogConfirm.setCancelable(false);
                dialogConfirm.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String parametros[] = {String.valueOf(alumno.getId())};

                        db.delete(PropiertiesHelper.NOMBRE_TABLA, "id=?", parametros);
                        consultarBD();
                        arrayAdapterListView.notifyDataSetChanged();
                        dialogUpdateDelete.dismiss();
                    }
                });
                dialogConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogConfirm.show();

            }
        });

        cardview_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateDelete.dismiss();
            }
        });

        cardview_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_NumeroControl.setError(null);
                editText_Nombre.setError(null);
                editText_Tema.setError(null);

                boolean flag_nControl = false;
                boolean flag_nombre = false;
                boolean flag_spinners = false;
                boolean flag_tema = false;

                if(!editText_NumeroControl.getText().toString().isEmpty() && !editText_NumeroControl.getText().toString().equals("")
                        && editText_NumeroControl.getText().toString() != null && editText_NumeroControl.getText().toString().length() == 8){
                    flag_nControl = true;
                }else {
                    editText_NumeroControl.setError("El número de control es invalido");
                }

                if(!editText_Nombre.getText().toString().isEmpty() && !editText_Nombre.getText().toString().equals("") && editText_Nombre != null){
                    flag_nombre = true;
                }else {
                    editText_Nombre.setError("Nombre requerido");
                }

                if(spinner_carrera.getSelectedItemPosition() != 0 && spinner_materia.getSelectedItemPosition() != 0 && spinner_semestre.getSelectedItemPosition() != 0){
                    flag_spinners = true;
                }

                if(!editText_Tema.getText().toString().isEmpty() && !editText_Tema.getText().toString().equals("") && editText_Tema.getText().toString()!= null){
                    flag_tema = true;
                }else {
                    editText_Tema.setError("Tema requerido");
                }


                if(flag_nControl && flag_nombre && flag_spinners && flag_tema){
                    SQLiteDatabase db = helper.getWritableDatabase();
                    String parametros[] = {String.valueOf(alumno.getId())};
                    ContentValues values = new ContentValues();
                    values.put(PropiertiesHelper.CAMPO_NCONTROL, editText_NumeroControl.getText().toString());
                    values.put(PropiertiesHelper.CAMPO_NOMBRE, editText_Nombre.getText().toString());
                    values.put(PropiertiesHelper.CAMPO_CARRERA, spinner_carrera.getSelectedItem().toString());
                    values.put(PropiertiesHelper.CAMPO_SEMESTRE, spinner_semestre.getSelectedItem().toString());
                    values.put(PropiertiesHelper.CAMPO_MATERIA, spinner_materia.getSelectedItem().toString());
                    values.put(PropiertiesHelper.CAMPO_TEMA, editText_Tema.getText().toString());


                    db.update(PropiertiesHelper.NOMBRE_TABLA, values,"id=?",parametros);

                    consultarBD();
                    arrayAdapterListView.notifyDataSetChanged();

                    dialogUpdateDelete.dismiss();
                }else {
                    Toast.makeText(AsesoradosActivity.this, "Algunos de los datos ingresados son inválidos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void consultarBD() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Alumno alumno = null;

        if(listaAlumnos.size()!=0){
            listaAlumnos.clear();
        }

        Cursor cursor = db.rawQuery("SELECT * FROM "+ PropiertiesHelper.NOMBRE_TABLA, null);

        while (cursor.moveToNext()){
            alumno = new Alumno();
            alumno.setId(cursor.getInt(0));
            alumno.setnControl(cursor.getInt(1));
            alumno.setNombre(cursor.getString(2));
            alumno.setSemestre(cursor.getString(3));
            alumno.setCarrera(cursor.getString(4));
            alumno.setMateria(cursor.getString(5));
            alumno.setTema(cursor.getString(6));
            alumno.setFecha(cursor.getString(7));

            listaAlumnos.add(alumno);
        }

        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion.clear();
        int i = 0;

        if(listaAlumnos.size()!=0) {
            for (Alumno alumno : listaAlumnos) {
                Log.e("I: ", (i++) + "");
                listaInformacion.add("Número de control:" + alumno.getnControl() + "\nNombre del alumno:" + alumno.getNombre() +
                        "\nCarrera:" + alumno.getCarrera() + "\nSemestre:" + alumno.getSemestre() + "\nMateria:" + alumno.getMateria() + "\nTema:" + alumno.getTema() + "\nFecha:" + alumno.getFecha());
            }
            listView_BD.setVisibility(View.VISIBLE);
            textView_sin_data.setVisibility(View.GONE);
        }else {
            listView_BD.setVisibility(View.GONE);
            textView_sin_data.setVisibility(View.VISIBLE);
        }


    }

    private int returnCarrera(String carrera){
        if(carrera.equals("Ingeniería en Sistemas Computacionales")){
            return 1;
        }else if(carrera.equals("Ingeniería en Administración")){
            return 2;
        }else if(carrera.equals("Ingeniería en Mecatrónica")){
            return 3;
        }else if(carrera.equals("Ingeniería Industrial")){
            return 4;
        }else if(carrera.equals("Ingeniería en Mecánica")){
            return 5;
        }else if(carrera.equals("Ingeniería en Industrias Alimentarias")){
            return 6;
        }else if(carrera.equals("Ingeniería Civil")){
            return 7;
        }else if(carrera.equals("Ingeniería Electrónica")){
            return 8;
        }
        return  0;
    }

    private int returnSemestre(String semestre){
        if(semestre.equals("1A")){
            return 1;
        }else if(semestre.equals("1B")){
            return 2;
        }else if(semestre.equals("2A")){
            return 3;
        }else if(semestre.equals("2B")){
            return 4;
        }else if(semestre.equals("3A")){
            return 5;
        }else if(semestre.equals("3B")){
            return 6;
        }else if(semestre.equals("4A")){
            return 7;
        }else if(semestre.equals("4B")){
            return 8;
        }else if(semestre.equals("5A")){
            return 9;
        }else if(semestre.equals("5B")){
            return 10;
        }else if(semestre.equals("6A")){
            return 11;
        }else if(semestre.equals("6B")){
            return 12;
        }else if(semestre.equals("7A")){
            return 13;
        }else if(semestre.equals("7B")){
            return 14;
        }else if(semestre.equals("8A")){
            return 15;
        }else if(semestre.equals("8B")){
            return 16;
        }else if(semestre.equals("9A")){
            return 17;
        }else if(semestre.equals("9B")){
            return 18;
        }else if(semestre.equals("10A")){
            return 19;
        }else if(semestre.equals("10B")){
            return 20;
        }

        return 0;
    }

    private int returnMateria(String materia){
        if(materia.equals("Álgebra")){
            return 1;
        }else if(materia.equals("Álgebra lineal")){
            return 2;
        }else if(materia.equals("Cálculo diferencial")){
            return 3;
        }else if(materia.equals("Cálculo integral")){
            return 4;
        }else if(materia.equals("Cálculo vectorial")){
            return 5;
        }else if(materia.equals("Ecuaciones diferenciales")){
            return 6;
        }else if(materia.equals("Química")){
            return 7;
        }else if(materia.equals("Física")){
            return 8;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_crud_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.item_buscar);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                consultarBDPorFiltro(searchView.getQuery().toString());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void consultarBDPorFiltro(String text) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Alumno alumno = null;

        listaAlumnos.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ PropiertiesHelper.NOMBRE_TABLA, null);

        while (cursor.moveToNext()){
            alumno = new Alumno();
            alumno.setId(cursor.getInt(0));
            alumno.setnControl(cursor.getInt(1));
            alumno.setNombre(cursor.getString(2));
            alumno.setSemestre(cursor.getString(3));
            alumno.setCarrera(cursor.getString(4));
            alumno.setMateria(cursor.getString(5));
            alumno.setTema(cursor.getString(6));
            alumno.setFecha(cursor.getString(7));

            if(alumno.getData().contains(text)) {
                listaAlumnos.add(alumno);
                Log.e("InfoAlumno: ", alumno.getData());
            }
        }
        obtenerLista();
        arrayAdapterListView.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){

            case R.id.item_add_asesorado:
                showDialogAddAlumno();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void showDialogAddAlumno(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_alumno,null);
        builder.setView(view).
                setTitle("Registrar alumno asesorado");

        final AlertDialog dialogAdd = builder.create();
        dialogAdd.setCancelable(false);
        dialogAdd.show();


        final EditText editText_NumeroControl = dialogAdd.findViewById(R.id.editText_numero_control_add);
        final EditText editText_Nombre = dialogAdd.findViewById(R.id.editText_nombre_add);
        final EditText editText_Tema = dialogAdd.findViewById(R.id.editTextText_tema_add);

        final Spinner spinner_carrera = dialogAdd.findViewById(R.id.spinner_carrera_add);
        final Spinner spinner_semestre = dialogAdd.findViewById(R.id.spinner_semestre_add);
        final Spinner spinner_materia = dialogAdd.findViewById(R.id.spinner_materia_add);
        final CardView cardview_cancelar = dialogAdd.findViewById(R.id.cardView_cancelar_add);
        final CardView cardview_add = dialogAdd.findViewById(R.id.cardView_agregar_add);

        ArrayAdapter<String> arrayAdapterCarrera = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        spinner_carrera.setAdapter(arrayAdapterCarrera);

        ArrayAdapter<String> arrayAdapterSemestre = new ArrayAdapter<>(this,  R.layout.custom_spinner_item, PropiertiesHelper.SEMESTRES);
        spinner_semestre.setAdapter(arrayAdapterSemestre);

        ArrayAdapter<String> arrayAdapterMateria = new ArrayAdapter<>(this,  R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        spinner_materia.setAdapter(arrayAdapterMateria);

        cardview_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });

        cardview_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_NumeroControl.setError(null);
                editText_Nombre.setError(null);
                editText_Tema.setError(null);

                boolean flag_nControl = false;
                boolean flag_nombre = false;
                boolean flag_spinners = false;
                boolean flag_tema = false;

                if (!editText_NumeroControl.getText().toString().isEmpty() && !editText_NumeroControl.getText().toString().equals("")
                        && editText_NumeroControl.getText().toString() != null && editText_NumeroControl.getText().toString().length() == 8) {
                    flag_nControl = true;
                } else {
                    editText_NumeroControl.setError("El número de control es invalido");
                }

                if (!editText_Nombre.getText().toString().isEmpty() && !editText_Nombre.getText().toString().equals("") && editText_Nombre != null) {
                    flag_nombre = true;
                } else {
                    editText_Nombre.setError("Nombre requerido");
                }

                if (spinner_carrera.getSelectedItemPosition() != 0 && spinner_materia.getSelectedItemPosition() != 0 && spinner_semestre.getSelectedItemPosition() != 0) {
                    flag_spinners = true;
                }

                if (!editText_Tema.getText().toString().isEmpty() && !editText_Tema.getText().toString().equals("") && editText_Tema.getText().toString() != null) {
                    flag_tema = true;
                } else {
                    editText_Tema.setError("Tema requerido");
                }


                if (flag_nControl && flag_nombre && flag_spinners && flag_tema) {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put(PropiertiesHelper.CAMPO_NCONTROL, editText_NumeroControl.getText().toString());
                    values.put(PropiertiesHelper.CAMPO_NOMBRE, editText_Nombre.getText().toString());
                    values.put(PropiertiesHelper.CAMPO_CARRERA, spinner_carrera.getSelectedItem().toString());
                    values.put(PropiertiesHelper.CAMPO_MATERIA, spinner_materia.getSelectedItem().toString());
                    values.put(PropiertiesHelper.CAMPO_TEMA, editText_Tema.getText().toString());
                    values.put(PropiertiesHelper.CAMPO_SEMESTRE, spinner_semestre.getSelectedItem().toString());
                    values.put(PropiertiesHelper.CAMPO_FECHA, PropiertiesHelper.obtenerFecha().substring(0, 10));
                    db.insert(PropiertiesHelper.NOMBRE_TABLA, "id", values);
                    Toast.makeText(AsesoradosActivity.this, "Alumno registrado con exito", Toast.LENGTH_LONG).show();
                    consultarBD();
                    arrayAdapterListView.notifyDataSetChanged();
                    dialogAdd.dismiss();
                } else {
                    Toast.makeText(AsesoradosActivity.this, "Algunos de los datos ingresados son inválidos", Toast.LENGTH_LONG).show();
                }

            }



        });
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}