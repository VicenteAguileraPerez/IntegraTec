package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Asesor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseFirestoreAsesorHelper
{
    public static Asesor asesor=null;
    public static final CollectionReference AsesoresCollection = FirebaseFirestoreAsesoriaPublicaAsesoriaHelper.db.collection("asesores");
    /***********************************************
     *      ASESORES COLLECTION
     ***********************************************/
    /**
     * Agrega los datos la primera vez
     * @param status
     * @param dialog
     * @param context
     * @param uid
     * @param email
     * @param password
     * @param param
     */
    public void addData(Status status, ProgressDialog dialog, Context context, String uid, String email, String password, String[] param)
    {
        if(param.length==3)
        {
            //asesor
            Map<String, Object> asesor = new HashMap<>();
            asesor.put("nombre", param[0]);
            asesor.put("apellido", param[1]);
            asesor.put("carrera", param[2]);
            asesor.put("email", email);
            asesor.put("password", password);
            asesor.put("uri_image", "");
            asesor.put("activo",false);
            Log.e("lista",  asesor.values().toArray().length+"");
            registerDataUserToFirestore(AsesoresCollection,uid,status,dialog, asesor,context);

        }
        else if(param.length==2)
        {
            //es un profesor
        }
    }

    /**
     * Registrar asesor en el sistema.
     * @param collectionReference
     * @param document
     * @param status
     * @param dialog
     * @param data
     * @param context
     */
    private void registerDataUserToFirestore(CollectionReference collectionReference, final String document, final Status status, final ProgressDialog dialog, final Map<String, Object> data, final Context context)
    {
        // Add a new document with a generated ID
        collectionReference.document(document).set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        dialog.dismiss();
                        if(task.isSuccessful())
                        {

                            final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setTitle("Aviso");
                            alertDialogBuilder.setMessage("Registrado comuníquese con el administrador para habilitar su cuenta...");
                            alertDialogBuilder.setPositiveButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface alertDialog, int i)
                                        {

                                            Intent intent = new Intent(context, OptionsActivity.class);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();
                                            alertDialog.dismiss();
                                        }
                                    }
                            );

                            alertDialogBuilder.show();



                        }
                        else
                        {
                            status.status("Error del registros de los datos. Inténtelo de nuevo");
                            Intent intent = new Intent(context, OptionsActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }

                    }
                });
    }

    /**
     * Obtiene la informacion del asesor
     * @param document
     * @param dialog
     * @param status
     * @param context
     */
    public void getData(String document, final ProgressDialog dialog, final Status status,final Context context)
    {
        dialog.show();
        AsesoresCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists())
                    {
                        Map<String,Object>data=document.getData();
                        asesor = new Asesor(document.getId(),String.valueOf(Objects.requireNonNull(data).get("nombre")),String.valueOf(data.get("apellido")),String.valueOf(data.get("email")),String.valueOf(data.get("password")),String.valueOf(data.get("carrera")),String.valueOf(data.get("uri_image")),(boolean)data.get("activo"));
                        if((boolean)Objects.requireNonNull(document.get("activo")))
                        {
                            status.status("Bienvenido "+ asesor.getNombre()+" "+ asesor.getApellidos());
                            Intent intent = new Intent(context, MainAdviserActivityApp.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                        else
                        {

                            if(!(context instanceof OptionsActivity))
                            {
                                final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setTitle("Aviso");
                                alertDialogBuilder.setMessage("Registrado comuníquese con el administrador para habilitar su cuenta...");
                                alertDialogBuilder.setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alertDialog, int i)
                                            {

                                                Intent intent = new Intent(context, OptionsActivity.class);
                                                context.startActivity(intent);
                                                ((Activity) context).finish();
                                                alertDialog.cancel();
                                            }
                                        }
                                );

                                alertDialogBuilder.show();



                            }
                            else{
                                final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("Aviso");
                                alertDialogBuilder.setMessage("Registrado comuníquese con el administrador para habilitar su cuenta...");
                                alertDialogBuilder.setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alertDialog, int i)
                                            {
                                                alertDialog.cancel();
                                            }
                                        }
                                );

                                alertDialogBuilder.show();
                            }
                        }
                    }
                    else
                    {
                        status.status("No existe esa cuenta");
                        Intent intent = new Intent(context, OptionsActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    }
                }
                else
                {

                    status.status("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrado");
                    Intent intent = new Intent(context, OptionsActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                }
                dialog.dismiss();

            }
        });

    }

    /**
     * Actualizar datos del asesor
     * @param nombre
     * @param apellido
     * @param carrera
     * @param dialog
     * @param status
     */
    public void updateDataAsesor(final String nombre, final String apellido, final String carrera, final ProgressDialog dialog, final Status status)
    {
        Map<String,Object> asesorMap = new HashMap<>();
        asesorMap.put("nombre", nombre);
        asesorMap.put("apellido", apellido);
        asesorMap.put("carrera", carrera);
        AsesoresCollection.document(asesor.getUid()).update(asesorMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        asesor.setNombre(nombre);
                        asesor.setApellidos(apellido);
                        asesor.setCarrera(carrera);
                        status.status("Datos actualizados");
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        status.status("Datos no actualizados, verifica tu conexión a Internet");
                        dialog.dismiss();
                    }
                });

    }

    /**
     * Actualiza la imagen del asesor
     * @param uri_image url de la imagen
     * @param status interfaz que retorna mensajes a las actividades
     */
    public void updateImageAsesor(final String uri_image, final Status status)
    {
        Map<String,Object> asesorMap = new HashMap<>();
        asesorMap.put("uri_image", uri_image);

        AsesoresCollection.document(asesor.getUid()).update(asesorMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        asesor.setuRI_image(uri_image);
                        status.status("Datos actualizados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        status.status("Imagen no actualizada, verifica tu conexión a Internet");
                    }
                });

    }

    /**
     * Obtiene todos los asesores para  poder enviar los mensajes en la {@link com.vicenteaguilera.integratec.controllers.BandejaActivity}
     * @param status
     * @param dialog
     * @param listaAsesores
     */
    public void getAllAsesores(final Status status, final ProgressDialog dialog, ListaAsesores listaAsesores)
    {

        AsesoresCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isComplete())
                        {
                            String nombre;
                            Map<String, String> asesores = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                nombre = document.getData().get("nombre").toString()+" "+document.getData().get("apellido").toString();
                                asesores.put(document.getId(),nombre);
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            listaAsesores.getAsesoresAll(asesores);
                            status.status("Contactos obtenidos");
                        }
                        else {
                            status.status("Error al obtener los asesores");
                        }
                        dialog.dismiss();
                    }
                });
    }

}
