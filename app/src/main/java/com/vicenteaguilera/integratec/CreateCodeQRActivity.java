package com.vicenteaguilera.integratec;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class CreateCodeQRActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ASK_PERMISSION = 111;
    private EditText editText_Nombre;
    private EditText editText_Tema;
    private EditText editText_NumeroControl;
    private Spinner spinner_Carrera;
    private Spinner spinner_Asignatura;
    private Spinner spinner_Semestre;
    private ImageView imageView;
    private CardView cardView_BtnCrearQR;
    private CardView cardView_ButtonGuardarQR;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_code_q_r);

        editText_Nombre = findViewById(R.id.editText_Nombre);
        editText_Tema = findViewById(R.id.editText_Tema);
        editText_NumeroControl = findViewById(R.id.editText_NumeroControl);
        spinner_Carrera = findViewById(R.id.spinner_Carrera);
        spinner_Asignatura = findViewById(R.id.spinner_Asignatura);
        spinner_Semestre = findViewById(R.id.spinner_Semestre);
        imageView = findViewById(R.id.imageView);
        cardView_BtnCrearQR = findViewById(R.id.cardView_ButtonCrearQR);
        cardView_ButtonGuardarQR = findViewById(R.id.cardView_ButtonGuardarQR);

        imageView.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> arrayAdapter_Carreras= new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);
        ArrayAdapter<String> arrayAdapter_Materias= new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.MATERIAS);
        ArrayAdapter<String> arrayAdapter_Semestres= new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.SEMESTRES);

        spinner_Asignatura.setAdapter(arrayAdapter_Materias);
        spinner_Carrera.setAdapter(arrayAdapter_Carreras);
        spinner_Semestre.setAdapter(arrayAdapter_Semestres);

        cardView_BtnCrearQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearQR();
            }
        });

        cardView_ButtonGuardarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap!=null)
                {
                    if (solicitarPermiso()) {
                        String nombre = "CodigoQR "+PropiertiesHelper.obtenerFecha();
                        try {
                            saveImage(bitmap, nombre);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Para guardar el código QR necesita conceder los permisos.",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Debes crear primero un QR.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void crearQR()
    {
        if(!editText_NumeroControl.getText().toString().isEmpty() && editText_NumeroControl.getText().toString().length()==8){

            if(!editText_Nombre.getText().toString().isEmpty()) {

                if(spinner_Semestre.getSelectedItemPosition() != 0) {

                    if(spinner_Carrera.getSelectedItemPosition() != 0) {

                        if(spinner_Asignatura.getSelectedItemPosition() != 0) {

                            if (!editText_Tema.getText().toString().isEmpty()) {

                                String texto = editText_NumeroControl.getText().toString() + "_" + editText_Nombre.getText().toString() + "_"
                                        + spinner_Carrera.getSelectedItem().toString() + "_" + spinner_Asignatura.getSelectedItem().toString() + "_"
                                        + editText_Tema.getText().toString() + "_" + spinner_Semestre.getSelectedItem().toString() + "_"+PropiertiesHelper.obtenerFecha().substring(0,10);

                                bitmap = QRCode.from(texto).withSize(400, 400).bitmap();
                                imageView.setImageBitmap(bitmap);
                                imageView.setVisibility(View.VISIBLE);
                            } else {
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
                    Snackbar.make(findViewById(android.R.id.content), "Debes seleccionar un semestre.", Snackbar.LENGTH_SHORT).show();
                }
            }
            else
            {
                Snackbar.make(findViewById(android.R.id.content), "Debes ingresar tú nombre.", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            Snackbar.make(findViewById(android.R.id.content), "Debes ingresar tú número de control completo.", Snackbar.LENGTH_SHORT).show();
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



    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            outputStream = contentResolver.openOutputStream(Objects.requireNonNull(imageUri));

        }
        else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/";
            File dir = new File(imagesDir, "QR IntegraTec" );
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File image = new File(dir, name + ".jpg");
            outputStream = new FileOutputStream(image);
            MakeSureFileWasCreatedThenMakeAvaliable(image);

            /*File image = new File(dir, name + ".png");
            outputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);*/
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        Objects.requireNonNull(outputStream).close();
        Toast.makeText(getApplicationContext(),"Imagen guardada.",Toast.LENGTH_SHORT).show();
    }

    /**
     * Metodo para actualizar la galería
     * @param file imagen a guardar
     */
    private void MakeSureFileWasCreatedThenMakeAvaliable(File file){
        MediaScannerConnection.scanFile(getApplicationContext(),
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }
}
