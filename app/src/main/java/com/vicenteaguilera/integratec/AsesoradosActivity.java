package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirestoreAlumno;
import com.vicenteaguilera.integratec.helpers.services.FirestoreAsesorado;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorados;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Alumno;
import com.vicenteaguilera.integratec.models.AlumnoAsesorado;
import com.vicenteaguilera.integratec.models.Asesorado;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsesoradosActivity extends AppCompatActivity implements Status, ListaAsesorados, View.OnClickListener {
    private ListView listView_BD;
    private ArrayList<String> listaInformacion;
    private ArrayList<Asesorado> listaAlumnos;
    private ArrayList<Asesorado> listaAlumnosFiltrados;
    private ArrayAdapter arrayAdapterListView;
    private TextView textView_sin_data;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private FirestoreAlumno firestoreAlumno = new FirestoreAlumno();
    private FirestoreAsesorado firestoreAsesorado = new FirestoreAsesorado();
    private FirestoreHelper firestoreHelper = new FirestoreHelper();

    private TextInputLayout textInputLayout_nombre_add_alumno;
    private TextInputLayout textInputLayout_carrera_add_alumno;

    public ArrayAdapter<String> arrayAdapter_carreras;
    public ArrayAdapter<String> arrayAdapter_materia;

    private boolean flag = false;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver,intentFilter);
        firestoreAsesorado.readAsesorados(AsesoradosActivity.this, FirestoreHelper.asesor.getUid());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asesorados);

        textView_sin_data = findViewById(R.id.textView_sin_data);

        listaAlumnos = new ArrayList<Asesorado>();
        listaAlumnosFiltrados = new ArrayList<Asesorado>();
        listaInformacion = new ArrayList<String>();
        listView_BD = findViewById(R.id.listView_BD);

        arrayAdapterListView = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaInformacion);
        listView_BD.setAdapter(arrayAdapterListView);

        listView_BD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogUpdateDelete(listaAlumnos.get(position),position);
            }
        });

       arrayAdapter_carreras  = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
       arrayAdapter_materia  = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        //createDialogAddAlumno();
    }

    private void showDialogUpdateDelete(final Asesorado alumno, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_update_delete, null);
        builder.setView(view).setTitle("Alumno registrado");

        final AlertDialog dialogUpdateDelete = builder.create();
        dialogUpdateDelete.setCancelable(false);
        dialogUpdateDelete.show();

        final TextInputLayout textInputLayout_numero_control_update = dialogUpdateDelete.findViewById(R.id.textInputLayout_numero_control_update);
        final TextInputLayout textInputLayout_nombre_update = dialogUpdateDelete.findViewById(R.id.textInputLayout_nombre_update);
        final TextInputLayout textInputLayout_tema_update = dialogUpdateDelete.findViewById(R.id.textInputLayout_tema_update);

        final TextInputLayout spinner_carrera_update = dialogUpdateDelete.findViewById(R.id.spinner_carrera_update);
        final TextInputLayout spinner_materia_update = dialogUpdateDelete.findViewById(R.id.spinner_materia_update);

        final MaterialButton button_cancelar_update = dialogUpdateDelete.findViewById(R.id.button_cancelar_update);
        final MaterialButton button_borrar_update = dialogUpdateDelete.findViewById(R.id.button_borrar_update);
        final MaterialButton button_modificar_update = dialogUpdateDelete.findViewById(R.id.button_modificar_update);

        ArrayAdapter<String> arrayAdapterCarrera = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        ((AutoCompleteTextView)spinner_carrera_update.getEditText()).setAdapter(arrayAdapterCarrera);

        ArrayAdapter<String> arrayAdapterMateria = new ArrayAdapter<>(this,  R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        ((AutoCompleteTextView)spinner_materia_update.getEditText()).setAdapter(arrayAdapterMateria);

        textInputLayout_numero_control_update.getEditText().setText(String.valueOf(alumno.getnControl()));
        textInputLayout_nombre_update.getEditText().setText(alumno.getNombre());
        textInputLayout_tema_update.getEditText().setText(alumno.getTema());
        spinner_carrera_update.getEditText().setText(alumno.getCarrera());
        spinner_materia_update.getEditText().setText(alumno.getMateria());

        button_borrar_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(AsesoradosActivity.this);
                dialogConfirm.setTitle("¿Desea eliminar este alumno?");
                dialogConfirm.setMessage("¿Esta seguro de borrar a "+alumno.getNombre()+"?");
                dialogConfirm.setCancelable(false);
                dialogConfirm.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog dialogD = ProgressDialog.show(AsesoradosActivity.this, "", "Eliminando...", true);
                        firestoreAsesorado.deleteDataAsesorado(AsesoradosActivity.this, dialogD, AsesoradosActivity.this, alumno.getId());
                        dialogUpdateDelete.dismiss();
                        firestoreAsesorado.readAsesorados(AsesoradosActivity.this, FirestoreHelper.asesor.getUid());
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

        button_cancelar_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateDelete.dismiss();
            }
        });

        button_modificar_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayout_numero_control_update.setError(null);
                textInputLayout_nombre_update.setError(null);
                textInputLayout_tema_update.setError(null);

                boolean flag_nControl = false;
                boolean flag_nombre = false;
                boolean flag_spinner_carrera = false;
                boolean flag_spinner_materia = false;
                boolean flag_tema = false;

                if(!textInputLayout_numero_control_update.getEditText().getText().toString().isEmpty() && !textInputLayout_numero_control_update.getEditText().getText().toString().equals("")
                        && textInputLayout_numero_control_update.getEditText().getText().toString() != null && textInputLayout_numero_control_update.getEditText().getText().toString().length() == 8){
                    flag_nControl = true;
                }else {
                    textInputLayout_numero_control_update.setError("El número de control es invalido");
                }

                if(!textInputLayout_nombre_update.getEditText().getText().toString().isEmpty() && !textInputLayout_nombre_update.getEditText().getText().toString().equals("")){
                    flag_nombre = true;
                }else {
                    textInputLayout_nombre_update.setError("Nombre requerido");
                }

                if(Objects.requireNonNull(spinner_carrera_update.getEditText()).getText().length() != 0 ){
                    flag_spinner_carrera = true;
                }else {
                    spinner_carrera_update.setError("Seleccione una opción valida");
                }

                if(Objects.requireNonNull(spinner_materia_update.getEditText()).getText().length() != 0 ){
                    flag_spinner_materia = true;
                }else {
                    spinner_materia_update.setError("Seleccione una opción valida");
                }

                if(!textInputLayout_tema_update.getEditText().getText().toString().isEmpty() && !textInputLayout_tema_update.getEditText().getText().toString().equals("") && textInputLayout_tema_update.getEditText().getText().toString()!= null){
                    flag_tema = true;
                }else {
                    textInputLayout_tema_update.setError("Tema requerido");
                }


                if(flag_nControl && flag_nombre && flag_spinner_carrera && flag_tema && flag_spinner_materia){
                    ProgressDialog dialog = ProgressDialog.show(AsesoradosActivity.this, "", "Actualizando...", true);
                    firestoreAsesorado.updateDataAsesorado(AsesoradosActivity.this, dialog, AsesoradosActivity.this, alumno.getId(),
                            textInputLayout_numero_control_update.getEditText().getText().toString(),
                            textInputLayout_nombre_update.getEditText().getText().toString(),
                            spinner_carrera_update.getEditText().getText().toString(),
                            spinner_materia_update.getEditText().getText().toString(),
                            textInputLayout_tema_update.getEditText().getText().toString(),
                            alumno.getFecha(),
                            FirestoreHelper.asesor.getUid());
                    dialogUpdateDelete.dismiss();
                    firestoreAsesorado.readAsesorados(AsesoradosActivity.this, FirestoreHelper.asesor.getUid());
                }else {
                    Toast.makeText(AsesoradosActivity.this, "Algunos de los datos ingresados son inválidos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void consultarBD() {
        if(listaAlumnos.size()!=0){
            listaAlumnos.clear();
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion.clear();
        int i = 0;

        if(listaAlumnos.size()!=0) {
            for (Asesorado alumno : listaAlumnos) {
                Log.e("I: ", (i++) + "");
                listaInformacion.add("Número de control:" + alumno.getnControl() + "\nNombre del alumno:" + alumno.getNombre() +"\nFecha:" + alumno.getFecha());
            }
            listView_BD.setVisibility(View.VISIBLE);
            textView_sin_data.setVisibility(View.GONE);
        }else {
            listView_BD.setVisibility(View.GONE);
            textView_sin_data.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_crud_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.item_buscar);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(getResources().getString(R.string.buscar));
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
        listaAlumnos.clear();

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

    private void createDialogAddAlumno(){

    }

    private void showDialogAddAlumno()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_alumno,null);
        builder.setView(view).
                setTitle("Registrar alumno asesorado");

        final AlertDialog dialogAdd = builder.create();
        dialogAdd.setCancelable(false);
        dialogAdd.show();

        final TextInputLayout textInputLayout_numeroControl_add_alumno = dialogAdd.findViewById(R.id.textInputLayout_numeroControl_add_alumno);
        textInputLayout_nombre_add_alumno = dialogAdd.findViewById(R.id.textInputLayout_nombre_add_alumno);
        textInputLayout_carrera_add_alumno = dialogAdd.findViewById(R.id.textInputLayout_carrera_add_alumno);
        final TextInputLayout textInputLayout_materia_add_alumno = dialogAdd.findViewById(R.id.spinner_materia_add);
        final TextInputLayout textInputLayout_tema_add_alumno = dialogAdd.findViewById(R.id.textInputLayout_tema_add_alumno);
        final TextInputEditText textInputEditText_fecha_add_alumno = dialogAdd.findViewById(R.id.textInputEditText_fecha_add_alumno);
        final MaterialButton button_registrar_add_alumno = dialogAdd.findViewById(R.id.button_registrar_add);
        final MaterialButton button_cancelar_add_alumno = dialogAdd.findViewById(R.id.button_cancelar_add);

        ((AutoCompleteTextView)textInputLayout_carrera_add_alumno.getEditText()).setAdapter(arrayAdapter_carreras);
        ((AutoCompleteTextView)textInputLayout_materia_add_alumno.getEditText()).setAdapter(arrayAdapter_materia);

        button_cancelar_add_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd.dismiss();
            }
        });
        button_registrar_add_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String numControl = textInputLayout_numeroControl_add_alumno.getEditText().getText().toString();
                String nombreCompleto = textInputLayout_nombre_add_alumno.getEditText().getText().toString();
                String carrera = textInputLayout_carrera_add_alumno.getEditText().getText().toString();
                String materia = textInputLayout_materia_add_alumno.getEditText().getText().toString();
                String tema = textInputLayout_tema_add_alumno.getEditText().getText().toString();
                String fecha = textInputEditText_fecha_add_alumno.getText().toString();

                boolean flagNumControl = false;
                boolean flagNombreCompleto = false;
                boolean flagCarrera = false;
                boolean flagMateria = false;
                boolean flagTema = false;
                boolean flagFecha = false;

                if(!numControl.isEmpty() && numControl.length()==8)
                {
                    flagNumControl = true;
                }
                {
                    textInputLayout_numeroControl_add_alumno.getEditText().setText("Número de control no válido.");
                }

                if(!nombreCompleto.isEmpty())
                {
                    flagNombreCompleto = true;
                }
                {
                    textInputLayout_nombre_add_alumno.getEditText().setText("Campo requerido.");
                }

                if(!carrera.isEmpty())
                {
                    flagCarrera = true;
                }
                {
                    textInputLayout_carrera_add_alumno.getEditText().setText("Campo requerido.");
                }

                if(!materia.isEmpty())
                {
                    flagMateria = true;
                }
                {
                    textInputLayout_materia_add_alumno.getEditText().setText("Campo requerido.");
                }

                if(!tema.isEmpty())
                {
                    flagTema = true;
                }
                {
                    textInputLayout_tema_add_alumno.getEditText().setText("Campo requerido.");
                }

                if(!fecha.isEmpty())
                {
                    flagFecha = true;
                }
                {
                    textInputEditText_fecha_add_alumno.setText("Campo requerido.");
                }

                if(flagNumControl && flagNombreCompleto && flagCarrera && flagMateria && flagTema && flagFecha)
                {
                    ProgressDialog dialog = ProgressDialog.show(AsesoradosActivity.this, "", "Resgistrando...", true);
                    firestoreAsesorado.addAsesorado(AsesoradosActivity.this, dialog, AsesoradosActivity.this, numControl, nombreCompleto, carrera, materia, tema, fecha, FirestoreHelper.asesor.getUid());
                    if(flag==false)
                    {
                        firestoreAlumno.addDataAlumno(AsesoradosActivity.this, dialog, numControl, nombreCompleto, carrera, AsesoradosActivity.this);
                    }
                    firestoreAsesorado.readAsesorados(AsesoradosActivity.this, FirestoreHelper.asesor.getUid());
                }
                else
                {
                    status("Debes llenar todos los campos requeridos.");
                }

            }
        });
        textInputLayout_numeroControl_add_alumno.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aquí se le da vida al icono de busqueda
                ProgressDialog dialog = ProgressDialog.show(AsesoradosActivity.this, "", "Buscando...", true);
                String numControl = textInputLayout_numeroControl_add_alumno.getEditText().getText().toString();
                if(numControl.length()==8)
                {
                    firestoreAlumno.getDataAlumno(AsesoradosActivity.this, numControl, dialog, AsesoradosActivity.this, AsesoradosActivity.this);
                }
                else
                {
                    textInputLayout_numeroControl_add_alumno.setError("Número de control no válido.");
                }
            }
        });
        textInputEditText_fecha_add_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatePicker
                /*MaterialDatePicker.Builder builder_date = MaterialDatePicker.Builder.datePicker();
                final MaterialDatePicker materialDatePicker = builder_date.build();
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textInputEditText_fecha_add_alumno.setText(materialDatePicker.getHeaderText());
                    }
                });*/

                MaterialTimePicker.Builder builder_time = new MaterialTimePicker.Builder();
                builder_time.setTimeFormat(TimeFormat.CLOCK_12H);
                final MaterialTimePicker materialTimePicker = builder_time.build();
                materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        textInputEditText_fecha_add_alumno.setText(materialTimePicker.getHour()+":"+materialTimePicker.getMinute());
                    }
                });

                materialTimePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textInputEditText_fecha_add_alumno.setError("Campo requerido");
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void status(String message) {
        Toast.makeText(AsesoradosActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getAsesorados(List<Asesorado> asesoradoList) {
        listaAlumnos = (ArrayList<Asesorado>) asesoradoList;
        obtenerLista();
        arrayAdapterListView.notifyDataSetChanged();
    }

    @Override
    public void getAlumno(Alumno alumno) {
        if(alumno!=null)
        {
            flag = true;
            textInputLayout_nombre_add_alumno.getEditText().setText(alumno.getNombre());
            textInputLayout_carrera_add_alumno.getEditText().setText(alumno.getCarrera());
        }
        else
        {
            ((AutoCompleteTextView)textInputLayout_carrera_add_alumno.getEditText()).setAdapter(null);
            ((AutoCompleteTextView)textInputLayout_carrera_add_alumno.getEditText()).setAdapter(arrayAdapter_carreras);
            textInputLayout_nombre_add_alumno.getEditText().setEnabled(true);
            textInputLayout_carrera_add_alumno.getEditText().setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {

    }
}