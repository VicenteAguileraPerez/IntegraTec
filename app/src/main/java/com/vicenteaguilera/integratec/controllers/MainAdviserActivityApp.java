package com.vicenteaguilera.integratec.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.documentfile.provider.DocumentFile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import harmony.java.awt.Color;

import com.vicenteaguilera.integratec.AsesoradosActivity;
import com.vicenteaguilera.integratec.CreateCodeQRActivity;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;
import com.vicenteaguilera.integratec.helpers.CaptureActivityPortrait;
import com.vicenteaguilera.integratec.helpers.flipper.DocumentFileCompat;
import com.vicenteaguilera.integratec.helpers.flipper.OperationFailedException;
import com.vicenteaguilera.integratec.helpers.flipper.Root;
import com.vicenteaguilera.integratec.helpers.flipper.StorageManagerCompat;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseStorageHelper;
import com.vicenteaguilera.integratec.helpers.services.FirestoreAsesorado;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
//import com.vicenteaguilera.integratec.helpers.utility.WaterMark;
import com.vicenteaguilera.integratec.helpers.utility.WaterMark;
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ImagesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.SharedPreferencesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorados;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorias;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StringHelper;
import com.vicenteaguilera.integratec.models.Alumno;
import com.vicenteaguilera.integratec.models.Asesorado;
import com.vicenteaguilera.integratec.models.Asesoria;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import id.zelory.compressor.Compressor;



public class MainAdviserActivityApp extends AppCompatActivity implements View.OnClickListener, Status, ListaAsesorias, ListaAsesorados {
    private TextInputLayout editTextText_otroLugar;
    private final static int GALLERY_INTENT = 1;
    private final int REQUEST_CODE_ASK_PERMISSION = 111;
    private File imagen=null;
    private static boolean status=true;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private TimePickerDialog picker=null;
    private TextInputLayout spinner_materias;//Se cambio por TextInputLayout
    private TextInputLayout spinner_lugares;//Se cambio por TextInputLayout
    private RadioGroup radioGroup;
    private TextInputLayout editText_URL;
    private TextInputEditText editText_HoraInicio;
    private TextInputEditText editText_HoraFinalizacion;
    private MaterialButton button_Publicar;
    private MaterialButton button_Terminar;
    private ImageButton imageButton_edit_image;
    private TextInputLayout editTextTextMultiLine;
    private TextView textView_Nombre;
    private ImageView imageView_perfil;
    private RadioButton radioButton_AOnline;
    private RadioButton radioBAPresencial;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();
    private FirestoreHelper firestoreHelper = new FirestoreHelper();
    private FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper();
    private InternetHelper internetHelper = new InternetHelper();
    //private int positionPlace=-1;
    //private int positionSubject=-1;
    private List<Asesoria> asesoriaList;
    private List<Asesorado> asesoradoList;
    private IntentResult result= null;
    private StorageManagerCompat manager;
    private final static String NOMBRE_DIRECTORIO = "PDFsIntegraTec";
    private final static String NOMBRE_DOCUMENTO = "PDF_Asesorias";
    private final static String NOMBRE_DOCUMENTO2 = "PDF_Asesorados";
    private final static String ETIQUETA_ERROR = "ERROR";
    private boolean flagPDFAsesorias=false;
    private boolean flagPDFAsesorados=false;
    private boolean flagDeleteData1=false;
    private boolean flagDeleteData2=false;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private ButtonHelper buttonHelper = new ButtonHelper();
    private ArrayAdapter<String> arrayAdapterLugares;
    private ArrayAdapter<String> arrayAdapterMaterias;
    private FirestoreAsesorado firestoreAsesorado = new FirestoreAsesorado();

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiReceiver);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver,intentFilter);

        //cancelarAsesoriaDespuesDeHora();

        clear();
        //sharedPreferencesHelper.deletePreferences();
        setData(sharedPreferencesHelper.getPreferences());
        firebaseAuthHelper.setContext(MainAdviserActivityApp.this);
        firebaseAuthHelper.setOnStatusListener(this);
        firebaseStorageHelper.setStatusListener(this);
        firestoreHelper.listenGetAsesoriasData(this);
        firestoreAsesorado.readAsesorados(this, FirestoreHelper.asesor.getUid());
        textView_Nombre.setText(FirestoreHelper.asesor.getNombre() +  " " + FirestoreHelper.asesor.getApellidos());
    }

    private void cancelarAsesoriaDespuesDeHora()
    {
        if(internetHelper.timeAutomatically(MainAdviserActivityApp.this.getContentResolver()))
        {
            Calendar cldr = Calendar.getInstance();
            if(cldr.get(Calendar.DAY_OF_WEEK)>=Calendar.MONDAY && cldr.get(Calendar.DAY_OF_WEEK)<=Calendar.FRIDAY)
            {
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                Log.e("Hour: ", hour+"");
                if(hour>=8 && hour<20) {
                    enableEditTextHoras();
                  button_Publicar.setEnabled(true);
                }
                else {
                    if(sharedPreferencesHelper.hasData())
                    {
                        ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "", "Terminando asesoría...", true);
                        dialog.show();
                        firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, returnAsesoria(), false);
                        Toast.makeText(MainAdviserActivityApp.this,"Terminamos la asesoría porque ya pasa de las horas hábiles de asesorias.", Toast.LENGTH_SHORT).show();
                        new AlertDialogPersonalized().alertDialogInformacion("No puede crear asesorías hasta las horas hábiles de 8:00 am a 8:00 pm y terminamos la asesoría publicada.",MainAdviserActivityApp.this);
                    }
                    else
                    {
                        new AlertDialogPersonalized().alertDialogInformacion("No puede crear asesorías hasta las horas hábiles de 8:00 am a 8:00 pm",MainAdviserActivityApp.this);
                    }

                    disableEditTextHoras();
                    button_Publicar.setEnabled(false);
                    sharedPreferencesHelper.deletePreferences();
                    Log.e("EditText H_Inicio is: ", editText_HoraInicio.isEnabled()+"");
                    Log.e("EditText H_Fin is: ", editText_HoraFinalizacion.isEnabled()+"");
                    clear();
                }
            }
            else
            {
                if(sharedPreferencesHelper.hasData())
                {
                    ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "", "Terminando asesoría...", true);
                    dialog.show();
                    firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, returnAsesoria(), false);
                    Toast.makeText(MainAdviserActivityApp.this,"Terminamos la asesoría porque ya pasa de las horas hábiles de asesorias.", Toast.LENGTH_SHORT).show();
                    new AlertDialogPersonalized().alertDialogInformacion("No puede crear asesorías solo de Lunes a Viernes y terminamos la asesoría publicada.",MainAdviserActivityApp.this);
                }
                else
                {
                    new AlertDialogPersonalized().alertDialogInformacion("No puede crear asesorías, solo de Lunes a Viernes",MainAdviserActivityApp.this);
                }

                disableEditTextHoras();
                button_Publicar.setEnabled(false);
                sharedPreferencesHelper.deletePreferences();
                Log.e("EditText H_Inicio is: ", editText_HoraInicio.isEnabled()+"");
                Log.e("EditText H_Fin is: ", editText_HoraFinalizacion.isEnabled()+"");
                clear();
            }

        }
        else {
            Intent intent = new Intent(MainAdviserActivityApp.this,MainAppActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            Toast.makeText(MainAdviserActivityApp.this,"Error no puede continuar hasta que habilite la hora automática en su dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableEditTextHoras()
    {
        editText_HoraInicio.setEnabled(false);
        editText_HoraInicio.setFocusable(false);
        editText_HoraInicio.setActivated(false);
        editText_HoraInicio.setClickable(false);
        editText_HoraInicio.setInputType(InputType.TYPE_NULL);

        editText_HoraFinalizacion.setEnabled(false);
        editText_HoraFinalizacion.setFocusable(false);
        editText_HoraFinalizacion.setActivated(false);
        editText_HoraFinalizacion.setClickable(false);
        editText_HoraFinalizacion.setInputType(InputType.TYPE_NULL);
    }
    private void enableEditTextHoras()
    {
        editText_HoraInicio.setEnabled(true);
        editText_HoraInicio.setFocusable(true);
        editText_HoraInicio.setActivated(true);
        editText_HoraInicio.setClickable(true);
        editText_HoraInicio.setInputType(InputType.TYPE_CLASS_TEXT);

        editText_HoraFinalizacion.setEnabled(true);
        editText_HoraFinalizacion.setFocusable(true);
        editText_HoraFinalizacion.setActivated(true);
        editText_HoraFinalizacion.setClickable(true);
        editText_HoraFinalizacion.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void setData(Map<String, Object> preferences)
    {
        if(preferences!=null)
        {
            status=false;
            disableEditTextHoras();
            button_Publicar.setText("Actualizar asesoría");
            Boolean tipo =  Boolean.parseBoolean(String.valueOf(preferences.get("tipo")));
            String url = (String) preferences.get("url");
            String h_inicio = (String) preferences.get("h_inicio");
            String h_final = (String) preferences.get("h_fin");
            String info = (String) preferences.get("info");

            if (tipo) {
                radioBAPresencial.setChecked(true);
                spinner_lugares.getEditText().setText((String)preferences.get("lugar"));
            }
            else {
                radioButton_AOnline.setChecked(true);
                editText_URL.getEditText().setText(url);
            }
            spinner_materias.getEditText().setText((String)preferences.get("materia"));

            editText_HoraInicio.setText(h_inicio);
            editText_HoraFinalizacion.setText(h_final);
            if (info != null) {
                editTextTextMultiLine.getEditText().setText(info);
            } else {
                editTextTextMultiLine.getEditText().setText("");
            }
        }
        else
        {
            status=true;
            enableEditTextHoras();
            button_Publicar.setText("Publicar asesoría");
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
        textView_Nombre = findViewById(R.id.textView_Nombre);
        radioButton_AOnline = findViewById(R.id.radioButton_AOnline);
        radioBAPresencial = findViewById(R.id.radioButton_APresencial);
        button_Publicar = findViewById(R.id.button_Publicar);
        button_Terminar = findViewById(R.id.button_Terminar);

        editText_HoraInicio = findViewById(R.id.editText_HoraInicio);
        editText_HoraInicio.setLongClickable(false);

        editText_HoraFinalizacion = findViewById(R.id.editText_HoraFin);
        editText_HoraFinalizacion.setLongClickable(false);

        editText_HoraInicio.setOnClickListener(this);
        editText_HoraFinalizacion.setOnClickListener(this);
        button_Publicar.setOnClickListener(this);
        button_Terminar.setOnClickListener(this);

        editText_URL.requestFocus();
        editTextFocusListener();
        radioButtonListener();

        editTextTextMultiLine.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        arrayAdapterLugares = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.LUGARES);
        arrayAdapterMaterias = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);

        ((AutoCompleteTextView)spinner_lugares.getEditText()).setAdapter(arrayAdapterLugares);
        ((AutoCompleteTextView)spinner_materias.getEditText()).setAdapter(arrayAdapterMaterias);

        imageButton_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(MainAdviserActivityApp.this);
                alertDialogBuilder.setTitle("Opciones");
                alertDialogBuilder.setMessage("Elige una opción a realizar, sino da click fuera del recuadro.");
                alertDialogBuilder.setPositiveButton("Modificar foto",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface alertDialog, int i)
                            {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),GALLERY_INTENT);
                                alertDialog.cancel();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Eliminar foto actual", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface alertDialog, int i)
                    {
                        if(!FirestoreHelper.asesor.getuRI_image().equals(""))
                        {
                            firebaseStorageHelper.deleteImage(FirestoreHelper.asesor.getUid());
                            setImage("");
                        }
                        else
                        {
                            status("No tienes ninguna foto agregada al sistema.");
                        }
                        alertDialog.cancel();
                    }
                });
                alertDialogBuilder.show();
                     }
        });
        setImage(FirestoreHelper.asesor.getuRI_image());
        sharedPreferencesHelper = new SharedPreferencesHelper(MainAdviserActivityApp.this);

        ((AutoCompleteTextView) spinner_lugares.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_lugares.getEditText().getText().toString().equals("Otro(Especifique)")){
                   editTextText_otroLugar.setVisibility(View.VISIBLE);
                }else {
                    editTextText_otroLugar.setVisibility(View.GONE);
                }
            }
        });
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
            case R.id.item_Horarios:
                intent = new Intent(this, HorarioActivity.class);
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

            case R.id.item_Asesorados:
                intent = new Intent(this, AsesoradosActivity.class);
                startActivity(intent);
                break;

            case R.id.item_EditarPerfil:
                showDialogEditProfile();
                break;

            case R.id.item_Crear_PDF_asesorados:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    flagPDFAsesorados=true;
                    flagPDFAsesorias=false;
                    crearPdfAndroidQ();
                }
                else{
                    flagPDFAsesorados=true;
                    flagPDFAsesorias=false;
                    crearPdf();
                }
                break;

            case R.id.item_Crear_PDF_asesorias:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    flagPDFAsesorados=false;
                    flagPDFAsesorias=true;
                    crearPdfAndroidQ();
                }
                else{
                    flagPDFAsesorados=false;
                    flagPDFAsesorias=true;
                    crearPdf();
                }
                /*intent = new Intent(this, AsesoradosActivity.class);
                startActivity(intent);*/
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
        if(idView==editText_HoraInicio.getId())
        {
            getHora(editText_HoraInicio);
        }
        else if(idView== editText_HoraFinalizacion.getId())
        {
            getHora(editText_HoraFinalizacion);
        }
        else if(idView==R.id.button_Publicar)
        {
           if(status)
           {
               publicarAsesoria(view);
           }
           else
           {
               actualizarAsesoría(view);
           }
        }
        else if(idView==R.id.button_Terminar)
        {
            if(sharedPreferencesHelper.hasData())
            {
                if(internetHelper.timeAutomatically(MainAdviserActivityApp.this.getContentResolver()))
                {
                    final Calendar cldr = Calendar.getInstance();
                    final int hour = cldr.get(Calendar.HOUR_OF_DAY);
                    int minutes = cldr.get(Calendar.MINUTE);
                    int horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0, 2));
                    int minfin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(3, 5));
                    boolean isTarde = editText_HoraFinalizacion.getText().toString().substring(6).equals("pm");
                    if(isTarde)
                    {
                        if(horaFin!=12)
                        {
                            horaFin+=12;
                        }

                        if((horaFin==hour && minutes-minfin>=0) || hour>horaFin)
                        {
                            ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                                    "Terminando asesoría..", true);
                            dialog.show();
                            firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, returnAsesoria(), false);
                            clear();
                            sharedPreferencesHelper.deletePreferences();

                            button_Publicar.setText("Publicar asesoría");
                            enableEditTextHoras();
                            status = true;
                        }
                        else
                        {
                            Toast.makeText(MainAdviserActivityApp.this,"Falta tiempo para terminar la asesoría", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if((horaFin==hour && minutes-minfin>=0) || hour>horaFin)
                        {
                            ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                                    "Terminando asesoría..", true);
                            dialog.show();
                            firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, returnAsesoria(), false);
                            clear();
                            sharedPreferencesHelper.deletePreferences();

                            button_Publicar.setText("Publicar asesoría");
                            enableEditTextHoras();
                            status = true;
                        }
                        else
                        {
                            Toast.makeText(MainAdviserActivityApp.this,"Falta tiempo para terminar la asesoría", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {
                    Toast.makeText(MainAdviserActivityApp.this,"Error no puede continuar hasta que habilite la hora automática en su dispositivo", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Snackbar.make(view,"Debes de crear una asesoría antes de eliminarla",Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void actualizarAsesoría(View view)
    {
        boolean flag_radioButton = false;
        boolean flag_spinnerMateria = false;
        boolean flag_otherPlace = false;

        editText_URL.setError(null);
        editText_HoraInicio.setError(null);
        editText_HoraFinalizacion.setError(null);
        editTextText_otroLugar.setError(null);

        if(radioBAPresencial.isChecked())
        {
            if(!spinner_lugares.getEditText().getText().toString().isEmpty())
            {
                if(spinner_lugares.getEditText().getText().toString().equals("Otro(Especifique)"))
                {
                    if(!editTextText_otroLugar.getEditText().getText().toString().isEmpty()) {
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
            if(!editText_URL.getEditText().getText().toString().isEmpty())
            {
                if(new StringHelper().validateURL(editText_URL.getEditText().getText().toString()))
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

        if(!spinner_materias.getEditText().getText().toString().isEmpty())
        {
            flag_spinnerMateria=true;
        }
        else
        {
            Snackbar.make(view, "Seleccionar materia de asesoría.", Snackbar.LENGTH_SHORT).show();
        }
        if(((flag_radioButton || flag_otherPlace)) && flag_spinnerMateria )
        {
                //firebase
                ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                        "Actualizando asesoría..." , true);
                dialog.show();
                firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, returnAsesoria(), true);

                //shared preferences
                sharedPreferencesHelper.addPreferences(dataToSave());
        }
    }

    private void publicarAsesoria(View view)
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
                if(!spinner_lugares.getEditText().getText().toString().isEmpty())
                {
                    if(spinner_lugares.getEditText().getText().toString().equals("Otro(Especifique)"))
                    {
                        if(!editTextText_otroLugar.getEditText().getText().toString().isEmpty()) {
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
                if(!editText_URL.getEditText().getText().toString().isEmpty())
                {
                    if(new StringHelper().validateURL(editText_URL.getEditText().getText().toString()))
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

            if(!spinner_materias.getEditText().getText().toString().isEmpty())
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
                if((((horaInicio>=1 && horaInicio<8) || horaInicio==12) && aux.equals("pm")) || ((horaInicio>=8 && horaInicio<=11) && aux.equals("am")))
                {

                    flag_TimeStar=true;
                }
                else
                {
                    editText_HoraInicio.setError("La hora de inicio debe estar entre las 8:00am y 8:00pm");
                    Toast.makeText(MainAdviserActivityApp.this, "La hora de inicio debe estar entre las 8:00am y 8:00pm", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                editText_HoraInicio.setError("Seleccionar hora de inicio.");
            }


            if(!editText_HoraFinalizacion.getText().toString().isEmpty())
            {
                int horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0,2));
                String minFin = editText_HoraFinalizacion.getText().toString().substring(3,5);
                String aux = editText_HoraFinalizacion.getText().toString().substring(6,8);
                if(((((horaFin>=1 && horaFin<8) || (horaFin>=1 && horaFin<=8 && minFin.equals("00"))) || horaFin==12) && aux.equals("pm")) || ((horaFin>=8 && horaFin<=11) && aux.equals("am")))
                {
                    flag_TimeEnd=true;
                }
                else
                {
                    editText_HoraFinalizacion.setError("La hora de fin debe estar entre las 8:00am y 8:00pm");
                    Toast.makeText(MainAdviserActivityApp.this, "La hora de fin debe estar entre las 8:00am y 8:00pm", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                editText_HoraFinalizacion.setError("Seleccionar hora de finalización.");
            }

            //if(internetHelper.timeAutomatically(MainAdviserActivityApp.this.getContentResolver()))
             //{
                    if(((flag_radioButton || flag_otherPlace)) && flag_spinnerMateria && flag_TimeStar && flag_TimeEnd)
                    {
                            if (getRangoValidoHoras()) {

                                //firebase
                                ProgressDialog dialog = ProgressDialog.show(MainAdviserActivityApp.this, "",
                                        "Actualizando asesoría...", true);
                                dialog.show();
                                firestoreHelper.registerDataAsesoriaPublicaToFirestore(FirestoreHelper.asesor.getUid(), this, dialog, returnAsesoria(), true);
                                status = false;
                                //shared preferences
                                sharedPreferencesHelper.addPreferences(dataToSave());


                                button_Publicar.setText("Actualizar asesoría");
                                new AlertDialogPersonalized().alertDialogInformacion("Para actualizar su asesoría, se le informa que no podrá cambiar la hora de inicio ni de fin por seguridad y para proporcionar servicios se asesoría reales para los asesorados y evitar datos falso.\n" +
                                        "Gracias por su compresión.", MainAdviserActivityApp.this);
                                disableEditTextHoras();
                            }
                    }
            /*}
            else
            {
                Toast.makeText(MainAdviserActivityApp.this,"Error no puede continuar hasta que habilite la hora automática en su dispositivo", Toast.LENGTH_SHORT).show();
            }*/
    }

    private Map<String, Object> returnAsesoria()
    {
        Map<String, Object> asesor = new HashMap<>();
        if (radioBAPresencial.isChecked()) {
            asesor.put("URL", "");
            if (spinner_lugares.getEditText().getText().toString().equals("Otro(Especifique)")) {
                asesor.put("lugar", editTextText_otroLugar.getEditText().getText().toString());
            } else {
                asesor.put("lugar", spinner_lugares.getEditText().getText().toString());
            }
        } else {
            asesor.put("URL", editText_URL.getEditText().getText().toString());
            asesor.put("lugar", "");

        }
        asesor.put("nombre", FirestoreHelper.asesor.getNombre() + " " + FirestoreHelper.asesor.getApellidos());
        asesor.put("image_asesor", FirestoreHelper.asesor.getuRI_image());
        asesor.put("materia", spinner_materias.getEditText().getText().toString());
        asesor.put("h_inicio", editText_HoraInicio.getText().toString());
        asesor.put("h_final", editText_HoraFinalizacion.getText().toString());
        asesor.put("informacion", editTextTextMultiLine.getEditText().getText().toString());
        String fecha = PropiertiesHelper.obtenerFecha().substring(0, 10);
        String f[] = fecha.split("-");
        asesor.put("fecha", f[2]+"-"+f[1]+"-"+f[0]);
        Log.e("Fecha: ", f[2]+"-"+f[1]+"-"+f[0]);
        return asesor;
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

                    }else if(checkedRadioButton.getId()==R.id.radioButton_APresencial) {
                        editText_URL.setVisibility(View.GONE);
                        spinner_lugares.setVisibility(View.VISIBLE);
                        if (spinner_lugares.getEditText().getText().toString().equals("Otro(Especifique)")) {
                            editTextText_otroLugar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void getHora(final EditText editText) {
        final Calendar cldr = Calendar.getInstance();
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);

        MaterialTimePicker.Builder builder_time = new MaterialTimePicker.Builder();
        builder_time.setHour(hour);
        builder_time.setMinute(minutes);
        builder_time.setTimeFormat(TimeFormat.CLOCK_12H);
        final MaterialTimePicker materialTimePicker = builder_time.build();
        materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hrs = materialTimePicker.getHour();
                int min = materialTimePicker.getMinute();
                String aux = "am";
                if (hrs > 12) {
                    hrs = hrs - 12;
                    aux = "pm";
                } else if (hrs == 12) {
                    aux = "pm";
                } else if (hrs == 0) {
                    hrs = 12;
                }
                String horas = hrs < 10 ? "0" + hrs : hrs + "";
                String minutos = min < 10 ? "0" + min : min + "";
                editText.setText(horas + ":" + minutos + " " + aux);
            }
        });

        materialTimePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setError("Campo requerido");
            }
        });
    }

    private boolean getRangoValidoHoras()
    {
        boolean  flag_TimeValid=false;

        final Calendar cldr = Calendar.getInstance();
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);

        int horaInicio;
        int horaFin;
        int minutesInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(3,5));
        int minutesFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(3,5));

        if(editText_HoraInicio.getText().toString().substring(6).equals("pm")) {
            if(editText_HoraInicio.getText().toString().substring(0,2).equals("12")) {
                horaInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(0,2));
            }
            else {
                horaInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(0,2))+12;
            }
        }
        else {
            horaInicio = Integer.parseInt(editText_HoraInicio.getText().toString().substring(0,2));
        }

        if(editText_HoraFinalizacion.getText().toString().substring(6).equals("pm")) {
            if(editText_HoraFinalizacion.getText().toString().substring(0,2).equals("12")) {
                horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0,2));
            }
            else {
                horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0,2))+12;
            }
        }
        else {
            horaFin = Integer.parseInt(editText_HoraFinalizacion.getText().toString().substring(0,2));
        }

        if((hour==horaInicio && (minutes==minutesInicio || (minutes-minutesInicio<=5 && minutes-minutesInicio>0)))
                || (hour==horaInicio+1 && (((60-minutesInicio)+minutes)>0 && ((60-minutesInicio)+minutes)<=5)) ) {

            if((editText_HoraInicio.getText().toString().substring(6).equals("am") && editText_HoraFinalizacion.getText().toString().substring(6).equals("am") || (editText_HoraInicio.getText().toString().substring(6).equals("pm") && editText_HoraFinalizacion.getText().toString().substring(6).equals("pm")) || (editText_HoraInicio.getText().toString().substring(6).equals("am") && editText_HoraFinalizacion.getText().toString().substring(6).equals("pm"))))
            {
                if(horaFin-horaInicio==1)
                {
                    //evalua min
                    if((minutesInicio-minutesFin)<=0)
                    {
                        flag_TimeValid = true;
                    }
                    else
                    {
                        clearAndShowMessage("Las asesorías deben durar 1 hora.");
                    }
                }
                else if(horaFin-horaInicio>1)
                {
                    flag_TimeValid = true;
                }
                else if(horaInicio==horaFin)
                {
                    clearAndShowMessage("Las asesorías deben durar 1 hora.");
                }
            }
        }
        else
        {
             clearAndShowMessage("La hora de inicio debe ser igual o menor a 5 min a la hora actual.");
        }
        return flag_TimeValid;
    }

    private void clearAndShowMessage(String message)
    {
        Toast.makeText(MainAdviserActivityApp.this,message, Toast.LENGTH_SHORT).show();
        editText_HoraInicio.setError("Hora inválida.");
        editText_HoraFinalizacion.setError("Hora inválida.");
        editText_HoraInicio.getText().clear();
        editText_HoraFinalizacion.getText().clear();
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

                setImage(imagen);
                firebaseStorageHelper.deleteImage(FirestoreHelper.asesor.getUid());
                firebaseStorageHelper.addImage(FirestoreHelper.asesor.getUid(),Uri.fromFile(imagen));
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==100 && resultCode == RESULT_OK)
        {
            Root root = manager.addRoot(getApplicationContext(), StorageManagerCompat.DEF_MAIN_ROOT, data);
            Log.e("root: ", Objects.requireNonNull(root).getUri().toString());
            if (root == null)
                return;
            DocumentFile f = root.toRootDirectory(getApplicationContext());
            if (f == null)
                return;
            try {
                DocumentFile subFolder = DocumentFileCompat.getSubFolder(f, NOMBRE_DIRECTORIO);
                DocumentFile myFile=null;
                if(flagPDFAsesorias)
                {
                    if(asesoriaList!=null)
                    {
                        try {
                            myFile = DocumentFileCompat.getFile(subFolder, NOMBRE_DOCUMENTO+"_"+PropiertiesHelper.obtenerFecha()+".pdf", "");
                            Document documento = new Document(PageSize.LETTER.rotate());
                            OutputStream os = getContentResolver().openOutputStream(Objects.requireNonNull(myFile).getUri());
                            dibujarPDF(documento, (FileOutputStream) os);
                            Toast.makeText(MainAdviserActivityApp.this, "Se creo tu archivo pdf "+ myFile.getUri().getPath(), Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No hay datos para generar el pdf.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(flagPDFAsesorados)
                {

                   /* if(cursor.getCount()!=0)
                    {
                        try {
                            myFile = DocumentFileCompat.getFile(subFolder, NOMBRE_DOCUMENTO2+"_"+PropiertiesHelper.obtenerFecha()+".pdf", "");
                            Document documento = new Document(PageSize.LETTER.rotate());
                            OutputStream os = getContentResolver().openOutputStream(Objects.requireNonNull(myFile).getUri());
                            dibujarPDF(documento, (FileOutputStream) os);
                            Toast.makeText(MainAdviserActivityApp.this, "Se creo tu archivo pdf "+ myFile.getUri().getPath(), Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No hay datos para generar el pdf.", Toast.LENGTH_SHORT).show();
                    }*/
                }
            } catch (OperationFailedException e) {
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

            textView_NumeroControl.setText("Número de control:"+array[0]);
            textView_Nombre.setText("Nombre completo:"+array[1]);
            textView_Carrera.setText("Carrera:"+array[2]);
            textView_Materia.setText("Materia:"+array[3]);
            textView_Tema.setText("Tema:"+array[4]);
            textView_Semestre.setText("Semestre:"+array[5]);
            textView_Fecha.setText("Fecha:"+array[6]);

            final MaterialButton button_registrar_alumno = dialogRegistrar.findViewById(R.id.button_registrar_alumno);
            final MaterialButton button_cancelar_alumno = dialogRegistrar.findViewById(R.id.button_cancelar_alumno);


            button_registrar_alumno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registrar(array);
                    dialogRegistrar.dismiss();
                }
            });

            button_cancelar_alumno.setOnClickListener(new View.OnClickListener() {
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

        final TextInputLayout editText_Name = dialogEditProfile.findViewById(R.id.editText_Name);
        final TextInputLayout editText_LastNames = dialogEditProfile.findViewById(R.id.editText_LastNames);
        final TextInputLayout spinner_career = dialogEditProfile.findViewById(R.id.spinner_carrera_edit);
        MaterialButton cardView_ButtonUpdate = dialogEditProfile.findViewById(R.id.cardView_Button_Update);
        MaterialButton cardView_ButtonClose = dialogEditProfile.findViewById(R.id.cardView_ButtonClose);


        ArrayAdapter<String> arrayAdapterCareer = new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        ((AutoCompleteTextView)spinner_career.getEditText()).setAdapter(arrayAdapterCareer);

        editText_Name.getEditText().setText(FirestoreHelper.asesor.getNombre());
        editText_LastNames.getEditText().setText(FirestoreHelper.asesor.getApellidos());
        spinner_career.getEditText().setText(FirestoreHelper.asesor.getCarrera());

        cardView_ButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag_Name=false;
                boolean flag_LastNames=false;
                boolean flag_Career=false;

                if(!editText_Name.getEditText().getText().toString().isEmpty()) {
                    flag_Name=true;
                }
                else {
                    editText_Name.setError("Nombre requerido");
                }

                if(!editText_LastNames.getEditText().getText().toString().isEmpty()) {
                    flag_LastNames=true;
                }
                else {
                    editText_LastNames.setError("Apellidos requeridos");
                }

                if(spinner_career.getEditText().getText().length()!=0) {
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
                    firestoreHelper.updateDataAsesor(editText_Name.getEditText().getText().toString(), editText_LastNames.getEditText().getText().toString(),
                            String.valueOf(spinner_career.getEditText().getText()),dialog,MainAdviserActivityApp.this);
                    dialogEditProfile.dismiss();
                   // Toast.makeText(MainAdviserActivityApp.this, "Actualizando datos...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardView_ButtonClose.setOnClickListener(new View.OnClickListener() {
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

        final AlertDialog dialogNewSemester =builder.create();
        dialogNewSemester.setCancelable(false);
        dialogNewSemester.show();

        MaterialButton cardView_Button_CreatePDF1= dialogNewSemester.findViewById(R.id.cardView_Button_CreatePDF1);
        MaterialButton cardView_Button_CreatePDF2= dialogNewSemester.findViewById(R.id.cardView_Button_CreatePDF2);
        MaterialButton cardView_ButtonDelete= dialogNewSemester.findViewById(R.id.cardView_Button_Delete);
        MaterialButton cardView_ButtonCancel = dialogNewSemester.findViewById(R.id.cardView_ButtonCancel);


        cardView_Button_CreatePDF1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    flagPDFAsesorados=false;
                    flagPDFAsesorias=true;
                    crearPdfAndroidQ();
                    flagDeleteData1=true;
                }
                else{
                    flagPDFAsesorados=false;
                    flagPDFAsesorias=true;
                    crearPdf();
                    flagDeleteData1=true;
                }
            }
        });

        cardView_Button_CreatePDF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    flagPDFAsesorados=true;
                    flagPDFAsesorias=false;
                    crearPdfAndroidQ();
                    flagDeleteData2=true;
                }
                else{
                    flagPDFAsesorados=true;
                    flagPDFAsesorias=false;
                    crearPdf();
                    flagDeleteData2=true;
                }
            }
        });

        cardView_ButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagDeleteData1 && flagDeleteData2)
                {
                    AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(MainAdviserActivityApp.this);
                    dialogConfirm.setTitle("Eliminar registros");
                    dialogConfirm.setMessage("¿Seguro de que desea eliminar todos los registros de los alumnos asesorados?");
                    dialogConfirm.setCancelable(false);
                    dialogConfirm.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog dialogE = ProgressDialog.show(MainAdviserActivityApp.this, "", "Eliminando...", true);
                            firestoreHelper.deleteAsesoriasData();
                            firestoreAsesorado.deleteAsesorados(FirestoreHelper.asesor.getUid(), dialogE);
                            flagDeleteData1=false;
                            flagDeleteData2=false;
                            dialogNewSemester.dismiss();
                        }
                    });
                    dialogConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialogConfirm.show();
                }
                else
                {
                    if(!flagDeleteData1 && !flagDeleteData2)
                    {
                        Toast.makeText(MainAdviserActivityApp.this, "Para poder eliminar debes primero dar click en" +
                                " ‘Crear PDF asesorías’ y posteriormente en ‘Crear PDF asesorados.", Toast.LENGTH_LONG).show();
                    }
                    else if(!flagDeleteData1)
                    {
                        Toast.makeText(MainAdviserActivityApp.this,"Te falta dar click en ‘Crear PDF asesorías.’",Toast.LENGTH_SHORT).show();
                    }
                    else if(!flagDeleteData2)
                    {
                        Toast.makeText(MainAdviserActivityApp.this,"Te falta dar click en ‘Crear PDF asesorados.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardView_ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagDeleteData1=false;
                flagDeleteData2=false;
                dialogNewSemester.dismiss();
            }
        });
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void status(String message)
    {
        if(message.equals("Datos actualizados"))
        {
           textView_Nombre.setText(FirestoreHelper.asesor.getNombre() + " "+ FirestoreHelper.asesor.getApellidos());
        }
        Toast.makeText(MainAdviserActivityApp.this,message,Toast.LENGTH_SHORT).show();
    }



    public Map<String,Object> dataToSave()
    {
        Map<String,Object> data = new HashMap<>();
        data.put("tipo",radioBAPresencial.isChecked());
        if(radioBAPresencial.isChecked()) {
            if (spinner_lugares.getEditText().getText().toString().equals("Otro(Especifique)")) {
                data.put("lugar", editTextText_otroLugar.getEditText().getText().toString());
            } else {
                data.put("lugar", spinner_lugares.getEditText().getText().toString());
            }
            data.put("url",  "");
        }
        else
        {
            data.put("url",  editText_URL.getEditText().getText().toString());
            data.put("lugar", -1);
            data.put("lugar2", "");
        }
        data.put("materia", spinner_materias.getEditText().getText().toString());
        data.put("h_inicio",editText_HoraInicio.getText().toString());
        data.put("h_fin",editText_HoraFinalizacion.getText().toString());
        data.put("info",editTextTextMultiLine.getEditText().getText().toString());

        return data;
    }
    private void clear()
    {
        radioBAPresencial.setChecked(true);
        spinner_lugares.getEditText().getText().clear();
        spinner_materias.getEditText().getText().clear();
        editText_HoraInicio.getText().clear();
        editText_HoraFinalizacion.getText().clear();
        editTextTextMultiLine.getEditText().getText().clear();
        editTextText_otroLugar.getEditText().getText().clear();
        editText_URL.getEditText().getText().clear();
    }

    //Creación de pdfs
    private void crearPdf() {
        if (solicitarPermiso()) {
            try {
                Document documento = new Document(PageSize.LETTER.rotate());
                File f = null;
                if(flagPDFAsesorias)
                {
                    if(asesoriaList!=null)
                    {
                        f = crearFichero(NOMBRE_DOCUMENTO+"_"+PropiertiesHelper.obtenerFecha()+".pdf");
                        FileOutputStream ficheroPdf = new FileOutputStream(Objects.requireNonNull(f).getAbsolutePath());
                        dibujarPDF(documento, ficheroPdf);
                        Toast.makeText(MainAdviserActivityApp.this, "Se creo tu archivo pdf en "+ f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No hay datos para generar el pdf.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(flagPDFAsesorados)
                {
                    if(asesoradoList!=null)
                    {
                        f = crearFichero(NOMBRE_DOCUMENTO2+"_"+PropiertiesHelper.obtenerFecha()+".pdf");
                        FileOutputStream ficheroPdf = new FileOutputStream(Objects.requireNonNull(f).getAbsolutePath());
                        dibujarPDF(documento, ficheroPdf);
                        Toast.makeText(MainAdviserActivityApp.this, "Se creo tu archivo pdf en "+ f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No hay datos para generar el pdf.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Para guardar el archivo PDF necesita conceder los permisos.",Toast.LENGTH_LONG).show();
        }
    }

    private boolean solicitarPermiso(){
        int permiso = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permiso!= PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSION);
            }
                return false;
        }else {
            return true;
        }
    }

   private void crearPdfAndroidQ() {
        manager = new StorageManagerCompat(getApplicationContext());
        Root root = manager.getRoot(StorageManagerCompat.DEF_MAIN_ROOT);

        if (root == null || !root.isAccessGranted(getApplicationContext())) {
            Intent data = manager.requireExternalAccess(getApplicationContext());
            startActivityForResult(data, 100);
            Log.e("Root: ", "null");
        }
        else
        {

            Log.e("Root: ", root.getUri().toString());
            DocumentFile f = root.toRootDirectory(getApplicationContext());
            DocumentFile subFolder = DocumentFileCompat.getSubFolder(f, NOMBRE_DIRECTORIO);
            DocumentFile myFile=null;
            if(flagPDFAsesorias)
            {
                if(asesoriaList!=null)
                {
                    try {
                        myFile = DocumentFileCompat.getFile(subFolder, NOMBRE_DOCUMENTO+"_"+PropiertiesHelper.obtenerFecha()+".pdf", "");
                        Document documento = new Document(PageSize.LETTER.rotate());
                        OutputStream os = getContentResolver().openOutputStream(Objects.requireNonNull(myFile).getUri());
                        dibujarPDF(documento, (FileOutputStream) os);
                        Toast.makeText(MainAdviserActivityApp.this, "Se creo tu archivo pdf "+ myFile.getUri().getPath(), Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No hay datos para generar el pdf.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(flagPDFAsesorados)
            {
                if(asesoradoList!=null)
                    {
                    try {
                        myFile = DocumentFileCompat.getFile(subFolder, NOMBRE_DOCUMENTO2+"_"+PropiertiesHelper.obtenerFecha()+".pdf", "");
                        Document documento = new Document(PageSize.LETTER.rotate());
                        OutputStream os = getContentResolver().openOutputStream(Objects.requireNonNull(myFile).getUri());
                        dibujarPDF(documento, (FileOutputStream) os);
                        Toast.makeText(MainAdviserActivityApp.this, "Se creo tu archivo pdf "+ myFile.getUri().getPath(), Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No hay datos para generar el pdf.", Toast.LENGTH_SHORT).show();
                }
                    }
        }
    }

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    public static File getRuta() {

        // El fichero sera almacenado en un directorio dentro del directorio descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }

    private void dibujarPDF(Document documento, FileOutputStream ficheroPdf) {
        try{
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ciencias_basicas);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());

            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);
            writer.setPageEvent(new WaterMark(imagen));

            Font fontHeaderFooter = FontFactory.getFont(FontFactory.TIMES_ROMAN, FontFactory.defaultEncoding,FontFactory.defaultEmbedding,12, Font.BOLD, Color.BLACK);
            Paragraph paragraphHeader=null;
            if(flagPDFAsesorias==true) {
                paragraphHeader = new Paragraph("TECNOLÓGICO NACIONAL DE MÉXICO\n" +
                        "Instituto Tecnológico Superior de Uruapan\n\n" +
                        "BITÁCORA DE ASESORES PAR\n\n", fontHeaderFooter);
            }
            else if(flagPDFAsesorados==true) {
                paragraphHeader = new Paragraph("TECNOLÓGICO NACIONAL DE MÉXICO\n" +
                        "Instituto Tecnológico Superior de Uruapan\n" +
                        "Formato de Registro De Asesorías\n\n" +
                        "Nombre del asesor par:________________________________" +
                        "Carrera:________________________________" +
                        "Periodo:________________\n\n", fontHeaderFooter);
            }

            Phrase phraseFooter = new Phrase("_____________________________________\nFirma del asesor\n", fontHeaderFooter);

            HeaderFooter cabecera = new HeaderFooter(new Phrase(paragraphHeader), false);
            cabecera.setAlignment(Element.ALIGN_CENTER);

            HeaderFooter pie = new HeaderFooter(new Phrase(phraseFooter), false);
            pie.setAlignment(Element.ALIGN_CENTER);

            documento.setHeader(cabecera);
            documento.setFooter(pie);

            Phrase numControl = new Phrase("No. Control", fontHeaderFooter);
            Phrase nombreEstudiante = new Phrase("Nombre del estudiante", fontHeaderFooter);
            Phrase carrera = new Phrase("Carrera", fontHeaderFooter);
            Phrase asignatura = new Phrase("Asignatura", fontHeaderFooter);
            Phrase tema = new Phrase("Tema", fontHeaderFooter);
            Phrase fecha = new Phrase("Fecha", fontHeaderFooter);

            Phrase nombreAsesor = new Phrase("Nombre del asesor par", fontHeaderFooter);
            Phrase hora_entrada = new Phrase("Hora de entrada", fontHeaderFooter);
            Phrase hora_salida = new Phrase("Hora de salida", fontHeaderFooter);

            PdfPCell cellNumControl = new PdfPCell(numControl);
            cellNumControl.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNumControl.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellNombreEstudiante = new PdfPCell(nombreEstudiante);
            cellNombreEstudiante.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNombreEstudiante.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellCarrera = new PdfPCell(carrera);
            cellCarrera.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCarrera.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellAsignatura = new PdfPCell(asignatura);
            cellAsignatura.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAsignatura.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellTema = new PdfPCell(tema);
            cellTema.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTema.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellFecha = new PdfPCell(fecha);
            cellFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellFecha.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellNombreAsesor = new PdfPCell(nombreAsesor);
            cellNombreAsesor.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellNombreAsesor.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellHoraEntrada = new PdfPCell(hora_entrada);
            cellHoraEntrada.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHoraEntrada.setBackgroundColor(Color.LIGHT_GRAY);

            PdfPCell cellHoraSalida = new PdfPCell(hora_salida);
            cellHoraSalida.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHoraSalida.setBackgroundColor(Color.LIGHT_GRAY);

            documento.open();

            if(flagPDFAsesorias)
            {
                PdfPTable tabla = new PdfPTable(5);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[] {(float) 31.25, (float) 31.25, (float) 12.5, (float) 12.5, (float) 12.5});
                tabla.addCell(cellNombreAsesor);
                tabla.addCell(cellAsignatura);
                tabla.addCell(cellFecha);
                tabla.addCell(cellHoraEntrada);
                tabla.addCell(cellHoraSalida);
                tabla.setHeaderRows(1);

                for (int i = 0; i < asesoriaList.size(); i++) {
                    tabla.addCell(asesoriaList.get(i).getNombre());
                    tabla.addCell(asesoriaList.get(i).getMateria());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    tabla.addCell(dateFormat.format(asesoriaList.get(i).getFecha()));
                    tabla.addCell(asesoriaList.get(i).getH_inicio());
                    tabla.addCell(asesoriaList.get(i).getH_final());
                }
                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                documento.add(tabla);
            }
            else if(flagPDFAsesorados)
            {
                PdfPTable tabla = new PdfPTable(6);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[] {(float) 12.5, 20, 19, 18, 18, (float) 12.5});
                tabla.addCell(cellNumControl);
                tabla.addCell(cellNombreEstudiante);
                tabla.addCell(cellCarrera);
                tabla.addCell(cellAsignatura);
                tabla.addCell(cellTema);
                tabla.addCell(cellFecha);
                tabla.setHeaderRows(1);


                for (int i = 0; i < asesoradoList.size(); i++) {
                    tabla.addCell(asesoradoList.get(i).getnControl());
                    tabla.addCell(asesoradoList.get(i).getNombre());
                    tabla.addCell(asesoradoList.get(i).getCarrera());
                    tabla.addCell(asesoradoList.get(i).getMateria());
                    tabla.addCell(asesoradoList.get(i).getTema());
                    tabla.addCell(asesoradoList.get(i).getFecha());
                }
                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                documento.add(tabla);
            }

        } catch (DocumentException e) {
            Log.e(ETIQUETA_ERROR, e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerramos el documento.
            documento.close();
        }
    }

    @Override
    public void getAsesorias(List<Asesoria> asesoriaList) {
        if(asesoriaList.size()!=0)
        {
            this.asesoriaList = asesoriaList;
            Collections.sort(this.asesoriaList, new Comparator<Asesoria>() {
                @Override
                public int compare(Asesoria a1, Asesoria a2) {
                    return Long.valueOf(a1.getFecha().getTime()).compareTo(a2.getFecha().getTime());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setImage(String image_url)
    {
        RequestManager rm =  Glide.with(getApplicationContext());
        if(image_url.equals(""))
        {
            Bitmap placeholder = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.user);
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), placeholder);
            circularBitmapDrawable.setCircular(true);
                    rm.load(FirestoreHelper.asesor.getuRI_image())
                    .placeholder(circularBitmapDrawable)
                    .fitCenter()
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                    .into(imageView_perfil);
        }
        else
        {
            rm.load(FirestoreHelper.asesor.getuRI_image())
                    .fitCenter()
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                    .into(imageView_perfil);
        }

    }
    private void setImage(File file)
    {
        Glide.with(getApplicationContext())
                .load(file)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imageView_perfil);
    }

    @Override
    public void getAsesorados(List<Asesorado> asesoradoList) {
        if(asesoradoList.size()!=0)
        {
            this.asesoradoList = asesoradoList;
        }
    }

    @Override
    public void getAlumno(Alumno alumno) {

    }
}

