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
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirestoreAsesorado;
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorados;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.AlumnoAsesorado;
import com.vicenteaguilera.integratec.models.Asesorado;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsesoradosActivity extends AppCompatActivity implements Status, ListaAsesorados {
    private ListView listView_BD;
    private ArrayList<String> listaInformacion;
    private ArrayList<AlumnoAsesorado> listaAlumnos;
    private ArrayAdapter arrayAdapterListView;
    private TextView textView_sin_data;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private ButtonHelper buttonHelper = new ButtonHelper();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver,intentFilter);
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

        listaAlumnos = new ArrayList<AlumnoAsesorado>();
        listaInformacion = new ArrayList<String>();
        listView_BD = findViewById(R.id.listView_BD);

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

    private void showDialogUpdateDelete(final AlumnoAsesorado alumno, int position) {
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

        //buttonHelper.actionClickButton(cardview_cancelar, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));
        //buttonHelper.actionClickButton(cardview_borrar, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));
        //buttonHelper.actionClickButton(cardview_modificar, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));

        ArrayAdapter<String> arrayAdapterCarrera = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        ((AutoCompleteTextView)spinner_carrera_update.getEditText()).setAdapter(arrayAdapterCarrera);


        ArrayAdapter<String> arrayAdapterMateria = new ArrayAdapter<>(this,  R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        ((AutoCompleteTextView)spinner_materia_update.getEditText()).setAdapter(arrayAdapterCarrera);

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

                    dialogUpdateDelete.dismiss();
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
            for (AlumnoAsesorado alumno : listaAlumnos) {
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
                ProgressDialog dialog = ProgressDialog.show(AsesoradosActivity.this, "", "Buscando...", true);
                //ProgressDialog dialog = ProgressDialog.show(AsesoradosActivity.this, "", "Agregando datos...", true);
                //ProgressDialog dialog = ProgressDialog.show(AsesoradosActivity.this, "", "Eliminando...", true);
                dialog.show();
                //new FirestoreAsesorado().addAsesorado(this, dialog, this, "18040756", "Salvador", "ISC", "Cálculo", "Derivadas", "31-01-2021", FirebaseAuthHelper.getCurrentUser().getUid());
                //new FirestoreAsesorado().addAsesorado(this, dialog, this, "18040076", "Antonio", "ISC", "Cálculo", "Derivadas", "31-01-2021", FirebaseAuthHelper.getCurrentUser().getUid());
                //new FirestoreAsesorado().deleteAsesorados(FirebaseAuthHelper.getCurrentUser().getUid(), dialog);
                new FirestoreAsesorado().readAsesorados(this, FirebaseAuthHelper.getCurrentUser().getUid());
                //showDialogAddAlumno();
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


        final TextInputLayout textInputLayout_numeroControl_add_alumno = dialogAdd.findViewById(R.id.textInputLayout_numeroControl_add_alumno);
        final TextInputEditText textInputEditText_fecha_add_alumno = dialogAdd.findViewById(R.id.textInputEditText_fecha_add_alumno);
        final TextInputLayout textInputLayout_nombre_add_alumno = dialogAdd.findViewById(R.id.textInputLayout_nombre_add_alumno);
        final TextInputLayout spinner_materia_add = dialogAdd.findViewById(R.id.spinner_materia_add);

        final EditText editText_fecha_add_alumno = dialogAdd.findViewById(R.id.textInputEditText_fecha_add_alumno);

        final CardView cardview_cancelar = dialogAdd.findViewById(R.id.cardView_cancelar_add);
        final CardView cardview_add = dialogAdd.findViewById(R.id.cardView_agregar_add);

        buttonHelper.actionClickButton(cardview_cancelar, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));
        buttonHelper.actionClickButton(cardview_add, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));

        ArrayAdapter<String> arrayAdapter_materia = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        ((AutoCompleteTextView)spinner_materia_add.getEditText()).setAdapter(arrayAdapter_materia);

        //DatePicker
        MaterialDatePicker.Builder builder_date = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker materialDatePicker = builder_date.build();


        textInputEditText_fecha_add_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textInputEditText_fecha_add_alumno.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });

        cardview_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });

        cardview_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //spinner_materia_add.getEditText().getText().toString();
            }
        });

        textInputLayout_numeroControl_add_alumno.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aquí se le da vida al icono de busqueda
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
        String a = "";
        for(int i = 0; i<asesoradoList.size(); i++)
        {
            a += "Nombre = " + asesoradoList.get(i).getNombre() + "\n";
            a += "NumControl = " + asesoradoList.get(i).getnControl() + "\n\n";
        }
        new AlertDialogPersonalized().alertDialogInformacion(a, this);
    }
}