package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.helpers.DateHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorias;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Asesor;
import com.vicenteaguilera.integratec.models.Asesoria;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class FirestoreHelper
{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AsesoresCollection = db.collection("asesores");
    private final CollectionReference AsesoriaPublicaCollection = db.collection("asesoria_publica");
    private final CollectionReference AsesoriaCollection = db.collection("asesoria");
    //private static Status status;
    //private ProgressDialog dialog;
    public static Asesor asesor=null;
    //agrega los datos la primera vez
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
   //getAsesor
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
                        asesor = new Asesor(document.getId(),String.valueOf(data.get("nombre")),String.valueOf(data.get("apellido")),String.valueOf(data.get("email")),String.valueOf(data.get("password")),String.valueOf(data.get("carrera")),String.valueOf(data.get("uri_image")),(boolean)data.get("activo"));
                        if((boolean)document.get("activo"))
                        {
                            status.status("Bienvenido "+asesor.getNombre()+" "+asesor.getApellidos());
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
    public void updateDataAsesor(final String nombre, final String apellido, final String carrera, final ProgressDialog dialog, final Status status)
    {
        Map<String,Object> asesor = new HashMap<>();
        asesor.put("nombre", nombre);
        asesor.put("apellido", apellido);
        asesor.put("carrera", carrera);
        AsesoresCollection.document(FirestoreHelper.asesor.getUid()).update(asesor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirestoreHelper.asesor.setNombre(nombre);
                FirestoreHelper.asesor.setApellidos(apellido);
                FirestoreHelper.asesor.setCarrera(carrera);
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
    public void updateImageAsesor(final String uri_image, final Status status)
    {
        Map<String,Object> asesor = new HashMap<>();
        asesor.put("uri_image", uri_image);

        AsesoresCollection.document(FirestoreHelper.asesor.getUid()).update(asesor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirestoreHelper.asesor.setuRI_image(uri_image);
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
     * Registrar asesoría publica en firebase
     * @param document
     * @param status
     * @param dialog
     * @param data
     * @param estado
     */
    public void registerDataAsesoriaPublicaToFirestore(String document, final Status status, final ProgressDialog dialog, final Map<String, Object> data, boolean estado)
    {
        // Add a new document with a generated ID
        if(estado) {
            AsesoriaPublicaCollection.document(document).set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                status.status("asesoría ajustada y publicada para los alumnos");
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                status.status("Error del registro de los datos. Inténtelo de nuevo");

                            }
                        }
                    });
        }
        else
        {

            AsesoriaPublicaCollection.document(asesor.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            status.status("asesoría terminada...");
                            addAsesoriaData(String.valueOf(data.get("materia")),String.valueOf(data.get("fecha")),String.valueOf(data.get("h_inicio")),String.valueOf(data.get("h_final")),status);
                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            status.status("Error del eliminación de asesoría. Inténtelo de nuevo");
                        }
                    });
        }
    }

    public void listenAsesorias(final ListaAsesores listaAsesores)
    {
        final List<RealtimeAsesoria> realtimeAsesoriaList = new ArrayList<>();
        final RealtimeAsesoria[] realtimeAsesoria = new RealtimeAsesoria[1];

        AsesoriaPublicaCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }
                        //como la lista se vuelve final si y si entrara por segunda vez y no hay el clear va a contener los elementos pasados + los actuales
                        //y ocaciones que haya repetidos
                        realtimeAsesoriaList.clear();
                        final int size = Objects.requireNonNull(snapshots).getDocuments().size();
                        Log.e("Documets: ", size+"");
                        boolean colocar=false;
                        for (final DocumentSnapshot dc : snapshots.getDocuments())
                        {
                                    final Map<String,Object> asesoria_add =  dc.getData();
                                    try {
                                         colocar = DateHelper.expiracionFecha(asesoria_add.get("fecha").toString(), asesoria_add.get("h_final").toString());
                                    } catch (ParseException parseException) {
                                        parseException.printStackTrace();
                                    }
                                    if(!colocar)
                                    {
                                        realtimeAsesoria[0] = new RealtimeAsesoria(dc.getId(),asesoria_add.get("lugar").toString(),
                                                asesoria_add.get("URL").toString(), asesoria_add.get("materia").toString(), asesoria_add.get("h_inicio").toString(),
                                                asesoria_add.get("h_final").toString(), asesoria_add.get("informacion").toString(),asesoria_add.get("fecha").toString(), asesoria_add.get("nombre").toString(), asesoria_add.get("image_asesor").toString());
                                        realtimeAsesoriaList.add(realtimeAsesoria[0]);
                                    }
                        }
                        listaAsesores.getAsesoresRealtime(realtimeAsesoriaList);
                    }
                });
    }
    /**
     *
     */
    public void addAsesoriaData(String materia, String fecha, String h_inicio,String h_final,final Status status)
    {
        Map<String, Object> asesoria = new HashMap<>();
        asesoria.put("asesor",asesor.getUid());
        asesoria.put("nombre", (asesor.getNombre()+" "+asesor.getApellidos()).toUpperCase());
        asesoria.put("materia", materia.toUpperCase());
        asesoria.put("fecha", fecha);
        asesoria.put("h_inicio", h_inicio.toUpperCase());
        asesoria.put("h_final", h_final.toUpperCase());
        AsesoriaCollection.add(asesoria).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isComplete())
                {
                    status.status("¡Asesoría terminada!");
                }
            }
        });

    }
    public void listenGetAsesoriasData(final ListaAsesorias listaAsesorias)
    {
        final List<Asesoria> asesoriaList = new ArrayList<>();
        final Asesoria[] asesorias = new Asesoria[1];
        AsesoriaCollection
                .whereEqualTo("asesor", asesor.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                final Map<String,Object> asesoria_add =  document.getData();
                                Date date = null;
                                try {
                                     date = new SimpleDateFormat("dd-MM-yyyy").parse(asesoria_add.get("fecha").toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                asesorias[0] = new Asesoria(asesoria_add.get("asesor").toString(), asesoria_add.get("nombre").toString(),
                                        asesoria_add.get("materia").toString(), date,
                                        asesoria_add.get("h_inicio").toString(), asesoria_add.get("h_final").toString());
                                asesoriaList.add(asesorias[0]);
                            }
                            listaAsesorias.getAsesorias(asesoriaList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void deleteAsesoriasData()
    {
        AsesoriaCollection
                .whereEqualTo("asesor", asesor.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                               document.getReference().delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
