package com.vicenteaguilera.integratec.helpers.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SaveImageHelper {
    private Context TheThis;
    private String carpeta = "/QR Integra";
    private String nombre = "MiQR";

    public void SaveImage(Context context, Bitmap ImageToSave) {

        TheThis = context;
        String file_ruta = TheThis.getExternalFilesDir(null).getAbsolutePath() + carpeta;
        //Log.e("Ruta: ", file_ruta);
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_ruta, carpeta );

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, nombre + CurrentDateAndTime + ".png");

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            Toast.makeText(TheThis, "Imagen guardada en: "+ file_ruta, Toast.LENGTH_SHORT).show();
        }

        catch(FileNotFoundException e) {
            Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!\n"+e, Toast.LENGTH_SHORT).show();
        }
        catch(IOException e) {
            Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!\n"+e , Toast.LENGTH_SHORT).show();
        }

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
