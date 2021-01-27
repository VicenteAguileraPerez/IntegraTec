package com.vicenteaguilera.integratec;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import net.glxn.qrgen.android.QRCode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class CreateCodeQRActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ASK_PERMISSION = 111;
    private TextInputLayout editText_Nombre;
    private TextInputLayout editText_NumeroControl;
    private TextInputLayout spinner_Carrera;
    private ImageView imageView;
    private MaterialButton button_crearQR;
    private MaterialButton button_guardarQR;
    private Bitmap bitmap;
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
        setContentView(R.layout.activity_create_code_q_r);

        editText_Nombre = findViewById(R.id.editText_nombre);
        editText_NumeroControl = findViewById(R.id.editText_NumeroControl);
        spinner_Carrera = findViewById(R.id.spinner_carrera);
        imageView = findViewById(R.id.imageView);
        button_crearQR = findViewById(R.id.button_crearqr);
        button_guardarQR = findViewById(R.id.button_guardarqr);

        imageView.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> arrayAdapter_Carreras= new ArrayAdapter<>(this, R.layout.custom_spinner_item, PropiertiesHelper.CARRERAS);


        ((AutoCompleteTextView)spinner_Carrera.getEditText()).setAdapter(arrayAdapter_Carreras);


        button_crearQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearQR();
            }
        });

        button_guardarQR.setOnClickListener(new View.OnClickListener() {
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
         textWachers();
    }


    private void crearQR()
    {
        if(!Objects.requireNonNull(editText_NumeroControl.getEditText()).getText().toString().isEmpty() && editText_NumeroControl.getEditText().getText().toString().length()==8){

            if(!Objects.requireNonNull(editText_Nombre.getEditText()).getText().toString().isEmpty()) {


                    if(Objects.requireNonNull(spinner_Carrera.getEditText()).getText().length() != 0) {
                                String texto = editText_NumeroControl.getEditText().getText().toString() + "_" + editText_Nombre.getEditText().getText().toString() + "_"
                                        + spinner_Carrera.getEditText().getText() + "_"+PropiertiesHelper.obtenerFecha().substring(0,10);

                                bitmap = QRCode.from(texto).withSize(400, 400).bitmap();
                                imageView.setImageBitmap(bitmap);
                                imageView.setVisibility(View.VISIBLE);
                        spinner_Carrera.setError(null);
                        editText_Nombre.setError(null);
                        editText_NumeroControl.setError(null);
                    }
                    else
                    {
                        spinner_Carrera.setError("Debes seleccionar una carrera");
                        Snackbar.make(findViewById(android.R.id.content), "Debes seleccionar una carrera.", Snackbar.LENGTH_SHORT).show();
                    }

            }
            else
            {
                editText_Nombre.setError("Campo vacío");
                Snackbar.make(findViewById(android.R.id.content), "Debes ingresar tú nombre.", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            editText_NumeroControl.setError("Campo vacío");
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


    private void textWachers()
    {
        editText_NumeroControl.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                    {
                        if(charSequence.length()==0)
                        {
                            editText_NumeroControl.setError("Campo vacío");
                        }
                        else if(charSequence.length()!=8)
                        {
                            editText_NumeroControl.setError("Debe tener 8 dígitos");
                        }
                        else
                        {
                            editText_NumeroControl.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
        editText_Nombre.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                    {
                        if(charSequence.length()==0)
                        {
                            editText_Nombre.setError("Campo vacío");
                        }
                        else
                        {
                            editText_Nombre.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
    }
}
