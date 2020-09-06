package com.vicenteaguilera.integratec.controllers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vicenteaguilera.integratec.AsesoradosActivity;
import com.vicenteaguilera.integratec.CreateCodeQRActivity;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.CaptureActivityPortrait;
import com.vicenteaguilera.integratec.helpers.DataBaseHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseStorageHelper;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ImagesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.SharedPreferencesHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StringHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class MainAdviserActivityApp extends AppCompatActivity implements View.OnClickListener, Status {
    private EditText editTextText_otroLugar;

    private final static int GALLERY_INTENT = 1;
    private File imagen=null;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private TimePickerDialog picker=null;
    private Spinner spinner_materias;
    private Spinner spinner_lugares;
    private RadioGroup radioGroup;
    private EditText editText_URL;
    private EditText editText_HoraInicio;
    private EditText editText_HoraFinalizacion;
    private CardView cardView_ButtonPublicar;
    private ImageButton imageButton_edit_image;
    private EditText editTextTextMultiLine;
    private TextView textView_Estado;
    private TextView textView_Nombre;
    private SwitchMaterial switchEstado;
    private ImageView imageView_perfil;
    private RadioButton radioButton_AOnline;
    private RadioButton radioBAPresencial;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();
    private  FirestoreHelper firestoreHelper = new FirestoreHelper();
    private FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper();
    private int positionPlace=-1;
    private int positionSubject=-1;
    private DataBaseHelper helper;

    private IntentResult result= null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        clear();
        setData(sharedPreferencesHelper.getPreferences());
        firebaseAuthHelper.setContext(MainAdviserActivityApp.this);
        firebaseAuthHelper.setOnStatusListener(this);
        firebaseStorageHelper.setStatusListener(this);
        textView_Nombre.setText(FirestoreHelper.asesor.getNombre() +  " " + FirestoreHelper.asesor.getApellidos());
    }

    private void setData(Map<String, Object> preferences)
    {
        Boolean estado = Boolean.parseBoolean(String.valueOf(preferences.get("estado")));
        Boolean tipo =  Boolean.parseBoolean(String.valueOf(preferences.get("tipo")));
        String url = (String) preferences.get("url");
        String lugar2 = (String) preferences.get("lugar2");
        String h_inicio = (String) preferences.get("h_inicio");
        String h_final = (String) preferences.get("h_fin");
        String info = (String) preferences.get("info");
        int pos;
        if(estado)
        {
            switchEstado.setChecked(estado);

            if (tipo) {
                radioBAPresencial.setChecked(true);
                pos=Integer.parseInt(String.valueOf(preferences.get("lugar")));
                positionPlace = pos;
                spinner_lugares.setSelection(pos);
                if(pos==PropiertiesHelper.LUGARES.length-1)
                {
                    editTextText_otroLugar.setText(lugar2);
                }
            }
            else {
                radioButton_AOnline.setChecked(true);
                editText_URL.setText(url);
            }
            pos = Integer.parseInt(String.valueOf(preferences.get("materia")));
            positionSubject = pos;
            spinner_materias.setSelection(pos);
            editText_HoraInicio.setText(h_inicio);
            editText_HoraFinalizacion.setText(h_final);
            if (info != null) {
                editTextTextMultiLine.setText(info);
            } else {
                editTextTextMultiLine.setText("");
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adviser_app);
        ActionBar toolbar = getSupportActionBar();
        Objects.requireNonNull(toolbar).setElevation(0);
        setTitle("Menú Asesor");
        editTextText_otroLugar = findViewById(R.id.editTextText_otroLugar);
        imageView_perfil = findViewById(R.id.imageView_perfil);
        imageButton_edit_image = findViewById(R.id.imageButton_edit_image);
        spinner_materias = findViewById(R.id.spinner_Materia);
        spinner_lugares = findViewById(R.id.spinner_Lugar);
        radioGroup = findViewById(R.id.radioGroup);
        editText_URL = findViewById(R.id.editView_URL);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        switchEstado = findViewById(R.id.switch_Estado);
        cardView_ButtonPublicar = findViewById(R.id.cardView_ButtonPublicar);
        textView_Estado = findViewById(R.id.textView_Estado);
        textView_Nombre = findViewById(R.id.textView_Nombre);
        radioButton_AOnline = findViewById(R.id.radioButton_AOnline);
        radioBAPresencial = findViewById(R.id.radioButton_APresencial);

        editText_HoraInicio = findViewById(R.id.editText_HoraInicio);
        editText_HoraInicio.setInputType(InputType.TYPE_NULL);
        editText_HoraFinalizacion = findViewById(R.id.editText_HoraFin);
        editText_HoraFinalizacion.setInputType(InputType.TYPE_NULL);

        editText_HoraInicio.setOnClickListener(this);
        editText_HoraFinalizacion.setOnClickListener(this);
        cardView_ButtonPublicar.setOnClickListener(this);
        switchEstado.setOnClickListener(this);

        editText_URL.requestFocus();
        editTextFocusListener();
        radioButtonListener();

        ArrayAdapter<String> arrayAdapterLugares = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.LUGARES);
        ArrayAdapter<String> arrayAdapterMaterias = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);

        spinner_lugares.setAdapter(arrayAdapterLugares);
        spinner_materias.setAdapter(arrayAdapterMaterias);
        imageButton_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),GALLERY_INTENT);
            }
        });
        Bitmap placeholder = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.user);
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), placeholder);
        circularBitmapDrawable.setCircular(true);
        Glide.with(getApplicationContext())
                .load(FirestoreHelper.asesor.getuRI_image())
                .placeholder(circularBitmapDrawable)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageView_perfil);
        sharedPreferencesHelper = new SharedPreferencesHelper(MainAdviserActivityApp.this);

        helper = new DataBaseHelper(this, PropiertiesHelper.NOMBRE_BD , null, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow, menu);
        menu.removeItem(R.id.item_ActualizarLista);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.item_AcercaDe:
                Toast.makeText(MainAdviserActivityApp.this, getResources().getText(R.string.acerca_de)+"...", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.item_QuejasSugerencias:
                Toast.makeText(MainAdviserActivityApp.this,getResources().getText(R.string.quejasSugerencias)+"...", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ComplaintSuggestionsActivity.class);
                startActivity(intent);
                break;

            case R.id.item_CerrarSesion:
                Toast.makeText(MainAdviserActivityApp.this, getResources().getText(R.string.cerrarSesion)+"...", Toast.LENGTH_SHORT).show();
                ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                        "Nos vemos pronto... "+FirebaseAuthHelper.getCurrentUser(), true);
                dialog.show();
                firebaseAuthHelper.signout(dialog);
                FirestoreHelper.asesor=null;
                break;

            case R.id.item_Leer_QR:
                escanearQR();
                break;
            case R.id.item_Crear_QR:
                intent = new Intent(this, CreateCodeQRActivity.class);
                startActivity(intent);
                break;
            case R.id.item_EditarPerfil:
                showDialogEditProfile();
                break;
            case R.id.item_Crear_PDF_asesorados:

                break;
            case R.id.item_Crear_PDF_asesorias:
                intent = new Intent(this, AsesoradosActivity.class);
                startActivity(intent);
                break;
            case R.id.item_nuevo_semestre:
                showDialogNewSemester();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        int idView = view.getId();
        if(idView==R.id.editText_HoraInicio)
        {
            getHora(editText_HoraInicio);
        }
        else if(idView==R.id.editText_HoraFin)
        {
            getHora(editText_HoraFinalizacion);
        }
        else if(idView==R.id.cardView_ButtonPublicar)
        {


                boolean flag_radioButton = false;
                boolean flag_spinnerMateria = false;
                boolean flag_TimeStar=false;
                boolean flag_TimeEnd=false;
                boolean flag_otherPlace = false;

                editText_URL.setError(null);
                editText_HoraInicio.setError(null);
                editText_HoraFinalizacion.setError(null);
                editTextText_otroLugar.setError(null);


            if(radioBAPresencial.isChecked())
                {
                    if(spinner_lugares.getSelectedItemPosition()>0)
                    {
                        if(spinner_lugares.getSelectedItem().toString().equals("Otro(Especifique)"))
                        {
                            if(!editTextText_otroLugar.getText().toString().isEmpty()) {
                                flag_otherPlace=true;
                            }
                            else {
                                editTextText_otroLugar.setError("Es necesario llenar este campo.");
                            }
                        }
                        else {
                            flag_radioButton=true;
                        }
                    }
                    else {
                        Snackbar.make(view, "Seleccionar un lugar para la asesoría.", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else if(radioButton_AOnline.isChecked())
                {
                    if(!editText_URL.getText().toString().isEmpty())
                    {
                        if(new StringHelper().validateURL(editText_URL.getText().toString()))
                        {
                            flag_radioButton=true;
                        }
                        else
                        {
                            editText_URL.setError("El texto que se ingreso no es una URL. Ejemplo URL: https://integratec.my.webex.com/ ");
                        }
                    }
                    else
                    {
                        editText_URL.setError("Ingresar url para la asesoría online.");
                    }
                }

                if(spinner_materias.getSelectedItemPosition()>0)
                {
                    flag_spinnerMateria=true;
                }
                else
                {
                    Snackbar.make(view, "Seleccionar materia de asesoría.", Snackbar.LENGTH_SHORT).show();
                }

                if(!editText_HoraInicio.getText().toString().isEmpty())
                {
                    int horaInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(0,2));
                    String aux = editText_HoraInicio.getText().toString().substring(6,8);
                    if((((horaInicio>=1 && horaInicio<=8) || horaInicio==12) && aux.equals("pm")) || ((horaInicio>=8 && horaInicio<=11) && aux.equals("am")))
                    {

                        flag_TimeStar=true;
                    }
                    else
                    {
                        editText_HoraInicio.setError("La hora de inicio debe estar entre las 8:00am y 8:00pm");
                    }
                }
                else
                {
                    editText_HoraInicio.setError("Seleccionar hora de inicio.");
                }


                if(!editText_HoraFinalizacion.getText().toString().isEmpty())
                {
                    int horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0,2));
                    String aux = editText_HoraFinalizacion.getText().toString().substring(6,8);
                    if((((horaFin>=1 && horaFin<=8) || horaFin==12) && aux.equals("pm")) || ((horaFin>=8 && horaFin<=11) && aux.equals("am")))
                    {
                        flag_TimeEnd=true;
                    }
                    else
                    {
                        editText_HoraFinalizacion.setError("La hora de fin debe estar entre las 8:00am y 8:00pm");
                    }
                }
                else
                {
                    editText_HoraFinalizacion.setError("Seleccionar hora de finalización.");
                }



                if(((flag_radioButton || flag_otherPlace)) && flag_spinnerMateria && flag_TimeStar && flag_TimeEnd)
                {
                    if(getRangoValidoHoras())
                    {
                        if(sharedPreferencesHelper.hasData() || switchEstado.isChecked())
                        {
                            Map<String, Object> asesor = new HashMap<>();
                            if (radioBAPresencial.isChecked()) {
                                asesor.put("URL", "");
                                if (spinner_lugares.getSelectedItemPosition() == PropiertiesHelper.LUGARES.length - 1) {
                                    asesor.put("lugar", editTextText_otroLugar.getText().toString());
                                } else {
                                    asesor.put("lugar", spinner_lugares.getSelectedItem().toString());
                                }
                            } else {
                                asesor.put("URL", editText_URL.getText().toString());
                                asesor.put("lugar", "");

                            }

                            asesor.put("nombre", FirestoreHelper.asesor.getNombre() + " " + FirestoreHelper.asesor.getApellidos());
                            asesor.put("image_asesor", FirestoreHelper.asesor.getuRI_image());
                            asesor.put("materia", spinner_materias.getSelectedItem().toString());
                            asesor.put("h_inicio", editText_HoraInicio.getText().toString());
                            asesor.put("h_final", editText_HoraFinalizacion.getText().toString());
                            asesor.put("informacion", editTextTextMultiLine.getText().toString());
                            asesor.put("fecha", PropiertiesHelper.obtenerFecha().substring(0, 10));


                            //firebase
                            ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                                    switchEstado.isChecked() ? "Publicando asesoría..." : "Terminando asesoría..", true);
                            dialog.show();
                            firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, asesor, switchEstado.isChecked());
                            Toast.makeText(this, getResources().getText(R.string.publicando) + "...", Toast.LENGTH_SHORT).show();
                            //shared preferences
                            if (switchEstado.isChecked()) {
                                sharedPreferencesHelper.addPreferences(dataToSave());
                            } else {
                                clear();
                                sharedPreferencesHelper.deletePreferences();
                            }
                        }
                        else
                        {
                            Snackbar.make(cardView_ButtonPublicar.getRootView(),"Debes de poner estado en activo para crear una asesoría, actualmente no tienes ninguna.",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
        }
        else if(idView==R.id.switch_Estado)
        {
            if(switchEstado.isChecked())
            {
                textView_Estado.setText(R.string.activo);
            }
            else
            {
                textView_Estado.setText(R.string.inactivo);
            }
        }

    }
    private void editTextFocusListener()
    {
        editText_HoraInicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus)
                {
                    getHora(editText_HoraInicio);
                }
            }
        });
        editText_HoraFinalizacion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {

                if(focus)
                {
                    getHora(editText_HoraFinalizacion);
                }

            }
        });
    }
    private void radioButtonListener()
    {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radiobutton) {

                RadioButton checkedRadioButton = radioGroup.findViewById(radiobutton);
                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked)
                {
                    if(checkedRadioButton.getId()==R.id.radioButton_AOnline)
                    {
                        spinner_lugares.setVisibility(View.GONE);
                        editText_URL.setVisibility(View.VISIBLE);
                        editTextText_otroLugar.setVisibility(View.GONE);

                    }else if(checkedRadioButton.getId()==R.id.radioButton_APresencial)
                    {
                        editText_URL.setVisibility(View.GONE);
                        spinner_lugares.setVisibility(View.VISIBLE);
                        if(spinner_lugares.getSelectedItem().toString().equals("Otro(Especifique)"))
                        {
                            editTextText_otroLugar.setVisibility(View.VISIBLE);
                        }
                        spinner_lugares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_lugares.getItemIdAtPosition(position)!=PropiertiesHelper.LUGARES.length-1){
                                    editTextText_otroLugar.setVisibility(View.GONE);
                                }else{
                                    editTextText_otroLugar.setVisibility(View.VISIBLE);
                                }
                                positionPlace = position;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                }

            }
        });
        /* parent The AdapterView where the selection happened
         * view The view within the AdapterView that was clicked
         * position The position of the view in the adapter
         * id The row id of the item that is selected*/
        spinner_materias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                positionSubject = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        switchEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status)
            {
                if (status) {
                    textView_Estado.setText(R.string.activo);

                } else {
                    textView_Estado.setText(R.string.inactivo);
                }
            }
        });
    }

    private void getHora(final EditText view) {
        if (picker != null)
        {
            if(picker.isShowing())
            {
                picker.cancel();
            }
            picker=null;
        }
        final Calendar cldr = Calendar.getInstance();
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog


        TimePickerDialog.OnTimeSetListener timePicker = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hrs, int min) {
                Toast.makeText(MainAdviserActivityApp.this, hrs + ":" + min, Toast.LENGTH_SHORT).show();
                String aux="am";
                if(hrs>12)
                {
                    hrs=hrs-12;
                    aux="pm";
                }
                else if (hrs==12)
                {
                    aux="pm";
                }
                else if (hrs==0)
                {
                    hrs=12;
                }
                String horas = hrs < 10 ? "0" + hrs : hrs + "";
                String minutos = min < 10 ? "0" + min : min + "";
                view.setText(horas + ":" + minutos + " "+aux);
            }
        };
        if (picker == null) {
            picker = new TimePickerDialog(MainAdviserActivityApp.this, timePicker, hour, minutes, false);
        }
        picker.show();
    }
    private boolean getRangoValidoHoras()
    {
       boolean  flag_TimeValid=false;

        if((editText_HoraInicio.getText().toString().substring(6).equals("pm") && editText_HoraFinalizacion.getText().toString().substring(6).equals("pm")) || (editText_HoraInicio.getText().toString().substring(6).equals("am") && editText_HoraFinalizacion.getText().toString().substring(6).equals("am") ))
        {
            int horaInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(0,2));
            int horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0,2));
            Log.e("if",horaInicio+" "+horaFin);
            //evalua horas para calcular que si sea una hora min
            if(horaInicio<horaFin)
            {

                //evalua min
                horaInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(3,5));
                horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(3,5));
                if((horaInicio-horaFin)<=0)
                {
                    flag_TimeValid = true;
                }
                else
                {
                    Toast.makeText(MainAdviserActivityApp.this,"Las asesorías deben durar 1 hora.", Toast.LENGTH_SHORT).show();
                    editText_HoraInicio.setError("Hora inválida.");
                    editText_HoraFinalizacion.setError("Hora inválida.");
                    editText_HoraInicio.getText().clear();
                    editText_HoraFinalizacion.getText().clear();
                }
            }
            else if(horaInicio==horaFin)
            {
                Toast.makeText(MainAdviserActivityApp.this,"Las asesorías deben durar 1 hora.", Toast.LENGTH_SHORT).show();
                editText_HoraInicio.setError("Hora inválida.");
                editText_HoraFinalizacion.setError("Hora inválida.");
                editText_HoraInicio.getText().clear();
                editText_HoraFinalizacion.getText().clear();
            }
            else
            {
                Toast.makeText(MainAdviserActivityApp.this,"Hora de inicio es mayor que hora final.", Toast.LENGTH_SHORT).show();
                editText_HoraInicio.setError("Hora inválida.");
                editText_HoraFinalizacion.setError("Hora inválida.");
                editText_HoraInicio.getText().clear();
                editText_HoraFinalizacion.getText().clear();
            }
        }
        else
        {
            Log.e("else","soy el else prin");
            Toast.makeText(MainAdviserActivityApp.this,"Hora de inicio es mayor que hora final", Toast.LENGTH_SHORT).show();
            editText_HoraInicio.setError("Hora invalida");
            editText_HoraFinalizacion.setError("Hora invalida");
            editText_HoraInicio.getText().clear();
            editText_HoraFinalizacion.getText().clear();
        }
        return flag_TimeValid;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT)
        {
            try
            {
                Uri uri = Objects.requireNonNull(data).getData();
                imagen = ImagesHelper.from(getApplicationContext(),uri);
                imagen= new Compressor(getApplicationContext()).compressToFile(imagen);

                Glide.with(getApplicationContext())
                        .load(imagen)
                        .fitCenter()
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView_perfil);

                firebaseStorageHelper.deleteImage(FirestoreHelper.asesor.getUid());
                firebaseStorageHelper.addImage(FirestoreHelper.asesor.getUid(),Uri.fromFile(imagen));


            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        else
        {
            result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result!=null)
            {
                if(result.getContents() != null)
                {
                    Toast.makeText(MainAdviserActivityApp.this,result.getContents(),Toast.LENGTH_SHORT).show();

                    String values = result.getContents();

                    showDialogRegistrar(values);
                }
                else
                {
                    Toast.makeText(MainAdviserActivityApp.this,"Cancelaste escaneo.",Toast.LENGTH_SHORT).show();
                }
        }
        }
    }

    private void showDialogRegistrar(String values) {
        final String array[] = values.split("_");

        if(array.length == 7){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_registar_alumno, null);
            builder.setView(view)
                    .setTitle("Registrar asesorado");

            final AlertDialog dialogRegistrar =builder.create();
            dialogRegistrar.setCancelable(false);
            dialogRegistrar.show();

            final TextView textView_NumeroControl = dialogRegistrar.findViewById(R.id.textView_NumeroControl);
            final TextView textView_Nombre = dialogRegistrar.findViewById(R.id.textView_Nombre);
            final TextView textView_Carrera = dialogRegistrar.findViewById(R.id.textView_Carrera);
            final TextView textView_Semestre = dialogRegistrar.findViewById(R.id.textView_Semestre);
            final TextView textView_Materia = dialogRegistrar.findViewById(R.id.textView_Materia);
            final TextView textView_Tema = dialogRegistrar.findViewById(R.id.textView_Tema);
            final TextView textView_Fecha = dialogRegistrar.findViewById(R.id.textView_Fecha);

            textView_NumeroControl.setText("Número de contro:"+array[0]);
            textView_Nombre.setText("Nombre completo:"+array[1]);
            textView_Carrera.setText("Carrera:"+array[2]);
            textView_Materia.setText("Materia:"+array[3]);
            textView_Tema.setText("Tema:"+array[4]);
            textView_Semestre.setText("Semestre:"+array[5]);
            textView_Fecha.setText("Fecha:"+array[6]);

            CardView cardView_Registrar = dialogRegistrar.findViewById(R.id.cardviewRegistrar);
            CardView cardView_Cancel = dialogRegistrar.findViewById(R.id.cardviewCancel);

            cardView_Registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registrar(array);
                    dialogRegistrar.dismiss();
                }
            });

            cardView_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogRegistrar.dismiss();
                }
            });

        }else {
            Toast.makeText(this, "El código QR no contiene todos los parametros necesarios para poder ser registadros con exito", Toast.LENGTH_LONG).show();
        }
    }

    private void registrar(String[] array) {
        SQLiteDatabase db =  helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PropiertiesHelper.CAMPO_NCONTROL, array[0]);
        values.put(PropiertiesHelper.CAMPO_NOMBRE, array[1]);
        values.put(PropiertiesHelper.CAMPO_CARRERA, array[2]);
        values.put(PropiertiesHelper.CAMPO_MATERIA, array[3]);
        values.put(PropiertiesHelper.CAMPO_TEMA, array[4]);
        values.put(PropiertiesHelper.CAMPO_SEMESTRE, array[5]);
        values.put(PropiertiesHelper.CAMPO_FECHA, array[6]);

        Long idR = db.insert(PropiertiesHelper.NOMBRE_TABLA, "id", values);
        Toast.makeText(this, "Alumno registrado con exito", Toast.LENGTH_LONG).show();
    }

    private void escanearQR()
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainAdviserActivityApp.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Escanea código QR");
        intentIntegrator.setOrientationLocked(false);//orientacion
        intentIntegrator.setCameraId(0);//camara
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    private void showDialogEditProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_edit_profile, null);
        builder.setView(view)
                .setTitle("Editar perfil");

        final AlertDialog dialogEditProfile =builder.create();
        dialogEditProfile.setCancelable(false);
        dialogEditProfile.show();

        final EditText editText_Name = dialogEditProfile.findViewById(R.id.editText_Name);
        final EditText editText_LastNames = dialogEditProfile.findViewById(R.id.editText_LastNames);
        final Spinner spinner_career = dialogEditProfile.findViewById(R.id.textView_email);
        CardView cardView_ButtonUpdate = dialogEditProfile.findViewById(R.id.cardView_Button_Delete);
        CardView cardView_ButtonCancel = dialogEditProfile.findViewById(R.id.cardView_ButtonPublicar);

        ArrayAdapter<String> arrayAdapterCareer = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        spinner_career.setAdapter(arrayAdapterCareer);

        editText_Name.setText(FirestoreHelper.asesor.getNombre());
        editText_LastNames.setText(FirestoreHelper.asesor.getApellidos());
        spinner_career.setSelection(retornaCarrera());

        cardView_ButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag_Name=false;
                boolean flag_LastNames=false;
                boolean flag_Career=false;

                if(!editText_Name.getText().toString().isEmpty()) {
                    flag_Name=true;
                }
                else {
                    editText_Name.setError("Nombre requerido");
                }

                if(!editText_LastNames.getText().toString().isEmpty()) {
                    flag_LastNames=true;
                }
                else {
                    editText_LastNames.setError("Apellidos requeridos");
                }

                if(spinner_career.getSelectedItemPosition()!=0) {
                    flag_Career=true;
                }
                else {
                    Snackbar.make(view, "Seleccionar una carrera", Snackbar.LENGTH_SHORT).show();
                }

                if(flag_Name && flag_LastNames && flag_Career)
                {
                    ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                            "Actualizando..", true);
                    dialog.show();
                    firestoreHelper.updateDataAsesor(editText_Name.getText().toString(), editText_LastNames.getText().toString(),
                            String.valueOf(spinner_career.getSelectedItem()),dialog,MainAdviserActivityApp.this);

                   // Toast.makeText(MainAdviserActivityApp.this, "Actualizando datos...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardView_ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditProfile.dismiss();
            }
        });
    }
    public void showDialogNewSemester()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_delete_data, null);
        builder.setView(view)
                .setTitle(R.string.nuevo_semestre);

        final AlertDialog dialogEditProfile =builder.create();
        dialogEditProfile.setCancelable(false);
        dialogEditProfile.show();


        CardView cardView_ButtonDelete_PDF= dialogEditProfile.findViewById(R.id.cardView_Button_Delete_PDF);
        CardView cardView_ButtonDelete= dialogEditProfile.findViewById(R.id.cardView_Button_Delete);
        CardView cardView_ButtonCancel = dialogEditProfile.findViewById(R.id.cardView_ButtonPublicar);

        cardView_ButtonDelete_PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreHelper.deleteAsesoriasData();
            }
        });
        cardView_ButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreHelper.deleteAsesoriasData();
            }
        });

        cardView_ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditProfile.dismiss();
            }
        });


    }

    @Override
    public void status(String message)
    {
        if(message.equals("Datos actualizados"))
        {
           textView_Nombre.setText(FirestoreHelper.asesor.getNombre() + " "+ FirestoreHelper.asesor.getApellidos());
        }

        Toast.makeText(MainAdviserActivityApp.this,message,Toast.LENGTH_SHORT).show();
    }


    public int retornaCarrera(){

        if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería en Sistemas Computacionales")){
            return 1;
        }else if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería en Administración")){
            return 2;
        }else if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería en Mecatrónica")){
            return 3;
        }else if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería Industrial")){
            return 4;
        }else if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería en Mecánica")){
            return 5;
        }else if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería en Industrias Alimentarias")){
            return 6;
        }else if(FirestoreHelper.asesor.getCarrera().equals("Ingeniería Civil")){
            return 7;
        }
        return 0;
    }
    public Map<String,Object> dataToSave()
    {
        Map<String,Object> data = new HashMap<>();
        data.put("estado",switchEstado.isChecked());
        data.put("tipo",radioBAPresencial.isChecked());
        if(radioBAPresencial.isChecked()) {
            if (spinner_lugares.getSelectedItemPosition() == PropiertiesHelper.LUGARES.length - 1) {
                data.put("lugar2", editTextText_otroLugar.getText().toString());
            } else {
                data.put("lugar2", "");
            }
            data.put("lugar", positionPlace);
            data.put("url",  "");
        }
        else
        {
            data.put("url",  editText_URL.getText().toString());
            data.put("lugar", -1);
            data.put("lugar2", "");
        }
        data.put("materia",positionSubject);
        data.put("h_inicio",editText_HoraInicio.getText().toString());
        data.put("h_fin",editText_HoraFinalizacion.getText().toString());
        data.put("info",editTextTextMultiLine.getText().toString());

        return data;
    }
    private void clear()
    {

        radioBAPresencial.setChecked(true);
        spinner_lugares.setSelection(0);
        spinner_materias.setSelection(0);
        editText_HoraInicio.getText().clear();
        editText_HoraFinalizacion.getText().clear();
        editTextTextMultiLine.getText().clear();
        editTextText_otroLugar.getText().clear();
        editText_URL.getText().clear();

    }
}
